package com.asarg.polysim.models.base;

import com.asarg.polysim.adapters.graphics.raster.Drawer;
import com.asarg.polysim.adapters.graphics.raster.SimulationCanvas;
import com.asarg.polysim.controllers.SimulationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ericmartinez on 1/5/15.
 */
public class SimulationNode extends SwingNode implements Observer {
    public final StringProperty left_status = new SimpleStringProperty("");
    public final StringProperty right_status = new SimpleStringProperty("");
    private final StringProperty left_previous_status = new SimpleStringProperty("");
    private final StringProperty right_previous_status = new SimpleStringProperty("");
    public Assembly assembly;
    public boolean stopped = true;
    public Frontier frontier;
    public FrontierElement currentFrontierAttachment = null;
    private PolyTile frontierTile;
    private boolean frontierClick = false;
    private ArrayList<FrontierElement> frontierAttachments;
    private int frontierIndex = 0;
    private Coordinate frontierClickPoint = null;
    private Coordinate lastMouseXY = new Coordinate(800, 600);
    private int dragCount = 0;
    private File file;

    public SimulationNode() {
        super();
    }

    public SimulationNode(Assembly asm, SimulationCanvas tc) {
        this.assembly = asm;
        assembly.addObserver(this);
        frontier = assembly.getFrontier();
        placeFrontierOnGrid();
        setCanvas(tc);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
                Coordinate clicked = Drawer.TileDrawer.getGridPoint(point, getCanvas().getOffset(), getCanvas().getTileDiameter());
                Tile clicked_tile = assembly.Grid.get(clicked);
                if (clicked_tile != null) {
                    processFrontierClick(clicked, clicked_tile);
                }
            }
        });
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
                lastMouseXY = point;
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                dragCount = 0;
            }
        });
        setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                //Point point = new Point((int)e.getX(), (int)e.getY());
                boolean rotation = e.getDeltaY() > 0.0;
                if (stopped) {
                    if (rotation) {
                        if (frontierClick) {
                            removeCurrentFrontierAttachment();
                            if (currentFrontierAttachment == null) {
                                frontierIndex = 0;
                                addFrontierAttachment(frontierIndex);

                            } else if (frontierIndex > 0) {
                                frontierIndex -= 1;
                                addFrontierAttachment(frontierIndex);
                            } else {
                                frontierIndex = frontierAttachments.size() - 1;
                                addFrontierAttachment(frontierIndex);
                            }

                            drawGrid();
                            return;
                        }
                        zoomInDraw();
                    } else {
                        if (frontierClick) {
                            removeCurrentFrontierAttachment();
                            if (currentFrontierAttachment == null) {
                                frontierIndex = 0;
                                addFrontierAttachment(frontierIndex);
                            } else if (frontierIndex + 1 < frontierAttachments.size()) {
                                frontierIndex += 1;
                                addFrontierAttachment(frontierIndex);
                            } else {
                                frontierIndex = 0;
                                addFrontierAttachment(0);
                            }
                            drawGrid();
                            return;
                        }
                        zoomOutDraw();
                    }
                }
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (!stopped) {
                    stopped = true;
                    return;
                }
                getCanvas().translateOffset(x - lastMouseXY.getX(), y - lastMouseXY.getY());
                lastMouseXY = point;
                getCanvas().reset();
                drawGrid();
                placeFrontierOnGrid();
            }
        });

        setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                System.out.println("exited");

            /* mouse moved away, remove the graphical cues */
                if (currentFrontierAttachment != null) {
                    Coordinate loc = currentFrontierAttachment.getOffset();
                    assembly.removePolytile(currentFrontierAttachment.getPolyTile(), loc.getX(), loc.getY());
                    currentFrontierAttachment = null;
                }
                getCanvas().reset();
                placeFrontierOnGrid();
                drawGrid();

                event.consume();
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                System.out.println("dropped");
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasContent(SimulationController.polyTileFormat)) {
                    currentFrontierAttachment = null;
                    assembly.cleanUp();
                    assembly.getOpenGlues();
                    frontier = assembly.calculateFrontier();
                    success = true;
                }
        /* let the source know whether the string was successfully
         * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

        setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                System.out.println("over");

                /* the drag-and-drop gesture entered the target */
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != this && event.getDragboard().hasContent(SimulationController.polyTileFormat)) {
                    removeFrontierFromGrid();

                    if (currentFrontierAttachment != null) {
                        Coordinate loc = currentFrontierAttachment.getOffset();
                        assembly.removePolytile(currentFrontierAttachment.getPolyTile(), loc.getX(), loc.getY());
                        currentFrontierAttachment = null;
                    }
                    getCanvas().reset();


                    Integer index = (Integer) event.getDragboard().getContent(SimulationController.polyTileFormat);
                    PolyTile dropped = getTileSet().get(index);
                    Coordinate point = new Coordinate((int) event.getX(), (int) event.getY());
                    Coordinate spot = Drawer.TileDrawer.getGridPoint(point, getCanvas().getOffset(), getCanvas().getTileDiameter());
                    FrontierElement fe = new FrontierElement(spot, spot, dropped, 0);

                    //translate to spot

                    //check if passes geometry check
                    if (assembly.geometryCheckSuccess(dropped, spot.getX(), spot.getY())) {
                        currentFrontierAttachment = fe;
                        assembly.placePolytile(dropped, spot.getX(), spot.getY());
                        drawGrid();
                        event.acceptTransferModes(TransferMode.COPY);
                    } else {
                        drawGrid();
                    }
                }

                event.consume();
            }
        });
    }

    public SimulationNode(Assembly asm, SimulationCanvas tc, File file) {
        this(asm, tc);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean save() {
        if (file != null) {
            removeFrontierFromGrid();
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(assembly, file);
                placeFrontierOnGrid();
                return true;
            } catch (JAXBException jaxbe) {
                placeFrontierOnGrid();
                return false;
            }
        }
        return false;
    }

    public boolean saveAs(File f) {
        removeFrontierFromGrid();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(assembly, f);
            file = f;
            placeFrontierOnGrid();
            return true;
        } catch (JAXBException jaxbe) {
            placeFrontierOnGrid();
            return false;
        }
    }

    public SimulationCanvas getCanvas() {
        return (SimulationCanvas) getContent();
    }

    public void setCanvas(SimulationCanvas tc) {
        setContent(tc);
    }

    public void drawGrid() {
        getCanvas().drawGrid(assembly.Grid);
        getCanvas().repaint();
    }

    public void resetFrontier() {
        exitFrontierMode();
        removeFrontierFromGrid();
    }

    public void exitFrontierMode() {
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().getX(), currentFrontierAttachment.getOffset().getY());
        }
        frontierClick = false;
        frontierClickPoint = null;
        frontierAttachments = null;
        frontierIndex = 0;
        currentFrontierAttachment = null;
        left_status.setValue(left_previous_status.getValue());
        right_status.setValue(right_previous_status.getValue());
        //mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
//        canvas.reset();
    }

    public void removeFrontierFromGrid() {
        for (FrontierElement fe : frontier) {
            //if frontier tile
            if (assembly.Grid.get(fe.getLocation()) == getFrontierPolyTile().getTile(0, 0))
                assembly.Grid.remove(fe.getLocation());
        }
    }

    private void processFrontierClick(Coordinate clicked, Tile clicked_tile) {
        PolyTile clicked_pt = clicked_tile.getParent();
        if (!frontierClick) {
            left_previous_status.setValue(left_status.getValue());
            right_previous_status.setValue(right_status.getValue());
            //mainMenu.statusLabelPreviousText = mainMenu.statusLabel.getText();
        } else {
            placeFrontierOnGrid();
        }
        frontierClick = clicked_pt.isFrontier();
        if (frontierClick) {
            //kick me out of any previous frontier state
            exitFrontierMode();
            left_status.setValue("Frontier Mode");
            frontierClick = true;
            frontierClickPoint = clicked;
            frontierAttachments = new ArrayList<FrontierElement>();
            frontierIndex = 0;
            for (FrontierElement fe : frontier) {
                if (fe.getLocation().equals(clicked) && fe.checkAttachment()) {
                    frontierAttachments.add(fe);
                }
            }
//            drawGrid();
        }

    }

    private void removeCurrentFrontierAttachment() {
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().getX(), currentFrontierAttachment.getOffset().getY());
            //left_status.setValue(left_previous_status.getValue());
            right_status.setValue(right_previous_status.getValue());
            //mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
        } else {
            assembly.Grid.remove(frontierClickPoint);
        }
        getCanvas().reset();
    }

    private void addFrontierAttachment(int index) {
        if (frontierAttachments.size() > 0) {
            currentFrontierAttachment = frontierAttachments.get(index);
            double probability = frontier.getProbability(currentFrontierAttachment);
            String status_str = String.format("Probability of Attachment: %.4f", probability);
            //mainMenu.statusLabel.setText(status_str);
            right_status.setValue(status_str);
            assembly.placePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().getX(), currentFrontierAttachment.getOffset().getY());
        }
    }

    private void updateAttachTime(double time) {
        String time_str = String.format("%.4f", time);
        right_status.setValue("Last Attachment took " + time_str + " time units");
        //mainMenu.statusLabel.setText("Last Attachment took " + time_str + "ms");
    }

    public void forward() {
        step(1, true);
        java.util.List<FrontierElement> attached = assembly.getAttached();
        if (!attached.isEmpty()) {
            updateAttachTime(attached.get(attached.size() - 1).getAttachTime());
        }
    }

    public void fast_forward() {
        step(2, true);
        java.util.List<FrontierElement> attached = assembly.getAttached();
        if (!assembly.getAttached().isEmpty()) {
            updateAttachTime(attached.get(attached.size() - 1).getAttachTime());
        }
    }

    public void backward() {
        step(-1, true);
    }

    public void fast_backward() {
        step(-2, true);
    }

    public void clear_seed() {
        assembly.clearSeed();
    }

    public void play() {
        stopped = false;
        boolean anyAttached = false;
        resetFrontier();
        while (!frontier.isEmpty()) {
            if (stopped) {
                // draw the entire grid when stopping, to see the frontier of the items.
                placeFrontierOnGrid();
                drawGrid();
                break;
            } else {
                anyAttached = true;
                playHelper();
            }
        }
        if (anyAttached) {
            updateAttachTime(assembly.getAttached().get(-1).getAttachTime());
        }
        //return null;
    }

    private void playHelper() {
        step(1, false);
        //get the latest attached frontier element
        paintPolytile(assembly.getAttached().get(assembly.getAttached().size() - 1));
    }

    public void paintPolytile(FrontierElement attachedFrontierElement) {
        getCanvas().drawTileOnGrid(attachedFrontierElement);
        // commented code is to get the square of the new polytile painted(to avoid painting all)
        // needs work and may not be needed at all.
//        PolyTile attached = attachedFrontierElement.getPolyTile();
//        // find the rectangle to repaint (highest x, highest y)*diameter centered on polytile location
//        int highestX =0, highestY=0, lowX =0, lowY =0;
//        for (Tile t : attached.getTiles()){
//            if (t.getLocation().x > highestX)
//                highestX = t.getLocation().x;
//            if (t.getLocation().y > highestY)
//                highestY = t.getLocation().y;
//
//            if (t.getLocation().x < lowX)
//                lowX = t.getLocation().x;
//            if (t.getLocation().y < lowY)
//                lowY = t.getLocation().y;
//        }
//        int x = (attachedFrontierElement.getOffset().x+lowX)*canvas.getTileDiameter()+canvas.getOffset().x - canvas.getTileDiameter()/2;
//        int y = (-attachedFrontierElement.getOffset().y+highestY)*canvas.getTileDiameter()+canvas.getOffset().y - canvas.getTileDiameter()/2;
//        int w = canvas.getTileDiameter();
//        int h = canvas.getTileDiameter();
//        canvas.repaint(x,y,w,h);
        getCanvas().repaint();
    }

    public void updateTileConfiguration(TileConfiguration tc) {
        assembly.changeTileConfiguration(tc);
    }

    public int getTemperature() {
        return assembly.getTileSystem().getTemperature();
    }

    public void setTemperature(int temperature) {
        resetFrontier();
        assembly.changeTemperature(temperature);
    }

    public ObservableList<PolyTile> getTileSet() {
        return assembly.getTileSystem().getTileTypes();
    }

    public int getWeightOption() {
        return assembly.getTileSystem().getWeightOption();
    }

    public void setWeightOption(int option) {
        TileSystem ts = assembly.getTileSystem();
        int old_option = ts.getWeightOption();
        try {
            ts.setWeightOption(option);
            removeFrontierFromGrid();
            assembly.changeTileSystem(ts);
            frontier = assembly.getFrontier();
        } catch (InvalidObjectException ignored) {

        }
    }

    public void changeTileSystem(TileSystem ts) {
        assembly.changeTileSystem(ts);
    }

    public TileSystem getTileSystem() {
        return assembly.getTileSystem();
    }

    public void clearAssemblyReseed(PolyTile pt) {
        frontier.clear();
        assembly.getFrontier().clear();
        assembly.Grid.clear();
        assembly.placeSeed(pt);
        assembly.getOpenGlues();

        frontier = assembly.calculateFrontier();
        getCanvas().reset();
        placeFrontierOnGrid();
        drawGrid();
    }

    public void placeFrontierOnGrid() {
        if (assembly.Grid.isEmpty()) {
            for (PolyTile pt : assembly.getTileSystem().getTileTypes()) {
                frontier.add(new FrontierElement(new Coordinate(0, 0), new Coordinate(0, 0), pt, 4));
            }
        }
        for (FrontierElement fe : frontier) {
            assembly.Grid.put(fe.getLocation(), getFrontierPolyTile().getTile(0, 0));
        }
    }

    public void zoomInDraw() {
        int tileDiameter = getCanvas().getTileDiameter();
        if (tileDiameter < getContent().getWidth()) {
            System.out.println(getContent().getWidth() + " " + tileDiameter);
            getCanvas().setTileDiameter((int) (tileDiameter * 1.5));
        } else return;

        getCanvas().reset();
        placeFrontierOnGrid();
        drawGrid();
    }

    public void zoomOutDraw() {
        int tileDiameter = getCanvas().getTileDiameter();
        if (tileDiameter > 2) {
            getCanvas().setTileDiameter((int) (tileDiameter * .75));
        } else return;

        getCanvas().reset();
        placeFrontierOnGrid();
        drawGrid();
    }


    private PolyTile getFrontierPolyTile() {
        if (frontierTile == null) {
            frontierTile = new PolyTile("");
            frontierTile.setColor("428bca");
            String glues[] = {null, null, null, null};
            frontierTile.addTile(0, 0, glues);
            frontierTile.setFrontier();
            return frontierTile;
        } else return frontierTile;
    }

    public void step(int i, boolean notify) {
        resetFrontier();

        switch (i) {
            case 1:
                assembly.attach(notify);
                break;
            case 2:
                assembly.attachAll(notify);
                break;
            case -1:
                assembly.detach(notify);
                break;
            case -2:
                assembly.detachAll(notify);
                break;
        }
    }

    public void update(Observable o, Object arg) {
        Pair<String, FrontierElement> pair = (Pair<String, FrontierElement>) arg;
        assembly = (Assembly) o;
        String msg = pair.getKey();
        FrontierElement fe = pair.getValue();

        if (msg.equals("attach")) {
            frontier = assembly.getFrontier();
            placeFrontierOnGrid();
            drawGrid();
//            paintPolytile(fe);
//            frontier = assembly.getFrontier();
//            for(FrontierElement new_fe : frontier){
//                paintPolytile(new_fe);
//            }
//            placeFrontierOnGrid();
        } else if (msg.equals("detach")) {
            resetFrontier();
            frontier = assembly.getFrontier();
            System.out.println("My Frontier size: " + frontier.size());
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();
        } else if (msg.equals("refresh")) {
            System.out.println("reset");
            resetFrontier();
            frontier = assembly.calculateFrontier();
            frontier.printDebugInformation();
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();

        } else if (msg.equals("Tile System")) {
            //resetFrontier();

            frontier = assembly.getFrontier();
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();
        }

    }

}

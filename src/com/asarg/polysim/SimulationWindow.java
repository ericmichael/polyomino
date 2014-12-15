package com.asarg.polysim;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import com.asarg.polysim.adapters.graphics.raster.Drawer;
import com.asarg.polysim.adapters.graphics.raster.TestCanvas;
import javafx.util.Pair;

public class SimulationWindow extends JFrame implements MouseWheelListener, MouseMotionListener, MouseListener, ComponentListener, Observer{

    TestCanvas canvas;


    TileEditorWindow tileEditorWindow ;

    GUI mainMenu;
    int width;
    int height;

    Point lastMouseXY = new Point(width,height);
    int dragCount = 0;

    Assembly assembly;
    Frontier frontier;
    PolyTile frontierTile;
    Workspace workspace;

    boolean frontierClick = false;
    ArrayList<FrontierElement> frontierAttachments;
    int frontierIndex = 0;
    FrontierElement currentFrontierAttachment = null;
    Point frontierClickPoint = null;

    public SimulationWindow(int w, int h, final Workspace workspace)
    {
        tileEditorWindow = workspace.createEditorWindow();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        width = w;
        height = h;

        this.workspace = workspace;
        assembly = workspace.assembly;
        frontier = assembly.calculateFrontier();
        setLayout(new BorderLayout());

        canvas = new TestCanvas();
        add(canvas, BorderLayout.CENTER);
        canvas.setSize(width, height);

        addGUIAndActionListeners();

        this.setJMenuBar(mainMenu);
        add(mainMenu.stepControlToolBar,BorderLayout.NORTH);
        add(mainMenu.statusPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        placeFrontierOnGrid();
        drawGrid();
    }

    public void resetFrontier(){
        exitFrontierMode();
        removeFrontierFromGrid();
    }

    private void updateAttachTime(double time){
        String time_str = String.format("%.4f", time);
        mainMenu.statusLabel.setText("Last Attachment took " + time_str + "ms");
    }

    public void step(int i){
        resetFrontier();

        switch(i){
            case 1: updateAttachTime(assembly.attach()); break;
            case 2: updateAttachTime(assembly.attachAll()); break;
            case -1: assembly.detach(); break;
            case -2: assembly.detachAll(); break;

        }
    }

    public void play(){
        resetFrontier();
        updateAttachTime(assembly.attach());
        frontier = assembly.calculateFrontier();
        placeFrontierOnGrid();
        // get the latest attached frontier element
        paintPolytile(assembly.getAttached().get(assembly.getAttached().size() - 1));

    }

    private void addGUIAndActionListeners(){
        mainMenu = new GUI(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        addComponentListener(this);
    }

    public void update(Observable o, Object arg) {
        Pair<String, FrontierElement> pair = (Pair<String, FrontierElement>) arg;
        assembly = (Assembly) o;
        String msg = pair.getKey();
        FrontierElement fe = pair.getValue();

        if(msg.equals("attach")){
            frontier = assembly.getFrontier();
            placeFrontierOnGrid();
            drawGrid();
//            paintPolytile(fe);
//            frontier = assembly.getFrontier();
//            for(FrontierElement new_fe : frontier){
//                paintPolytile(new_fe);
//            }
//            placeFrontierOnGrid();
        }else if(msg.equals("detach")){
            resetFrontier();
            frontier = assembly.getFrontier();
            System.out.println("My Frontier size: " + frontier.size());
            canvas.reset();
            placeFrontierOnGrid();
            drawGrid();
        }else if(msg.equals("refresh")){
            resetFrontier();
            frontier = assembly.calculateFrontier();
            canvas.reset();
            placeFrontierOnGrid();
            drawGrid();

        }else if(msg.equals("Tile System")){
            resetFrontier();
            frontier = assembly.getFrontier();
            canvas.reset();
            placeFrontierOnGrid();
            drawGrid();
        }

    }


    public void drawGrid()    {
        //PlaceFrontierOnGrid();
        canvas.drawGrid(assembly.Grid);
        canvas.repaint();
    }

    public void paintPolytile(FrontierElement attachedFrontierElement){
        canvas.drawTileOnGrid(attachedFrontierElement);
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
        canvas.repaint();
    }

    private PolyTile getFrontierPolyTile(){
        if(frontierTile==null){
            frontierTile = new PolyTile("");
            frontierTile.setColor("428bca");
            String glues[]= {null, null, null, null};
            frontierTile.addTile(0,0, glues);
            frontierTile.setFrontier();
            return frontierTile;
        }else return frontierTile;
    }

    public void placeFrontierOnGrid() {
        if (assembly.Grid.isEmpty()){
            for (PolyTile pt: assembly.getTileSystem().getTileTypes()){
                frontier.add(new FrontierElement(new Point(0,0), new Point(0,0), pt, 4));
            }
        }
        for (FrontierElement fe : frontier){
            assembly.Grid.put(fe.getLocation(), getFrontierPolyTile().getTile(0,0));
        }
    }

    public void removeFrontierFromGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.remove(fe.getLocation());
        }
    }

    public void zoomInDraw() {
        int tileDiameter = canvas.getTileDiameter();
        if(tileDiameter < getWidth()) {
            System.out.println(getWidth() + " " + tileDiameter);
            canvas.setTileDiameter((int) (tileDiameter * 1.5));
        }
        else return;

        canvas.reset();
        placeFrontierOnGrid();
        drawGrid();
    }
    public void zoomOutDraw() {
        int tileDiameter = canvas.getTileDiameter();
        if(tileDiameter > 2) {
            canvas.setTileDiameter((int) (tileDiameter * .75));
        }
        else return;

        canvas.reset();
        placeFrontierOnGrid();
        drawGrid();
    }

    private void removeCurrentFrontierAttachment() {
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
            mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
        } else {
            assembly.Grid.remove(frontierClickPoint);
        }
        canvas.reset();
    }

    private void addFrontierAttachment(int index) {
        if( frontierAttachments.size()>0){
            currentFrontierAttachment = frontierAttachments.get(index);
            double probability = frontier.getProbability(currentFrontierAttachment);
            String status_str = String.format("Probability of Attachment: %.4f", probability);
            mainMenu.statusLabel.setText(status_str);
            assembly.placePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() == 1)
        {
            if(frontierClick){
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null){
                    frontierIndex=0;
                    addFrontierAttachment(frontierIndex);

                }else if(frontierIndex > 0){
                    frontierIndex-=1;
                    addFrontierAttachment(frontierIndex);
                }else {
                    frontierIndex=frontierAttachments.size()-1;
                    addFrontierAttachment(frontierIndex);
                }

                drawGrid();
                return;
            }
            zoomOutDraw();
        }
        else if(e.getWheelRotation() == -1) {
            if(frontierClick){
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null) {
                    frontierIndex=0;
                    addFrontierAttachment(frontierIndex);
                }
                else if(frontierIndex+1 < frontierAttachments.size()){
                    frontierIndex+=1;
                    addFrontierAttachment(frontierIndex);
                }else{
                    frontierIndex=0;
                    addFrontierAttachment(0);
                }
                drawGrid();
                return;
            }
            zoomInDraw();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        canvas.translateOffset(e.getX() - lastMouseXY.x, e.getY() - lastMouseXY.y);
        lastMouseXY=e.getPoint();
        canvas.reset();
        drawGrid();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void exitFrontierMode(){
        if(currentFrontierAttachment!=null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
        frontierClick = false;
        frontierClickPoint = null;
        frontierAttachments = null;
        frontierIndex = 0;
        currentFrontierAttachment=null;
        mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
//        canvas.reset();
    }

    private void processFrontierClick(Point clicked, Tile clicked_tile){
        PolyTile clicked_pt = clicked_tile.getParent();
        if(!frontierClick){
            mainMenu.statusLabelPreviousText = mainMenu.statusLabel.getText();
        }else{
            placeFrontierOnGrid();
        }
        frontierClick = clicked_pt.isFrontier();
        if(frontierClick) {
            //kick me out of any previous frontier state
            exitFrontierMode();
            frontierClick=true;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == canvas) {
            Point clicked = Drawer.TileDrawer.getGridPoint(e.getPoint(), canvas.getOffset(), canvas.getTileDiameter());
            Tile clicked_tile = assembly.Grid.get(clicked);
            if(clicked_tile!=null){
                processFrontierClick(clicked, clicked_tile);
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseXY = e.getPoint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragCount = 0;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void componentResized(ComponentEvent e) {
        remove(canvas);
        add(canvas);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}

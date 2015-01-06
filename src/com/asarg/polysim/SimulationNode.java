package com.asarg.polysim;

import com.asarg.polysim.adapters.graphics.raster.Drawer;
import com.asarg.polysim.adapters.graphics.raster.TestCanvas;
import javafx.embed.swing.SwingNode;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by ericmartinez on 1/5/15.
 */
public class SimulationNode extends SwingNode implements Observer,MouseWheelListener, MouseMotionListener, MouseListener {
    public Assembly assembly;

    Frontier frontier;
    PolyTile frontierTile;
    boolean frontierClick = false;
    ArrayList<FrontierElement> frontierAttachments;
    int frontierIndex = 0;
    FrontierElement currentFrontierAttachment = null;
    Point frontierClickPoint = null;

    Point lastMouseXY = new Point(800, 600);
    int dragCount = 0;

    public SimulationNode(){
        super();
    }

    public SimulationNode(Assembly asm, TestCanvas tc){
        this.assembly = asm;
        assembly.addObserver(this);
        frontier = assembly.calculateFrontier();
        placeFrontierOnGrid();
        setCanvas(tc);
        getCanvas().addMouseWheelListener(this);
        getCanvas().addMouseListener(this);
        getCanvas().addMouseMotionListener(this);
    }

    public void setCanvas(TestCanvas tc){
        setContent(tc);
    }

    public TestCanvas getCanvas(){
        return (TestCanvas) getContent();
    }

    public void drawGrid(){
        getCanvas().drawGrid(assembly.Grid);
        getCanvas().repaint();
    }

    public void resetFrontier(){
        exitFrontierMode();
        removeFrontierFromGrid();
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
        //mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
//        canvas.reset();
    }

    public void removeFrontierFromGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.remove(fe.getLocation());
        }
    }

    private void processFrontierClick(Point clicked, Tile clicked_tile){
        PolyTile clicked_pt = clicked_tile.getParent();
        if(!frontierClick){
            //mainMenu.statusLabelPreviousText = mainMenu.statusLabel.getText();
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

    private void removeCurrentFrontierAttachment() {
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
            //mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
        } else {
            assembly.Grid.remove(frontierClickPoint);
        }
        getCanvas().reset();
    }

    private void addFrontierAttachment(int index) {
        if( frontierAttachments.size()>0){
            currentFrontierAttachment = frontierAttachments.get(index);
            double probability = frontier.getProbability(currentFrontierAttachment);
            String status_str = String.format("Probability of Attachment: %.4f", probability);
            //mainMenu.statusLabel.setText(status_str);
            assembly.placePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
    }

    private void updateAttachTime(double time){
        String time_str = String.format("%.4f", time);
        //mainMenu.statusLabel.setText("Last Attachment took " + time_str + "ms");
    }

    public void forward(){ step(1,true); }
    public void fast_forward(){ step(2, true); }
    public void backward(){ step(-1, true); }
    public void fast_backward(){ step(-2, true); }

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

    public void zoomInDraw() {
        int tileDiameter = getCanvas().getTileDiameter();
        if(tileDiameter < getContent().getWidth()) {
            System.out.println(getContent().getWidth() + " " + tileDiameter);
            getCanvas().setTileDiameter((int) (tileDiameter * 1.5));
        }
        else return;

        getCanvas().reset();
        placeFrontierOnGrid();
        drawGrid();
    }
    public void zoomOutDraw() {
        int tileDiameter = getCanvas().getTileDiameter();
        if(tileDiameter > 2) {
            getCanvas().setTileDiameter((int) (tileDiameter * .75));
        }
        else return;

        getCanvas().reset();
        placeFrontierOnGrid();
        drawGrid();
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

    public void step(int i, boolean notify) {
        resetFrontier();

        switch (i) {
            case 1:
                updateAttachTime(assembly.attach(notify));
                break;
            case 2:
                updateAttachTime(assembly.attachAll(notify));
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
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();
        }else if(msg.equals("refresh")){
            resetFrontier();
            frontier = assembly.calculateFrontier();
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();

        }else if(msg.equals("Tile System")){
            resetFrontier();
            frontier = assembly.getFrontier();
            getCanvas().reset();
            placeFrontierOnGrid();
            drawGrid();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == getCanvas()) {
            Point clicked = Drawer.TileDrawer.getGridPoint(e.getPoint(), getCanvas().getOffset(), getCanvas().getTileDiameter());
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
    public void mouseWheelMoved(MouseWheelEvent e) {
//        if (!mainMenu.stopped){
//            System.out.println("Do not drag while playing!");
//            JOptionPane.showMessageDialog(null, "Do not drag while playing!\nAssembly must be paused.");
//            return;
//        }

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
//        if (!mainMenu.stopped){
//            System.out.println("Do not drag while playing!");
//            JOptionPane.showMessageDialog(null,"Do not drag while playing!\nAssembly must be paused.");
//            return;
//        }
        getCanvas().translateOffset(e.getX() - lastMouseXY.x, e.getY() - lastMouseXY.y);
        lastMouseXY=e.getPoint();
        getCanvas().reset();
        drawGrid();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}

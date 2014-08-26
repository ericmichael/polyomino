package com.asarg.polysim;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestCanvasFrame extends JFrame implements MouseWheelListener, MouseMotionListener, MouseListener,KeyListener, ComponentListener{

    TestCanvas tc;
    JMenuBar tcfBar = new JMenuBar();
    JMenuItem nextStepMI = new JMenuItem("Step >");
    JMenuItem previousStepMI = new JMenuItem("< Previous");
    ControlButton next = new ControlButton("forward");
    ControlButton prev = new ControlButton("backward");
    ControlButton play = new ControlButton("play");
    ControlButton fastf = new ControlButton("fast-forward");
    ControlButton fastb = new ControlButton("fast-backward");

    int width;
    int height;

    Point lastMouseXY = new Point(width,height);
    int dragCount = 0;

    Assembly assembly;
    Frontier frontier;
    PolyTile frontierTile;


    public TestCanvasFrame(int w, int h, final Assembly assembly)
    {
        this.assembly = assembly;
        width = w;
        height = h;
        frontier = this.assembly.calculateFrontier();
        setLayout(new BorderLayout());
        tc = new TestCanvas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addComponentListener(this);


        add(tc, BorderLayout.SOUTH);
        tc.setSize(w,h);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(next))
                {
                    if(!frontier.isEmpty()) {
                        assembly.attach();
                        frontier = assembly.calculateFrontier();
                        drawGrid();
                    }
                }else if(e.getSource().equals(prev))
                {
                    if(!assembly.getAttached().isEmpty()) {
                        assembly.detach();
                        tc.reset();
                        frontier = assembly.calculateFrontier();
                        drawGrid();
                    }
                }else if(e.getSource().equals(play)){
                    while(!frontier.isEmpty()){
                        assembly.attach();
                        tc.reset();
                        frontier = assembly.calculateFrontier();
                        drawGrid();
                        try {
                            Thread.sleep(1000);
                        }catch(InterruptedException ie) {
                        }
                    }
                }else if(e.getSource().equals(fastb)){
                    while(!assembly.getAttached().isEmpty()){
                        assembly.detach();
                    }
                    tc.reset();
                    frontier = assembly.calculateFrontier();
                    drawGrid();
                }else if(e.getSource().equals(fastf)){
                    while(!frontier.isEmpty()){
                        assembly.attach();
                        frontier = assembly.calculateFrontier();
                    }
                    tc.reset();
                    drawGrid();

                }

            }
        };

        next.addActionListener(al);
        prev.addActionListener(al);
        play.addActionListener(al);
        fastf.addActionListener(al);
        fastb.addActionListener(al);
        tcfBar.add(fastb);
        tcfBar.add(prev);
        tcfBar.add(play);
        tcfBar.add(next);
        tcfBar.add(fastf);
        setJMenuBar(tcfBar);
        pack();
        setVisible(true);
        drawGrid();
    }


    public void drawGrid()
    {
        PlaceFrontierOnGrid();
        tc.drawGrid(assembly.Grid);
        repaint();
        RemoveFrontierFromGrid();

    }

    private PolyTile getFrontierPolyTile(){
        if(frontierTile==null){
            frontierTile = new PolyTile("");
            frontierTile.setColor("428bca");
            String glues[]= {null, null, null, null};
            frontierTile.addTile(0,0, glues);
            return frontierTile;
        }else return frontierTile;
    }

    private void PlaceFrontierOnGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.put(fe.getLocation(), getFrontierPolyTile().getTile(0,0));
        }
    }

    private void RemoveFrontierFromGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.remove(fe.getLocation());
        }
    }

    public void zoomInDraw()
    {
        int tileDiameter = tc.getTileDiameter();
        if(tileDiameter< width/2)
            tc.setTileDiameter((int)(tileDiameter*1.25));
        else return;

        tc.reset();
        drawGrid();
        repaint();
    }
    public void zoomOutDraw()
    {
        int tileDiameter = tc.getTileDiameter();
        if(tileDiameter > 10) {
            tc.setTileDiameter((int) (tileDiameter * .75));
        }
        else return;

        tc.reset();
        drawGrid();
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {


        if(e.getWheelRotation() == 1)
        {
           zoomOutDraw();
        }
        if(e.getWheelRotation() == -1)
        {
           zoomInDraw();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        tc.translateOffset(e.getX() - lastMouseXY.x, e.getY() - lastMouseXY.y);
        lastMouseXY=e.getPoint();
        tc.reset();
        drawGrid();
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
    public void keyTyped(KeyEvent e) {





    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            zoomInDraw();
        }else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            zoomOutDraw();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(!frontier.isEmpty()) {
                assembly.attach();
                drawGrid();
                frontier = assembly.calculateFrontier();
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            assembly.detach();
            tc.reset();
            drawGrid();
            frontier = assembly.calculateFrontier();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void componentResized(ComponentEvent e) {


        remove(tc);
        add(tc);
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

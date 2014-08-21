package com.asarg.polysim;




import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestCanvasFrame extends JFrame implements MouseWheelListener, MouseMotionListener, MouseListener{

    TestCanvas tc;
    JMenuBar tcfBar = new JMenuBar();
    JMenuItem nextStepMI = new JMenuItem("Step >");
    JMenuItem previousStepMI = new JMenuItem("< Previous");

    int width;
    int height;

    Point lastMouseXY = new Point(width,height);
    int dragCount = 0;

    Assembly assembly;
    Frontier frontier;


    public TestCanvasFrame(int w, int h, final Assembly assembly)
    {
        this.assembly = assembly;
        width = w;
        height = h;
        frontier = this.assembly.calculateFrontier();
        setLayout(new BorderLayout());
        tc = new TestCanvas(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        add(tc, BorderLayout.SOUTH);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(nextStepMI))
                {
                    if(!frontier.isEmpty()) {
                        assembly.attach();
                        drawGrid();
                        frontier = assembly.calculateFrontier();
                    }
                }else if(e.getSource().equals(previousStepMI))
                {
                    assembly.detach();
                    tc.reset();
                    drawGrid();
                    frontier = assembly.calculateFrontier();
                }

            }
        };

        nextStepMI.addActionListener(al);
        previousStepMI.addActionListener(al);
        tcfBar.add(previousStepMI);
        tcfBar.add(nextStepMI);
        setJMenuBar(tcfBar);
        pack();
        setVisible(true);
        drawGrid();
    }

    public void drawPolyTile(PolyTile pt)
    {
        tc.drawPolyTile(pt);


    }
    public void drawGrid()
    {
        tc.drawGrid(assembly.Grid);
        repaint();

    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if(e.getWheelRotation() == 1)
        {
            tc.setTileDiameter(tc.getTileDiameter()-10);
        }
        if(e.getWheelRotation() == -1)
        {
            tc.setTileDiameter(tc.getTileDiameter()+10);
        }
        tc.reset();
        drawGrid();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("SDSDF");
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
}

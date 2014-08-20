package com.asarg.polysim;




import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TestCanvasFrame extends JFrame {

    TestCanvas tc;
    JMenuBar tcfBar = new JMenuBar();
    JMenuItem nextStepMI = new JMenuItem("Step >");
    JMenuItem previousStepMI = new JMenuItem("< Previous");

    int width;
    int height;

    Assembly assembly;
    java.util.List<Pair<Point, PolyTile>> frontier;


    public TestCanvasFrame(int w, int h, final Assembly assembly)
    {
        this.assembly = assembly;
        width = w;
        height = h;
        frontier = this.assembly.calculateFrontier();
        setLayout(new BorderLayout());
        tc = new TestCanvas(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FrontierCanvas frontierCanvas = new FrontierCanvas(w,h);
        add(frontierCanvas, BorderLayout.NORTH);
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



    class FrontierCanvas extends JPanel
    {
        Dimension frameRes = new Dimension();
        Dimension myRes = new Dimension();
        BufferedImage frontierBFI;
        Graphics2D frontierGFX;
        FrontierCanvas(int w, int h)
        {
            myRes.setSize(w, h/5);
            frontierBFI = new BufferedImage(myRes.width, myRes.height, BufferedImage.TYPE_INT_ARGB);
            frontierGFX = frontierBFI.createGraphics();
            frontierGFX.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        }




        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(myRes.width, myRes.height);
        }
        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(frontierBFI,0,0,null);


        }

        public void drawFrontier()
        {

            frontierGFX.setComposite(AlphaComposite.Clear);
            frontierGFX.fillRect(0,0, myRes.width, myRes.height);
            frontierGFX.setComposite(AlphaComposite.SrcOver);



        }


    }


}

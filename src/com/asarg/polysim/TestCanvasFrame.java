package com.asarg.polysim;




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

    TestCanvasFrame(int w, int h)
    {
        setLayout(new BorderLayout());
        tc = new TestCanvas(w,h);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FrontierCanvas frontierCanvas = new FrontierCanvas(w,h);
        add(frontierCanvas, BorderLayout.NORTH);
        add(tc, BorderLayout.SOUTH);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(nextStepMI))
                {

                    System.out.println("yo");
                }

            }
        };

        nextStepMI.addActionListener(al);
        tcfBar.add(nextStepMI);
        setJMenuBar(tcfBar);
        pack();
    }

    public void drawPolyTile(PolyTile pt)
    {
        tc.drawPolyTile(pt);


    }
    public void drawGrid(HashMap<Point, Tile> hmpt)
    {
        tc.drawGrid(hmpt);
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

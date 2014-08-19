package com.asarg.polysim;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class TestCanvasFrame extends JFrame {

    TestCanvas tc;

    TestCanvasFrame()
    {
        setLayout(new BorderLayout());
        tc = new TestCanvas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel frontierCanvas = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(1280,200);
            }
        };
      //  add(frontierCanvas, BorderLayout.NORTH);
        add(tc, BorderLayout.CENTER);
      // JButton stepForwards = new JButton("Step >");
      // add(stepForwards);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        pack();
    }

    public void drawPolyTile(PolyTile pt)
    {
        tc.drawPolyTile(pt);


    }
    public void drawGrid(HashMap<Point, Tile> hmpt)
    {
        tc.drawGrid(hmpt);

    }


}

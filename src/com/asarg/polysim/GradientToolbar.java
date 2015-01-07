package com.asarg.polysim;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ericmartinez on 8/28/14.
 */
public class GradientToolbar extends JToolBar {
    @Override
    protected void paintComponent(Graphics g) {
        // Create the 2D copy
        Graphics2D g2 = (Graphics2D) g.create();

        // Apply vertical gradient
        g2.setPaint(new GradientPaint(0, 0, new Color(0xf2f2f2), 0, getHeight(), new Color(0xb8b8b8)));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Dipose of copy
        g2.dispose();
    }
}

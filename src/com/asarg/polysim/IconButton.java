package com.asarg.polysim;

/**
 * Created by ericmartinez on 8/22/14.
 */
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.IOException;

public class IconButton extends JButton {
    public IconButton() {
        setFocusable(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        try {
            InputStream in = this.getClass().getResourceAsStream("resources/fontawesome.ttf");
            Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
            Font ttfReal = ttfBase.deriveFont(Font.BOLD, 16);
            setFont(ttfReal);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setForeground(new Color(0x5e5e5e));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            setForeground(Color.black);
        }else {
            setForeground(new Color(0x5e5e5e));
        }
        super.paintComponent(g);
    }
}
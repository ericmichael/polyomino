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
        try {
            InputStream in = this.getClass().getResourceAsStream("/fontawesome.ttf");
            Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, in);
            Font ttfReal = ttfBase.deriveFont(Font.BOLD, 24);
            setFont(ttfReal);


        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
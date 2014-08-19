package com.asarg.polysim;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Set;

public class Drawer {


    //clears given graphics object, clip must have been set
    public static  void clearGraphics(Graphics2D g)
    {
        Rectangle bounds = g.getClipBounds();
        g.setComposite(AlphaComposite.Clear);
        g.drawRect(0,0, bounds.width, bounds.height);
        g.setComposite(AlphaComposite.Src);
    }

    public static class TileDrawer
    {
        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter)
        {
            Rectangle2D labelBounds = g.getFont().getStringBounds(tile.getLabel(), g.getFontRenderContext());
            int colorInt = Integer.parseInt(tile.getColor(), 16);
            g.setColor(new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF, 100));
            g.fillRect(x,y, diameter, diameter);
            g.setColor(Color.black);
            g.setStroke(new BasicStroke(diameter/20));
            g.drawRect(x,y,diameter,diameter);
            g.drawString(tile.getLabel(), x + diameter/2 - (int)labelBounds.getWidth()/2, y + diameter/2 + (int)labelBounds.getHeight()/2);

        }
        public static void drawTiles(Graphics2D g, Set<Map.Entry<Point, Tile>> tiles, int diameter, Point offset)
        {

            for(Map.Entry<Point, Tile> mep : tiles) {
                Point pt = mep.getKey();
                Tile tile = mep.getValue();
                drawTile(g,tile, pt.x * diameter + offset.x - diameter / 2,-pt.y * diameter + offset.y - diameter / 2, diameter );
            }
        }
    }

}

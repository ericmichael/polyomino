package com.asarg.polysim;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class Drawer {

    //clears given graphics object, clip must have been set
    public static  void clearGraphics(Graphics2D g)
    {
        Rectangle bounds = g.getClipBounds();
        g.setComposite(AlphaComposite.Clear);
        g.drawRect(0,0, bounds.width, bounds.height);
        g.setComposite(AlphaComposite.SrcOver);
    }

    public static class TileDrawer
    {
        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter)
        {
            int colorInt = Integer.parseInt(tile.getColor(), 16);
            g.setColor(new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF));

            g.fillRect(x,y, diameter, diameter);

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

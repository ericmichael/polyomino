package com.asarg.polysim;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;


public class Drawer {


    //clears given graphics object, clip must have been set
    public static void clearGraphics(Graphics2D g) {
        Rectangle bounds = g.getClipBounds();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, bounds.width, bounds.height);
        g.setComposite(AlphaComposite.Src);

    }

    public static Dimension getStringPixelDimension(Graphics2D gContext, String string) {
        Rectangle2D stringBounds = gContext.getFontMetrics().getStringBounds(string, gContext);
        int stringBoundWidth = (int) stringBounds.getWidth();
        int stringBoundHeight = (int) stringBounds.getHeight();
        final BufferedImage tBFI = new BufferedImage(stringBoundWidth, stringBoundHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tGFX = tBFI.createGraphics();
        tGFX.setFont(gContext.getFont());
        tGFX.setColor(Color.WHITE);
        tGFX.fillRect(0, 0, stringBoundWidth, stringBoundHeight);
        tGFX.setColor(Color.BLACK);
        tGFX.drawString(string, 0, stringBoundHeight);


        int maxX = 0;
        int minX = 0;
        int maxY = 0;
        int minY = 0;

        //minX scan
        outerXLoop1:
        for (int i = 0; i < stringBoundWidth; i++) {
            for (int j = 0; j < stringBoundHeight; j++) {

                if (tBFI.getRGB(i, j) == Color.BLACK.getRGB()) {

                    minX = i;
                    break outerXLoop1;
                }



            }
        }
        //maxX scan
        outerXLoop2:
        for (int i = stringBoundWidth - 1; i > 0; i--) {
            for (int j = 0; j < stringBoundHeight; j++) {
                if (tBFI.getRGB(i, j)== Color.BLACK.getRGB()) {

                    maxX = i;
                    break outerXLoop2;
                }

            }
        }
        //minY scan
        outerYLoop1:
         for(int i = 0; i < stringBoundHeight; i++ )
         {
             for(int j = 0; j < stringBoundWidth; j++)
             {
                 if(tBFI.getRGB(j,i)== Color.BLACK.getRGB())
                 {

                     minY = i;
                     break outerYLoop1;
                 }
             }
         }

        //maxY scan
        outerYLoop2:
        for(int i = stringBoundHeight -1; i > 0; i-- )
        {
            for(int j = 0; j < stringBoundWidth; j++)
            {
                if(tBFI.getRGB(j,i) == Color.BLACK.getRGB())
                {

                    maxY = i;
                    break outerYLoop2;
                }
            }
        }


        return  new Dimension(maxX - minX, maxY - minY);
    }


    public static class TileDrawer {

        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter) {


            AffineTransform gOriginalATransform = g.getTransform();

            String tileLabel = tile.getLabel();
            String northGlue = tile.getGlueN();
            String eastGlue = tile.getGlueE();
            String southGlue = tile.getGlueS();
            String westGlue = tile.getGlueW();
            //System.out.println(westGlue + "@334234234");

            g.setFont(g.getFont().deriveFont((float)(diameter/3)));
            Dimension labelBounds = getStringPixelDimension(g,tileLabel );


            Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(tileLabel, g);

            // consider the blank space between the render lines and the start of the first baseline pixel of the string
            int labelXShift =(int) Math.ceil(((int)Math.ceil(stringBounds.getWidth()) - labelBounds.width) /2);



            //get hex color string to int, then create new color out of the rgb
            int colorInt = Integer.parseInt(tile.getColor(), 16);
            g.setColor(new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF, 100));

            // the tiles fill color
            g.fillRect(x, y, diameter, diameter);

            //now a black border with thickness based on diameter
            g.setColor(Color.black);
            g.setStroke(new BasicStroke(diameter / 20));
            g.drawRect(x, y, diameter, diameter);

            //draw tile label
            g.drawString(tileLabel, x + (diameter / 2) - (labelBounds.width/ 2) - labelXShift, y + (diameter / 2) + (labelBounds.height / 2) );

            //glue drawing
            if (westGlue != null && !westGlue.isEmpty()) {
                g.setFont(g.getFont().deriveFont((float)(diameter/4)));
                Dimension westGluePixelDim = getStringPixelDimension(g,westGlue);
                Rectangle2D r2d = g.getFontMetrics().getStringBounds(westGlue, g);
                //get string image
                BufferedImage tBFI = new BufferedImage((int) r2d.getHeight(), (int)r2d.getWidth(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D tGFX = tBFI.createGraphics();
                tGFX.setFont(g.getFont());
                tGFX.setColor(g.getColor());
                AffineTransform posNinety = new AffineTransform();
                posNinety.setToRotation(Math.toRadians(90),x+westGluePixelDim.height, y+ westGluePixelDim.width/2 + diameter/2);
                g.setTransform(posNinety);
                g.drawString(  westGlue,  x+westGluePixelDim.height,  y- westGluePixelDim.width/2 + diameter/2);
                g.setTransform(gOriginalATransform);
                //System.out.println("SDFS");
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.setFont(g.getFont().deriveFont((float)(diameter/4)));
                Dimension eastGluePixelDim = getStringPixelDimension(g,eastGlue);
                AffineTransform negNinety = new AffineTransform();
                negNinety.setToRotation(Math.toRadians(-90),x+ diameter - eastGluePixelDim.height, y+ eastGluePixelDim.width/2 + diameter/2 );
                g.setTransform(negNinety);
                g.drawString(eastGlue, x+ diameter - eastGluePixelDim.height, y +eastGluePixelDim.width/2 + diameter/2);
                g.setTransform(gOriginalATransform);
                //System.out.println("SDFS");
            }





        }


        public static void drawTiles(Graphics2D g, Set<Map.Entry<Point, Tile>> tiles, int diameter, Point offset) {



            for (Map.Entry<Point, Tile> mep : tiles) {
                Point pt = mep.getKey();
                Tile tile = mep.getValue();
                drawTile(g, tile, pt.x * diameter + offset.x - diameter / 2, -pt.y * diameter + offset.y - diameter / 2, diameter);
            }
        }
    }

}

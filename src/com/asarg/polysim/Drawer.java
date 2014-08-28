package com.asarg.polysim;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
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
    public static Point calculateGridDimension(List<Point> gridPoints)
    {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

          for(Point p : gridPoints)
          {
              if(p.x > maxX)
              {
                  maxX = p.x;
              }
              if(p.x < minX)
              {
                  minX = p.x;
              }
              if(p.y > maxY)
              {
                  maxY = p.y;
              }
              if(p.y < minY)
              {
                  minY = p.y;
              }
          }

        return new Point(1+maxX - minX,1+maxY -minY);

    }


    public static Dimension getStringPixelDimension(Graphics2D gContext, String string) {
        Rectangle2D stringBounds = gContext.getFontMetrics().getStringBounds(string, gContext);
        int stringBoundWidth = (int) stringBounds.getWidth();
        int stringBoundHeight = (int) stringBounds.getHeight();
        if(stringBoundWidth > 2 && stringBoundWidth > 2) {
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
                    if (tBFI.getRGB(i, j) == Color.BLACK.getRGB()) {

                        maxX = i;
                        break outerXLoop2;
                    }

                }
            }
            //minY scan
            outerYLoop1:
            for (int i = 0; i < stringBoundHeight; i++) {
                for (int j = 0; j < stringBoundWidth; j++) {
                    if (tBFI.getRGB(j, i) == Color.BLACK.getRGB()) {

                        minY = i;
                        break outerYLoop1;
                    }
                }
            }

            //maxY scan
            outerYLoop2:
            for (int i = stringBoundHeight - 1; i > 0; i--) {
                for (int j = 0; j < stringBoundWidth; j++) {
                    if (tBFI.getRGB(j, i) == Color.BLACK.getRGB()) {

                        maxY = i;
                        break outerYLoop2;
                    }
                }
            }
            return  new Dimension(maxX - minX, maxY - minY);
        }
        else return new Dimension(1,1);



    }


    public static class TileDrawer {

        public static Point calculatePolyTileGridDimension(PolyTile polyt)
        {

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            List<Tile> tiles = polyt.getTiles();
            for(Tile t :tiles )
            {
                Point pt = t.getLocation();
                if(pt.x > maxX)
                {
                    maxX = pt.x;
                }
                if(pt.x < minX)
                {
                    minX = pt.x;
                }
                if(pt.y > maxY)
                {
                    maxY = pt.y;
                }
                if(pt.y < minY)
                {
                    minY = pt.y;
                }
            }

            return new Point(1+maxX - minX,1+maxY -minY);

        }
        public static Dimension calculatePolyTilePixelXRange(PolyTile polyt)
        {


            return new Dimension(0,0);
        }
        public static Dimension calculatePolyTilePixelYRange(PolyTile polyt)
        {

            return new Dimension(0,0);
        }

        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter) {



            Rectangle clip = g.getClipBounds();
            if(x>clip.width + diameter || x < 0 -diameter || y>clip.height + diameter || y < 0 -diameter)
                return;
            AffineTransform gOriginalATransform = g.getTransform();

            String tileLabel = tile.getLabel();
            String northGlue = tile.getGlueN();
            String eastGlue = tile.getGlueE();
            String southGlue = tile.getGlueS();
            String westGlue = tile.getGlueW();




            //get hex color string to int, then create new color out of the rgb
            int colorInt = Integer.parseInt(tile.getColor(), 16);
            Color tileColor =new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF, 100);
            g.setColor(tileColor);

            // the tiles fill color
            g.fillRect(x, y, diameter, diameter);

            //change stroke/color for rest
            g.setColor(Color.black);
            g.setStroke(new BasicStroke(diameter / 25));


            //Drawing the glues-------------------------------------

            //west glue flipped
           /* if (westGlue != null && !westGlue.isEmpty()) {

                g.setFont(g.getFont().deriveFont((float)(diameter/8)));
                Dimension westGluePixelDim = getStringPixelDimension(g,westGlue);

                Rectangle2D r2d = g.getFontMetrics().getStringBounds(westGlue, g);
                //get string image
                BufferedImage tBFI = new BufferedImage((int)r2d.getHeight(), westGluePixelDim.width+3, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tGFX = tBFI.createGraphics();
                tGFX.setColor(tileColor);
                tGFX.fillRect(0,0 , (int)r2d.getHeight(), westGluePixelDim.width+3);
                tGFX.setFont(g.getFont());
                tGFX.setColor(g.getColor());
                AffineTransform posNinety = new AffineTransform();
                posNinety.setToRotation(Math.toRadians(90),0, 0);
                tGFX.setTransform(posNinety);
                tGFX.drawString( westGlue, 0, -g.getFontMetrics().getMaxDescent());
                g.drawImage(tBFI, x + g.getFontMetrics().getMaxDescent(), y + diameter/2 - westGluePixelDim.width/2, null);
            }*/
            if (westGlue != null && !westGlue.isEmpty()) {

                g.setFont(g.getFont().deriveFont((float)(diameter/8)));
                Dimension westGluePixelDim = getStringPixelDimension(g,westGlue);

                Rectangle2D r2d = g.getFontMetrics().getStringBounds(westGlue, g);
                //get string image
                BufferedImage tBFI = new BufferedImage((int)r2d.getHeight(), westGluePixelDim.width+3, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tGFX = tBFI.createGraphics();
                tGFX.setColor(tileColor);
                tGFX.fillRect(0,0 , (int)r2d.getHeight(), westGluePixelDim.width+3);
                tGFX.setFont(g.getFont());
                tGFX.setColor(g.getColor());
                AffineTransform posNinety = new AffineTransform();
                posNinety.setToRotation(Math.toRadians(-90),0, 0);
                tGFX.setTransform(posNinety);
                tGFX.drawString( westGlue, -westGluePixelDim.width-3,westGluePixelDim.height+g.getFontMetrics().getMaxDescent());
                g.drawImage(tBFI, x + g.getFontMetrics().getMaxDescent(), y + diameter/2 - westGluePixelDim.width/2, null);
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.setFont(g.getFont().deriveFont((float)(diameter/8)));
                Dimension eastGluePixelDim = getStringPixelDimension(g,eastGlue);
                Rectangle2D r2d = g.getFontMetrics().getStringBounds(eastGlue, g);

                //get string image
                BufferedImage tBFI = new BufferedImage((int)r2d.getHeight(), eastGluePixelDim.width+3, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tGFX = tBFI.createGraphics();
                tGFX.setColor(tileColor);
                tGFX.fillRect(0,0 , (int)r2d.getHeight(), eastGluePixelDim.width+3);
                tGFX.setFont(g.getFont());
                tGFX.setColor(g.getColor());
                AffineTransform negNinety = new AffineTransform();
                negNinety.setToRotation(Math.toRadians(-90),0, 0);
                tGFX.setTransform(negNinety);
                tGFX.drawString( eastGlue, -eastGluePixelDim.width-3, eastGluePixelDim.height + g.getFontMetrics().getMaxDescent() );
                g.drawImage(tBFI, x + diameter - (int) (r2d.getHeight()), y + diameter/2 - eastGluePixelDim.width/2, null);
              
            }

            if(southGlue != null && !southGlue.isEmpty())
            {
                g.setFont(g.getFont().deriveFont((float)(diameter/8)));
                Dimension southGluePixelDim = getStringPixelDimension(g, southGlue);
                g.drawString(southGlue, x + diameter/2 - southGluePixelDim.width/2, y + diameter - southGluePixelDim.height + diameter/50);

            }
            if(northGlue != null && !northGlue.isEmpty())
            {

                g.setFont(g.getFont().deriveFont((float)(diameter/8)));
                Dimension northGluePixelDim = getStringPixelDimension(g, northGlue);
                g.drawString(northGlue, x + diameter/2 - northGluePixelDim.width/2, y + northGluePixelDim.height + diameter/18) ;

            }


            //draw tile label
            g.setFont(g.getFont().deriveFont((float)(diameter/6)));
            Dimension labelBounds = getStringPixelDimension(g,tileLabel );


            Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(tileLabel, g);

            // consider the blank space between the render lines and the start of the first baseline pixel of the string
            int labelXShift =(int) Math.ceil(((int)Math.ceil(stringBounds.getWidth()) - labelBounds.width) /2);

            g.drawString(tileLabel, x + (diameter / 2) - (labelBounds.width/ 2) - labelXShift, y + (diameter / 2) + (labelBounds.height / 2) );





            //now a black border with thickness based on diameter

            g.drawRect(x, y, diameter, diameter);




        }


        public static void drawTiles(Graphics2D g, Set<Map.Entry<Point, Tile>> tiles, int diameter, Point offset) {

            for (Map.Entry<Point, Tile> mep : tiles) {
                Point pt = mep.getKey();
                Tile tile = mep.getValue();

                drawTile(g, tile, pt.x * diameter + offset.x - diameter / 2, -pt.y * diameter + offset.y - diameter / 2, diameter);
                g.setFont(new Font("Courier", Font.PLAIN, 10));
                g.setColor(Color.BLACK);
                g.drawString(pt.x + " " + pt.y, pt.x * diameter + offset.x , -pt.y * diameter + offset.y );
            }

        }


        public static void drawPolyTile(Graphics2D g, PolyTile pt, int diameter, Point offset) {
            Drawer.clearGraphics(g);
            java.util.List<Tile> lt = pt.tiles;
            for (Tile t : lt) {



                drawTile(g, t, t.getLocation().x * diameter + offset.x - diameter / 2, -t.getLocation().y * diameter + offset.y - diameter / 2, diameter);
            }




        }
        public static void drawCenteredPolyTile(Graphics2D g, PolyTile pt)
        {
           Point polyDim = calculatePolyTileGridDimension(pt);
            Rectangle graphicsDim = g.getClipBounds();

            int diameter =  (int) Math.floor(Math.min(graphicsDim.getWidth(), graphicsDim.getHeight()) /Math.max(polyDim.x, polyDim.y))/2 ;
            Point offset = new Point(0, 0 );


            AffineTransform orig = g.getTransform();

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Tile t : pt.getTiles()) {
               Point Tpt = t.getLocation();
               maxX = Tpt.x > maxX ? Tpt.x: maxX;
               maxY = Tpt.y  > maxY ? Tpt.y : maxY;
               minX = Tpt.x < minX ? Tpt.x : minX;
               minY = Tpt.y  < minY ? Tpt.y : minY;
            }
            Point xRange = new Point(minX * diameter, maxX*diameter);
            Point yRange = new Point(minY*diameter, maxY*diameter);

         /* if( ((-maxY * diameter) - diameter/2 + offset.y) < 0 )
            {

                g.translate(0,  -((-maxY * diameter) - diameter/2 + offset.y) -1  - diameter/2);
            }*/

            offset.translate(-minX*diameter + diameter/2 +((graphicsDim.width-(polyDim.x*diameter))/2), +maxY*diameter + diameter/2 + ((graphicsDim.height-(polyDim.y*diameter)) /2));

            drawPolyTile(g, pt,diameter, offset );

            g.setTransform(orig);


        }

    }

}

package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.PolyTile;
import com.asarg.polysim.Tile;
import javafx.util.Pair;

import java.awt.*;
import java.awt.geom.AffineTransform;
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

        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter) {
            g.setFont(g.getFont().deriveFont((float)(diameter/6)));
            Rectangle clip = g.getClipBounds();

            if (x > clip.width + diameter || x < 0 -diameter || y > clip.height + diameter || y < 0 -diameter)
                return;
            AffineTransform gOriginalATransform = g.getTransform();

            String tileLabel = tile.getLabel();
            String northGlue = tile.getGlueN();
            String eastGlue = tile.getGlueE();
            String southGlue = tile.getGlueS();
            String westGlue = tile.getGlueW();


            //get hex color string to int, then create new color out of the rgb
            //int colorInt = Integer.parseInt(tile.getColor(), 16);
            //Color tileColor =new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF, 100);
            Color tileColor = Color.decode("#"+tile.getColor());
            g.setColor(tileColor);

            // the tiles fill color
            g.fillRect(x, y, diameter, diameter);

            //change stroke/color for rest
            g.setColor(Color.black);
            g.setStroke(new BasicStroke(diameter / 25));


            //Drawing the glues-------------------------------------
            // font metrics is used to calculate horizontal and vertical size of the string.
            // horizontal: stringWidth(string);  vertical: getAscent()?
            FontMetrics font = g.getFontMetrics();

            if (westGlue != null && !westGlue.isEmpty()) {

                g.rotate(Math.PI/2);
                g.drawString(westGlue, y+ (diameter/2-font.stringWidth(westGlue)/2) , -x  -diameter/20);
                g.setTransform(gOriginalATransform);
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.rotate(Math.PI/2);
                g.drawString(eastGlue,  y+(diameter/2-font.stringWidth(eastGlue)/2), -x -diameter +diameter/6 );
                g.setTransform(gOriginalATransform);

            }

            if(southGlue != null && !southGlue.isEmpty())
            {
                g.drawString(southGlue, x + diameter/2 - font.stringWidth(southGlue)/2, y + diameter - diameter/20);
            }
            if(northGlue != null && !northGlue.isEmpty())
            {
                g.drawString(northGlue, x + diameter/2 - font.stringWidth(northGlue)/2, y + diameter/8) ;
            }

//            //draw tile label

//            // consider the blank space between the render lines and the start of the first baseline pixel of the string
//            int labelXShift =(int) Math.ceil(((int)Math.ceil(stringBounds.getWidth()) - labelBounds.width) /2);
//
//            g.drawString(tileLabel, x + (diameter / 2) - (labelBounds.width/ 2) - labelXShift, y + (diameter / 2) + (labelBounds.height / 2) );

            g.drawString(tileLabel, x + (diameter / 2) - (font.stringWidth(tileLabel)/ 2) , y + (diameter / 2)  );

            //now a black border with thickness based on diameter.
            if ( tile.getParent().isFrontier() ){
                // float array sets the spacing between dashes (by controlling how big they are).
                Stroke dashed = new BasicStroke(diameter/25,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,new float[]{4},0);
                g.setStroke(dashed);
            }

            g.drawRect(x, y, diameter, diameter);

            g.setStroke(new BasicStroke(diameter / 25));
        }

        public static void drawHexTile(Graphics2D g, Tile tile, int x, int y, int diameter){
            Polygon sprite = new Polygon();
            for(int i=0; i<6; i++) {
                sprite.addPoint(x+(diameter+10)/2 + (int)((diameter+10)*Math.cos(i*2*Math.PI/6)), y + (int)((diameter+10)*Math.sin(i*2*Math.PI/6)));
            }
            g.drawRect(x, y, diameter, diameter);
            g.drawPolygon(sprite);
        }

        public static void drawTiles(Graphics2D g, Set<Map.Entry<Point, Tile>> tiles, int diameter, Point offset) {

            for (Map.Entry<Point, Tile> mep : tiles) {
                Point pt = mep.getKey();
                Tile tile = mep.getValue();

//                System.out.println(pt.toString()+" "+offset.x+" "+offset.y);
                drawTile(g, tile, pt.x * diameter + offset.x - diameter / 2, -pt.y * diameter + offset.y - diameter / 2, diameter);
//                drawHexTile(g, tile, pt.x * diameter + offset.x - diameter / 2, -pt.y * diameter + offset.y - diameter / 2, diameter);
            }

         }

        public static void drawTileSelection(Graphics2D g, Point location, int diameter, Point offset, Color color)
        {
            clearGraphics(g);

            g.setColor(color);
            Stroke dashed = new BasicStroke(diameter/25,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,0,new float[]{4},0);
            g.setStroke(dashed);
            g.drawRect( location.x * diameter + offset.x - diameter / 2, -location.y * diameter + offset.y - diameter / 2, diameter ,diameter);

        }

        public static void drawPolyTile(Graphics2D g, PolyTile pt, int diameter, Point offset) {
            Drawer.clearGraphics(g);
            java.util.List<Tile> lt = pt.tiles;
            for (Tile t : lt) {

                drawTile(g, t, t.getLocation().x * diameter + offset.x - diameter / 2, -t.getLocation().y * diameter + offset.y - diameter / 2, diameter);
            }

        }

        // used in when needed to paint a single polytile at some grid location
        // location = from the tile grid (0,0) etc; offset = from the canvas (400, 300) or whatever.
        public static void drawNewPolyTile(Graphics2D g, List<Tile> tiles, int diameter, Point location, Point offset) {
            for (Tile t : tiles) {
//                System.out.println(t.getLocation());
                drawTile(g, t, (location.x+t.getLocation().x) * diameter + offset.x - diameter / 2, -(location.y+t.getLocation().y) * diameter + offset.y - diameter / 2, diameter);
            }
        }

        public static Pair<Point, Integer> drawCenteredPolyTile(Graphics2D g, PolyTile pt)
        {
            Point polyDim = calculatePolyTileGridDimension(pt);
            Rectangle graphicsDim = g.getClipBounds();

            int diameter =  (int)( Math.ceil(Math.min(graphicsDim.getWidth(), graphicsDim.getHeight()) /Math.max(polyDim.x, polyDim.y))/1.5) ;
            Point offset = new Point(0, 0 );

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

            offset.translate(-minX*diameter + diameter/2 +((graphicsDim.width-(polyDim.x*diameter))/2), +maxY*diameter + diameter/2 + ((graphicsDim.height-(polyDim.y*diameter)) /2));

            drawPolyTile(g, pt,diameter, offset );
            return new Pair<Point, Integer>(offset, diameter);
        }
        public static Pair<Point, Integer> drawCenteredPolyTile(Graphics2D g, PolyTile pt, Point offset/* provide an offset x and y*/)
        {
            Point polyDim = calculatePolyTileGridDimension(pt);
            Rectangle graphicsDim = g.getClipBounds();

            int diameter =  (int)( Math.ceil(Math.min(graphicsDim.getWidth(), graphicsDim.getHeight()) /Math.max(polyDim.x, polyDim.y))/1.5) ;


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


            offset.translate(-minX*diameter + diameter/2 +((graphicsDim.width-(polyDim.x*diameter))/2), +maxY*diameter + diameter/2 + ((graphicsDim.height-(polyDim.y*diameter)) /2));

            drawPolyTile(g, pt,diameter, offset );
            return new Pair<Point, Integer>(offset, diameter);
        }

        public static Pair<Point, Integer> drawCenteredPolyTile(Graphics2D g, PolyTile pt, int diameter)
        {
            Point polyDim = calculatePolyTileGridDimension(pt);
            Rectangle graphicsDim = g.getClipBounds();


            Point offset = new Point(0, 0 );

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Tile t : pt.getTiles()) {
                Point Tpt = t.getLocation();
                maxX = Tpt.x > maxX ? Tpt.x : maxX;
                maxY = Tpt.y > maxY ? Tpt.y : maxY;
                minX = Tpt.x < minX ? Tpt.x : minX;
                minY = Tpt.y < minY ? Tpt.y : minY;
            }

            offset.translate(-minX*diameter + diameter/2 +((graphicsDim.width-(polyDim.x*diameter))/2), +maxY*diameter + diameter/2 + ((graphicsDim.height-(polyDim.y*diameter)) /2));

            drawPolyTile(g, pt,diameter, offset );
            return new Pair<Point, Integer>(offset, diameter);
        }


        public static Point getGridPoint(Point canvasPoint, Point offset, int diameter)
        {
            return new Point((int)Math.round((canvasPoint.x-offset.x)/(double)diameter),(int) Math.round((-canvasPoint.y +offset.y)/(double)diameter));
        }

    }

}

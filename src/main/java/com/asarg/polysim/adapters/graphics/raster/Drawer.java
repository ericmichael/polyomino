package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Pair;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Drawer {


    //clears given graphics object, clip must have been set
    public static void clearGraphics(Graphics2D g) {
        Rectangle bounds = g.getClipBounds();
        if (g instanceof FXGraphics2D)
            g.clearRect(0, 0, bounds.width, bounds.height);
        else {
            g.setComposite(AlphaComposite.Clear);
            g.fillRect(0, 0, bounds.width, bounds.height);
            g.setComposite(AlphaComposite.Src);
        }
    }

    public static class TileDrawer {

        public static Coordinate calculatePolyTileGridDimension(PolyTile polyt) {

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            List<Tile> tiles = polyt.getTiles();
            for (Tile t : tiles) {
                Coordinate pt = t.getLocation();
                if (pt.getX() > maxX) {
                    maxX = pt.getX();
                }
                if (pt.getX() < minX) {
                    minX = pt.getX();
                }
                if (pt.getY() > maxY) {
                    maxY = pt.getY();
                }
                if (pt.getY() < minY) {
                    minY = pt.getY();
                }
            }

            return new Coordinate(1 + maxX - minX, 1 + maxY - minY);

        }

        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter) {
            g.setFont(g.getFont().deriveFont((float) (diameter / 6)));
            Rectangle clip = g.getClipBounds();

            if (x > clip.width + diameter || x < 0 - diameter || y > clip.height + diameter || y < 0 - diameter)
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
            Color tileColor = Color.decode("#" + tile.getColor());
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

                g.rotate(Math.PI / 2);
                g.drawString(westGlue, y + (diameter / 2 - font.stringWidth(westGlue) / 2), -x - diameter / 20);
                g.setTransform(gOriginalATransform);
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.rotate(Math.PI / 2);
                g.drawString(eastGlue, y + (diameter / 2 - font.stringWidth(eastGlue) / 2), -x - diameter + diameter / 6);
                g.setTransform(gOriginalATransform);

            }

            if (southGlue != null && !southGlue.isEmpty()) {
                g.drawString(southGlue, x + diameter / 2 - font.stringWidth(southGlue) / 2, y + diameter - diameter / 20);
            }
            if (northGlue != null && !northGlue.isEmpty()) {
                g.drawString(northGlue, x + diameter / 2 - font.stringWidth(northGlue) / 2, y + diameter / 8);
            }

//            //draw tile label

//            // consider the blank space between the render lines and the start of the first baseline pixel of the string
//            int labelXShift =(int) Math.ceil(((int)Math.ceil(stringBounds.getWidth()) - labelBounds.width) /2);
//
//            g.drawString(tileLabel, x + (diameter / 2) - (labelBounds.width/ 2) - labelXShift, y + (diameter / 2) + (labelBounds.height / 2) );

            g.drawString(tileLabel, x + (diameter / 2) - (font.stringWidth(tileLabel) / 2), y + (diameter / 2));

            //now a black border with thickness based on diameter.
            if (tile.getParent().isFrontier()) {
                // float array sets the spacing between dashes (by controlling how big they are).
                Stroke dashed = new BasicStroke(diameter / 25, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
                g.setStroke(dashed);
            }

            g.drawRect(x, y, diameter, diameter);

            g.setStroke(new BasicStroke(diameter / 25));
        }

        public static void drawTileOutline(Graphics2D g, Tile tile, int x, int y, int diameter, boolean hasNorth, boolean hasEast, boolean hasSouth, boolean hasWest, boolean selection) {
            Stroke dashed = new BasicStroke(diameter / 25, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
            Stroke simple = new BasicStroke(diameter / 25);

            if (!tile.getParent().isFrontier()) {
                //draw wimpy borders
                g.setStroke(dashed);
                g.setColor(Color.gray);
                if (hasNorth) g.drawLine(x, y, x + diameter, y);
                if (hasEast) g.drawLine(x + diameter, y, x + diameter, y + diameter);
                if (hasSouth) g.drawLine(x, y + diameter, x + diameter, y + diameter);
                if (hasWest) g.drawLine(x, y, x, y + diameter);

                g.setStroke(simple);

                if (!selection)
                    g.setColor(Color.black);
                else {
                    dashed = new BasicStroke(diameter / 10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
                    g.setStroke(dashed);
                    g.setColor(Color.decode("#007AFF"));
                }

                if (!hasNorth) g.drawLine(x, y, x + diameter, y);
                if (!hasEast) g.drawLine(x + diameter, y, x + diameter, y + diameter);
                if (!hasSouth) g.drawLine(x, y + diameter, x + diameter, y + diameter);
                if (!hasWest) g.drawLine(x, y, x, y + diameter);

            } else {
                g.setColor(Color.black);

                g.setStroke(dashed);
                g.drawLine(x, y, x + diameter, y);
                g.drawLine(x + diameter, y, x + diameter, y + diameter);
                g.drawLine(x, y + diameter, x + diameter, y + diameter);
                g.drawLine(x, y, x, y + diameter);
            }
        }


        public static void drawTile(Graphics2D g, Tile tile, int x, int y, int diameter, boolean hasNorth, boolean hasEast, boolean hasSouth, boolean hasWest) {
            Rectangle clip = g.getClipBounds();

            if (x > clip.width + diameter || x < 0 - diameter || y > clip.height + diameter || y < 0 - diameter)
                return;

            //get hex color string to int, then create new color out of the rgb
            //int colorInt = Integer.parseInt(tile.getColor(), 16);
            //Color tileColor =new Color(colorInt >> 16, (colorInt & 0x00FF00) >> 8, colorInt & 0x0000FF, 100);
            Color tileColor = Color.decode("#" + tile.getColor());
            g.setColor(tileColor);

            // the tiles fill color
            g.fillRect(x, y, diameter, diameter);

            drawTileOutline(g, tile, x, y, diameter, hasNorth, hasEast, hasSouth, hasWest, false);

        }

        private static void drawStringsOnTiles(Graphics2D g, Tile tile, int x, int y, int diameter, boolean hasNorth, boolean hasEast, boolean hasSouth, boolean hasWest) {
            Rectangle clip = g.getClipBounds();

            if (x > clip.width + diameter || x < 0 - diameter || y > clip.height + diameter || y < 0 - diameter)
                return;

            g.setFont(g.getFont().deriveFont((float) (diameter / 6)));

            String tileLabel = tile.getLabel();
            String northGlue = tile.getGlueN();
            String eastGlue = tile.getGlueE();
            String southGlue = tile.getGlueS();
            String westGlue = tile.getGlueW();

            //Drawing the glues-------------------------------------
            // font metrics is used to calculate horizontal and vertical size of the string.
            // horizontal: stringWidth(string);  vertical: getAscent()?
            FontMetrics font = g.getFontMetrics();

            if (westGlue != null && !westGlue.isEmpty()) {

                g.rotate(Math.PI / 2);
                g.drawString(westGlue, y + (diameter / 2 - font.stringWidth(westGlue) / 2), -x - diameter / 20);
                g.rotate(-Math.PI / 2);
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.rotate(Math.PI / 2);
                g.drawString(eastGlue, y + (diameter / 2 - font.stringWidth(eastGlue) / 2), -x - diameter + diameter / 6);
                g.rotate(-Math.PI / 2);

            }

            if (southGlue != null && !southGlue.isEmpty()) {
                g.drawString(southGlue, x + diameter / 2 - font.stringWidth(southGlue) / 2, y + diameter - diameter / 20);
            }
            if (northGlue != null && !northGlue.isEmpty()) {
                g.drawString(northGlue, x + diameter / 2 - font.stringWidth(northGlue) / 2, y + diameter / 8);
            }

            g.drawString(tileLabel, x + (diameter / 2) - (font.stringWidth(tileLabel) / 2), y + (diameter / 2));
        }


        //Wil be deprecated when updated FXGraphics2d hits on maven.
        //This hack works around a bug in FXGraphics2d. rotate does not work in current version
        private static void drawStringsOnTiles(GraphicsContext gc, FXGraphics2D g, Tile tile, int x, int y, int diameter, boolean hasNorth, boolean hasEast, boolean hasSouth, boolean hasWest) {
            Rectangle clip = g.getClipBounds();

            if (x > clip.width + diameter || x < 0 - diameter || y > clip.height + diameter || y < 0 - diameter)
                return;

            g.setFont(g.getFont().deriveFont((float) (diameter / 6)));

            String tileLabel = tile.getLabel();
            String northGlue = tile.getGlueN();
            String eastGlue = tile.getGlueE();
            String southGlue = tile.getGlueS();
            String westGlue = tile.getGlueW();

            //Drawing the glues-------------------------------------
            // font metrics is used to calculate horizontal and vertical size of the string.
            // horizontal: stringWidth(string);  vertical: getAscent()?
            FontMetrics font = g.getFontMetrics();

            double theta = Math.PI / 2;
            double bug = theta * Math.PI / 180;

            if (westGlue != null && !westGlue.isEmpty()) {
                g.rotate(theta);
                //undo whatever bug they did
                gc.rotate(-bug);

                //apply correct rotation
                gc.rotate(Math.toDegrees(theta));
                g.drawString(westGlue, y + (diameter / 2 - font.stringWidth(westGlue) / 2), -x - diameter / 20);

                g.rotate(-theta);
                gc.rotate(bug);
                gc.rotate(-Math.toDegrees(theta));
            }
            if (eastGlue != null && !eastGlue.isEmpty()) {
                g.rotate(theta);
                gc.rotate(-bug);
                //apply correct rotation
                gc.rotate(Math.toDegrees(theta));
                g.drawString(eastGlue, y + (diameter / 2 - font.stringWidth(eastGlue) / 2), -x - diameter + diameter / 6);
                g.rotate(-theta);
                gc.rotate(bug);
                gc.rotate(-Math.toDegrees(theta));

            }

            if (southGlue != null && !southGlue.isEmpty()) {
                g.drawString(southGlue, x + diameter / 2 - font.stringWidth(southGlue) / 2, y + diameter - diameter / 20);
            }
            if (northGlue != null && !northGlue.isEmpty()) {
                g.drawString(northGlue, x + diameter / 2 - font.stringWidth(northGlue) / 2, y + diameter / 8);
            }

            g.drawString(tileLabel, x + (diameter / 2) - (font.stringWidth(tileLabel) / 2), y + (diameter / 2));
        }

        public static void drawTiles(SimulationCanvas sc) {
            Set<Map.Entry<Coordinate, Tile>> tiles = sc.grid.entrySet();

            for (Map.Entry<Coordinate, Tile> mep : tiles) {
                Coordinate pt = mep.getKey();
                Tile tile = mep.getValue();

                PolyTile parent = tile.getParent();
                Coordinate unitLocation = tile.getLocation();
                boolean hasNorth = parent.getTile(unitLocation.getNorth()) != null;
                boolean hasEast = parent.getTile(unitLocation.getEast()) != null;
                boolean hasSouth = parent.getTile(unitLocation.getSouth()) != null;
                boolean hasWest = parent.getTile(unitLocation.getWest()) != null;

                Coordinate offset = sc.center;
                int diameter = sc.getTileDiameter();

                drawTile(sc.getGraphics(), tile, pt.getX() * diameter + offset.getX() - diameter / 2, -pt.getY() * diameter + offset.getY() - diameter / 2, diameter, hasNorth, hasEast, hasSouth, hasWest);
                drawStringsOnTiles(sc.getGraphicsContext2D(), sc.getGraphics(), tile, pt.getX() * diameter + offset.getX() - diameter / 2, -pt.getY() * diameter + offset.getY() - diameter / 2, diameter, hasNorth, hasEast, hasSouth, hasWest);
            }

        }

        public static void drawTileSelection(Graphics2D g, Coordinate location, int diameter, Coordinate offset, Color color) {
            g.setColor(color);
            Stroke dashed = new BasicStroke(diameter / 25, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
            g.setStroke(dashed);
            g.drawRect(location.getX() * diameter + offset.getX() - diameter / 2, -location.getY() * diameter + offset.getY() - diameter / 2, diameter, diameter);

        }

        public static void drawPolyTileSelection(Graphics2D g, PolyTile pt, int diameter, Coordinate location, Coordinate offset) {
            java.util.List<Tile> lt = pt.tiles;
            for (Tile t : lt) {
                Coordinate unitLocation = t.getLocation();
                boolean hasNorth = pt.getTile(unitLocation.getNorth()) != null;
                boolean hasEast = pt.getTile(unitLocation.getEast()) != null;
                boolean hasSouth = pt.getTile(unitLocation.getSouth()) != null;
                boolean hasWest = pt.getTile(unitLocation.getWest()) != null;
                System.out.println("Location: " + location);
                System.out.println("Unit location: " + unitLocation);
                int x = (location.getX() + unitLocation.getX()) * diameter + offset.getX() - diameter / 2;
                int y = -(location.getY() + unitLocation.getY()) * diameter + offset.getY() - diameter / 2;
                System.out.println("(" + x + "," + y + ")");
                drawTileOutline(g, t, x, y, diameter, hasNorth, hasEast, hasSouth, hasWest, true);
            }
        }

        public static void drawPolyTile(Graphics2D g, PolyTile pt, int diameter, Coordinate offset) {
            Drawer.clearGraphics(g);
            java.util.List<Tile> lt = pt.tiles;
            for (Tile t : lt) {
                Coordinate unitLocation = t.getLocation();
                boolean hasNorth = pt.getTile(unitLocation.getNorth()) != null;
                boolean hasEast = pt.getTile(unitLocation.getEast()) != null;
                boolean hasSouth = pt.getTile(unitLocation.getSouth()) != null;
                boolean hasWest = pt.getTile(unitLocation.getWest()) != null;
                drawTile(g, t, t.getLocation().getX() * diameter + offset.getX() - diameter / 2, -t.getLocation().getY() * diameter + offset.getY() - diameter / 2, diameter, hasNorth, hasEast, hasSouth, hasWest);
                drawStringsOnTiles(g, t, t.getLocation().getX() * diameter + offset.getX() - diameter / 2, -t.getLocation().getY() * diameter + offset.getY() - diameter / 2, diameter, hasNorth, hasEast, hasSouth, hasWest);
            }

        }

        // used in when needed to paint a single polytile at some grid location
        // location = from the tile grid (0,0) etc; offset = from the canvas (400, 300) or whatever.
        public static void drawNewPolyTile(Graphics2D g, List<Tile> tiles, int diameter, Coordinate location, Coordinate offset) {
            for (Tile t : tiles) {
//                System.out.println(t.getLocation());
                drawTile(g, t, (location.getX() + t.getLocation().getX()) * diameter + offset.getX() - diameter / 2, -(location.getY() + t.getLocation().getY()) * diameter + offset.getY() - diameter / 2, diameter);
            }
        }

        public static Pair<Coordinate, Integer> drawCenteredPolyTile(Graphics2D g, PolyTile pt) {
            Coordinate polyDim = calculatePolyTileGridDimension(pt);
            Rectangle graphicsDim = g.getClipBounds();

            int diameter = (int) (Math.ceil(Math.min(graphicsDim.getWidth(), graphicsDim.getHeight()) / Math.max(polyDim.getX(), polyDim.getY())) / 1.5);
            Coordinate offset = new Coordinate(0, 0);

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (Tile t : pt.getTiles()) {
                Coordinate Tpt = t.getLocation();
                maxX = Tpt.getX() > maxX ? Tpt.getX() : maxX;
                maxY = Tpt.getY() > maxY ? Tpt.getY() : maxY;
                minX = Tpt.getX() < minX ? Tpt.getX() : minX;
                minY = Tpt.getY() < minY ? Tpt.getY() : minY;
            }
            Coordinate xRange = new Coordinate(minX * diameter, maxX * diameter);
            Coordinate yRange = new Coordinate(minY * diameter, maxY * diameter);

            offset = offset.translate(-minX * diameter + diameter / 2 + ((graphicsDim.width - (polyDim.getX() * diameter)) / 2), +maxY * diameter + diameter / 2 + ((graphicsDim.height - (polyDim.getY() * diameter)) / 2));

            drawPolyTile(g, pt, diameter, offset);
            return new Pair<Coordinate, Integer>(offset, diameter);
        }

        public static Coordinate getGridPoint(Coordinate canvasPoint, Coordinate offset, int diameter) {
            Coordinate p = new Coordinate((int) Math.round((canvasPoint.getX() - offset.getX()) / (double) diameter), (int) Math.round((-canvasPoint.getY() + offset.getY()) / (double) diameter));
            return new Coordinate((int) Math.round((canvasPoint.getX() - offset.getX()) / (double) diameter), (int) Math.round((-canvasPoint.getY() + offset.getY()) / (double) diameter));
        }

    }

}

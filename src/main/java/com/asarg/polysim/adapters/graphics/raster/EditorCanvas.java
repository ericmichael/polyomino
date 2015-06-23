package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * Created by ericmartinez on 1/12/15.
 */
public class EditorCanvas extends JPanel {
    //the current canvas display info
    Coordinate canvasCenteredOffset = new Coordinate(0, 0);
    int tileDiameter = 1;

    SimpleObjectProperty<Tile> selectedTile = new SimpleObjectProperty<Tile>(null);

    PolyTile pt;
    //canvas stuff
    BufferedImage polyTileCanvas;
    Graphics2D polyTileCanvasGFX;
    BufferedImage overLayer;
    Graphics2D overLayerGFX;

    double height, width;

    //Canvas panning
    Coordinate lastXY = null;

    public EditorCanvas(double width, double height) {
        super();
        this.height = height;
        this.width = width;

        resize((int) width, (int) height);
        MouseAdapter gridListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastXY = new Coordinate((int) e.getPoint().getX(), (int) e.getPoint().getY());
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);

                if (e.getWheelRotation() == 1 && tileDiameter > 1) {
                    tileDiameter = (int) Math.floor(tileDiameter * .95);

                    Drawer.TileDrawer.drawPolyTile(polyTileCanvasGFX, pt, tileDiameter, canvasCenteredOffset);
                    //   canvasCenteredOffset = ofnt.getKey();
                    if (selectedTile.get() != null)
                        Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.get().getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                    repaint();

                } else if (e.getWheelRotation() == -1 && tileDiameter * 3 < getWidth() && tileDiameter * 3 < getHeight()) {
                    tileDiameter = (int) Math.ceil(tileDiameter * 1.05);
                    Drawer.TileDrawer.drawPolyTile(polyTileCanvasGFX, pt, tileDiameter, canvasCenteredOffset);
                    //  canvasCenteredOffset = ofnt.getKey();
                    if (selectedTile.get() != null)
                        Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.get().getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                    repaint();

                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                canvasCenteredOffset = canvasCenteredOffset.translate(e.getX() - lastXY.getX(), e.getY() - lastXY.getY());
                lastXY = new Coordinate((int) e.getPoint().getX(), (int) e.getPoint().getY());
                Drawer.TileDrawer.drawPolyTile(polyTileCanvasGFX, pt, tileDiameter, canvasCenteredOffset);
                Drawer.clearGraphics(overLayerGFX);
                if (selectedTile.get() != null)
                    Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.get().getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (pt != null) {
                    Coordinate gridPoint = Drawer.TileDrawer.getGridPoint(new Coordinate((int) e.getPoint().getX(), (int) e.getPoint().getY()), canvasCenteredOffset, tileDiameter);
                    Tile tile = pt.getTile((int) gridPoint.getX(), (int) gridPoint.getY());
                    if (tile != null) {
                        selectTile(tile);
                    } else if (pt.adjacentExits(gridPoint)) {
                        Tile newTile = new Tile();
                        newTile.setTileLocation((int) gridPoint.getX(), (int) gridPoint.getY());

                        pt.addTile(newTile);
                        selectedTile.set(newTile);
                        Drawer.TileDrawer.drawPolyTile(polyTileCanvasGFX, pt, tileDiameter, canvasCenteredOffset);  // Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX,polytile, tileDiameter);
                        Drawer.clearGraphics(overLayerGFX);
                        //  canvasCenteredOffset = newOffDia.getKey();
                        // tileDiameter = newOffDia.getValue();
                        Drawer.TileDrawer.drawTileSelection(overLayerGFX, Drawer.TileDrawer.getGridPoint(new Coordinate((int) e.getPoint().getX(), (int) e.getPoint().getY()), canvasCenteredOffset, tileDiameter), tileDiameter, canvasCenteredOffset, Color.CYAN);
                        repaint();

                    }
                }
            }
        };
        addMouseListener(gridListener);
        addMouseWheelListener(gridListener);
        addMouseMotionListener(gridListener);
    }

    public PolyTile getPolyTile() {
        return pt;
    }

    public void setPolyTile(PolyTile pt) {
        if (this.pt != pt) {
            this.pt = pt;
            selectedTile.set(null);
            if(pt!=null) drawPolyTile();
            else clearPolyTile();
        }
    }

    public SimpleObjectProperty<Tile> getSelectedTileProperty() {
        return selectedTile;
    }

    public void selectTile(Tile tile) {
        selectTileHelper(tile);
        selectedTile.set(tile);
    }

    private void selectTileHelper(Tile tile) {
        Drawer.clearGraphics(overLayerGFX);
        Drawer.TileDrawer.drawTileSelection(overLayerGFX, tile.getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
        repaint();
    }

    public void drawPolyTile() {

        Drawer.clearGraphics(overLayerGFX);
        Pair<Coordinate, Integer> ppi = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, pt);
        canvasCenteredOffset = ppi.getKey();
        tileDiameter = ppi.getValue();
        Drawer.clearGraphics(overLayerGFX);

        if (selectedTile.get() != null) {
            Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.get().getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
        }

        repaint();
    }

    public void clearPolyTile(){
        Drawer.clearGraphics(overLayerGFX);
        Drawer.clearGraphics(polyTileCanvasGFX);
        repaint();
    }

    @Override
    public void resize(int width, int height) {
        resetBlank(width, height);
        if (pt != null) drawPolyTile();
    }

    public void resetBlank(int width, int height) {
        polyTileCanvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0, 0, width, height);
        overLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        overLayerGFX = overLayer.createGraphics();
        overLayerGFX.setClip(0, 0, width, height);
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(polyTileCanvas, 0, 0, null);
        g2.drawImage(overLayer, 0, 0, null);
    }
}

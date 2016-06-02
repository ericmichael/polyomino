package com.asarg.polysim.adapters.graphics.raster;


import com.asarg.polysim.models.base.ActiveGrid;
import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.FrontierElement;
import com.asarg.polysim.models.base.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class SimulationCanvas extends JPanel {


    private final Dimension res = new Dimension();
    private BufferedImage canvasBFI;
    private Graphics2D cg2d;
    private int tileDiameter = 50;
    private Coordinate center;
    ActiveGrid parent;

    public SimulationCanvas(ActiveGrid parent, int w, int h) {
        center = new Coordinate(w / 2, h / 2);
        canvasBFI = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        cg2d = canvasBFI.createGraphics();
        cg2d.setComposite(AlphaComposite.Src);
        cg2d.setColor(Color.black);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
        this.parent = parent;
    }

    public SimulationCanvas(ActiveGrid parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) screenSize.getWidth();
        int h = (int) screenSize.getHeight();
        center = new Coordinate(w / 2, h / 2);
        canvasBFI = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        cg2d = canvasBFI.createGraphics();
        cg2d.setComposite(AlphaComposite.Src);
        cg2d.setColor(Color.black);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
        this.parent = parent;
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);
        center = new Coordinate(w / 2, h / 2);
        canvasBFI = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        cg2d = canvasBFI.createGraphics();
        cg2d.setComposite(AlphaComposite.Src);
        cg2d.setColor(Color.black);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
    }


    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        center = new Coordinate(width / 2, height / 2);
    }

    public void reset() {
        Drawer.clearGraphics(cg2d);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        canvasBFI.setAccelerationPriority(1);
        g.drawImage(canvasBFI, 0, 0, null);
    }

    public void drawGrid(HashMap<Coordinate, Tile> hmpt, Coordinate selected) {
        Drawer.TileDrawer.drawTiles(cg2d, hmpt, tileDiameter, center);
        if(selected!=null) {
            Tile selectedTile = hmpt.get(selected);
            if(selectedTile!=null) {
                Coordinate offset = new Coordinate(selected.getX() - selectedTile.getLocation().getX(), selected.getY() - selectedTile.getLocation().getY());
                Drawer.TileDrawer.drawPolyTileSelection(cg2d, selectedTile.getParent(), tileDiameter, offset, center);
            }
        }
    }

    // draws the given polytile onto the loaded graphics object (alternative to drawGrid which draws the
    //entire grid)
    public void drawTileOnGrid(FrontierElement attached) {
//        System.out.println(attached.getLocation());
//        System.out.println(attached.getOffset());
//        System.out.println(center);
        Drawer.TileDrawer.drawNewPolyTile(cg2d, attached.getPolyTile().getTiles(), tileDiameter, attached.getOffset(), center);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public int getTileDiameter() {


        return tileDiameter;
    }

    public void setTileDiameter(int td) {
        tileDiameter = td;
        reset();
    }

    public void translateOffset(int x, int y) {

        center = center.translate(x, y);
        reset();
    }

    public final Coordinate getOffset() {
        return center;
    }
}

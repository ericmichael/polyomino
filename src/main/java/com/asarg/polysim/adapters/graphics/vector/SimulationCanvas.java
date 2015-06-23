package com.asarg.polysim.adapters.graphics.vector;


import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.FrontierElement;
import com.asarg.polysim.models.base.Tile;
import org.jfree.fx.FXGraphics2D;


import java.awt.*;
import javafx.scene.canvas.Canvas;

import java.util.HashMap;

public class SimulationCanvas extends Canvas {
    FXGraphics2D cg2d;
    Dimension res = new Dimension();
    private int tileDiameter = 50;
    private Coordinate center;

    public SimulationCanvas(int w, int h) {
        super(w,h);
        center = new Coordinate(w / 2, h / 2);
        cg2d = new FXGraphics2D(getGraphicsContext2D());
//        cg2d.setComposite(AlphaComposite.Src);
//        cg2d.setColor(Color.white);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
    }

    public SimulationCanvas() {
        super(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    //@Override
    public void resize(int w, int h) {
        setWidth(w);
        setHeight(h);
        center = new Coordinate(w / 2, h / 2);
        cg2d = new FXGraphics2D(getGraphicsContext2D());
//        cg2d.setComposite(AlphaComposite.Src);
//        cg2d.setColor(Color.white);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
    }


    public void reset() {
        Drawer.clearGraphics(cg2d);
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        this.setBackground(Color.WHITE);
//        canvasBFI.setAccelerationPriority(1);
//        g.drawImage(canvasBFI, 0, 0, null);
//    }
//

    public void drawGrid(HashMap<Coordinate, Tile> hmpt) {

        Drawer.TileDrawer.drawTiles(cg2d, hmpt.entrySet(), tileDiameter, center);

    }

    // draws the given polytile onto the loaded graphics object (alternative to drawGrid which draws the
    //entire grid)
    public void drawTileOnGrid(FrontierElement attached) {
//        System.out.println(attached.getLocation());
//        System.out.println(attached.getOffset());
//        System.out.println(center);
        Drawer.TileDrawer.drawNewPolyTile(cg2d, attached.getPolyTile().getTiles(), tileDiameter, attached.getOffset(), center);
    }

    public int getTileDiameter() {


        return tileDiameter;
    }

    public void setTileDiameter(int td) {
        tileDiameter = td;
    }

    public void translateOffset(int x, int y) {
        center = center.translate(x, y);
    }

    public final Coordinate getOffset() {
        return center;
    }


}
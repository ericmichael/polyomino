package com.asarg.polysim;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Dom on 8/15/2014.
 */
public class TestCanvas extends JPanel {


    private int tileDiameter = 50;
    private Point center;
    BufferedImage canvasBFI;
    Graphics2D cg2d;
    Dimension res = new Dimension();
    private int w;
    private int h;

    TestCanvas(int w, int h) {
        center = new Point(w / 2, h / 2);
        canvasBFI = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        cg2d = canvasBFI.createGraphics();
        cg2d.setComposite(AlphaComposite.Src);
        cg2d.setColor(Color.black);
        res.setSize(w, h);
        cg2d.setClip(0, 0, w, h);
        this.w = w;
        this.h = h;
    }
    TestCanvas()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        canvasBFI = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        cg2d = canvasBFI.createGraphics();
        cg2d.setComposite(AlphaComposite.Src);
        cg2d.setColor(Color.black);
        res.setSize(screenSize.width,screenSize.height);
        cg2d.setClip(0, 0, screenSize.width, screenSize.height);


    }



    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width,height);
        center = new Point(width/2, height/2);
    }
    public void reset() {

        Drawer.clearGraphics(cg2d);

    }


    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(canvasBFI, 0, 0, null);
    }


    public void drawGrid(HashMap<Point, Tile> hmpt) {

        Drawer.TileDrawer.drawTiles(cg2d, hmpt.entrySet(), tileDiameter, center);

        repaint();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
    public void setTileDiameter(int td)
    {
        tileDiameter = td;
    }
    public int getTileDiameter()
    {


        return tileDiameter;
    }
    public void translateOffset(int x, int y)
    {
        center.translate(x, y);
    }
    public final Point getOffset(){ return center;}
    public Point getGridPoint(Point canvasPoint)
    {


        return new Point((int)Math.round((canvasPoint.x-getOffset().x)/(double)getTileDiameter()),(int) Math.round((-canvasPoint.y +getOffset().y)/(double)getTileDiameter()));
    }


}

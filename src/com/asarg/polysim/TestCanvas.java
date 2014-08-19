package  com.asarg.polysim;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by Dom on 8/15/2014.
 */
public class TestCanvas extends JPanel {


    private int tileDiameter = 30;
    private Dimension center = new Dimension(640, 400);
    BufferedImage canvasBFI = new BufferedImage(1280, 800, BufferedImage.TYPE_INT_ARGB);
    Graphics2D cg2d = canvasBFI.createGraphics();

    TestCanvas()
    {
        cg2d.setColor(Color.black);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);

        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(canvasBFI, 0, 0, null);



    }
 /*   public void drawTiles(Set<Point> tiles)
    {
        for(Point p : tiles)
        {
            cg2d.drawRect(p.x*tileDiameter + center.width - tileDiameter/2 , -p.y*tileDiameter + center.height - tileDiameter/2, tileDiameter,tileDiameter);
        }
        repaint();
    }
    */


    public void drawPolyTile(PolyTile pt)
    {
        cg2d.setComposite(AlphaComposite.Clear);
        cg2d.fillRect(0,0,canvasBFI.getWidth(), canvasBFI.getHeight());
        cg2d.setComposite(AlphaComposite.SrcOver);
        List<Tile> lt = pt.tiles;
        if(lt.isEmpty())System.out.println("SDFSA");
        for(Tile t : lt)
        {

            cg2d.setColor(Color.black);
            cg2d.fillRect(t.getLocation().x*tileDiameter + center.width - tileDiameter/2 , -t.getLocation().y*tileDiameter + center.height - tileDiameter/2, tileDiameter,tileDiameter);
            if(t.getGlueE() != null && !t.getGlueE().isEmpty())
            {
                cg2d.setColor(Color.cyan);
                cg2d.fillRect(t.getLocation().x*tileDiameter + center.width - tileDiameter/2 + tileDiameter, -t.getLocation().y*tileDiameter + center.height - tileDiameter/2, tileDiameter,tileDiameter);
                System.out.println(t.getGlueE());
            }
            if(t.getGlueW() != null && !t.getGlueW().isEmpty())
            {
                cg2d.setColor(Color.cyan);
                cg2d.fillRect(t.getLocation().x*tileDiameter + center.width - tileDiameter/2 - tileDiameter, -t.getLocation().y*tileDiameter + center.height - tileDiameter/2, tileDiameter,tileDiameter);
            }
            if(t.getGlueS() != null && !t.getGlueS().isEmpty())
            {
                cg2d.setColor(Color.cyan);
                cg2d.fillRect(t.getLocation().x*tileDiameter + center.width - tileDiameter/2 , -t.getLocation().y*tileDiameter + center.height - tileDiameter/2 + tileDiameter, tileDiameter,tileDiameter);
            }
            if(t.getGlueN() != null && !t.getGlueN().isEmpty())
            {
                cg2d.setColor(Color.cyan);
                cg2d.fillRect(t.getLocation().x*tileDiameter + center.width - tileDiameter/2 , -t.getLocation().y*tileDiameter + center.height - tileDiameter/2  - tileDiameter, tileDiameter,tileDiameter);
            }
        }
         repaint();
    }

    public void drawGrid(HashMap<Point, Tile> hmpt)
    {
        for(Map.Entry<Point,Tile> mep : hmpt.entrySet())
        {
            Point pt = mep.getKey();
            Tile ti = mep.getValue();

            int colorInt = Integer.parseInt(ti.getColor(), 16);
            cg2d.setColor(new Color(colorInt>>16,(colorInt&0x00FF00)>>8,colorInt & 0x0000FF));
            
            cg2d.fillRect(pt.x * tileDiameter + center.width - tileDiameter / 2, -pt.y * tileDiameter + center.height - tileDiameter / 2, tileDiameter, tileDiameter);

        }

       repaint();

    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(1280,800);
    }


}

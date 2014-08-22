package com.asarg.polysim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

/**
 * Created by Dom on 8/21/2014.
 */
public class TileEditorWindow extends JFrame implements ComponentListener{

    JPanel wP;
    Dimension res = new Dimension();
    JScrollPane jsp;
    TileEditorWindow(int width, int height) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        setLayout(new BorderLayout());
        res.setSize(width,height);

        JPanel eP = new JPanel();
        wP = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(res.width/7, res.height/100);
            }
        };
        jsp = new JScrollPane()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(getContentPane().getWidth()/7, getContentPane().getHeight()-5);

            }
        };



        ImageIcon[] icon = new ImageIcon[20];
        final BufferedImage polyBFI = new BufferedImage(100 - jsp.getVerticalScrollBar().getWidth(), 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D polyGFX = polyBFI.createGraphics();
        polyGFX.setClip(0, 0, 100, 100);
        Drawer.TileDrawer.drawCenteredPolyTile(polyGFX, Main.tetrisL());
        icon[0] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawCenteredPolyTile(polyGFX, Main.tetrisX());
        icon[1] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawCenteredPolyTile(polyGFX, Main.tetrisF());
        icon[2] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawCenteredPolyTile(polyGFX, Main.tetrisU());
        icon[3] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawCenteredPolyTile(polyGFX, Main.tetrisV());
        icon[4] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawPolyTile(polyGFX, Main.tetrisL(), 15, new Point(50, 50));
        icon[5] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawPolyTile(polyGFX, Main.tetrisX(), 15, new Point(50, 50));
        icon[6] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawPolyTile(polyGFX, Main.tetrisF(), 15, new Point(50, 50));
        icon[7] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawPolyTile(polyGFX, Main.tetrisV(), 15, new Point(50, 50));
        icon[8] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));
        Drawer.TileDrawer.drawPolyTile(polyGFX, Main.tetrisU(), 15, new Point(50, 50));
        icon[9] = new ImageIcon(toolkit.createImage(polyBFI.getSource()));

        JList<Icon> polyList = new JList<Icon>(icon);


        jsp.setViewportView(polyList);
        polyList.setBackground(getBackground());

        wP.add(jsp);
        System.out.println(jsp.getVerticalScrollBar().getHeight());


        JPanel cP = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(res.width/2, res.height);
            }


            @Override
            public void paintComponent(Graphics g) {

            }

        };

        add(wP, BorderLayout.WEST);
        add(cP, BorderLayout.CENTER);
        add(eP, BorderLayout.EAST);
        pack();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(res.width, res.height);

    }

    @Override
    public void componentResized(ComponentEvent e) {
        remove(jsp);
        add(jsp);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}

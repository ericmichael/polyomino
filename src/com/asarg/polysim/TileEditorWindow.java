package com.asarg.polysim;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dom on 8/21/2014.
 */
public class TileEditorWindow extends JFrame implements ComponentListener{

    JPanel wP;
    Dimension res = new Dimension();
    BufferedImage polyTileCanvas;

    List<ImageIcon> iconList = new ArrayList<ImageIcon>();
    List<PolyTile> polytileList = new ArrayList<PolyTile>();
    JList<Icon> polyJList = new JList<Icon>();
    BufferedImage iconDrawSpace;
    Graphics2D iconDrawSpaceGraphics;

    final private Toolkit toolkit = Toolkit.getDefaultToolkit();



    //Components


    JMenuItem newPolyTileMeniItem = new JMenuItem("New Polytile");
    JScrollPane jsp;


    TileEditorWindow(int width, int height) {

        setLayout(new FlowLayout());
        res.setSize(width,height);


        //tileset creation canvas stuff

        polyTileCanvas = new BufferedImage((int)(res.width*.75), res.height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0,0, (int)(res.width*.75), res.height);

        JPanel cP = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int)(res.width*.75), res.height);
            }


            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(polyTileCanvas,0,0, null);

            }

        };

        JPanel eP = new JPanel();
        wP = new JPanel()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(res.width/8, res.height);
            }
        };


        iconDrawSpace = new BufferedImage(res.width/8, res.width/8, BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0, 0, res.width/8, res.width/8);



        jsp = new JScrollPane()
        {
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(wP.getWidth(), getContentPane().getHeight()-5);

            }
        };


        jsp.setViewportView(polyJList);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        polyJList.setBackground(getBackground());

        wP.setLayout(new FlowLayout());
        jsp.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        wP.add(jsp,FlowLayout.LEFT);

        System.out.println(jsp.getVerticalScrollBar().getHeight());


        //create a menu bar-------------------------------
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newPolyTileMeniItem);
        menubar.add(fileMenu);
        setJMenuBar(menubar);

        //Action listener stuff for the menu bar and List
        ActionListener menuListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == newPolyTileMeniItem)
                {

                    newPolyTile();
                    addPolyTile(Main.tetrisF());
                    addPolyTile(Main.tetrisV());
                    addPolyTile(Main.tetrisX());


                }
            }
        };
        newPolyTileMeniItem.addActionListener(menuListener);


        polyJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.getSelectedIndex()));
                repaint();
            }
        });


        //Add panels to TileSet Editor Frame
        add(wP, FlowLayout.LEFT);
        add(cP, FlowLayout.CENTER);
       // add(eP, BorderLayout.EAST);
        pack();

    }

    public void newPolyTile()
    {
        PolyTile newPolytile = new PolyTile();
        polytileList.add(newPolytile);
        Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, newPolytile);
        iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
        ImageIcon[] icons = new ImageIcon[iconList.size()];
        iconList.toArray(icons);
        polyJList.setListData(icons);


        repaint();




    }
    public void addPolyTile(PolyTile polyTile)
    {

        polytileList.add(polyTile);
        Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, polyTile);
        iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
        ImageIcon[] icons = new ImageIcon[iconList.size()];
        iconList.toArray(icons);
        polyJList.setListData(icons);



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

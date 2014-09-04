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
    JPanel cP;
    Dimension res = new Dimension();
    BufferedImage polyTileCanvas;
    Graphics2D polyTileCanvasGFX;

    List<ImageIcon> iconList = new ArrayList<ImageIcon>();
    List<PolyTile> polytileList = new ArrayList<PolyTile>();
    JList<Icon> polyJList = new JList<Icon>();
    BufferedImage iconDrawSpace;
    Graphics2D iconDrawSpaceGraphics;

    final private Toolkit toolkit = Toolkit.getDefaultToolkit();

    //Components


    JMenuItem newPolyTileMenuItem = new JMenuItem("New Polytile");
    JMenuItem clearListsMenuItem = new JMenuItem("Clear/Reset");
    JScrollPane jsp;


    TileEditorWindow(int width, int height) {

        setLayout(new GridBagLayout());
        GridBagConstraints gbCon = new GridBagConstraints();




        res.setSize(width,height);


        //tileset creation canvas stuff

        polyTileCanvas = new BufferedImage((int)(res.width*.75), (int)(res.width*.75), BufferedImage.TYPE_INT_ARGB);
        polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0,0, (int)(res.width*.75), (int)(res.width*.75));

         cP = new JPanel() {
         /*   @Override
            public Dimension getPreferredSize() {

                return new Dimension((int)(res.width*.75), (int)(res.width*.75));

            }
*/

            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(polyTileCanvas,0,0, null);

            }

        };

        JPanel eP = new JPanel(){

         /*   @Override
            public Dimension getPreferredSize()
            {
                return new Dimension((int)(res.width*.15), res.height);
            }*/
        };
        wP = new JPanel()
        {
            /*
            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension((int)(res.width*.10), res.height);
            }*/
        };

        iconDrawSpace = new BufferedImage((int)(res.width*.10), (int)(res.width*.10), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0, 0, (int)(res.width*.10),(int)(res.width*.10));

        jsp = new JScrollPane()
        {
           @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(getContentPane().getWidth(), getContentPane().getHeight()-5);

            }

        };

        jsp.setViewportView(polyJList);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        polyJList.setBackground(getBackground());

        wP.setLayout(new BorderLayout());
      //  jsp.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);


        System.out.println(jsp.getVerticalScrollBar().getHeight());


        //form stuff
        eP.setLayout(new GridBagLayout());
        GridBagConstraints gC = new GridBagConstraints();
        gC.weighty=.1;
        gC.weightx= 0.5;
        gC.gridy=0;
        gC.gridx=0;
        eP.add(new JLabel("Name: ") , gC);
        gC.gridx=1;
        eP.add(new JLabel("Sup"),gC);


        //create a menu bar-------------------------------
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newPolyTileMenuItem);
        fileMenu.add(clearListsMenuItem);
        menubar.add(fileMenu);
        setJMenuBar(menubar);

        //Action listener stuff for the menu bar and List
        ActionListener menuListener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == newPolyTileMenuItem)
                {

                    newPolyTile();
                    addPolyTile(Main.tetrisF());
                    addPolyTile(Main.tetrisV());
                    addPolyTile(Main.tetrisX());


                }
                if(e.getSource() == clearListsMenuItem)
                {
                    clearLists();
                    repaint();

                }
            }
        };
        newPolyTileMenuItem.addActionListener(menuListener);
        clearListsMenuItem.addActionListener(menuListener);

        polyJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!((JList)(e.getSource())).isSelectionEmpty())
                Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.getSelectedIndex()));
                repaint();
            }
        });

        addComponentListener(this);
        //Add panels to TileSet Editor Frame

        gbCon.gridy=0;
        gbCon.gridx=0;
        gbCon.weightx=.10;
        gbCon.weighty=1;
        gbCon.fill = GridBagConstraints.BOTH;

        add(wP, gbCon);
        gbCon.gridx=1;
        gbCon.weightx=.75;
        add(cP, gbCon);


        gbCon.gridx=2;
        gbCon.weightx=.15;
        add(eP,gbCon);

        cP.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));

        wP.add(jsp);
        pack();

    }

    public void newPolyTile()
    {
        PolyTile newPolytile = new PolyTile();
        polytileList.add(newPolytile);

        iconDrawSpace = new BufferedImage(wP.getWidth(), wP.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0,0,wP.getWidth(), wP.getWidth());
        Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, newPolytile);
        iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
        ImageIcon[] icons = new ImageIcon[iconList.size()];
        iconList.toArray(icons);
        polyJList.setListData(icons);


    }
    public void addPolyTile(PolyTile polyTile)
    {

        polytileList.add(polyTile);
        iconDrawSpace = new BufferedImage(wP.getWidth(), wP.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0,0,wP.getWidth(), wP.getWidth());

        Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, polyTile);
        iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
        ImageIcon[] icons = new ImageIcon[iconList.size()];
        iconList.toArray(icons);
        polyJList.setListData(icons);


    }



    public void reDrawJList()
    {
        iconDrawSpace = new BufferedImage(wP.getWidth(), wP.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0,0,wP.getWidth(), wP.getWidth());
        iconList.clear();
        polyJList.removeAll();
        for(PolyTile pt : polytileList)
        {
            Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, pt);
            iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
            ImageIcon[] icons = new ImageIcon[iconList.size()];
            iconList.toArray(icons);
            polyJList.setListData(icons);
        }

    }
    public void clearLists()
    {

       polytileList.clear();
       iconList.clear();
       polyJList.removeAll();
        newPolyTile();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(res.width, res.height);

    }

    @Override
    public void componentResized(ComponentEvent e) {


        System.out.println(wP.getWidth());

        Dimension newCPSize = cP.getSize();
        polyTileCanvas = new BufferedImage(newCPSize.width, newCPSize.height, BufferedImage.TYPE_INT_ARGB);
        polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0,0, newCPSize.width, newCPSize.height);

        res.setSize(getWidth(), getHeight());

        reDrawJList();
        if(!polyJList.isSelectionEmpty())
                 Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.getSelectedIndex()));
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

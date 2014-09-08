package com.asarg.polysim;

import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
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

    //canvas stuff
    BufferedImage polyTileCanvas;
    Graphics2D polyTileCanvasGFX;
    BufferedImage overLayer;
    Graphics2D overLayerGFX;
    
    //the current canvas display info
    Point canvasCenteredOffset = new Point(0,0);
    int tileDiameter= 1;


    List<ImageIcon> iconList = new ArrayList<ImageIcon>();
    List<PolyTile> polytileList = new ArrayList<PolyTile>();
    JList<Icon> polyJList = new JList<Icon>();
    BufferedImage iconDrawSpace;
    Graphics2D iconDrawSpaceGraphics;

    //for tile info display
    Font infoFont = new Font("Courier", Font.PLAIN, 20);
    JTextField tileLabelTF = new JTextField(5)
    {
       /* @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(, toolkit.getFontMetrics(infoFont).getHeight());
        }*/
    };
    JTextField nGlueLabelTF = new JTextField(5);
    JTextField eGlueLabelTF = new JTextField(5);
    JTextField sGlueLabelTF = new JTextField(5);
    JTextField wGlueLabelTF = new JTextField(5);
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
        overLayer = new BufferedImage((int)(res.width*.75), (int)(res.width*.75), BufferedImage.TYPE_INT_ARGB);
        overLayerGFX = overLayer.createGraphics();
        overLayerGFX.setClip(0,0, (int)(res.width*.75), (int)(res.width*.75));
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
                g2.drawImage(overLayer, 0, 0, null);

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

        //tile information panel stuff
        eP.setLayout(new GridBagLayout());
        GridBagConstraints gC = new GridBagConstraints();
        gC.weighty=0;
       // gC.weightx= 0.5;
      /*  gC.gridy=0;
        gC.gridx=0;
        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.add(new JLabel("Tile Label: ") , gC);
        gC.gridy=1;
        labelPanel.add(new JLabel("N. Glue label"),gC);
        gC.gridy=2;
        labelPanel.add(new JLabel("E. Glue label"),gC);
        gC.gridy=3;
        labelPanel.add(new JLabel("S. Glue label"),gC);
        gC.gridy=4;
        labelPanel.add(new JLabel("W. Glue label"),gC);
        gC.gridx=1;
        gC.gridy=0;

    gC.fill = GridBagConstraints.HORIZONTAL;

        labelPanel.add(tileLabelTF, gC);
        gC.gridy=1;
        labelPanel.add(nGlueLabelTF, gC);
        gC.gridy=2;
        labelPanel.add(eGlueLabelTF, gC);
        gC.gridy=3;
        labelPanel.add(sGlueLabelTF, gC);

        gC.gridy=4;
        labelPanel.add(wGlueLabelTF, gC);
*/
        JPanel tlPanel = new JPanel();
        JPanel ngPanel = new JPanel();
        JPanel egPanel = new JPanel();
        JPanel sgPanel = new JPanel();
        JPanel wgPanel = new JPanel();

        tlPanel.add(new JLabel("Tile Label:"));
        tlPanel.add(tileLabelTF);


        ngPanel.add(new JLabel("N."));
        ngPanel.add(nGlueLabelTF);
        egPanel.add(new JLabel("E."));
        egPanel.add(eGlueLabelTF);
        sgPanel.add(new JLabel("S."));
        sgPanel.add(sGlueLabelTF);
        wgPanel.add(new JLabel("W."));
        wgPanel.add(wGlueLabelTF);


        gC.gridy=0;
        eP.add(tlPanel, gC);
        gC.gridy=1;
        eP.add(new JLabel("-Glues Labels-"), gC);
        gC.gridy=2;
        eP.add(ngPanel,gC);
        gC.gridy=3;
        eP.add(egPanel,gC);
        gC.gridy=4;
        eP.add(sgPanel,gC);
        gC.gridy=5;
        eP.add(wgPanel,gC);


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
                if(!((JList)(e.getSource())).isSelectionEmpty()) {
                    Drawer.clearGraphics(overLayerGFX);
                    Pair<Point, Integer> ppi = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.getSelectedIndex()));
                    canvasCenteredOffset = ppi.getKey();
                    tileDiameter = ppi.getValue();
                }
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


        MouseListener gridListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                Point gridPoint = Drawer.TileDrawer.getGridPoint(e.getPoint(), canvasCenteredOffset, tileDiameter);

                Tile selectedTile =polytileList.get(polyJList.getSelectedIndex()).getTile(gridPoint.x, gridPoint.y);
                if(selectedTile!=null) {
                    displayInfo(selectedTile);
                    Drawer.clearGraphics(overLayerGFX);
                    Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                   repaint();
                }
            }
        };

        cP.addMouseListener(gridListener);

    }


    public void displayInfo(Tile tile)
    {
        tileLabelTF.setText(tile.getLabel());
        nGlueLabelTF.setText(tile.getGlueN());
        eGlueLabelTF.setText(tile.getGlueE());
        sGlueLabelTF.setText(tile.getGlueS());
        wGlueLabelTF.setText(tile.getGlueW());
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

        Dimension newCPSize = cP.getSize();
        polyTileCanvas = new BufferedImage(newCPSize.width, newCPSize.height, BufferedImage.TYPE_INT_ARGB);
        polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0,0, newCPSize.width, newCPSize.height);
        overLayer = new BufferedImage(newCPSize.width, newCPSize.height, BufferedImage.TYPE_INT_ARGB);
        overLayerGFX = overLayer.createGraphics();
        overLayerGFX.setClip(0,0, newCPSize.width, newCPSize.height);

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

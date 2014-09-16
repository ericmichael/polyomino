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
import java.util.Map;

/**
 * Created by Dom on 8/21/2014.
 */
public class TileEditorWindow extends JFrame implements ComponentListener {


    Assembly assembly;
    TileSystem tileSystem;
    final private Toolkit toolkit = Toolkit.getDefaultToolkit();
    JPanel wP;
    JPanel cP;
    Dimension res = new Dimension();

    //canvas stuff
    BufferedImage polyTileCanvas;
    Graphics2D polyTileCanvasGFX;
    BufferedImage overLayer;
    Graphics2D overLayerGFX;

    //the current canvas display info
    Point canvasCenteredOffset = new Point(0, 0);
    int tileDiameter = 1;
    Tile selectedTile = null;


    List<ImageIcon> iconList = new ArrayList<ImageIcon>();
    List<PolyTile> polytileList = new ArrayList<PolyTile>();
    JList<Icon> polyJList = new JList<Icon>();
    BufferedImage iconDrawSpace;
    Graphics2D iconDrawSpaceGraphics;

    //> 3/For tile info display
    Font infoFont = new Font("Courier", Font.PLAIN, 20);
    JTextField tileLabelTF = new JTextField(5) {
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

    JButton applyButton = new JButton("Set");
    JButton removeButton = new JButton("Remove");


    //Components


    //< 3/
    //Menubar Menu items
    JMenuItem newPolyTileMenuItem = new JMenuItem("New Polytile");
    JMenuItem clearListsMenuItem = new JMenuItem("Clear/Reset");
    JMenuItem updateAssemblyMenuItem = new JMenuItem("Update Assembly");
    JMenuItem exportMenuItem = new JMenuItem("Export XML");
    JMenuItem removePolyTileMenuItem = new JMenuItem("Remove");
    JScrollPane jsp;


    TileEditorWindow(int width, int height, Assembly assem) {


        this.assembly = assem;
        this.tileSystem = this.assembly.getTileSystem();
        setLayout(new BorderLayout());
        GridBagConstraints gbCon = new GridBagConstraints();


        res.setSize(width, height);


        //tileset creation canvas stuff

        polyTileCanvas = new BufferedImage((int) (res.width * .75), (int) (res.width * .75), BufferedImage.TYPE_INT_ARGB);
        polyTileCanvasGFX = polyTileCanvas.createGraphics();
        polyTileCanvasGFX.setClip(0, 0, (int) (res.width * .75), (int) (res.width * .75));
        overLayer = new BufferedImage((int) (res.width * .75), (int) (res.width * .75), BufferedImage.TYPE_INT_ARGB);
        overLayerGFX = overLayer.createGraphics();
        overLayerGFX.setClip(0, 0, (int) (res.width * .75), (int) (res.width * .75));
        cP = new JPanel() {
         /*   @Override
            public Dimension getPreferredSize() {

                return new Dimension((int)(res.width*.75), (int)(res.width*.75));

            }
*/

            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(polyTileCanvas, 0, 0, null);
                g2.drawImage(overLayer, 0, 0, null);

            }

        };

        JPanel eP = new JPanel() {

         /*   @Override
            public Dimension getPreferredSize()
            {
                return new Dimension((int)(res.width*.15), res.height);
            }*/
        };
        wP = new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int) (res.width * .10), res.height);
            }
        };

        iconDrawSpace = new BufferedImage((int) (res.width * .10), (int) (res.width * .10), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0, 0, (int) (res.width * .10), (int) (res.width * .10));

        jsp = new JScrollPane() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(getContentPane().getWidth(), getContentPane().getHeight() - 5);

            }

        };

        jsp.setViewportView(polyJList);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        polyJList.setBackground(getBackground());

        wP.setLayout(new BorderLayout());

        //tile information panel stuff
        eP.setLayout(new GridBagLayout());
        GridBagConstraints gC = new GridBagConstraints();
        gC.weighty = 0;

        JPanel tlPanel = new JPanel();
        JPanel ngPanel = new JPanel();
        JPanel egPanel = new JPanel();
        JPanel sgPanel = new JPanel();
        JPanel wgPanel = new JPanel();
        JPanel applyRemovePanel = new JPanel();

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
        applyRemovePanel.add(applyButton);
        applyRemovePanel.add(removeButton);


        gC.gridy = 0;
        eP.add(tlPanel, gC);
        gC.gridy = 1;
        eP.add(new JLabel("-Glues Labels-"), gC);
        gC.gridy = 2;
        eP.add(ngPanel, gC);
        gC.gridy = 3;
        eP.add(egPanel, gC);
        gC.gridy = 4;
        eP.add(sgPanel, gC);
        gC.gridy = 5;
        eP.add(wgPanel, gC);
        gC.gridy = 6;
        eP.add(applyRemovePanel, gC);


        //create a menu bar-------------------------------
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        final JMenu assemblyMenu = new JMenu("Assembly");
        JMenu polyTileMenu = new JMenu("PolyTile");
        fileMenu.add(newPolyTileMenuItem);
        fileMenu.add(clearListsMenuItem);
        fileMenu.add(exportMenuItem);
        assemblyMenu.add(updateAssemblyMenuItem);
        polyTileMenu.add(removePolyTileMenuItem);
        menubar.add(fileMenu);
        menubar.add(assemblyMenu);
        menubar.add(polyTileMenu);
        setJMenuBar(menubar);

        //Action listener stuff for the menu bar and List
        ActionListener tileEditorActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == newPolyTileMenuItem) {

                    newPolyTile();
                } else if (e.getSource() == clearListsMenuItem) {
                    clearLists();
                    repaint();

                } else if (e.getSource() == updateAssemblyMenuItem) {
                    TileConfiguration tileConfig = new TileConfiguration();
                    for (PolyTile polyTile : polytileList) {
                        tileConfig.addTileType(polyTile);
                    }

                    //reuse glue functions for now
                    for (Map.Entry<Pair<String, String>, Integer> glF : tileSystem.getGlueFunction().entrySet()) {
                        String gLabelL = glF.getKey().getKey();
                        String gLabelR = glF.getKey().getValue();
                        int strentgh = glF.getValue();
                        tileConfig.addGlueFunction(gLabelL, gLabelR, strentgh);
                    }

                    tileSystem.loadTileConfiguration(tileConfig);
                    assembly.changeTileSystem(tileSystem);
                    assembly.cleanUp();
                    assembly.getOpenGlues();
                    assembly.calculateFrontier();


                }
                else if(e.getSource() == removePolyTileMenuItem)
                {
                    if(!polyJList.isSelectionEmpty())
                    {

                        polytileList.remove(polyJList.getSelectedIndex());
                        reDrawJList();

                    }
                }
                else if (e.getSource() == applyButton) {
                    setTileData(selectedTile);
                    Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.isSelectionEmpty() ? 0 : polyJList.getSelectedIndex()), tileDiameter);
                    repaint();

                } else
                if (e.getSource() == removeButton) {


                }
            }
        };

        newPolyTileMenuItem.addActionListener(tileEditorActionListener);
        clearListsMenuItem.addActionListener(tileEditorActionListener);
        exportMenuItem.addActionListener(tileEditorActionListener);
        updateAssemblyMenuItem.addActionListener(tileEditorActionListener);
        removePolyTileMenuItem.addActionListener(tileEditorActionListener);

        polyJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!((JList) (e.getSource())).isSelectionEmpty()) {
                    Drawer.clearGraphics(overLayerGFX);
                    Pair<Point, Integer> ppi = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.getSelectedIndex()));
                    canvasCenteredOffset = ppi.getKey();
                    tileDiameter = ppi.getValue();
                    Drawer.clearGraphics(overLayerGFX);
                    selectedTile = null;
                }
                repaint();
            }
        });

        addComponentListener(this);
        //Add panels to TileSet Editor Frame

        gbCon.gridy = 0;
        gbCon.gridx = 0;
        gbCon.weightx = .13;
        gbCon.weighty = 1;
        gbCon.fill = GridBagConstraints.BOTH;

        // add(wP, gbCon);
        add(wP, BorderLayout.WEST);
        gbCon.gridx = 1;
        gbCon.weightx = .75;
        //  add(cP, gbCon);
        add(cP, BorderLayout.CENTER);

        gbCon.gridx = 2;
        gbCon.weightx = .12;
        //   add(eP,gbCon);
        add(eP, BorderLayout.EAST);

        cP.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        wP.add(jsp);

        pack();


        MouseAdapter gridListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if(!polyJList.isSelectionEmpty()) {
                    PolyTile polytile =  polytileList.get(polyJList.getSelectedIndex());
                    Point gridPoint = Drawer.TileDrawer.getGridPoint(e.getPoint(), canvasCenteredOffset, tileDiameter);
                    Tile tile = polytile.getTile(gridPoint.x, gridPoint.y);
                    if (tile != null) {
                        selectedTile = tile;
                        if (selectedTile != null) {
                            displayTileData(selectedTile);
                            Drawer.clearGraphics(overLayerGFX);
                            Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                            repaint();
                        }
                    } else if (polytile.adjacentExits(gridPoint)) {
                        polytile.addTile(gridPoint.x, gridPoint.y, new String[4]);
                        Pair<Point, Integer> newOffDia = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX,polytile, tileDiameter);
                        canvasCenteredOffset = newOffDia.getKey();
                        tileDiameter = newOffDia.getValue();
                        repaint();

                    }
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);

                if (e.getWheelRotation() == 1 && tileDiameter > 1) {
                    tileDiameter *= .95;
                    Pair<Point, Integer> ofnt = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.isSelectionEmpty() ? 0 : polyJList.getSelectedIndex()), tileDiameter);
                    canvasCenteredOffset = ofnt.getKey();
                    if (selectedTile != null)
                        Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                    repaint();

                } else if (e.getWheelRotation() == -1 && tileDiameter * 3 < cP.getWidth() && tileDiameter * 3 < cP.getHeight()) {
                    tileDiameter *= 1.05;
                    Pair<Point, Integer> ofnt = Drawer.TileDrawer.drawCenteredPolyTile(polyTileCanvasGFX, polytileList.get(polyJList.isSelectionEmpty() ? 0 : polyJList.getSelectedIndex()), tileDiameter);
                    canvasCenteredOffset = ofnt.getKey();
                    if (selectedTile != null)
                        Drawer.TileDrawer.drawTileSelection(overLayerGFX, selectedTile.getLocation(), tileDiameter, canvasCenteredOffset, Color.CYAN);
                    repaint();

                }
            }
        };


        cP.addMouseListener(gridListener);
        cP.addMouseWheelListener(gridListener);
        applyButton.addActionListener(tileEditorActionListener);


        //split pane

        JSplitPane wPSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wP, cP);
        wPSplitPane.setOneTouchExpandable(true);
        wPSplitPane.setDividerLocation(wP.getWidth());
        add(wPSplitPane);


        wP.addComponentListener(this);


        initilizeLists();

    }


    public void setTileData(Tile tile) {
        tile.setLabel(tileLabelTF.getText());
        tile.setGlueN(nGlueLabelTF.getText());
        tile.setGlueE(eGlueLabelTF.getText());
        tile.setGlueS(sGlueLabelTF.getText());
        tile.setGlueW(wGlueLabelTF.getText());

    }

    public void displayTileData(Tile tile) {
        if (selectedTile != null)
            tileLabelTF.setText(tile.getLabel());
        nGlueLabelTF.setText(tile.getGlueN());
        eGlueLabelTF.setText(tile.getGlueE());
        sGlueLabelTF.setText(tile.getGlueS());
        wGlueLabelTF.setText(tile.getGlueW());
    }

    public void newPolyTile() {
        PolyTile newPolytile = new PolyTile();
        polytileList.add(newPolytile);
        reDrawJList();

    }

    public void addPolyTile(PolyTile polyTile) {

        polytileList.add(polyTile);
        iconDrawSpace = new BufferedImage(wP.getWidth(), wP.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0, 0, wP.getWidth(), wP.getWidth());

        Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, polyTile);
        iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
        ImageIcon[] icons = new ImageIcon[iconList.size()];
        iconList.toArray(icons);
        polyJList.setListData(icons);


    }


    public void reDrawJList() {
        iconDrawSpace = new BufferedImage(wP.getWidth(), wP.getWidth(), BufferedImage.TYPE_INT_ARGB);
        iconDrawSpaceGraphics = iconDrawSpace.createGraphics();
        iconDrawSpaceGraphics.setClip(0, 0, wP.getWidth(), wP.getWidth());
        iconList.clear();
        polyJList.removeAll();
        for (PolyTile pt : polytileList) {
            Drawer.TileDrawer.drawCenteredPolyTile(iconDrawSpaceGraphics, pt);
            iconList.add(new ImageIcon(toolkit.createImage(iconDrawSpace.getSource())));
            ImageIcon[] icons = new ImageIcon[iconList.size()];
            iconList.toArray(icons);
            polyJList.setListData(icons);
        }

    }

    public void initilizeLists() {
        clearLists();
        List<PolyTile> polyList = new ArrayList<PolyTile>(assembly.getTileSystem().getTileTypes());
        for(PolyTile polytile : polyList)
        {
            polytileList.add(polytile.getCopy());
        }
        reDrawJList();
    }

    public void clearLists() {

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
        polyTileCanvasGFX.setClip(0, 0, newCPSize.width, newCPSize.height);
        overLayer = new BufferedImage(newCPSize.width, newCPSize.height, BufferedImage.TYPE_INT_ARGB);
        overLayerGFX = overLayer.createGraphics();
        overLayerGFX.setClip(0, 0, newCPSize.width, newCPSize.height);

        res.setSize(getWidth(), getHeight());


        reDrawJList();
        if (!polyJList.isSelectionEmpty())
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

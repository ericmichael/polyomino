package com.asarg.polysim;




import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TestCanvasFrame extends JFrame implements MouseWheelListener, MouseMotionListener, MouseListener,KeyListener, ComponentListener{

    TestCanvas tc;
    GradientToolbar stepControlToolBar = new GradientToolbar();
    ActionListener actionListener;
    TileEditorWindow tileEditorWindow = new TileEditorWindow(800,600);
    ControlButton next = new ControlButton("forward");
    ControlButton prev = new ControlButton("backward");
    ControlButton play = new ControlButton("play");
    ControlButton fastf = new ControlButton("fast-forward");
    ControlButton fastb = new ControlButton("fast-backward");
    IconButton optionButton = new IconButton();
    JMenuBar mainMenu = new JMenuBar();
    // Menu Bar Items
    JMenuItem newMenuItem = new JMenuItem("New Assembly");
    JMenuItem loadAssemblyMenuItem = new JMenuItem("Load");
    JMenuItem saveAssemblyMenuItem = new JMenuItem("Save");
    JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
    JMenuItem closeMenuItem = new JMenuItem("Close");

    JMenuItem importTileSetMenuItem = new JMenuItem("Import Tile Set");
    JMenuItem tileSetEditorMenuItem = new JMenuItem("Tile Set Editor");

    JMenuItem undoMenuItem = new JMenuItem("Undo");
    JMenuItem redoMenuItem = new JMenuItem("Redo");

    JMenuItem seedCreatorMenuItem = new JMenuItem("Seed Creator");
    JMenuItem tileSystemOptionsMenuItem = new JMenuItem("Options");

    int width;
    int height;

    Point lastMouseXY = new Point(width,height);
    int dragCount = 0;

    Assembly assembly;
    Frontier frontier;
    PolyTile frontierTile;

    boolean frontierClick = false;
    ArrayList<FrontierElement> frontierAttachments;
    int frontierIndex = 0;
    FrontierElement currentFrontierAttachment = null;
    Point frontierClickPoint = null;


    public TestCanvasFrame(int w, int h, final Assembly assembly)
    {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.assembly = assembly;
        width = w;
        height = h;
        frontier = this.assembly.calculateFrontier();
        setLayout(new BorderLayout());

        tc = new TestCanvas();
        add(tc, BorderLayout.SOUTH);
        tc.addMouseWheelListener(this);
        tc.addMouseListener(this);
        tc.addMouseMotionListener(this);
        tc.setSize(width, height);

        addActionListeners();

        addToolBars();

        addMenuBars();

        pack();
        setVisible(true);
        drawGrid();
    }

    private void addToolBars(){
        stepControlToolBar.add(fastb);
        fastb.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(prev);
        prev.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(play);
        play.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(next);
        next.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(fastf);
        fastf.setPreferredSize(new Dimension(30, 25));

        optionButton.setText(String.valueOf('\uf013'));
        stepControlToolBar.add(optionButton);

        stepControlToolBar.setBorder(new EtchedBorder());
        stepControlToolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(stepControlToolBar);
    }

    private void addMenuBars(){
        JMenu fileMenu = new JMenu("File");
        newMenuItem = new JMenuItem("New Assembly");
        newMenuItem.addActionListener(actionListener);
        loadAssemblyMenuItem = new JMenuItem("Load");
        loadAssemblyMenuItem.addActionListener(actionListener);
        saveAssemblyMenuItem = new JMenuItem("Save");
        saveAssemblyMenuItem.addActionListener(actionListener);
        saveAsMenuItem = new JMenuItem("Save as...");
        saveAsMenuItem.addActionListener(actionListener);
        closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(actionListener);
        fileMenu.add(newMenuItem);
        fileMenu.add(loadAssemblyMenuItem);
        fileMenu.add(saveAssemblyMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);

        JMenu toolsMenu = new JMenu("Tools");
        importTileSetMenuItem = new JMenuItem("Import Tile Set");
        importTileSetMenuItem.addActionListener(actionListener);
        tileSetEditorMenuItem = new JMenuItem("Tile Set Editor");
        tileSetEditorMenuItem.addActionListener(actionListener);
        toolsMenu.add(importTileSetMenuItem);
        toolsMenu.add(tileSetEditorMenuItem);

        JMenu editMenu = new JMenu("Edit");
        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(actionListener);
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(actionListener);
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);

        JMenu tileSystemMenu = new JMenu("Tile System");
        seedCreatorMenuItem = new JMenuItem("Seed Creator");
        seedCreatorMenuItem.addActionListener(actionListener);
        tileSystemOptionsMenuItem = new JMenuItem("Options");
        tileSystemOptionsMenuItem.addActionListener(actionListener);
        tileSystemMenu.add(seedCreatorMenuItem);
        tileSystemMenu.add(tileSystemOptionsMenuItem);

        mainMenu.add(fileMenu);
        mainMenu.add(editMenu);
        mainMenu.add(toolsMenu);
        mainMenu.add(tileSystemMenu);
        setJMenuBar(mainMenu);
    }
    private void resetFrontier(){
        removeFrontierAttachments();
        RemoveFrontierFromGrid();
    }
    private void step(int i){
        if(i==1) { //forward
            if(!frontier.isEmpty()) {
                resetFrontier();
                assembly.attach();
                frontier = assembly.calculateFrontier();
                drawGrid();
            }
        }else if(i==2) { //fastforward
            while(!frontier.isEmpty()){
                resetFrontier();
                assembly.attach();
                frontier = assembly.calculateFrontier();
            }
            tc.reset();
            drawGrid();
        }else if(i==-1) { //backward
            if(!assembly.getAttached().isEmpty()) {
                resetFrontier();
                assembly.detach();
                tc.reset();
                frontier = assembly.calculateFrontier();
                drawGrid();
            }
        }else if(i==-2) { //fastbackward
            resetFrontier();
            while(!assembly.getAttached().isEmpty()){
                assembly.detach();
            }
            tc.reset();
            frontier = assembly.calculateFrontier();
            drawGrid();
        }
    }

    private void addActionListeners(){
        addKeyListener(this);
        addComponentListener(this);
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(next))
                {
                    step(1);
                }else if(e.getSource().equals(prev))
                {
                    step(-1);
                }else if(e.getSource().equals(play)){
                    while(!frontier.isEmpty()){
                        removeFrontierAttachments();
                        RemoveFrontierFromGrid();
                        assembly.attach();
                        tc.reset();
                        frontier = assembly.calculateFrontier();
                        drawGrid();
                        try {
                            Thread.sleep(1000);
                        }catch(InterruptedException ie) {
                        }
                    }
                }else if(e.getSource().equals(fastb)){
                    step(-2);
                }else if(e.getSource().equals(fastf)){
                    step(2);
                } else if (e.getSource().equals(newMenuItem)) {
                    System.out.println("new assembly");
                    TestCanvasFrame tcf = new TestCanvasFrame(800, 600, new Assembly());
                } else if (e.getSource().equals(loadAssemblyMenuItem)) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    int result = fileChooser.showOpenDialog(getParent());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        File tileConfig;
                        try {
                            tileConfig = new File(selectedFile.getParentFile() + "tileconfig.xml");
                        }
                        catch(Exception exc){
                            tileConfig = null;
                        }
                        try {
                            JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                            unmarshaller = jaxbContext.createUnmarshaller();
                            assembly = (Assembly) unmarshaller.unmarshal(selectedFile);
                            if(tileConfig!=null){
                                TileConfiguration tc;

                                jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
                                unmarshaller = jaxbContext.createUnmarshaller();
                                tc = (TileConfiguration) unmarshaller.unmarshal(new File("./Examples/RNG_ATAM/tileconfig.xml"));

                                assembly.getTileSystem().loadTileConfiguration(tc);

                                for(PolyTile p : assembly.getTileSystem().getTileTypes()) {
                                    p.setGlues();
                                }
                            }

                            TestCanvasFrame tcf = new TestCanvasFrame(800, 600, assembly);
                        } catch (javax.xml.bind.JAXBException jaxbe) {
                            javax.swing.JOptionPane.showMessageDialog(null, "Failed to load assembly");
                        }
                    }
                } else if (e.getSource().equals(closeMenuItem)){
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to exit the application?",
                            "Exit Application",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION)
                        System.exit(0);
                } else if (e.getSource().equals(tileSetEditorMenuItem)){
                    tileEditorWindow.setVisible(true);
                }
            }
        };

        next.addActionListener(actionListener);
        prev.addActionListener(actionListener);
        play.addActionListener(actionListener);
        fastf.addActionListener(actionListener);
        fastb.addActionListener(actionListener);
    }


    public void drawGrid()
    {
        PlaceFrontierOnGrid();
        tc.drawGrid(assembly.Grid);
        repaint();
    }

    private PolyTile getFrontierPolyTile(){
        if(frontierTile==null){
            frontierTile = new PolyTile("");
            frontierTile.setColor("428bca");
            String glues[]= {null, null, null, null};
            frontierTile.addTile(0,0, glues);
            frontierTile.setFrontier();
            return frontierTile;
        }else return frontierTile;
    }

    private void PlaceFrontierOnGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.put(fe.getLocation(), getFrontierPolyTile().getTile(0,0));
        }
    }

    private void RemoveFrontierFromGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.remove(fe.getLocation());
        }
    }

    public void zoomInDraw()
    {
        int tileDiameter = tc.getTileDiameter();
        if(tileDiameter< width/2)
            tc.setTileDiameter((int)(tileDiameter*1.25));
        else return;

        tc.reset();
        drawGrid();
        repaint();
    }
    public void zoomOutDraw()
    {
        int tileDiameter = tc.getTileDiameter();
        if(tileDiameter > 10) {
            tc.setTileDiameter((int) (tileDiameter * .75));
        }
        else return;

        tc.reset();
        drawGrid();
        repaint();
    }

    private void nextFrontierPoint(){
        System.out.println("next frontier point");
    }

    private void prevFrontierPoint(){
        System.out.println("prev frontier point");
    }

    private void removeCurrentFrontierAttachment(){
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
            currentFrontierAttachment=null;
        } else {
            assembly.Grid.remove(frontierClickPoint);
        }

    }

    private void addFrontierAttachment(int index){
        if( frontierAttachments.size()>0){
            currentFrontierAttachment = frontierAttachments.get(index);
            assembly.placePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() == 1)
        {
            System.out.println("wheel");
            if(frontierClick){
                System.out.println("wheel with frontier");
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null){
                    addFrontierAttachment(frontierIndex);
                }else if(frontierIndex!=0){
                    addFrontierAttachment(frontierIndex-1);
                    frontierIndex-=1;
                }else {
                    frontierIndex=frontierAttachments.size()-1;
                    addFrontierAttachment(frontierIndex);
                }


                tc.drawGrid(assembly.Grid);
                repaint();
                return;
            }
            zoomOutDraw();
        }
        else if(e.getWheelRotation() == -1)
        {
            System.out.println("wheel");
            if(frontierClick){
                System.out.println("wheel with frontier");
                int flength = frontierAttachments.size();
                System.out.println("Flength: " + flength);
                System.out.println("index: " + frontierIndex);
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null) {
                    addFrontierAttachment(frontierIndex);
                }
                else if(frontierIndex+1 < frontierAttachments.size()){
                    addFrontierAttachment(frontierIndex+1);
                    frontierIndex+=1;
                }else{
                    addFrontierAttachment(0);
                    frontierIndex=0;
                }

                tc.drawGrid(assembly.Grid);
                repaint();
                return;
            }
            zoomInDraw();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        tc.translateOffset(e.getX() - lastMouseXY.x, e.getY() - lastMouseXY.y);
        lastMouseXY=e.getPoint();
        tc.reset();
        drawGrid();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void removeFrontierAttachments(){
        if(currentFrontierAttachment!=null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
        frontierClick = false;
        frontierClickPoint = null;
        frontierAttachments = null;
        frontierIndex = 0;
        currentFrontierAttachment=null;
        drawGrid();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == tc) {
            Point clicked = tc.getGridPoint(e.getPoint());
            Tile clicked_tile = assembly.Grid.get(clicked);
            if(clicked_tile!=null){
                PolyTile clicked_pt = clicked_tile.getParent();
                System.out.println(clicked_pt);
                frontierClick = clicked_pt.isFrontier();
                if(frontierClick) {
                    try {
                        if (currentFrontierAttachment != null) {
                            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
                        }
                    }catch(NullPointerException npe){

                    }
                    frontierClickPoint = clicked;
                    frontierAttachments = new ArrayList<FrontierElement>();
                    frontierIndex = 0;
                    for (FrontierElement fe : frontier) {
                        if (fe.getLocation().equals(clicked) && fe.checkAttachment()) {
                            frontierAttachments.add(fe);
                        }
                    }
                    drawGrid();
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMouseXY = e.getPoint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragCount = 0;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            zoomInDraw();
        }else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            zoomOutDraw();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            step(1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            step(-1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            removeFrontierAttachments();
            RemoveFrontierFromGrid();
            tc.reset();
            frontier = assembly.calculateFrontier();
            drawGrid();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void componentResized(ComponentEvent e) {
        remove(tc);
        add(tc);
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

package com.asarg.polysim;




import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class TestCanvasFrame extends JFrame implements MouseWheelListener, MouseMotionListener, MouseListener,KeyListener, ComponentListener{

    TestCanvas canvas;

    ActionListener actionListener;
    TileEditorWindow tileEditorWindow ;

    GUI mainMenu = new GUI();
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
        tileEditorWindow = new TileEditorWindow(800,600, assembly);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.assembly = assembly;
        width = w;
        height = h;
        frontier = this.assembly.calculateFrontier();
        setLayout(new BorderLayout());

        canvas = new TestCanvas();
        add(canvas, BorderLayout.CENTER);
        canvas.setSize(width, height);

        addActionListeners();

        this.setJMenuBar(mainMenu);
        add(mainMenu.stepControlToolBar,BorderLayout.NORTH);
        add(mainMenu.statusPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
        placeFrontierOnGrid();
        drawGrid();
    }

    private void resetFrontier(){
        exitFrontierMode();
        removeFrontierFromGrid();
    }

    private void updateAttachTime(double time){
        String time_str = String.format("%.4f", time);
        mainMenu.statusLabel.setText("Attachment took " + time_str + "ms");
    }

    private void step(int i){
        if(i==1) { //forward
            if(!frontier.isEmpty()) {
                resetFrontier();
                updateAttachTime(assembly.attach());
                frontier = assembly.calculateFrontier();
                placeFrontierOnGrid();
                drawGrid();
            }
        }else if(i==2) { //fastforward
            while(!frontier.isEmpty()){
                resetFrontier();
                updateAttachTime(assembly.attach());
                frontier = assembly.calculateFrontier();
            }
            canvas.reset();
            placeFrontierOnGrid();
            drawGrid();
        }else if(i==-1) { //backward
            if(!assembly.getAttached().isEmpty()) {
                resetFrontier();
                assembly.detach();
                canvas.reset();
                frontier = assembly.calculateFrontier();
                placeFrontierOnGrid();
                drawGrid();
            }
        }else if(i==-2) { //fastbackward
            resetFrontier();
            while(!assembly.getAttached().isEmpty()){
                assembly.detach();
            }
            canvas.reset();
            frontier = assembly.calculateFrontier();
            placeFrontierOnGrid();
            drawGrid();
        }
    }

    private void addActionListeners(){
        canvas.addMouseWheelListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        addKeyListener(this);
        addComponentListener(this);
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(mainMenu.next)) {
                    if(!frontier.isEmpty()) {
                        step(1);
                    }
                }else if(e.getSource().equals(mainMenu.prev)) {
                    if(!assembly.getAttached().isEmpty()) {
                        step(-1);
                    }
                }else if(e.getSource().equals(mainMenu.play)){
                    while(!frontier.isEmpty()){
                        resetFrontier();
                        assembly.attach();
                        canvas.reset();
                        frontier = assembly.calculateFrontier();
                        placeFrontierOnGrid();
                        drawGrid();
                        try {
                            Thread.sleep(1000);
                        }catch(InterruptedException ie) {
                            System.out.println(ie.getMessage());
                        }
                    }
                }else if(e.getSource().equals(mainMenu.fastb)){
                    step(-2);
                }else if(e.getSource().equals(mainMenu.fastf)){
                    step(2);
                } else if (e.getSource().equals(mainMenu.newMenuItem)) {
                    System.out.println("new assembly");
                    // creates a new assembly frame when "new assembly button is clicked".
                    TestCanvasFrame tcf = new TestCanvasFrame(800, 600, new Assembly());
                } else if (e.getSource().equals(mainMenu.loadAssemblyMenuItem)) {
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
                            Unmarshaller unmarshaller;
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
                } else if (e.getSource().equals(mainMenu.closeMenuItem)){
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to exit the application?",
                            "Exit Application",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION)
                        System.exit(0);
                } else if (e.getSource().equals(mainMenu.tileSetEditorMenuItem)){
                    tileEditorWindow.setVisible(true);
                } else if (e.getSource().equals(mainMenu.setTemperatureMenuItem)){
                    String temperatureString = JOptionPane.showInputDialog(null, "Set the system's temperature", "2");
                    int temperature = Integer.parseInt(temperatureString);
                    assembly.getTileSystem().setTemperature(temperature);
                    resetFrontier();
                    frontier.clear();
                    canvas.reset();
                    frontier = assembly.calculateFrontier();
                    placeFrontierOnGrid();
                    drawGrid();
                    System.out.println("Change in temperature detected. Beware of errors.");
                }
            }
        };

        mainMenu.next.addActionListener(actionListener);
        mainMenu.prev.addActionListener(actionListener);
        mainMenu.play.addActionListener(actionListener);
        mainMenu.fastf.addActionListener(actionListener);
        mainMenu.fastb.addActionListener(actionListener);
        mainMenu.newMenuItem.addActionListener(actionListener);
        mainMenu.loadAssemblyMenuItem.addActionListener(actionListener);
        mainMenu.saveAssemblyMenuItem.addActionListener(actionListener);
        mainMenu.saveAsMenuItem.addActionListener(actionListener);
        mainMenu.closeMenuItem.addActionListener(actionListener);
        mainMenu.importTileSetMenuItem.addActionListener(actionListener);
        mainMenu.tileSetEditorMenuItem.addActionListener(actionListener);
        mainMenu.undoMenuItem.addActionListener(actionListener);
        mainMenu.redoMenuItem.addActionListener(actionListener);
        mainMenu.seedCreatorMenuItem.addActionListener(actionListener);
        mainMenu.tileSystemOptionsMenuItem.addActionListener(actionListener);
        mainMenu.setTemperatureMenuItem.addActionListener(actionListener);
    }


    public void drawGrid()    {
        //PlaceFrontierOnGrid();
        canvas.drawGrid(assembly.Grid);
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

    private void placeFrontierOnGrid() {
        if (assembly.Grid.isEmpty()){
            for (PolyTile pt: assembly.getTileSystem().getTileTypes()){
                frontier.add(new FrontierElement(new Point(0,0), new Point(0,0), pt, 4));
            }
        }
        for (FrontierElement fe : frontier){
            assembly.Grid.put(fe.getLocation(), getFrontierPolyTile().getTile(0,0));
        }
    }

    private void removeFrontierFromGrid(){
        for (FrontierElement fe : frontier){
            assembly.Grid.remove(fe.getLocation());
        }
    }

    public void zoomInDraw() {
        int tileDiameter = canvas.getTileDiameter();
        if(tileDiameter< width/2)
            canvas.setTileDiameter((int)(tileDiameter*1.25));
        else return;

        canvas.reset();
        placeFrontierOnGrid();
        drawGrid();
    }
    public void zoomOutDraw() {
        int tileDiameter = canvas.getTileDiameter();
        if(tileDiameter > 10) {
            canvas.setTileDiameter((int) (tileDiameter * .75));
        }
        else return;

        canvas.reset();
        placeFrontierOnGrid();
        drawGrid();
    }

    private void removeCurrentFrontierAttachment() {
        if (currentFrontierAttachment != null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
            mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
        } else {
            assembly.Grid.remove(frontierClickPoint);
        }
        canvas.reset();
    }

    private void addFrontierAttachment(int index) {
        if( frontierAttachments.size()>0){
            currentFrontierAttachment = frontierAttachments.get(index);
            double probability = frontier.getProbability(currentFrontierAttachment);
            String status_str = String.format("Probability of Attachment: %.4f", probability);
            mainMenu.statusLabel.setText(status_str);
            assembly.placePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() == 1)
        {
            if(frontierClick){
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null){
                    frontierIndex=0;
                    addFrontierAttachment(frontierIndex);

                }else if(frontierIndex > 0){
                    frontierIndex-=1;
                    addFrontierAttachment(frontierIndex);
                }else {
                    frontierIndex=frontierAttachments.size()-1;
                    addFrontierAttachment(frontierIndex);
                }

                drawGrid();
                return;
            }
            zoomOutDraw();
        }
        else if(e.getWheelRotation() == -1) {
            if(frontierClick){
                removeCurrentFrontierAttachment();
                if(currentFrontierAttachment==null) {
                    frontierIndex=0;
                    addFrontierAttachment(frontierIndex);
                }
                else if(frontierIndex+1 < frontierAttachments.size()){
                    frontierIndex+=1;
                    addFrontierAttachment(frontierIndex);
                }else{
                    frontierIndex=0;
                    addFrontierAttachment(0);
                }
                drawGrid();
                return;
            }
            zoomInDraw();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        canvas.translateOffset(e.getX() - lastMouseXY.x, e.getY() - lastMouseXY.y);
        lastMouseXY=e.getPoint();
        canvas.reset();
        drawGrid();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void exitFrontierMode(){
        if(currentFrontierAttachment!=null) {
            assembly.removePolytile(currentFrontierAttachment.getPolyTile(), currentFrontierAttachment.getOffset().x, currentFrontierAttachment.getOffset().y);
        }
        frontierClick = false;
        frontierClickPoint = null;
        frontierAttachments = null;
        frontierIndex = 0;
        currentFrontierAttachment=null;
        mainMenu.statusLabel.setText(mainMenu.statusLabelPreviousText);
        canvas.reset();
    }

    private void processFrontierClick(Point clicked, Tile clicked_tile){
        PolyTile clicked_pt = clicked_tile.getParent();
        if(!frontierClick){
            mainMenu.statusLabelPreviousText = mainMenu.statusLabel.getText();
        }else{
            placeFrontierOnGrid();
        }
        frontierClick = clicked_pt.isFrontier();
        if(frontierClick) {
            //kick me out of any previous frontier state
            exitFrontierMode();
            frontierClick=true;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == canvas) {
            Point clicked = Drawer.TileDrawer.getGridPoint(e.getPoint(), canvas.getOffset(), canvas.getTileDiameter());
            Tile clicked_tile = assembly.Grid.get(clicked);
            if(clicked_tile!=null){
                processFrontierClick(clicked, clicked_tile);
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
            exitFrontierMode();
            frontier = assembly.calculateFrontier();
            placeFrontierOnGrid();
            drawGrid();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (currentFrontierAttachment != null) {
                FrontierElement fe = new FrontierElement(currentFrontierAttachment.getLocation(),
                        currentFrontierAttachment.getOffset(),currentFrontierAttachment.getPolyTile(),4);
                resetFrontier();
                assembly.attach(fe);
                frontier = assembly.calculateFrontier();
                placeFrontierOnGrid();
                exitFrontierMode();
                drawGrid();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void componentResized(ComponentEvent e) {
        remove(canvas);
        add(canvas);
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

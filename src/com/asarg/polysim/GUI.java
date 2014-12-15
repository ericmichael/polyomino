package com.asarg.polysim;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;


public class GUI extends JMenuBar{
    SimulationWindow window;
    GradientToolbar stepControlToolBar = new GradientToolbar();

    // Menu Bar Items
    JMenuItem newMenuItem = new JMenuItem("New Assembly");
    JMenuItem loadAssemblyMenuItem = new JMenuItem("Load");
    JMenuItem saveAssemblyMenuItem = new JMenuItem("Save");
    JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
    JMenuItem closeMenuItem = new JMenuItem("Close");

    JMenuItem importTileSetMenuItem = new JMenuItem("Import Tile Set");
    JMenuItem tileSetEditorMenuItem = new JMenuItem("Tile Set Editor");

//    JMenuItem undoMenuItem = new JMenuItem("Undo");
//    JMenuItem redoMenuItem = new JMenuItem("Redo");

    JMenuItem seedCreatorMenuItem = new JMenuItem("Seed Creator");
    JMenuItem setTemperatureMenuItem = new JMenuItem("Set Temperature");
    JMenuItem tileSystemOptionsMenuItem = new JMenuItem("Options");

    //tool bar items
    ControlButton next = new ControlButton("forward");
    ControlButton prev = new ControlButton("backward");
    ControlButton play = new ControlButton("play");
    ControlButton stop = new ControlButton("stop");
    ControlButton fastf = new ControlButton("fast-forward");
    ControlButton fastb = new ControlButton("fast-backward");
    IconButton optionButton = new IconButton();

    // status panel
    JPanel statusPanel;
    JLabel statusLabel = new JLabel();
    String statusLabelPreviousText = "";

    boolean stopped;

    KeyListener keyListener = new KeyListener(){
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("key pressed");
            if(e.getKeyCode() == KeyEvent.VK_PAGE_UP)
            {
                window.zoomInDraw();
            }else if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
            {
                window.zoomOutDraw();
            }
            else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                window.step(1);
            }
            else if(e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                window.step(-1);
            }
            else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                window.exitFrontierMode();
                window.canvas.reset();
                window.frontier = window.assembly.calculateFrontier();
                window.placeFrontierOnGrid();
                window.drawGrid();
            }
            else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (window.currentFrontierAttachment != null) {
                    FrontierElement fe = new FrontierElement(window.currentFrontierAttachment.getLocation(),
                            window.currentFrontierAttachment.getOffset(),window.currentFrontierAttachment.getPolyTile(),4);
                    window.resetFrontier();
                    window.assembly.attach(fe);
                    window.frontier = window.assembly.calculateFrontier();
                    window.placeFrontierOnGrid();
                    window.exitFrontierMode();
                    window.drawGrid();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(next)) {
                window.step(1);
            }
            else if(e.getSource().equals(prev))
                window.step(-1);
            else if(e.getSource().equals(play)){
                Thread playThread = new Thread(){
                    @Override
                    public void run() {
                        stopped = false;
                        window.resetFrontier();
                        while (!window.frontier.isEmpty()){
                            if (stopped) {
                                // draw the entire grid when stopping, to see the frontier of the items.
                                window.drawGrid();
                                break;
                            }
                            window.play();
                        }
                    }
                };
                playThread.start();

            }else if(e.getSource().equals(stop))
                stopped = true;
            else if(e.getSource().equals(fastb))
                window.step(-2);
            else if(e.getSource().equals(fastf))
                window.step(2);
            else if (e.getSource().equals(newMenuItem)) {
                System.out.println("new assembly");
                // creates a new assembly frame when "new assembly button is clicked".
                Workspace w = new Workspace(new Assembly());
                //SimulationWindow tcf = new SimulationWindow(800, 600, new Assembly());
            } else if (e.getSource().equals(loadAssemblyMenuItem)) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(getParent());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                        Unmarshaller unmarshaller;
                        unmarshaller = jaxbContext.createUnmarshaller();
                        Assembly assembly = (Assembly) unmarshaller.unmarshal(selectedFile);
                        Workspace w = new Workspace(assembly);
                        //SimulationWindow tcf = new SimulationWindow(800, 600, assembly);
                    } catch (javax.xml.bind.JAXBException jaxbe) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Failed to load assembly");
                    }
                }
            } else if(e.getSource().equals(saveAsMenuItem)){
                //export
                System.out.println("export");
                window.removeFrontierFromGrid();

                try {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileFilter(new FileNameExtensionFilter(
                            "XML Document (*.xml)", "xml"));
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();

                        if(!file.getAbsolutePath().toLowerCase().endsWith(".xml")){
                            file = new File(file + ".xml");
                        }
                        JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                        Marshaller marshaller = jaxbContext.createMarshaller();
                        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        marshaller.marshal(window.assembly, file);
                        System.out.println("done");
                    }
                }catch(JAXBException jaxbe){
                    System.out.println(jaxbe.getMessage());
                    jaxbe.printStackTrace();
                }
                window.placeFrontierOnGrid();

            }
            else if (e.getSource().equals(closeMenuItem)){
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to exit the application?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION)
                    System.exit(0);
            } else if (e.getSource().equals(tileSetEditorMenuItem)){
                window.tileEditorWindow.setVisible(true);
            } else if (e.getSource().equals(setTemperatureMenuItem)){
                String temperatureString = JOptionPane.showInputDialog(null, "Set the system's temperature", "2");
                int temperature = Integer.parseInt(temperatureString);
                window.assembly.getTileSystem().setTemperature(temperature);
                window.resetFrontier();
                window.frontier.clear();
                window.canvas.reset();
                window.frontier = window.assembly.calculateFrontier();
                window.placeFrontierOnGrid();
                window.drawGrid();
                System.out.println("Change in temperature detected. Beware of errors.");
            }
        }
    };

    public GUI(SimulationWindow sim) {
        window = sim;
        addMenuBars();
        addToolBars();
        addStatusBar();
        //addActionListeners();
        System.out.println("window" + this.window);
        window.addKeyListener(keyListener);
        setVisible(true);
        System.out.println("adding menu bar");

    }
    private void addMenuBars(){
        JMenu fileMenu = new JMenu("File");
        newMenuItem.addActionListener(actionListener);
        loadAssemblyMenuItem.addActionListener(actionListener);
        saveAssemblyMenuItem.addActionListener(actionListener);
        saveAsMenuItem.addActionListener(actionListener);
        closeMenuItem.addActionListener(actionListener);
        fileMenu.add(newMenuItem);
        fileMenu.add(loadAssemblyMenuItem);
        fileMenu.add(saveAssemblyMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);

        JMenu toolsMenu = new JMenu("Tools");
        importTileSetMenuItem.addActionListener(actionListener);
        tileSetEditorMenuItem.addActionListener(actionListener);
        toolsMenu.add(importTileSetMenuItem);
        toolsMenu.add(tileSetEditorMenuItem);

        JMenu editMenu = new JMenu("Edit");
//        undoMenuItem.addActionListener(actionListener);
//        redoMenuItem.addActionListener(actionListener);
        setTemperatureMenuItem.addActionListener(actionListener);
//        editMenu.add(undoMenuItem);
//        editMenu.add(redoMenuItem);
        editMenu.add(setTemperatureMenuItem);

        JMenu tileSystemMenu = new JMenu("Tile System");
        seedCreatorMenuItem.addActionListener(actionListener);
        tileSystemOptionsMenuItem.addActionListener(actionListener);
        tileSystemMenu.add(seedCreatorMenuItem);
        tileSystemMenu.add(tileSystemOptionsMenuItem);

        this.add(fileMenu);
        this.add(editMenu);
        this.add(toolsMenu);
        this.add(tileSystemMenu);
    }

    private void addToolBars(){
        stepControlToolBar.add(fastb);
        stepControlToolBar.add(prev);
        stepControlToolBar.add(play);
        stepControlToolBar.add(stop);
        stepControlToolBar.add(next);
        stepControlToolBar.add(fastf);

        fastb.setPreferredSize(new Dimension(30, 25));
        prev.setPreferredSize(new Dimension(30, 25));
        play.setPreferredSize(new Dimension(30, 25));
        stop.setPreferredSize(new Dimension(30, 25));
        next.setPreferredSize(new Dimension(30, 25));
        fastf.setPreferredSize(new Dimension(30, 25));

        fastb.addActionListener(actionListener);
        prev.addActionListener(actionListener);
        play.addActionListener(actionListener);
        stop.addActionListener(actionListener);
        next.addActionListener(actionListener);
        fastf.addActionListener(actionListener);

        optionButton.setText(String.valueOf('\uf013'));
        stepControlToolBar.add(optionButton);

        stepControlToolBar.setBorder(new EtchedBorder());
        stepControlToolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    private void addStatusBar(){
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel.setText("");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }


}

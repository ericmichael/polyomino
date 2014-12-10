package com.asarg.polysim;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;


public class GUI extends JMenuBar {
    ActionListener actionListener;
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

    public GUI() {

        addMenuBars();
        addToolBars();
        addStatusBar();
//        addActionListeners();
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
        fastb.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(prev);
        prev.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(play);
        play.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(stop);
        play.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(next);
        next.setPreferredSize(new Dimension(30, 25));
        stepControlToolBar.add(fastf);
        fastf.setPreferredSize(new Dimension(30, 25));

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

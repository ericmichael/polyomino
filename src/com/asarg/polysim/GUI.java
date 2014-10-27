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

    JMenuItem undoMenuItem = new JMenuItem("Undo");
    JMenuItem redoMenuItem = new JMenuItem("Redo");

    JMenuItem seedCreatorMenuItem = new JMenuItem("Seed Creator");
    JMenuItem tileSystemOptionsMenuItem = new JMenuItem("Options");

    //tool bar items
    ControlButton next = new ControlButton("forward");
    ControlButton prev = new ControlButton("backward");
    ControlButton play = new ControlButton("play");
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

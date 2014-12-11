package com.asarg.polysim;

/**
 * Created by ericmartinez on 12/11/14.
 */

public class Workspace{
    SimulationWindow window;
    TileEditorWindow editorWindow;

    Assembly assembly;

    public Workspace(Assembly assembly){
        this.assembly = assembly;
        createWindow();
    }

    public SimulationWindow createWindow(){
        window = new SimulationWindow(800, 600, this);
        assembly.addObserver(window);
        return window;
    }

    public TileEditorWindow createEditorWindow(){
        editorWindow = new TileEditorWindow(800, 600, this);
        assembly.addObserver(editorWindow);
        return editorWindow;
    }
}

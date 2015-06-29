package com.asarg.polysim.models.TwoHAM;

import com.asarg.polysim.models.base.*;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ericmartinez on 6/15/15.
 */
public class TwoHAMAssembly {
    private TileSystem tileSystem;
    ArrayList<PolyTile> terminalSet = new ArrayList<PolyTile>();
    ArrayList<PolyTile> expanded = new ArrayList<PolyTile>();
    ArrayList<Pair<PolyTile, PolyTile>> processed = new ArrayList<Pair<PolyTile, PolyTile>>();
    ArrayList<ArrayList<PolyTile>> attached = new ArrayList<ArrayList<PolyTile>>();

    public TwoHAMAssembly() {
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2, 0);
    }

    public TwoHAMAssembly(TileSystem ts) {
        tileSystem = new TileSystem(ts.getTemperature());
        try {
            tileSystem.setWeightOption(ts.getWeightOption());
        }catch(InvalidObjectException ioe){}

        tileSystem.setGlueFunction(ts.getGlueFunction());
        tileSystem.getTileTypes().addAll(ts.getTileTypes());
    }

    private ArrayList<PolyTile> getNextAssembliesAsPolyTiles(Assembly asm){
        ArrayList<PolyTile> nextAssemblies = new ArrayList<PolyTile>();
        for (FrontierElement fe : asm.getFrontier()) {
            asm.placePolytile(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());
            PolyTile asmPt = asm.toPolyTile();
            if(!nextAssemblies.contains(asmPt))
                nextAssemblies.add(asmPt);
            asm.removePolytile(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());
        }
        return nextAssemblies;
    }

    private ArrayList<Assembly> getNextAssemblies(Assembly asm){
        ArrayList<Assembly> nextAssemblies = new ArrayList<Assembly>();

        for(PolyTile pt : getNextAssembliesAsPolyTiles(asm)){
            nextAssemblies.add(polyTileToAssembly(pt));
        }
        return nextAssemblies;
    }

    public TileSystem getTileSystem(){
        return tileSystem;
    }

    protected Assembly polyTileToAssembly(PolyTile pt){
        Assembly clonedAssembly = new Assembly(tileSystem);
        clonedAssembly.placeSeed(pt);
        clonedAssembly.cleanUp();
        clonedAssembly.getOpenGlues();
        clonedAssembly.calculateFrontier();
        clonedAssembly.printDebugInformation();
        return clonedAssembly;
    }

    protected boolean isTerminalAssembly(Assembly asm){
        //asm.changeTileSystem(tileSystem);
        return asm.calculateFrontier().isEmpty();
    }

    boolean isDone(){
        //everyone who is not expanded is terminal
        for(PolyTile pt : tileSystem.getTileTypes()){
            if(!expanded.contains(pt) && !terminalSet.contains(pt)) {
                System.out.println("Not done");
                return false;
            }
        }

        System.out.println("Tile types size: "+ tileSystem.getTileTypes().size());
        System.out.println("Terminal types size: "+ terminalSet.size());

        System.out.println("Done");
        System.out.println("");
        return true;
    }

    public ArrayList<PolyTile> getTerminalSet(){
        System.out.println("terminal set requested");
        while(!isDone()){
            forward();
        }
        return terminalSet;
    }

    boolean addPerimeters(){
        ArrayList<PolyTile> toAdd = new ArrayList<PolyTile>();
        for(PolyTile pt: tileSystem.getTileTypes()) {
            if(!expanded.contains(pt)) {
                Assembly subassembly = polyTileToAssembly(pt);

                for (PolyTile subasm : getNextAssembliesAsPolyTiles(subassembly)) {
                    if (!tileSystem.getTileTypes().contains(subasm)) {
                        if (!toAdd.contains(subasm))
                            toAdd.add(subasm);
                    }
                }
                expanded.add(pt);
            }
        }

        tileSystem.getTileTypes().addAll(toAdd);
        attached.add(toAdd);

        return toAdd.size()!=0;
    }

    void forward(){
        System.out.println("in forward pass");
        System.out.println("Expanded: " + expanded.size());
        System.out.println("Terminal: " + terminalSet.size());
        System.out.println("Tile Set: " + tileSystem.getTileTypes().size());

        //loop through all tiles
        //attempt to expand any that i didn't have before, flag
        boolean anyExpanded = addPerimeters();

        if(!anyExpanded){
            for(PolyTile pt: tileSystem.getTileTypes()){
                if(!terminalSet.contains(pt)){
                    if(isTerminalAssembly(polyTileToAssembly(pt))){
                        terminalSet.add(pt);
                    }
                }
            }
        }
    }

    void backward(){
        if(!attached.isEmpty()) {
            ArrayList<PolyTile> lastRound = attached.get(attached.size()-1);
            for(PolyTile pt : lastRound){
                tileSystem.getTileTypes().remove(pt);
                expanded.remove(pt);
                terminalSet.remove(pt);
            }
            attached.remove(attached.size()-1);
        }
    }
}

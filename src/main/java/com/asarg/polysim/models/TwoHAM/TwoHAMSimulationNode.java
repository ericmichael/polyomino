package com.asarg.polysim.models.TwoHAM;

import com.asarg.polysim.adapters.graphics.raster.SimulationCanvas;
import com.asarg.polysim.models.base.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by ericmartinez on 6/15/15.
 */
public class TwoHAMSimulationNode extends SimulationNode {

    public TwoHAMSimulationNode(Assembly asm, SimulationCanvas tc, File file) {
        super(asm, tc, file);
        System.out.println("TWOHAM MODE");
    }

    private Assembly polyTileToAssembly(PolyTile pt){
        Assembly clonedAssembly = new Assembly(assembly.getTileSystem());
        clonedAssembly.placeSeed(pt);
        return clonedAssembly;
    }

    public void forward() {
        //removeFrontierFromGrid();
        ArrayList<PolyTile> subassemblies = new ArrayList<PolyTile>();
        for(PolyTile pt: assembly.getTileSystem().getTileTypes()){
            Assembly subassembly = polyTileToAssembly(pt);
            subassembly.getOpenGlues();
            Frontier frontier = subassembly.calculateFrontier();
            System.out.println("Frontier Size: " + frontier.size());

            for(FrontierElement fe : frontier){
                subassembly.placePolytile(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y);
                subassemblies.add(subassembly.toPolyTile());
                subassembly.removePolytile(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y);
            }
        }

        ArrayList<PolyTile> subassemblies_no_dups = new ArrayList<PolyTile>();
        Iterator iterator = subassemblies.iterator();

        while (iterator.hasNext())
        {
            PolyTile o = (PolyTile) iterator.next();
            if(!subassemblies_no_dups.contains(o)) {
                subassemblies_no_dups.add(o);
            }
        }

        for(PolyTile pt: subassemblies_no_dups){
            pt.normalize();
            assembly.getTileSystem().getTileTypes().add(pt);
        }
//        step(1, true);
//        java.util.List<FrontierElement> attached = assembly.getAttached();
//        if (!attached.isEmpty()) {
//            updateAttachTime(attached.get(attached.size() - 1).getAttachTime());
//        }
    }
//
//    public void fast_forward() {
//        step(2, true);
//        java.util.List<FrontierElement> attached = assembly.getAttached();
//        if (!assembly.getAttached().isEmpty()) {
//            updateAttachTime(attached.get(attached.size() - 1).getAttachTime());
//        }
//    }
//
//    public void backward() {
//        step(-1, true);
//    }
//
//    public void fast_backward() {
//        step(-2, true);
//    }
//
//    public void clear_seed(){
//        assembly.clearSeed();
//    }
//
//    public void play() {
//        stopped = false;
//        boolean anyAttached = false;
//        resetFrontier();
//        while (!frontier.isEmpty()) {
//            if (stopped) {
//                // draw the entire grid when stopping, to see the frontier of the items.
//                placeFrontierOnGrid();
//                drawGrid();
//                break;
//            } else {
//                anyAttached = true;
//                playHelper();
//            }
//        }
//        if (anyAttached) {
//            updateAttachTime(assembly.getAttached().get(-1).getAttachTime());
//        }
//        //return null;
//    }
//
//    private void playHelper() {
//        step(1, false);
//        //get the latest attached frontier element
//        paintPolytile(assembly.getAttached().get(assembly.getAttached().size() - 1));
//    }
}

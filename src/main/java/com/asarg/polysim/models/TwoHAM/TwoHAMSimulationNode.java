package com.asarg.polysim.models.TwoHAM;

import com.asarg.polysim.adapters.graphics.raster.SimulationCanvas;
import com.asarg.polysim.models.base.*;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by ericmartinez on 6/15/15.
 */
public class TwoHAMSimulationNode extends SimulationNode {
    private final TwoHAMAssembly twoHAMAssembly;

    public TwoHAMSimulationNode(TwoHAMAssembly asm, File file) {
        super(new Assembly(), file);

        twoHAMAssembly = new TwoHAMAssembly();
        assembly.changeTileSystem(twoHAMAssembly.getTileSystem());
        System.out.println("TWOHAM MODE");
    }

    private Assembly polyTileToAssembly(PolyTile pt) {
        Assembly clonedAssembly = new Assembly(assembly.getTileSystem());
        clonedAssembly.placeSeed(pt);
        return clonedAssembly;
    }

    public boolean exportTerminalTileSet(File file) {
        try {
            TileConfiguration tc = new TileConfiguration();

            for (PolyTile pt : twoHAMAssembly.getTerminalSet())
                tc.addTileType(pt);

            tc.getGlueFunction().putAll(twoHAMAssembly.getTileSystem().getGlueFunction());
            JAXBContext jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tc, file);
            return true;
        } catch (JAXBException jxb) {
            jxb.printStackTrace();
        }
        return false;
    }

    public TileSystem getTileSystem() {
        return twoHAMAssembly.getTileSystem();
    }

    @Override
    public void forward() {
        twoHAMAssembly.forward();
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
    public void backward() {
        twoHAMAssembly.backward();
    }

    //
    @Override
    public ObservableList<PolyTile> getTileSet() {
        return twoHAMAssembly.getTileSystem().getTileTypes();
    }

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

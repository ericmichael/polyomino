package com.asarg.polysim.examples;

import com.asarg.polysim.Assembly;
import com.asarg.polysim.TileSystem;
import com.asarg.polysim.Workspace;
import com.asarg.polysim.models.datam.daTAMTile;
/**
 * Created by ericmartinez on 8/20/14.
 */
public class RNGDominoSimulation {
    private TileSystem ts;
    private Assembly assembly;

    public static daTAMTile dL(){
        daTAMTile d = new daTAMTile("L", true);
        String glues[] = {null, "c", "s", null, null, null};
        d.setGlues(glues);
        return d;
    }

    public static daTAMTile dR(){
        daTAMTile d = new daTAMTile("R", true);
        String glues[] = {"c", "c", null, null, null, "s"};
        d.setGlues(glues);
        return d;
    }

    public static daTAMTile dCoin(){
        daTAMTile d = new daTAMTile("C", true);
        String glues[] = {null, null, null, "c", "c", null};
        d.setGlues(glues);
        return d;
    }

    public RNGDominoSimulation(int temperature){
        ts = new TileSystem(temperature);
        ts.addPolyTile(dR());
        ts.addPolyTile(dCoin());

        ts.addGlueFunction("c", "c", 1);
        ts.addGlueFunction("s", "s", 2);
        assembly = new Assembly(ts);
        assembly.placeSeed(dL());
    }

    public static void main(String args[]){
        RNGDominoSimulation dsim = new RNGDominoSimulation(2);
        Workspace w = new Workspace(dsim.assembly);
        //SimulationWindow tcf = new SimulationWindow(800,600, dsim.assembly);
    }
}

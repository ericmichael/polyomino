package com.asarg.polysim.examples;

import com.asarg.polysim.Assembly;
import com.asarg.polysim.Workspace;
import com.asarg.polysim.models.hextam.hexTAMTile;
import com.asarg.polysim.models.hextam.hexTileSystem;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class RNGHexSimulation {
    private hexTileSystem ts;
    private Assembly assembly;

    public RNGHexSimulation(int temperature) {
        ts = new hexTileSystem(temperature);
        ts.addPolyTile(choiceTile());
        ts.addPolyTile(seedTile());

        ts.addGlueFunction("a", "a", 2);
        ts.addGlueFunction("-1", "-1", -1);

        assembly = new Assembly(ts);
        assembly.placeSeed(seedTile());
    }

    public static hexTAMTile tile() {
        hexTAMTile d = new hexTAMTile("a");
        String glues[] = {"a", "a", "a", "a", "a", "a"};
        d.setGlues(glues);
        return d;
    }

    public static hexTAMTile choiceTile() {
        hexTAMTile d = new hexTAMTile("choice tile");
        String glues[] = {null, "-1", "a", "a", "-1", null};
        d.setGlues(glues);
        return d;
    }

    public static hexTAMTile seedTile() {
        hexTAMTile d = new hexTAMTile("seed tile");
        String glues[] = {"a", "-1", null, null, "-1", "a"};
        d.setGlues(glues);
        return d;
    }

    public static void main(String args[]) {
        RNGHexSimulation hsim = new RNGHexSimulation(2);
        Workspace w = new Workspace(hsim.assembly);
        //SimulationWindow tcf = new SimulationWindow(800,600, hsim.assembly);
    }
}

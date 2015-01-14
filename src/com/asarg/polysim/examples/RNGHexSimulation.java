package com.asarg.polysim.examples;

import com.asarg.polysim.Assembly;
import com.asarg.polysim.SimulationApplication;
import com.asarg.polysim.models.hextam.hexTAMTile;
import com.asarg.polysim.models.hextam.hexTileSystem;
import javafx.application.Application;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class RNGHexSimulation extends SimulationApplication {
    private hexTileSystem ts;

    public RNGHexSimulation() {
        ts = new hexTileSystem(2);
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
        Application.launch(RNGHexSimulation.class, (java.lang.String[]) null);
    }
}

package com.asarg.polysim.examples;

import com.asarg.polysim.SimulationApplication;
import com.asarg.polysim.models.base.Assembly;
import com.asarg.polysim.models.base.TileSystem;
import com.asarg.polysim.models.datam.daTAMTile;
import javafx.application.Application;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class RNGDominoSimulation extends SimulationApplication {

    public RNGDominoSimulation() {
        super();
        TileSystem ts = new TileSystem(2);
        ts.addPolyTile(dR());
        ts.addPolyTile(dCoin());

        ts.addGlueFunction("c", "c", 1);
        ts.addGlueFunction("s", "s", 2);
        assembly = new Assembly(ts);
        assembly.placeSeed(dL());
    }

    public static daTAMTile dL() {
        daTAMTile d = new daTAMTile("L", true);
        String glues[] = {null, "c", "s", null, null, null};
        d.setGlues(glues);
        return d;
    }

    public static daTAMTile dR() {
        daTAMTile d = new daTAMTile("R", true);
        String glues[] = {"c", "c", null, null, null, "s"};
        d.setGlues(glues);
        return d;
    }

    public static daTAMTile dCoin() {
        daTAMTile d = new daTAMTile("C", true);
        String glues[] = {null, null, null, "c", "c", null};
        d.setGlues(glues);
        return d;
    }

    public static void main(String args[]) {
        Application.launch(RNGDominoSimulation.class, (java.lang.String[]) null);
    }
}

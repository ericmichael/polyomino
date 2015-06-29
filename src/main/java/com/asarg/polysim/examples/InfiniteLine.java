package com.asarg.polysim.examples;

/**
 * Created by Mario on 8/22/2014.
 */

import com.asarg.polysim.models.base.Assembly;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.SimulationApplication;
import com.asarg.polysim.models.base.TileSystem;
import com.asarg.polysim.models.atam.ATAMTile;
import javafx.application.Application;

public class InfiniteLine extends SimulationApplication {

    public InfiniteLine() {
        super();
        TileSystem ts = new TileSystem(2);

        ts.addGlueFunction("a", "a", 2);
        assembly = new Assembly(ts);
        PolyTile p = new PolyTile("thing");
        String[] g = {"a", "a", "a", "a"};
        String[] g2 = {null, "a", null, "a"};
        p.addTile(0, 0, g2);
        ts.addPolyTile(p);
        assembly.placeSeed(dL());

    }

    public static PolyTile dL() {
        ATAMTile d = new ATAMTile("L");
        String glues[] = {"a", "a", "a", "a"};
        String glues2[] = {null, "a", null, null};
        d.setGlues(glues2);
        return d;
    }

    public static void main(String args[]) {
        Application.launch(InfiniteLine.class, (java.lang.String[]) null);
    }
}

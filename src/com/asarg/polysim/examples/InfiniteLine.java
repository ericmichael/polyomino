package com.asarg.polysim.examples;

/**
 * Created by Mario on 8/22/2014.
 */

import com.asarg.polysim.Assembly;
import com.asarg.polysim.PolyTile;
import com.asarg.polysim.TileSystem;
import com.asarg.polysim.Workspace;
import com.asarg.polysim.models.atam.ATAMTile;

public class InfiniteLine {
    private TileSystem ts;
    private Assembly assembly;

    public static PolyTile dL(){
        ATAMTile d = new ATAMTile("L");
        String glues[] = {"a", "a", "a", "a"};
        String glues2[] = {null, "a",null, null};
        d.setGlues(glues2);
        return d;
    }

    public InfiniteLine(int temperature){
        ts = new TileSystem(temperature);

        ts.addGlueFunction("a", "a", 2);
        assembly = new Assembly(ts);
        PolyTile p = new PolyTile("thing");
        String[] g = {"a","a","a","a"};
        String[] g2 = {null ,"a", null, "a"};
        p.addTile(0,0, g2);
        ts.addPolyTile(p);
        assembly.placeSeed(dL());

    }

    public static void main(String args[]){
        InfiniteLine asim = new InfiniteLine(2);
        Workspace w = new Workspace(asim.assembly);
        //SimulationWindow tcf = new SimulationWindow(800,600, asim.assembly);
    }
}

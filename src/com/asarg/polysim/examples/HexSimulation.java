package com.asarg.polysim.examples;

import com.asarg.polysim.Assembly;
import com.asarg.polysim.TestCanvasFrame;
import com.asarg.polysim.models.hextam.hexTileSystem;
import com.asarg.polysim.models.hextam.hexTAMTile;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class HexSimulation {
    private hexTileSystem ts;
    private Assembly assembly;

    public static hexTAMTile tile(){
        hexTAMTile d = new hexTAMTile("a");
        String glues[] = {"a", "a", "a", "a", "a", "a"};
        d.setGlues(glues);
        return d;
    }

    public HexSimulation(int temperature){
        ts = new hexTileSystem(temperature);
        ts.addPolyTile(tile());

        ts.addGlueFunction("a", "a", 2);

        assembly = new Assembly(ts);
        assembly.placeSeed(tile());
    }

    public static void main(String args[]){
        HexSimulation hsim = new HexSimulation(2);
        TestCanvasFrame tcf = new TestCanvasFrame(800,600, hsim.assembly);
    }
}

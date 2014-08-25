package com.asarg.polysim.examples;

/**
 * Created by Mario on 8/22/2014.
 */
import com.asarg.polysim.*;
import com.asarg.polysim.models.atam.*;
import com.asarg.polysim.TestCanvasFrame;
import com.asarg.polysim.TileSystem;

public class InfiniteLine {
    private TileSystem ts;
    private Assembly assembly;

    public static ATAMTile dL(){
        ATAMTile d = new ATAMTile("L");
        String glues[] = {"a", "a", "a", "a"};
        d.setGlues(glues);
        return d;
    }

    public InfiniteLine(int temperature){
        ts = new TileSystem(temperature);

        ts.addGlueFunction("a", "a", 2);
        assembly = new Assembly(ts);
        PolyTile p = new PolyTile();
        String[] g = new String[]{"a","a","a","a"};
        p.addTile(0,0, g);
        ts.addPolyTile(p);
        assembly.placeSeed(dL());

    }

    public static void main(String args[]){
        InfiniteLine asim = new InfiniteLine(2);
        TestCanvasFrame tcf = new TestCanvasFrame(800,600, asim.assembly);
    }
}

package com.asarg.polysim.models.hextam;

import com.asarg.polysim.TileSystem;

/**
 * Created by ericmartinez on 8/20/14.
 */
public class hexTileSystem extends TileSystem{
    public hexTileSystem() { super(); }

    public hexTileSystem(int temp){ super(temp); }

    public hexTileSystem(int temp, int wO){ super(temp, wO); }

    public void addGlueFunction(String label1, String label2, int temp) {
        super.addGlueFunction(label1, label2, temp);
        super.addGlueFunction(label1+'\u0000', label2+'\u0000', temp);
    }
}

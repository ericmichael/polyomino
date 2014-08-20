package com.asarg.polysim.models.atam;

import com.asarg.polysim.PolyTile;
import com.asarg.polysim.Tile;

/**
 * Created by ericmartinez on 8/19/14.
 */
public class ATAMTile extends PolyTile {

    public ATAMTile() {
        System.out.println("atam tile with no name and infinite counts");
    }
    public ATAMTile(String n) {
        super(n);
        setColor("FFFFF");
    }
    public ATAMTile(String n, double conc){
        super(n, conc);
        setColor("FFFFF");
    }
    public ATAMTile(String n, int c){
        super(n, c);
        setColor("FFFFF");
    }
    // add tile, increases the size the polytile by creating a tile with the given data
    public void addTile(String[] gl) {
        if(getTiles().size()==0) {
            super.addTile(0,0, gl);
        }
    }

    // returns the tile at specified location.
    public Tile getTile() {
        if(!getTiles().isEmpty()) {
            return super.getTile(0,0);
        }else return null;
    }

}

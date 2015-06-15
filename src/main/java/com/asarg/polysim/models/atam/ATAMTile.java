package com.asarg.polysim.models.atam;

import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;

/**
 * Created by ericmartinez on 8/19/14.
 */
public class ATAMTile extends PolyTile {

    public ATAMTile() {
        System.out.println("atam tile with no name and infinite counts");
    }

    public ATAMTile(String n) {
        super(n);
        setGlues(null, null, null, null);
    }

    public ATAMTile(String n, double conc) {
        super(n, conc);
        setGlues(null, null, null, null);
    }

    public ATAMTile(String n, int c) {
        super(n, c);
        setGlues(null, null, null, null);
    }

    public ATAMTile(String name, String n, String e, String s, String w, double conc) {
        super(n, conc);
        setGlues(n, e, s, w);
    }

    public ATAMTile(String name, String n, String e, String s, String w, int c) {
        super(n, c);
        setGlues(n, e, s, w);
    }


    public void setGlues(String n, String e, String s, String w) {
        String glues[] = {n, e, s, w};
        if (getTiles().isEmpty()) {
            addTile(glues);
        } else {
            super.removeTile(0, 0);
            addTile(glues);
        }
    }

    public void setGlues(String[] g) {
        if (getTiles().isEmpty()) {
            addTile(g);
        } else {
            super.removeTile(0, 0);
            addTile(g);
        }
    }

    // add tile, increases the size the polytile by creating a tile with the given data
    private void addTile(String[] gl) {
        if (getTiles().size() == 0) {
            super.addTile(0, 0, gl);
        }
    }

    // returns the tile at specified location.
    public Tile getTile() {
        if (!getTiles().isEmpty()) {
            return super.getTile(0, 0);
        } else return null;
    }

}

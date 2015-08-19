package com.asarg.polysim.models.datam;

import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by ericmartinez on 8/19/14.
 */
public class daTAMTile extends PolyTile {
    private final Point left = new Point(0, 0);
    Point top = left;
    Point right = new Point(1, 0);
    Point bottom = new Point(0, -1);
    private boolean horizontal;

    public daTAMTile() {
        System.out.println("datam tile with no name and infinite counts");
    }

    public daTAMTile(String n, boolean horizontal) {
        super(n);
        this.horizontal = horizontal;
        String glues[] = {null, null, null, null, null, null};
        setGlues(glues);
    }

    public daTAMTile(String n, boolean horizontal, double conc) {
        super(n, conc);
        String glues[] = {null, null, null, null, null, null};
        setGlues(glues);
    }

    public daTAMTile(String n, boolean horizontal, int c) {
        super(n, c);
        String glues[] = {null, null, null, null, null, null};
        setGlues(glues);
    }


    public void setGlues(String[] gl) {
        if (gl.length == 6) {
            String t1[] = new String[4];
            String t2[] = new String[4];

            if (horizontal) {
                t1[0] = gl[0];
                t1[1] = null;
                t1[2] = gl[4];
                t1[3] = gl[5];

                t2[0] = gl[1];
                t2[1] = gl[2];
                t2[2] = gl[3];
                t2[3] = null;
            } else {
                t1[0] = gl[0];
                t1[1] = gl[1];
                t1[2] = null;
                t1[3] = gl[5];

                t2[0] = null;
                t2[1] = gl[2];
                t2[2] = gl[3];
                t2[3] = gl[4];
            }
            removeTiles();
            addTile(t1);
            addTile(t2);
        }
    }

    // deletes tile at the specified location
    private void removeTiles() {
        Iterator<Tile> t = tiles.iterator();
        while (t.hasNext()) {
            t.next();
            t.remove();
        }
    }

    // add tile, increases the size the polytile by creating a tile with the given data
    private void addTile(String[] gl) {
        int size = getTiles().size();
        if (size == 0) {
            super.addTile(0, 0, gl);
        } else if (size == 1) {
            if (horizontal) {
                super.addTile(1, 0, gl);
            } else {
                super.addTile(0, -1, gl);
            }
        }
    }

    // returns the tile at specified location.
    public Tile getTile(int x, int y) {
        if (!getTiles().isEmpty()) {
            return super.getTile(x, y);
        } else return null;
    }

}

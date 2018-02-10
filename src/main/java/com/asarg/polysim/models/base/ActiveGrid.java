package com.asarg.polysim.models.base;

import com.asarg.polysim.adapters.graphics.raster.SimulationCanvas;

import java.util.HashMap;

/**
 * Created by Eric Martinez on 6/2/2016.
 */
public class ActiveGrid extends HashMap<Coordinate, Tile> {

    SimulationCanvas canvas;
    private Coordinate selected;

    public ActiveGrid() {
        canvas = null;
    }

    public ActiveGrid(SimulationCanvas canvas) {
        this.canvas = canvas;
    }

    public ActiveGrid(SimulationCanvas canvas, PolyTile pt) {
        this();
        this.placePolytile(pt, 0, 0);
    }


    //Check if a PolyTile could fit without overlaps when translated by x, y
    public boolean geometryCheckSuccess(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            if (this.containsKey(new Coordinate(x + t.getLocation().getX(), y + t.getLocation().getY()))) {
                return false;
            }
        }
        return true;
    }

    //Translate a PolyTile by (x, y) and place it
    public void placePolytile(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            Coordinate tmp = new Coordinate(t.getLocation());
            tmp = tmp.translate(x, y);
            this.put(tmp, t);
        }

    }

    //If PolyTile p when translated by (x,y) matches the PolyTile at the destination, delete it from Grid
    public void removePolytile(PolyTile p, int x, int y) {
        boolean polytilePresent = true;
        for (Tile t : p.tiles) {
            Coordinate tmp = new Coordinate(t.getLocation());
            tmp = tmp.translate(x, y);
            Tile existing = this.get(tmp);

            if (existing == null) polytilePresent = false;
        }
        if (polytilePresent) {
            for (Tile t : p.tiles) {
                Coordinate tmp = new Coordinate(t.getLocation());
                tmp = tmp.translate(x, y);
                this.remove(tmp);
            }
        }
    }

    //Select a PolyTile at the Coordinate c
    public void select(Coordinate c) {
        this.selected = c;
        draw();
    }

    //Deselect any selection
    public void deselect() {
        this.selected = null;
        draw();
    }

    public Coordinate getSelection() {
        return selected;
    }

    public SimulationCanvas getCanvas() {
        return this.canvas;
    }

    //Where should things be drawn
    public void setCanvas(SimulationCanvas canvas) {
        this.canvas = canvas;
    }

    /*
    Override HashMap stuff to allow update drawing
     */
    public Tile put(Coordinate key, Tile value) {
        Tile return_value = super.put(key, value);
        draw();
        return return_value;
    }

    public Tile remove(Object key) {
        Tile return_value = super.remove(key);
        draw();
        return return_value;
    }

    public void clear() {
        super.clear();
        draw();
    }

    public void draw() {
        if (canvas != null) {
            canvas.draw();
        }
    }


}

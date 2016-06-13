package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.models.base.ActiveGrid;
import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.*;

/**
 * Created by ericmartinez on 1/12/15.
 */
public class EditorCanvas extends SimulationCanvas {

    private PolyTile pt;
    private Coordinate lastMouseXY = new Coordinate(800, 600);
    private final SimpleObjectProperty<Tile> selectedTile = new SimpleObjectProperty<Tile>(null);

    public EditorCanvas() {
        super(new ActiveGrid());
        grid.setCanvas(this);

        int width = (int) getWidth();
        int height = (int) getHeight();
        center = new Coordinate(width / 2, height / 2);
        cg2d.setClip(0, 0, width, height);

        setOnMousePressed(e -> {
            Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
            lastMouseXY = point;
        });
    }

    public void handleClick(Coordinate gridPoint){
        if (pt != null) {
            Tile tile = grid.get(gridPoint);
            if (tile != null) {
                grid.select(gridPoint);
                selectedTile.set(tile);
                drawGrid();
            } else if (pt.adjacentExits(gridPoint)) {
                Tile newTile = new Tile();
                newTile.setTileLocation(gridPoint.getX(), gridPoint.getY());

                pt.addTile(newTile);
                setPolyTile(pt);
                grid.select(gridPoint);
                selectedTile.set(tile);
                drawGrid();
            }
        }
    }

    public void drawGrid() {
        Drawer.TileDrawer.drawTiles(this);
        Tile selectedTile = getSelectedTile();
        if (selectedTile != null) {
            Drawer.TileDrawer.drawTileSelection(cg2d, selectedTile.getLocation(), tileDiameter, center, Color.decode("#007AFF"));
        }
    }

    public PolyTile getPolyTile() {
        return pt;
    }

    public void setPolyTile(PolyTile pt) {
        clear();
        if (pt != null) {
            grid.placePolytile(pt, 0,0);
        }
        this.pt = pt;
    }

    public void clear(){
        grid.deselect();
        this.pt=null;
        grid.clear();
        selectedTile.set(null);
    }

    public Tile getSelectedTile(){
        return selectedTile.get();
    }

    public SimpleObjectProperty getSelectedTileProperty(){
        return selectedTile;
    }
}

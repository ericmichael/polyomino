package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.models.base.ActiveGrid;
import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.FrontierElement;
import com.asarg.polysim.models.base.Tile;
import javafx.scene.canvas.Canvas;
import org.jfree.fx.FXGraphics2D;

public abstract class SimulationCanvas extends Canvas {


    protected FXGraphics2D cg2d;
    protected int tileDiameter = 50;
    protected Coordinate center;
    protected ActiveGrid grid;

    public SimulationCanvas(ActiveGrid grid) {
        super();
        this.cg2d = new FXGraphics2D(getGraphicsContext2D());
        System.out.println("Should have createdgraphics object");

        this.grid = grid;
        // Redraw canvas when size changes.
        widthProperty().addListener(e -> recenterAndDraw());
        heightProperty().addListener(e -> recenterAndDraw());

        int width = (int) getWidth();
        int height = (int) getHeight();
        center = new Coordinate(width / 2, height / 2);
        cg2d.setClip(0, 0, width, height);
    }

    public void draw() {
        int width = (int) getWidth();
        int height = (int) getHeight();
        getGraphicsContext2D().clearRect(0, 0, width, height);
        cg2d.setClip(0, 0, width, height);
        drawGrid();
    }

    public void recenterAndDraw() {
        int width = (int) getWidth();
        int height = (int) getHeight();
        center = new Coordinate(width / 2, height / 2);
        draw();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public void reset() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        draw();
    }


    public void drawGrid() {
        Drawer.TileDrawer.drawTiles(this);
        if (grid.getSelection() != null) {
            Tile selectedTile = grid.get(grid.getSelection());
            if (selectedTile != null) {
                Coordinate offset = new Coordinate(grid.getSelection().getX() - selectedTile.getLocation().getX(), grid.getSelection().getY() - selectedTile.getLocation().getY());
                Drawer.TileDrawer.drawPolyTileSelection(cg2d, selectedTile.getParent(), tileDiameter, offset, center);
            }
        }
    }

    // draws the given polytile onto the loaded graphics object (alternative to drawGrid which draws the
    //entire grid)
    public void drawTileOnGrid(FrontierElement attached) {
        Drawer.TileDrawer.drawNewPolyTile(cg2d, attached.getPolyTile().getTiles(), tileDiameter, attached.getOffset(), center);
    }

    public int getTileDiameter() {
        return tileDiameter;
    }

    public void setTileDiameter(int td) {
        tileDiameter = td;
        reset();
    }

    public void translateOffset(int x, int y) {

        center = center.translate(x, y);
        reset();
    }

    public final Coordinate getOffset() {
        return center;
    }

    public FXGraphics2D getGraphics() {
        return cg2d;
    }
}

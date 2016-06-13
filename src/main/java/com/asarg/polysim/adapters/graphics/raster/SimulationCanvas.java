package com.asarg.polysim.adapters.graphics.raster;

import com.asarg.polysim.models.base.ActiveGrid;
import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.FrontierElement;
import com.asarg.polysim.models.base.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import org.jfree.fx.FXGraphics2D;

public abstract class SimulationCanvas extends Canvas {


    public Coordinate selected;
    protected FXGraphics2D cg2d;
    protected int tileDiameter = 50;
    protected Coordinate center;
    protected ActiveGrid grid;
    protected Coordinate lastMouseXY = new Coordinate(800, 600);
    protected int dragCount = 0;


    public SimulationCanvas(ActiveGrid grid) {
        super();
        this.cg2d = new FXGraphics2D(getGraphicsContext2D());

        this.grid = grid;
        // Redraw canvas when size changes.
        widthProperty().addListener(e -> recenterAndDraw());
        heightProperty().addListener(e -> recenterAndDraw());

        int width = (int) getWidth();
        int height = (int) getHeight();
        center = new Coordinate(width / 2, height / 2);
        cg2d.setClip(0, 0, width, height);

        setOnMousePressed(e -> {
            Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
            lastMouseXY = point;
        });

        setOnMouseReleased(e -> dragCount = 0);

        setOnMouseClicked(e -> {
            Coordinate point = new Coordinate((int) e.getX(), (int) e.getY());
            Coordinate clicked = Drawer.TileDrawer.getGridPoint(point, getOffset(), getTileDiameter());
            handleClick(clicked);
        });

        setOnScroll(e -> {
            handleScroll(e);
        });
    }

    public abstract void handleClick(Coordinate clicked);

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

    public void zoomInDraw() {
        int tileDiameter = getTileDiameter();
        if (tileDiameter < getWidth()) {
            setTileDiameter((int) (tileDiameter * 1.5));
        } else return;

    }

    public void zoomOutDraw() {
        int tileDiameter = getTileDiameter();
        if (tileDiameter > 2) {
            setTileDiameter((int) (tileDiameter * .75));
        } else return;
    }

    public void handleScroll(ScrollEvent e) {
        boolean rotation = e.getDeltaY() > 0.0;
        if (rotation) {
            zoomInDraw();
        } else {
            zoomOutDraw();
        }
    }

}

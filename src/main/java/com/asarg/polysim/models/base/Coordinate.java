package com.asarg.polysim.models.base;

import javafx.util.Pair;

/**
 * Created by ericmartinez on 6/23/15.
 */
public final class Coordinate {

    private final int x;
    private final int y;

    public Coordinate(Coordinate c) {
        this.x = c.getX();
        this.y = c.getY();
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Coordinate getNorth(){ return new Coordinate(this.x, this.y+1);}

    public Coordinate getEast(){ return new Coordinate(this.x+1, this.y);}

    public Coordinate getSouth(){ return new Coordinate(this.x, this.y-1);}

    public Coordinate getWest(){ return new Coordinate(this.x-1, this.y);}

    /*
    TODO: Don't remember what this does
     */
    public static Pair<Coordinate, Coordinate> getOffset(Coordinate aPoint, Coordinate ptPoint, int offsetX, int offsetY) {
        Coordinate placement = new Coordinate(offsetX, offsetY);
        placement = placement.translate(aPoint.getX(), aPoint.getY());
        int xOffset = -(ptPoint.getX() - placement.getX());
        int yOffset = -(ptPoint.getY() - placement.getY());
        Coordinate tmp2 = new Coordinate(xOffset, yOffset);
        return new Pair<Coordinate, Coordinate>(placement, tmp2);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Coordinate)) {
            return false;
        }
        Coordinate point = (Coordinate) other;
        return this.x == point.x && this.y == point.y;
    }

    @Override
    public int hashCode() {
        return this.x + 31 * this.y;
    }


    //    returns new coordinate
    @Deprecated
    public Coordinate translate(int dx, int dy) {
        return new Coordinate(x + dx, y + dy);
    }

    public String toString(){
        return "(" + this.x + ","+this.y+")";
    }
}
package com.asarg.polysim.models.base;

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
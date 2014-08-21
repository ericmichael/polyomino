package com.asarg.polysim;

/* Frontier Element
    Will be used for the frontier list of polytiles.
     It should contain the coordinates of the tile with the glue that the polytile is being matched to,
     the the offset of the polytile to be moved to the correct position, the polytile itself,
     and the the direction the tile to be matched is facing.
*/

import java.awt.*;

public class FrontierElement {
    private Point tileWithGlue;
    private Point polytileOffset;
    private PolyTile polyTile;
    private int direction;

    public FrontierElement(Point tg, Point poff, PolyTile p,int d ){
        tileWithGlue = tg;
        polytileOffset = poff;
        polyTile = p;
        direction = d;
    }

    public Point getLocation(){return tileWithGlue;}
    public Point getOffset(){return polytileOffset;}
    public PolyTile getPolyTile(){return polyTile;}
    public int getDirection(){return direction;}

    public boolean equals(FrontierElement ele2){
        // check offset to see if they are the same.
        if ( !ele2.getOffset().equals(polytileOffset))
            return false;

        // check polytile to see if they are the same.
        if ( !ele2.getPolyTile().equals(polyTile))
            return false;

        // if both the offset and polytile match, it's the same placement.
        return true;
    }

}

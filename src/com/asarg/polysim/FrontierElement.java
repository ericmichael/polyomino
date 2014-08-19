package com.asarg.polysim;

/* Frontier Elemnt
    Will be used for the frontier list of polytiles.
     It should contain the coordinates of the tile with the glue that the polytile is being matched to,
     the the offset of the polytile to be moved to the correct position, the polytile itself,
     and the the direction the tile to be matched is facing.
*/

import java.awt.*;

/**
 * Created by Mario M on 8/19/2014.
 */
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

    public boolean isEqual(FrontierElement ele2){

        return true;
    }

}

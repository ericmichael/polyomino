package com.asarg.polysim;/*Tile class
information for single tiles (not to be confused with polytiles).
Should store a location and an id.
*/
import java.awt.*;

public class Tile {
    // each point in coordinates has 4 edges with a label and a direction
    private Point tileLocation = new Point();
    // glue labels are 4 field array of strings: 0=N, 1=E, 2=S, 3=W
    private String[] glueLabels = new String[4];
    // tiles have an id that references the polyTile they belong to.
    private PolyTile polyTile;

    public Tile( int x, int y, String[] gl, PolyTile parent) {
        tileLocation.setLocation(x, y);
        glueLabels = gl;
        polyTile = parent;
    }

    public void changeTileGlue(String[] gl){
        glueLabels = gl;
    }

    public void setTileLocation(int x, int y){
        tileLocation.setLocation(x, y);
    }
    public Point getLocation(){
        return tileLocation;
    }

    public String[] getGlueLabels() {
        return glueLabels;
    }
    public String getGlueN() {
        return glueLabels[0];
    }
    public String getGlueE() {
        return glueLabels[1];
    }
    public String getGlueS() {
        return glueLabels[2];
    }
    public String getGlueW() {
        return glueLabels[3];
    }

}
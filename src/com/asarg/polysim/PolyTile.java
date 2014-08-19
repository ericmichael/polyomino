package com.asarg.polysim;
/* polytile class
    polytile with shape, glues, concentration, & label are defined.
    TODO: Check connected tiles in the polytile and change their strength to infinite
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PolyTile {
    // tiles that make up the shape of the polytile.
    List<Tile> tiles = new ArrayList<Tile>();
    // polytiles have a concentration or a count.
    private double concentration;
    private int count;
    // polytiles can be labeled and can have a unique name/id. (id might be useless)
    private String polyName;
    private int polyID;
    // tileID increases every time a new tile is created, changes are not accounted for.
    //          (delete one, that number is lost forever)
    private static int tileID = 0;

    public HashMap<Point, String> northGlues = new HashMap<Point, String>();
    public HashMap<Point, String> eastGlues = new HashMap<Point, String>();
    public HashMap<Point, String> southGlues = new HashMap<Point, String>();
    public HashMap<Point, String> westGlues = new HashMap<Point, String>();

    public PolyTile() {
        System.out.println("polytile with no name and infinite counts");
    }
    public PolyTile(String n) {
        polyName = n;
        System.out.println("polytile "+polyName+" with infinite counts");
    }
    public PolyTile(String n, double conc){
        polyName = n;
        concentration = conc;
        System.out.println("polytile "+polyName+" created");
    }
    public PolyTile(String n, int c){
        polyName = n;
        count = c;
        System.out.println("polytile "+polyName+" created");
    }

    // add tile, increases the size the polytile by creating a tile with the given data
    public void addTile(int x, int y, String[] gl) {
        if(getTile(x, y) == null){
            Tile tile = new Tile(x, y, gl, this);
            tiles.add(tile);
            setGlues();
        }else{
            System.out.println("Tile already exists at this relative coordinate");
        }
    }

    // deletes tile at the specified location
    public void removeTile(int x, int y){
        for (Tile tile : tiles){
            if (tile.getLocation().equals(new Point(x,y))) {
                System.out.println("Tile found in polyTile! Removing...");
                tiles.remove(tile);
                return;
            }
        }
        System.out.println("Tile not found, could not remove");
    }

    // returns the tile at specified location.
    public Tile getTile(int x,int y) {
        for (Tile tile : tiles){
            if (tile.getLocation().equals(new Point(x, y))) {
                return tile;
            }
        }
        return null;
    }
    public double getConcentration(){return concentration;}
    public int getCount(){return count;}
    public String getPolyName(){return polyName;}


    public void changeConcentration(double c){
        concentration = c;
        count = 0;
        System.out.println("concentration changed to "+c);
    }
    public void changeCount(int c){
        concentration = 0;
        count = c;
        System.out.println("concentration changed to "+c);
    }
    public void changeName(String n){
        polyName = n;
    }

    //Sets glue edges from polytile and stores them in 4 lists, one for each direction
    public void setGlues(){
        northGlues.clear();
        eastGlues.clear();
        southGlues.clear();
        westGlues.clear();
        for (Tile t : tiles) {
            String[] glueLabels = t.getGlueLabels();
            if (glueLabels[0]!=null && !glueLabels[0].equals("")) {
                northGlues.put(t.getLocation(), glueLabels[0]);
            }
            if (glueLabels[1]!=null && !glueLabels[1].equals("")) {
                eastGlues.put(t.getLocation(), glueLabels[1]);
            }
            if (glueLabels[2]!=null && !glueLabels[2].equals("")) {
                southGlues.put(t.getLocation(), glueLabels[2]);
            }
            if (glueLabels[3]!=null && !glueLabels[3].equals("")) {
                westGlues.put(t.getLocation(), glueLabels[3]);
            }
        }
    }

    public boolean isEqual(PolyTile toCompare){
        // check if the polytile data is the same
        if (concentration != toCompare.getConcentration())
            return false;
        if (!polyName.equals(toCompare.getPolyName()))
            return false;
        if (count != toCompare.getCount())
            return false;

        // check if all tiles in the polytile are equal, by looking through their coordinates
        for (Tile t : tiles){
            Point tLoc = t.getLocation();
            Tile t2 = toCompare.getTile((int)tLoc.getX(), (int)tLoc.getY());
            if (!t.isEqual(t2))
                return false;
        }
        return true;
    }

    public String toString(){
        if(polyName!=null){
            return "PolyTile: " + polyName;
        }else{
            return super.toString();
        }
    }
}
package com.asarg.polysim;
/* polytile class
    polytile with shape, glues, concentration, & label are defined.
    TODO: Check connected tiles in the polytile and change their strength to infinite
*/

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"polyName", "color", "tiles", "concentration", "count"})
public class PolyTile {
    // tiles that make up the shape of the polytile.
    @XmlElement(name = "Tile")
    protected List<Tile> tiles = new ArrayList<Tile>();
    // polytiles have a concentration or a count. Initialized to -1 to use as "not set" exception.
    private double concentration = -1;
    private int count = -1;
    // polytiles can be labeled and can have a unique name/id. (id might be useless)
    @XmlAttribute(name = "Label")
    private String polyName;
    private int polyID;
    // tileID increases every time a new tile is created, changes are not accounted for.
    //          (delete one, that number is lost forever)

    private String color;

    private Boolean frontier = false;

    @XmlTransient
    public HashMap<Point, String> northGlues = new HashMap<Point, String>();
    @XmlTransient
    public HashMap<Point, String> eastGlues = new HashMap<Point, String>();
    @XmlTransient
    public HashMap<Point, String> southGlues = new HashMap<Point, String>();
    @XmlTransient
    public HashMap<Point, String> westGlues = new HashMap<Point, String>();

    // default color for tiles is cyan for some reason
    public PolyTile() {
        tiles.add(new Tile(0,0, this));
        polyName = "New";
        setColor("00FFFF");
    }
    public PolyTile(int c, double con){
        count = c;
        concentration = con;
        System.out.println("polytile with no name created with count "+c+" and concentration "+con);
        setColor("00FFFF");
    }
    public PolyTile(String n) {
        polyName = n;
        System.out.println("polytile "+polyName+" with no count or concentration");
        setColor("00FFFF");
    }
    public PolyTile(String n, double conc){
        polyName = n;
        concentration = conc;
        System.out.println("polytile "+polyName+" created with no count and concentration "+conc);
        setColor("00FFFF");
    }
    public PolyTile(String n, int c){
        polyName = n;
        count = c;
        System.out.println("polytile "+polyName+" created with count "+c+" and no concentration.");
        setColor("00FFFF");
    }
    public PolyTile(String n, int c, double con, String colr){
        polyName = n;
        count = c;
        concentration = con;
        color = colr;
        System.out.println("polytile "+polyName+" created with count "+c+" and concentration "+con);
    }
    public void setFrontier() { frontier = true; setColor("EEEEEE"); }

    public boolean isFrontier() { return frontier; }

    public List<Tile> getTiles(){
        return tiles;
    }


    public boolean adjacentExists(int x, int y)
    {
        if(getTile(x+1,y) != null || getTile(x-1,y) != null || getTile(x,y+1) != null || getTile(x,y-1) != null)
            return true;

        return false;
    }
    public boolean adjacentExits(Point gridPoint)
    {
        int x = gridPoint.x;
        int y = gridPoint.y;
        if(getTile(x+1,y) != null || getTile(x-1,y) != null || getTile(x,y+1) != null || getTile(x,y-1) != null)
            return true;
        return false;
    }
    public boolean adjacentExistsExc(Point gridPoint, Point exclusionPoint)
    {

        Point northNeighbor =  new Point(gridPoint.x, gridPoint.y+1);
        Point eastNeighbor = new Point(gridPoint.x+1, gridPoint.y);
        Point southNeighbor = new Point(gridPoint.x, gridPoint.y -1);
        Point westNeighbor = new Point(gridPoint.x-1, gridPoint.y);
        if(!exclusionPoint.equals(northNeighbor) && getTile(northNeighbor)!=null )
        {
            return true;
        }
        if(!exclusionPoint.equals(eastNeighbor) && getTile(eastNeighbor)!=null)
        {
            return true;
        }
        if(!exclusionPoint.equals(southNeighbor) && getTile(southNeighbor)!=null )
        {
            return true;
        }
        if(!exclusionPoint.equals(westNeighbor) && getTile(westNeighbor)!=null)
        {
            return true;
        }


        return false;

    }
    public boolean breaksChain(Point gridPoint)
    {
          boolean breaksChain=false;

        HashSet<Point> checkedPoints = new HashSet<Point>();
        checkedPoints.add(gridPoint);
        int bondCount  = 0;
        for(Tile tile : tiles)
        {
            if(tile.getLocation()!=gridPoint) {
                Point tilePoint = tile.getLocation();
                Point northPoint = new Point(tilePoint.x, tilePoint.y + 1);
                Point eastPoint = new Point(tilePoint.x + 1, tilePoint.y);
                Point southPoint = new Point(tilePoint.x, tilePoint.y - 1);
                Point westPoint = new Point(tilePoint.x - 1, tilePoint.y);


                if (!checkedPoints.contains(northPoint) && getTile(northPoint) != null)//north
                {
                    bondCount++;
                }
                if (!checkedPoints.contains(eastPoint) && getTile(eastPoint) != null)//east
                {
                    bondCount++;
                }
                if (!checkedPoints.contains(southPoint) && getTile(southPoint) != null)//south
                {
                    bondCount++;
                }
                if (!checkedPoints.contains(westPoint) && getTile(westPoint) != null)//west
                {

                    bondCount++;
                }


                checkedPoints.add(tilePoint);
            }
        }

        return !(tiles.size()-2 <= bondCount);

    }

    public void setColor(String color){
        this.color = color;
    }

    @XmlAttribute(name = "Color")
    public String getColor(){
        return color;
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
    public void addTile(Tile tile)
    {

        if(tile.getLocation()!=null) {
            if (getTile(tile.getLocation().x, tile.getLocation().y) == null) {
                tile.setParent(this);
                tiles.add(tile);
                setGlues();
            }
        }
        else System.out.println("tile does not have a location");
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
    public Tile getTile(Point gridPoint)
    {
        for (Tile tile : tiles){
            if (tile.getLocation().equals(new Point(gridPoint.x, gridPoint.y))) {
                return tile;
            }
        }
        return null;
    }

    public double getConcentration(){return concentration;}
    public int getCount(){return count;}
    public String getPolyName(){return polyName;}

    public void setConcentration(double c){
        concentration = c;
        count = 0;
        //System.out.println("concentration changed to "+c);
    }
    public void setCount(int c){
        count = c;
        //System.out.println("concentration changed to "+c);
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


    public boolean equals(PolyTile toCompare){
        // check if the simple polytile data is the same
        if (polyName != null && toCompare.getPolyName() != null) {
            if (concentration != toCompare.getConcentration()) return false;
            if (!polyName.equals(toCompare.getPolyName())) return false;
            if (count != toCompare.getCount()) return false;
        }

        // check if all tiles in the polytile are equal, by looking through their coordinates
        for (Tile t : tiles){
            Point tLoc = t.getLocation();
            Tile t2 = toCompare.getTile((int)tLoc.getX(), (int)tLoc.getY());
            if (!t.equals(t2)) return false;
        }

        // the coordinates of both polytiles need to be checked: do the same for the points in the other polytile
        for (Tile t : toCompare.tiles){
            Point tLoc = t.getLocation();
            Tile t2 = this.getTile((int)tLoc.getX(), (int)tLoc.getY());
            if (!t.equals(t2)) return false;
        }
        return true;
    }

    public String toString(){
        if(frontier){
            return "Frontier Tile";
        }else if(polyName!=null){
            return "PolyTile: " + polyName;
        }
        else{
            return super.toString();
        }
    }

    public PolyTile getCopy()
    {
        PolyTile temp = new PolyTile(polyName,count,concentration, color);
        for(Tile t : tiles)
        {
            String[] glues = new String[4];
            glues[0] = t.getGlueN();
            glues[1] = t.getGlueE();
            glues[2] = t.getGlueS();
            glues[3] = t.getGlueW();
            temp.addTile(t.getLocation().x, t.getLocation().y, glues);

        }

        return temp;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent){
        setGlues();
    }
}
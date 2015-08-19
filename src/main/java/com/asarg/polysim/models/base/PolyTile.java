package com.asarg.polysim.models.base;
/* polytile class
    polytile with shape, glues, concentration, & label are defined.
    TODO: Check connected tiles in the polytile and change their strength to infinite
*/

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@XmlRootElement
@XmlType(propOrder = {"polyName", "color", "tiles", "concentration", "count"})
public class PolyTile {
    // tiles that make up the shape of the polytile.
    @XmlElement(name = "Tile")
    public final List<Tile> tiles = new ArrayList<Tile>();
    @XmlTransient
    public final HashMap<Coordinate, String> northGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    public final HashMap<Coordinate, String> eastGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    public final HashMap<Coordinate, String> southGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    public final HashMap<Coordinate, String> westGlues = new HashMap<Coordinate, String>();
    // tileID increases every time a new tile is created, changes are not accounted for.
    //          (delete one, that number is lost forever)
    // polytiles have a concentration or a count. Initialized to -1 to use as "not set" exception.
    private double concentration = -1;
    private int count = -1;
    // polytiles can be labeled and can have a unique name/id. (id might be useless)
    @XmlAttribute(name = "Label")
    private String polyName;
    private int polyID;
    private String color;
    private Boolean frontier = false;

    // default color for tiles is cyan for some reason
    public PolyTile() {
        tiles.add(new Tile(0, 0, this));
        polyName = "New";
        setColor("00FFFF");
    }

    public PolyTile(int c, double con) {
        count = c;
        concentration = con;
        System.out.println("polytile with no name created with count " + c + " and concentration " + con);
        setColor("00FFFF");
    }

    public PolyTile(String n) {
        polyName = n;
        System.out.println("polytile " + polyName + " with no count or concentration");
        setColor("00FFFF");
    }

    public PolyTile(String n, double conc) {
        polyName = n;
        concentration = conc;
        System.out.println("polytile " + polyName + " created with no count and concentration " + conc);
        setColor("00FFFF");
    }

    public PolyTile(String n, int c) {
        polyName = n;
        count = c;
        System.out.println("polytile " + polyName + " created with count " + c + " and no concentration.");
        setColor("00FFFF");
    }

    public PolyTile(String n, int c, double con, String colr) {
        polyName = n;
        count = c;
        concentration = con;
        color = colr;
        System.out.println("polytile " + polyName + " created with count " + c + " and concentration " + con);
    }

    public PolyTile(PolyTile other) {
        polyName = other.polyName;
        count = other.count;
        concentration = other.concentration;
        color = other.color;
        frontier = other.frontier;
        for (Tile t : other.getTiles()) {
            tiles.add(new Tile(t));
        }
        setGlues();
    }

    public void setFrontier() {
        frontier = true;
        setColor("EEEEEE");
    }

    public boolean isFrontier() {
        return frontier;
    }

    public List<Tile> getTiles() {
        return tiles;
    }


    public boolean adjacentExists(int x, int y) {
        return getTile(x + 1, y) != null || getTile(x - 1, y) != null || getTile(x, y + 1) != null || getTile(x, y - 1) != null;

    }

    public boolean adjacentExits(Coordinate gridPoint) {
        int x = gridPoint.getX();
        int y = gridPoint.getY();
        return getTile(x + 1, y) != null || getTile(x - 1, y) != null || getTile(x, y + 1) != null || getTile(x, y - 1) != null;
    }

    public boolean adjacentExistsExc(Coordinate gridPoint, Coordinate exclusionPoint) {

        Coordinate northNeighbor = new Coordinate(gridPoint.getX(), gridPoint.getY() + 1);
        Coordinate eastNeighbor = new Coordinate(gridPoint.getX() + 1, gridPoint.getY());
        Coordinate southNeighbor = new Coordinate(gridPoint.getX(), gridPoint.getY() - 1);
        Coordinate westNeighbor = new Coordinate(gridPoint.getX() - 1, gridPoint.getY());
        if (!exclusionPoint.equals(northNeighbor) && getTile(northNeighbor) != null) {
            return true;
        }
        if (!exclusionPoint.equals(eastNeighbor) && getTile(eastNeighbor) != null) {
            return true;
        }
        if (!exclusionPoint.equals(southNeighbor) && getTile(southNeighbor) != null) {
            return true;
        }
        return !exclusionPoint.equals(westNeighbor) && getTile(westNeighbor) != null;


    }

    public boolean breaksChain(Coordinate gridPoint) {
        boolean breaksChain = false;

        HashSet<Coordinate> checkedPoints = new HashSet<Coordinate>();
        checkedPoints.add(gridPoint);
        int bondCount = 0;
        for (Tile tile : tiles) {
            if (!tile.getLocation().equals(gridPoint)) {
                Coordinate tilePoint = tile.getLocation();
                Coordinate northPoint = new Coordinate(tilePoint.getX(), tilePoint.getY() + 1);
                Coordinate eastPoint = new Coordinate(tilePoint.getX() + 1, tilePoint.getY());
                Coordinate southPoint = new Coordinate(tilePoint.getX(), tilePoint.getY() - 1);
                Coordinate westPoint = new Coordinate(tilePoint.getX() - 1, tilePoint.getY());


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

        return !(tiles.size() - 2 <= bondCount);

    }

    @XmlAttribute(name = "Color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // add tile, increases the size the polytile by creating a tile with the given data
    public void addTile(int x, int y, String[] gl) {
        if (getTile(x, y) == null) {
            Tile tile = new Tile(x, y, gl, this);
            tiles.add(tile);
            setGlues();
        } else {
            System.out.println("Tile already exists at this relative coordinate");
        }
    }

    public void addTile(Tile tile) {

        if (tile.getLocation() != null) {
            if (getTile(tile.getLocation().getX(), tile.getLocation().getY()) == null) {
                tile.setParent(this);
                tiles.add(tile);
                setGlues();
            }
        } else System.out.println("tile does not have a location");
    }

    // deletes tile at the specified location
    public void removeTile(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.getLocation().equals(new Coordinate(x, y))) {
                System.out.println("Tile found in polyTile! Removing...");
                tiles.remove(tile);
                return;
            }
        }
        System.out.println("Tile not found, could not remove");
    }

    // returns the tile at specified location.
    public Tile getTile(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.getLocation().equals(new Coordinate(x, y))) {
                return tile;
            }
        }
        return null;
    }

    public Tile getTile(Coordinate gridPoint) {
        for (Tile tile : tiles) {
            if (tile.getLocation().equals(new Coordinate(gridPoint.getX(), gridPoint.getY()))) {
                return tile;
            }
        }
        return null;
    }

    public double getConcentration() {
        return concentration;
    }

    public void setConcentration(double c) {
        concentration = c;
        count = 0;
        //System.out.println("concentration changed to "+c);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
        //System.out.println("concentration changed to "+c);
    }

    public String getPolyName() {
        return polyName;
    }

    public void changeName(String n) {
        polyName = n;
    }

    //Sets glue edges from polytile and stores them in 4 lists, one for each direction
    public void setGlues() {
        northGlues.clear();
        eastGlues.clear();
        southGlues.clear();
        westGlues.clear();

        for (Tile t : tiles) {
            String[] glueLabels = t.getGlueLabels();
            if (glueLabels[0] != null) {
                northGlues.put(t.getLocation(), glueLabels[0]);
            }
            if (glueLabels[1] != null) {
                eastGlues.put(t.getLocation(), glueLabels[1]);
            }
            if (glueLabels[2] != null) {
                southGlues.put(t.getLocation(), glueLabels[2]);
            }
            if (glueLabels[3] != null) {
                westGlues.put(t.getLocation(), glueLabels[3]);
            }
        }
    }

    public PolyTile normalize() {
        PolyTile copy = new PolyTile(this);

        Tile min = null;
        for (Tile t : copy.tiles) {
            if (min == null) min = t;
            else {
                if (t.getLocation().getX() <= min.getLocation().getX() && t.getLocation().getY() <= min.getLocation().getY()) {
                    min = t;
                }
            }
        }

        int x_translate = -min.getLocation().getX();
        int y_translate = -min.getLocation().getY();

        for (Tile t : copy.tiles) {
            t.setLocation(t.getLocation().translate(x_translate, y_translate));
        }
        return copy;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 13 + (polyName == null ? 0 : polyName.hashCode());
        hash = hash * 17 + tiles.size();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        PolyTile toCompare = (PolyTile) obj;
        // check if the simple polytile data is the same
        if (polyName != null && toCompare.getPolyName() != null) {
            if (concentration != toCompare.getConcentration()) return false;
            if (!polyName.equals(toCompare.getPolyName())) return false;
            if (count != toCompare.getCount()) return false;
        }


        PolyTile first = normalize();
        PolyTile second = toCompare.normalize();

        // check if all tiles in the polytile are equal, by looking through their coordinates
        for (Tile t : first.getTiles()) {
            Coordinate tLoc = t.getLocation();
            Tile t2 = second.getTile(tLoc.getX(), tLoc.getY());
            if (t2 == null) return false;
            else if (!t.equals(t2)) return false;
        }

        // the coordinates of both polytiles need to be checked: do the same for the points in the other polytile
        for (Tile t : second.getTiles()) {
            Coordinate tLoc = t.getLocation();
            Tile t2 = first.getTile(tLoc.getX(), tLoc.getY());
            if (t2 == null) return false;
            else if (!t.equals(t2)) return false;
        }
        return true;
    }

    public String toString() {
        if (frontier) {
            return "Frontier Tile";
        } else if (polyName != null) {
            return "PolyTile: " + polyName;
        } else {
            return super.toString();
        }
    }

    public PolyTile getCopy() {
        PolyTile temp = new PolyTile(polyName, count, concentration, color);
        for (Tile t : tiles) {
            String[] glues = new String[4];
            glues[0] = t.getGlueN();
            glues[1] = t.getGlueE();
            glues[2] = t.getGlueS();
            glues[3] = t.getGlueW();
            temp.addTile(t.getLocation().getX(), t.getLocation().getY(), glues);
        }

        return temp;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        setGlues();
    }
}
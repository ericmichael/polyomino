/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
package com.asarg.polysim.models.base;

import com.asarg.polysim.xml.GridXmlAdapter;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.*;
import java.util.List;

@XmlRootElement(name = "Assembly")
@XmlAccessorType(XmlAccessType.FIELD)
public class Assembly extends Observable {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    // placeholder for the grid
    @XmlElement(name = "AssemblyGrid")
    @XmlJavaTypeAdapter(GridXmlAdapter.class)
    public final HashMap<Coordinate, Tile> Grid = new HashMap<Coordinate, Tile>();
    //Open glue ends stored by their coordinate
    @XmlTransient
    private final
    HashMap<Coordinate, String> openNorthGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    private final
    HashMap<Coordinate, String> openEastGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    private final
    HashMap<Coordinate, String> openSouthGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    private final
    HashMap<Coordinate, String> openWestGlues = new HashMap<Coordinate, String>();
    @XmlTransient
    private final ArrayList<FrontierElement> possibleAttach = new ArrayList<FrontierElement>();
    // frontier list: calculated, increased, decreased, and changed here.
    @XmlTransient
    private final Frontier frontier;
    @XmlElement(name = "History")
    private final History attached = new History();
    // tile system, it can be changed so it needs its own class
    @XmlElement(name = "TileSystem")
    private TileSystem tileSystem;

    public Assembly() {
        tileSystem = new TileSystem(2, 0);
        frontier = new Frontier(tileSystem);
    }

    public Assembly(TileSystem ts) {
        tileSystem = new TileSystem(ts.getTemperature());
        try {
            tileSystem.setWeightOption(ts.getWeightOption());
        } catch (InvalidObjectException ioe) {
            System.out.println("invalid option");
        }

        tileSystem.getGlueFunction().putAll(ts.getGlueFunction());
        tileSystem.getTileTypes().addAll(ts.getTileTypes());
        frontier = new Frontier(tileSystem);
    }




    //change tile system stub
    public void changeTileSystem(TileSystem newTS) {
        System.out.println("WARNING: CHANGING THE TILE SYSTEM, PREPARE FOR ERRORS!");
        tileSystem = newTS;
        cleanUp();
        getOpenGlues();
        frontier.changeTileSystem(newTS);
        frontier.clear();
        calculateFrontier();
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("Tile System", null));
        System.out.println("Frontier: " + frontier.size());
    }

    public void changeTemperature(int temperature) {
        tileSystem.setTemperature(temperature);
        changeTileSystem(tileSystem);
    }

    public void changeTileConfiguration(TileConfiguration tc) {
        tileSystem.loadTileConfiguration(tc);
        frontier.changeTileSystem(tileSystem);
    }

    public TileSystem getTileSystem() {
        return tileSystem;
    }

    //Finds open glues on assembly grid and puts them in 4 maps.
    public void getOpenGlues() {
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
        for (Map.Entry<Coordinate, Tile> t : Grid.entrySet()) {
            String[] glueLabels = t.getValue().getGlueLabels();
            //Check if glues are open by checking if their corresponding adjacent point is open
            Coordinate spot = t.getKey();

            if (glueLabels[0] != null && Grid.get(spot.translate(0, 1)) == null) {
                openNorthGlues.put(t.getKey(), glueLabels[0]);
            }
            if (glueLabels[1] != null && Grid.get(spot.translate(1, 0)) == null) {
                openEastGlues.put(t.getKey(), glueLabels[1]);
            }
            if (glueLabels[2] != null && Grid.get(spot.translate(0, -1)) == null) {
                openSouthGlues.put(t.getKey(), glueLabels[2]);
            }
            if (glueLabels[3] != null && Grid.get(spot.translate(-1, 0)) == null) {
                openWestGlues.put(t.getKey(), glueLabels[3]);
            }
        }
    }

    public void placePolytile(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            Coordinate tmp = new Coordinate(t.getLocation());
            tmp = tmp.translate(x, y);
            Grid.put(tmp, t);
        }
    }

    public void removePolytile(PolyTile p, int x, int y) {
        boolean polytilePresent = true;
        for (Tile t : p.tiles) {
            Coordinate tmp = new Coordinate(t.getLocation());
            tmp = tmp.translate(x, y);
            Tile existing = Grid.get(tmp);

            if (existing == null) polytilePresent = false;
        }
        if (polytilePresent) {
            for (Tile t : p.tiles) {
                Coordinate tmp = new Coordinate(t.getLocation());
                tmp = tmp.translate(x, y);
                Grid.remove(tmp);
            }
        }
    }

    public boolean geometryCheckSuccess(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            if (Grid.containsKey(new Coordinate(x + t.getLocation().getX(), y + t.getLocation().getY()))) {
                return false;
            }
        }
        return true;
    }

    private Pair<Coordinate, Coordinate> getOffset(Coordinate aPoint, Coordinate ptPoint, int offsetX, int offsetY) {
        Coordinate placement = new Coordinate(offsetX, offsetY);
        placement = placement.translate(aPoint.getX(), aPoint.getY());
        int xOffset = -(ptPoint.getX() - placement.getX());
        int yOffset = -(ptPoint.getY() - placement.getY());
        Coordinate tmp2 = new Coordinate(xOffset, yOffset);
        return new Pair<Coordinate, Coordinate>(placement, tmp2);
    }

    private void fillPossibleList(PolyTile pt, int direction) {
        HashMap<Coordinate, String> ptGlues;
        HashMap<Coordinate, String> glues;
        int offsetX;
        int offsetY;

        switch (direction) {
            case NORTH:
                ptGlues = pt.southGlues;
                glues = openNorthGlues;
                offsetX = 0;
                offsetY = 1;
                break;
            case EAST:
                ptGlues = pt.westGlues;
                glues = openEastGlues;
                offsetX = 1;
                offsetY = 0;
                break;
            case SOUTH:
                ptGlues = pt.northGlues;
                glues = openSouthGlues;
                offsetX = 0;
                offsetY = -1;
                break;
            default:
                ptGlues = pt.eastGlues;
                glues = openWestGlues;
                offsetX = -1;
                offsetY = 0;
                break;
        }

        String glue1;
        String glue2;
        for (Coordinate ptPoint : ptGlues.keySet()) {
            for (Coordinate aPoint : glues.keySet()) {
                glue1 = ptGlues.get(ptPoint);
                glue2 = glues.get(aPoint);
                if (tileSystem.getStrength(glue1, glue2) > 0) {
                    Pair<Coordinate, Coordinate> locAndOffset = getOffset(aPoint, ptPoint, offsetX, offsetY);
                    FrontierElement fe = new FrontierElement(locAndOffset.getKey(), locAndOffset.getValue(), pt, direction);
                    possibleAttach.add(fe);
                }
            }
        }
    }

    //Adds coordinate of placement of certain polytile to a grid location iff the polytile has a matching glue with assembly
    public void checkMatchingGlues(PolyTile t) {
        fillPossibleList(t, NORTH);
        fillPossibleList(t, EAST);
        fillPossibleList(t, SOUTH);
        fillPossibleList(t, WEST);
    }

    public List<FrontierElement> getAttached() {
        return attached.getHistory();
    }

    public Frontier getFrontier() {
        return frontier;
    }

    public Frontier calculateFrontier() {
        ArrayList<FrontierElement> toRemove = new ArrayList<FrontierElement>();
        for (PolyTile t : tileSystem.getTileTypes()) {
            checkMatchingGlues(t);
        }
        for (FrontierElement fe : possibleAttach) {
            boolean isStable = checkStability(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());
            boolean fitsGeometrically = geometryCheckSuccess(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());

            if (isStable && fitsGeometrically) {
                if (!frontier.contains(fe)) {
                    frontier.add(fe);
                }
                toRemove.add(fe);
            }
        }
        for (FrontierElement e : toRemove) {
            possibleAttach.remove(e);
        }
        return frontier;
    }

    // delete from frontier

    private boolean checkStability(PolyTile p, int x, int y) {
        int totalStrength = 0;
        //For all tiles and their edges, check the tile they would possibly bond with for the strength
        //Example: a tile's north glue needs to see the above tile's south glue
        for (Tile t : p.tiles) {
            String nPolytileGlue = t.getGlueN();
            if (nPolytileGlue != null) {
                Coordinate pt = new Coordinate(t.getLocation().getX() + x, t.getLocation().getY() + y + 1);
                Tile nAssemblyTile = Grid.get(pt);
                if (nAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(nPolytileGlue, nAssemblyTile.getGlueS());

            }

            String ePolytileGlue = t.getGlueE();
            if (ePolytileGlue != null) {
                Coordinate pt = new Coordinate(t.getLocation().getX() + x + 1, t.getLocation().getY() + y);
                Tile eAssemblyTile = Grid.get(pt);
                if (eAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(ePolytileGlue, eAssemblyTile.getGlueW());

            }

            String sPolytileGlue = t.getGlueS();
            if (sPolytileGlue != null) {
                Coordinate pt = new Coordinate(t.getLocation().getX() + x, t.getLocation().getY() + y - 1);
                Tile sAssemblyTile = Grid.get(pt);
                if (sAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(sPolytileGlue, sAssemblyTile.getGlueN());

            }

            String wPolytileGlue = t.getGlueW();
            if (wPolytileGlue != null) {
                Coordinate pt = new Coordinate(t.getLocation().getX() + x - 1, t.getLocation().getY() + y);
                Tile wAssemblyTile = Grid.get(pt);
                if (wAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(wPolytileGlue, wAssemblyTile.getGlueE());

            }
        }
        return totalStrength >= tileSystem.getTemperature();
    }

    public void cleanUp() {
        frontier.clear();
        possibleAttach.clear();
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
    }

    public double attach(boolean notify) {
        if (!frontier.isEmpty()) {
            FrontierElement fe = frontier.get(frontier.randomSelect());
            attachP(fe);
            calculateFrontier();
            if (notify) {
                setChanged();
                notifyObservers(new Pair<String, FrontierElement>("attach", fe));
            }
            return fe.getAttachTime();
        } else return -1.0;
    }

    public double attach(FrontierElement fe) {
        double time = attachP(fe);
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("attach", fe));
        return time;
    }

    private double attachP(FrontierElement fe) {
        fe.setAttachTime(getDistribution(frontier.getTotalConcentration()));
        placePolytile(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());
        frontier.remove(fe);
        attached.getHistory().add(fe);
        cleanUp();
        getOpenGlues();
        return fe.getAttachTime();
    }

    public double attachAll(boolean notify) {
        double last = -1.0;
        while (!frontier.isEmpty()) {
            FrontierElement fe = frontier.get(frontier.randomSelect());
            last = attachP(fe);
            calculateFrontier();
        }
        if (notify) {
            setChanged();
            notifyObservers(new Pair<String, FrontierElement>("refresh", null));
        }
        return last;
    }

    public void detach(boolean notify) {
        if (!attached.getHistory().isEmpty()) {
            FrontierElement last = attached.getHistory().remove(attached.getHistory().size() - 1);
            detach(last);
            calculateFrontier();
            if (notify) {
                setChanged();
                notifyObservers(new Pair<String, FrontierElement>("detach", last));
            }
        }
    }

    private void detach(FrontierElement fe) {
        removePolytile(fe.getPolyTile(), fe.getOffset().getX(), fe.getOffset().getY());
        cleanUp();
        getOpenGlues();
    }

    public void detachAll(boolean notify) {
        while (!attached.getHistory().isEmpty()) {
            FrontierElement last = attached.getHistory().remove(attached.getHistory().size() - 1);
            detach(last);

        }
        if (notify) {
            setChanged();
            notifyObservers(new Pair<String, FrontierElement>("refresh", null));
        }
    }

    public void placeSeed(PolyTile t) {
        if (Grid.size() == 0)
            placePolytile(t, 0, 0);
        else
            System.out.println("Grid not empty");

        getOpenGlues();
    }

    public void clearSeed() {
        detachAll(false);
        Grid.clear();
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("refresh", null));
    }

    public Set<Coordinate> pointsInGrid() {
        return Grid.keySet();
    }

    public double getDistribution(double rate) {
        Random rand = new Random();
        double randNum = rand.nextDouble();
        double logger = Math.log(1 - randNum);
        double time = logger / (-rate);
        return time;

    }

    //Prints assembly as grid, with the number being the number of tiles in a spot
    //For debugging purposes
    @Override
    public String toString() {
        int minimumX, maximumX, minimumY, maximumY;
        Point[] tiles = Grid.keySet().toArray(new Point[Grid.keySet().size()]);
        Point[] tiles2 = new Point[Grid.keySet().size()];
        for (int i = 0; i < tiles.length; i++)
            tiles2[i] = new Point(tiles[i]);
        minimumX = tiles2[0].x;
        maximumX = tiles2[0].x;
        minimumY = tiles2[0].y;
        maximumY = tiles2[0].y;

        //Find minimum and maximum of assembly coordinates
        for (Point p : tiles2) {
            if (p.x < minimumX)
                minimumX = p.x;
            else if (p.x > maximumX)
                maximumX = p.x;

            if (p.y < minimumY)
                minimumY = p.y;
            else if (p.y > maximumY)
                maximumY = p.y;
        }

        //Shift everything to 0,0 as bottom left
        for (Point aTiles2 : tiles2) {
            aTiles2.x += (-1) * minimumX;
            aTiles2.y += (-1) * minimumY;
        }
        maximumX += (-1) * minimumX;
        maximumY += (-1) * minimumY;

        int[][] assemblyMatrix = new int[maximumY + 1][maximumX + 1];
        for (Point p : tiles2) {
            assemblyMatrix[maximumY - p.y][p.x]++;
        }

        StringBuilder matrixString = new StringBuilder();

        for (int[] anAssemblyMatrix : assemblyMatrix) {
            for (int j = 0; j < anAssemblyMatrix.length; j++) {
                matrixString.append(anAssemblyMatrix[j] + " ");
            }
            matrixString.append("\n");
        }

        return matrixString.toString();
    }

    public PolyTile toPolyTile() {
        PolyTile assemblyPT = new PolyTile();
        assemblyPT.getTiles().clear();
        for (Map.Entry<Coordinate, Tile> t : Grid.entrySet()) {
            Coordinate location = t.getKey();
            Tile tile = t.getValue();
            Tile tile_copy = new Tile(location.getX(), location.getY(), tile.getGlueLabels(), assemblyPT);
            assemblyPT.addTile(tile_copy);
        }
        return assemblyPT;
    }

    public void printDebugInformation() {
        System.out.println("--- Debug Information ----");
        System.out.println("Grid Size: " + Grid.size());
        System.out.println("Frontier Size: " + frontier.size());
        System.out.println("Attached List Size: " + attached.getHistory().size());
        System.out.println("Possible Attached List Size: " + possibleAttach.size());
        System.out.println("Open North Glues: " + openNorthGlues.size());
        System.out.println("Open East Glues: " + openEastGlues.size());
        System.out.println("Open South Glues: " + openSouthGlues.size());
        System.out.println("Open West Glues: " + openWestGlues.size());


        System.out.println("--------------------------");

        tileSystem.printDebugInformation();
        frontier.printDebugInformation();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        changeTileSystem(getTileSystem());
        getOpenGlues();
    }
}
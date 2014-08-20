/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
package com.asarg.polysim;
import javafx.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Assembly {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    // tile system, it can be changed so it needs its own class
    private TileSystem tileSystem;
    // placeholder for the grid
    public HashMap<Point, Tile> Grid = new HashMap<Point, Tile>();
    // frontier list: calculated, increased, decreased, and changed here.
    private List<Pair<Point, PolyTile>> frontier = new ArrayList<Pair<Point, PolyTile>>();
    private List<Pair<Point, PolyTile>> attached = new ArrayList<Pair<Point, PolyTile>>();


    //Open glue ends stored by their coordinate
    HashMap<Point, String> openNorthGlues = new HashMap<Point, String>();
    HashMap<Point, String> openEastGlues = new HashMap<Point, String>();
    HashMap<Point, String> openSouthGlues = new HashMap<Point, String>();
    HashMap<Point, String> openWestGlues = new HashMap<Point, String>();
    ArrayList<ArrayList<Object>> possibleAttach = new ArrayList<ArrayList<Object>>();

    public Assembly(){
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2);

        //tileSystem.addPolyTile();
    }

    //change tile system stub
    public void changeTileSystem(TileSystem newTS){
        System.out.println("WARNING: CHANGING THE TILE SYSTEM, PREPARE FOR ERRORS!");
        tileSystem = newTS;
    }

    public Assembly(TileSystem ts){
        tileSystem = ts;
    }
    //Finds open glues on assembly grid and puts them in 4 maps.
    private void getOpenGlues() {
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
        for (Map.Entry<Point, Tile> t : Grid.entrySet()) {
            String[] glueLabels = t.getValue().getGlueLabels();
            //Check if glues are open by checking if their corresponding adjacent point is open
            if (glueLabels[0] != null && Grid.get(new Point(t.getKey().x, t.getKey().y + 1)) == null) {
                openNorthGlues.put(t.getKey(), glueLabels[0]);
            }
            if (glueLabels[1] != null && Grid.get(new Point(t.getKey().x + 1, t.getKey().y)) == null) {
                openEastGlues.put(t.getKey(), glueLabels[1]);
            }
            if (glueLabels[2] !=null && Grid.get(new Point(t.getKey().x, t.getKey().y - 1)) == null) {
                openSouthGlues.put(t.getKey(), glueLabels[2]);
            }
            if (glueLabels[3] !=null && Grid.get(new Point(t.getKey().x - 1, t.getKey().y)) == null) {
                openWestGlues.put(t.getKey(), glueLabels[3]);
            }
        }
    }

    private void placePolytile(PolyTile p, int x, int y) {
        for(Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Grid.put(tmp, t);
        }
    }

    private void removePolytile(PolyTile p, int x, int y) {
        for(Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Tile existing = Grid.get(tmp);
            if(existing.samePolyTile(p)) {
                Grid.remove(tmp);
            }else System.out.println("not removing polytile");
        }
    }

    private boolean geometryCheckSuccess(PolyTile p, int x, int y){
        for(Tile t : p.tiles) {
            if(Grid.containsKey(new Point(x+t.getLocation().x,  y+t.getLocation().y))){
                return false;
            }
        }
        return true;
    }

    private Pair<Point,Point> getOffset(Point aPoint, Point ptPoint, int offsetX, int offsetY){
        Point tmp = new Point(aPoint);
        tmp.translate(offsetX,offsetY);
        Point placement = new Point(tmp);
        int xOffset = (int)(tmp.getX() - ptPoint.getX());
        int yOffset = (int)(tmp.getY() - ptPoint.getY());
        tmp.setLocation(xOffset, yOffset);
        return new Pair<Point,Point>(placement, tmp);
    }

    private void fillPossibleList(PolyTile pt, int direction){
        HashMap<Point, String> ptGlues;
        HashMap<Point, String> glues;
        int offsetX;
        int offsetY;

        switch(direction){
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
        for (Point ptPoint : ptGlues.keySet()) {
            for (Point aPoint : glues.keySet()) {
                glue1 = ptGlues.get(ptPoint);
                glue2 = glues.get(aPoint);
                if (tileSystem.getStrength(glue1, glue2) > 0) {
                    Pair<Point, Point> locAndOffset = getOffset(aPoint, ptPoint, offsetX, offsetY);
                    Pair<Pair<Point, Point>, PolyTile> x = new Pair<Pair<Point, Point>, PolyTile>(locAndOffset, pt);
                    ArrayList attachment = new ArrayList();
                    attachment.add(locAndOffset.getKey());
                    attachment.add(locAndOffset.getValue());
                    attachment.add(new Integer(direction));
                    attachment.add(pt);
                    possibleAttach.add(attachment);
                }
            }
        }
    }

    //Adds coordinate of placement of certain polytile to a grid location iff the polytile has a matching glue with assembly
    private void checkMatchingGlues( PolyTile t ) {
        fillPossibleList(t, NORTH);
        fillPossibleList(t, EAST);
        fillPossibleList(t, SOUTH);
        fillPossibleList(t, WEST);
    }
    // calculate frontier

    public List<Pair<Point, PolyTile>> calculateFrontier() {
        ArrayList toRemove = new ArrayList();
        for(PolyTile t : tileSystem.getTileTypes()){
            checkMatchingGlues(t);
        }
        for(ArrayList e : possibleAttach) {
            Point location = (Point) e.get(0);
            Point offset = (Point) e.get(1);
            int direction = ((Integer) e.get(2)).intValue();
            PolyTile pt = (PolyTile) e.get(3);

            if(checkStability(pt, offset.x, offset.y) &&
                    geometryCheckSuccess(pt, offset.x, offset.y)) {
                Pair<Point, PolyTile> candidate = new Pair<Point, PolyTile>(offset, pt);
                if(!frontier.contains(candidate)){
                    frontier.add(candidate);
                }
                toRemove.add(e);
            }
        }
        for(Object e : toRemove){
            possibleAttach.remove(e);
        }
        return frontier;
    }

    private boolean checkStability(PolyTile p, int x, int y) {
        int totalStrength = 0;
        //For all tiles and their edges, check the tile they would possibly bond with for the strength
        //Example: a tile's north glue needs to see the above tile's south glue
        for(Tile t : p.tiles) {
            String nPolytileGlue = t.getGlueN();
            if(nPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x, y+1);
                Tile nAssemblyTile = Grid.get(pt);
                if(nAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(nPolytileGlue, nAssemblyTile.getGlueS());

            }

            String ePolytileGlue = t.getGlueE();
            if(ePolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x+1, y);
                Tile eAssemblyTile = Grid.get(pt);
                if(eAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(ePolytileGlue, eAssemblyTile.getGlueW());

            }

            String sPolytileGlue = t.getGlueS();
            if(sPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x, y-1);
                Tile sAssemblyTile = Grid.get(pt);
                if(sAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(sPolytileGlue, sAssemblyTile.getGlueN());

            }

            String wPolytileGlue = t.getGlueW();
            if(wPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x-1, y);
                Tile wAssemblyTile = Grid.get(pt);
                if(wAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(wPolytileGlue, wAssemblyTile.getGlueE());

            }
        }
        if(totalStrength >= tileSystem.getTemperature())
            return true;
        else
            return false;
    }

    // delete from frontier

    // add to frontier

    //Place "random" polytile from frontier
    private double addFromFrontier(){
        Random rn = new Random();
        Pair<Point, PolyTile> x = frontier.get(rn.nextInt(frontier.size()));
        placePolytile(x.getValue(), x.getKey().x, x.getKey().y );
        frontier.remove(x);
        attached.add(x);
        return x.getValue().getConcentration();
    }

    private void cleanUp() {
        frontier.clear();
        possibleAttach.clear();
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
    }


    public double attach(){
        double r = addFromFrontier();
        cleanUp();
        getOpenGlues();
        return r;
    }

    public void detach(){
        if(!attached.isEmpty()) {
            Pair<Point, PolyTile> last = attached.remove(attached.size() - 1);
            removePolytile(last.getValue(), last.getKey().x, last.getKey().y);
            cleanUp();
            getOpenGlues();
        }
    }

    //Add "random" polytile from frontier based on Polytile's concentrations
    public void weightedAddFromFrontier() {
        //generate cumulative density list
        ArrayList<Double> cdList = new ArrayList();
        double totalConcentration = 0.0;
        for (Pair<Point, PolyTile> p : frontier) {
            PolyTile pt = p.getValue();
            totalConcentration += pt.getConcentration();
            cdList.add(totalConcentration);
        }
        Random rn = new Random();
        double x = rn.nextDouble() * totalConcentration;
        //Binary search for random number in cdList
        int index = weightedAddBinarySearchHelper(cdList, x);
        Pair<Point, PolyTile> pt = frontier.get(index);
        placePolytile(pt.getValue(), pt.getKey().x, pt.getKey().y);
        frontier.remove(x);
    }

    private static int weightedAddBinarySearchHelper(ArrayList<Double> cdList, double x) {
        int lo = 0;
        int hi = cdList.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (mid == 0)
                return mid;
            if (x < cdList.get(mid) && x >= cdList.get(mid - 1))
                return mid;
            else if (x < cdList.get(mid))
                hi = mid - 1;
            else if (x > cdList.get(mid))
                lo = mid + 1;
        }
        return -1;
    }

    public void placeSeed(PolyTile t){
        if(Grid.size() == 0)
            placePolytile(t, 0, 0);
        else
            System.out.println("Grid not empty");

        getOpenGlues();
    }

    public Set<Point> pointsInGrid(){
        return Grid.keySet();
    }

    //Prints assembly as grid, with the number being the number of tiles in a spot
    //For debugging purposes
    @Override
    public String toString() {
        int minimumX, maximumX, minimumY, maximumY;
        Point[] tiles = Grid.keySet().toArray(new Point[Grid.keySet().size()]);
        Point[] tiles2 = new Point[Grid.keySet().size()];
        for(int i = 0; i<tiles.length; i++)
            tiles2[i] = new Point(tiles[i]);
        minimumX = tiles2[0].x;
        maximumX = tiles2[0].x;
        minimumY = tiles2[0].y;
        maximumY = tiles2[0].y;

        //Find minimum and maximum of assembly coordinates
        for(Point p : tiles2) {
            if(p.x < minimumX)
                minimumX = p.x;
            else if(p.x > maximumX)
                maximumX = p.x;

            if(p.y < minimumY)
                minimumY = p.y;
            else if(p.y > maximumY)
                maximumY = p.y;
        }

        //Shift everything to 0,0 as bottom left
        for(int i = 0; i < tiles2.length; i++) {
            tiles2[i].x += (-1)*minimumX;
            tiles2[i].y += (-1)*minimumY;
        }
        maximumX += (-1)*minimumX;
        maximumY += (-1)*minimumY;

        int[][] assemblyMatrix = new int[maximumY + 1][maximumX + 1];
        for(Point p : tiles2) {
            assemblyMatrix[maximumY - p.y][p.x]++;
        }

        StringBuilder matrixString = new StringBuilder();

        for(int i = 0; i < assemblyMatrix.length; i++) {
            for(int j = 0; j < assemblyMatrix[i].length; j++) {
                matrixString.append(assemblyMatrix[i][j] + " ");
            }
            matrixString.append("\n");
        }

        return matrixString.toString();
    }
}
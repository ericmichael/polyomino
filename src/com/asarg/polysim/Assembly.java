/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
package com.asarg.polysim;
import javafx.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Assembly {
    // tile system, it can be changed so it needs its own class
    private TileSystem tileSystem;
    // placeholder for the grid
    public HashMap<Point, Tile> Grid = new HashMap<Point, Tile>();
    // frontier list: calculated, increased, decreased, and changed here.
    private List<Pair<Point, PolyTile>> frontier = new ArrayList<Pair<Point, PolyTile>>();

    //Open glue ends stored by their coordinate
    HashMap<Point, String> openNorthGlues = new HashMap<Point, String>();
    HashMap<Point, String> openEastGlues = new HashMap<Point, String>();
    HashMap<Point, String> openSouthGlues = new HashMap<Point, String>();
    HashMap<Point, String> openWestGlues = new HashMap<Point, String>();
    List<Pair<Point, PolyTile>> possibleAttach = new ArrayList<Pair<Point, PolyTile>>();

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

    private boolean geometryCheckSuccess(PolyTile p, int x, int y){
        for(Tile t : p.tiles) {
            if(Grid.containsKey(new Point(x+t.getLocation().x,  y+t.getLocation().y))){
                return false;
            }
        }
        return true;
    }

    //Adds coordinate of placement of certain polytile to a grid location iff the polytile has a matching glue with assembly
    private void checkMatchingGlues( PolyTile t ) {
        for (Point ptPoint : t.southGlues.keySet()) {
            for (Point aPoint : openNorthGlues.keySet()) {
                if (t.southGlues.get(ptPoint).equals(openNorthGlues.get(aPoint))){
                    Point tmp = new Point(aPoint);
                    tmp.translate(0, 1);
                    //System.out.println("placePoint: " + tmp);
                    int xOffset = (int)(tmp.getX() - ptPoint.getX());
                    int yOffset = (int)(tmp.getY() - ptPoint.getY());
                    tmp.setLocation(xOffset, yOffset);
                    Pair<Point, PolyTile> x = new Pair<Point, PolyTile>(tmp, t);
                    //System.out.println("aPoint: " + aPoint);
                    //System.out.println("ptPoint: " + ptPoint);
                    possibleAttach.add(x);
                }
            }
        }
        for (Point ptPoint : t.westGlues.keySet()) {
            for (Point aPoint : openEastGlues.keySet()) {
                if (t.westGlues.get(ptPoint).equals(openEastGlues.get(aPoint))){
                    Point tmp = new Point(aPoint);
                    tmp.translate(1, 0);
                    //System.out.println("placePoint: " + tmp);
                    int xOffset = (int)(tmp.getX() - ptPoint.getX());
                    int yOffset = (int)(tmp.getY() - ptPoint.getY());
                    tmp.setLocation(xOffset, yOffset);
                    Pair<Point, PolyTile> x = new Pair<Point, PolyTile>(tmp, t);
                    //System.out.println("aPoint: " + aPoint);
                    //System.out.println("ptPoint: " + ptPoint);
                    possibleAttach.add(x);
                }
            }
        }
        for (Point ptPoint : t.northGlues.keySet()) {
            for (Point aPoint : openSouthGlues.keySet()) {
                if (t.northGlues.get(ptPoint).equals(openSouthGlues.get(aPoint))){
                    Point tmp = new Point(aPoint);
                    tmp.translate(0, -1);
                    //System.out.println("placePoint: " + tmp);
                    int xOffset = (int)(tmp.getX() - ptPoint.getX());
                    int yOffset = (int)(tmp.getY() - ptPoint.getY());
                    tmp.setLocation(xOffset, yOffset);
                    Pair<Point, PolyTile> x = new Pair<Point, PolyTile>(tmp, t);
                    //System.out.println("aPoint: " + aPoint);
                    //System.out.println("ptPoint: " + ptPoint);
                    possibleAttach.add(x);
                }
            }
        }
        for (Point ptPoint : t.eastGlues.keySet()) {
            for (Point aPoint : openWestGlues.keySet()) {
                if (t.eastGlues.get(ptPoint).equals(openWestGlues.get(aPoint))){
                    Point tmp = new Point(aPoint);
                    tmp.translate(-1, 0);
                    //System.out.println("placePoint: " + tmp);
                    int xOffset = (int)(tmp.getX() - ptPoint.getX());
                    int yOffset = (int)(tmp.getY() - ptPoint.getY());
                    tmp.setLocation(xOffset, yOffset);
                    Pair<Point, PolyTile> x = new Pair<Point, PolyTile>(tmp, t);
                    //System.out.println("aPoint: " + aPoint);
                    //System.out.println("ptPoint: " + ptPoint);
                    possibleAttach.add(x);
                }
            }
        }
    }
    // calculate frontier

    public List<Pair<Point, PolyTile>> calculateFrontier() {
        List<Pair<Point, PolyTile>> toRemove = new ArrayList<Pair<Point, PolyTile>>();
        for(PolyTile t : tileSystem.getTileTypes()){
            checkMatchingGlues(t);
        }
        for(Pair<Point, PolyTile> e : possibleAttach) {
            if(checkStability(e.getValue(), (e.getKey()).x, (e.getKey()).y) &&
                    geometryCheckSuccess(e.getValue(), (e.getKey()).x, (e.getKey()).y)) {
                Pair<Point, PolyTile> candidate = new Pair<Point, PolyTile>(e.getKey(), e.getValue());
                if(!frontier.contains(candidate)){
                    frontier.add(candidate);
                }
                toRemove.add(e);
            }
        }
        for(Pair<Point, PolyTile> e : toRemove){
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

    //Place "random" polytile from frontier
    private void addFromFrontier(){
        Random rn = new Random();
        Pair<Point, PolyTile> x = frontier.get(rn.nextInt(frontier.size()));
        placePolytile(x.getValue(), x.getKey().x, x.getKey().y );
        frontier.remove(x);
    }

    private void cleanUp() {
        frontier.clear();
        possibleAttach.clear();
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
    }


    public void attach(){
        addFromFrontier();
        cleanUp();
        getOpenGlues();
    }

    //Add "random" polytile from frontier based on Polytile's concentrations
    public void weightedAddFromFrontier(){
        //generate cumulative density list
        ArrayList<Double> cdList = new ArrayList();
        double totalConcentration = 0.0;
        for(Pair<Point, PolyTile> p : frontier) {
            PolyTile pt = p.getValue();
            totalConcentration += pt.getConcentration();
            cdList.add(totalConcentration);
        }
        Random rn = new Random();
        double x = rn.nextDouble() * totalConcentration;
        //Binary search for random number in cdList
        int index = weightedAddBinarySearchHelper(cdList, x, cdList.size()/2);
        Pair<Point, PolyTile> pt = frontier.get(index);
        placePolytile(pt.getValue(), pt.getKey().x, pt.getKey().y );
        frontier.remove(x);
    }

    private int weightedAddBinarySearchHelper(ArrayList<Double> cdList, double x, int mid){
        if(cdList.get(mid-1) <= x && cdList.get(mid) > x)
            return mid;
        else if(cdList.get(mid) > x)
            mid = mid/2;
        else if(cdList.get(mid) < x)
            mid = (cdList.size()+mid)/2;
        return weightedAddBinarySearchHelper(cdList, x, mid);
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
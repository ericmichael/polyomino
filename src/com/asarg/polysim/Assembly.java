/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
package com.asarg.polysim;
import com.asarg.polysim.xml.GridXmlAdapter;
import com.asarg.polysim.xml.OpenGlueXmlAdapter;
import javafx.util.Pair;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.*;

@XmlRootElement(name = "Assembly")
@XmlAccessorType(XmlAccessType.FIELD)
public class Assembly {
    static final int NORTH = 0;
    static final int EAST = 1;
    static final int SOUTH = 2;
    static final int WEST = 3;

    // tile system, it can be changed so it needs its own class
    @XmlElement(name = "TileSystem")
    private TileSystem tileSystem;
    // placeholder for the grid
    @XmlElement(name = "AssemblyGrid")
    @XmlJavaTypeAdapter(GridXmlAdapter.class)
    public HashMap<Point, Tile> Grid = new HashMap<Point, Tile>();
    // frontier list: calculated, increased, decreased, and changed here.
    @XmlTransient
    private Frontier frontier;
    @XmlTransient
    private ArrayList<FrontierElement> attached = new ArrayList<FrontierElement>();


    //Open glue ends stored by their coordinate
    @XmlElement(name = "OpenNorthGlues")
    @XmlJavaTypeAdapter(OpenGlueXmlAdapter.class)
    HashMap<Point, String> openNorthGlues = new HashMap<Point, String>();
    @XmlElement(name = "OpenEastGlues")
    @XmlJavaTypeAdapter(OpenGlueXmlAdapter.class)
    HashMap<Point, String> openEastGlues = new HashMap<Point, String>();
    @XmlElement(name = "OpenSouthGlues")
    @XmlJavaTypeAdapter(OpenGlueXmlAdapter.class)
    HashMap<Point, String> openSouthGlues = new HashMap<Point, String>();
    @XmlElement(name = "OpenWestGlues")
    @XmlJavaTypeAdapter(OpenGlueXmlAdapter.class)
    HashMap<Point, String> openWestGlues = new HashMap<Point, String>();

    @XmlTransient
    ArrayList<FrontierElement> possibleAttach = new ArrayList<FrontierElement>();

    public Assembly(){
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2, 0);
        frontier = new Frontier( tileSystem );
    }

    //change tile system stub
    public void changeTileSystem(TileSystem newTS){
        System.out.println("WARNING: CHANGING THE TILE SYSTEM, PREPARE FOR ERRORS!");
        tileSystem = newTS;
        frontier.changeTileSystem(newTS);
    }

    public void changeTileConfiguration(TileConfiguration tc){
        tileSystem.loadTileConfiguration(tc);
        frontier.changeTileSystem(tileSystem);
    }

    public TileSystem getTileSystem(){
        return tileSystem;
    }

    public Assembly(TileSystem ts){
        tileSystem = ts;
        frontier = new Frontier( tileSystem );
    }
    //Finds open glues on assembly grid and puts them in 4 maps.
    public void getOpenGlues() {
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

    public void placePolytile(PolyTile p, int x, int y) {
        for(Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Grid.put(tmp, t);
        }
    }

    public void removePolytile(PolyTile p, int x, int y) {
        for(Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Tile existing = Grid.get(tmp);
            if (existing.samePolyTile(p)) {
                if (existing.samePolyTile(p)) {
                    Grid.remove(tmp);
                } else System.out.println("not removing polytile");
            }
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
                    FrontierElement fe = new FrontierElement(locAndOffset.getKey(), locAndOffset.getValue(), pt, direction);
                    possibleAttach.add(fe);
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

    public ArrayList<FrontierElement> getAttached(){
        return attached;
    }

    // calculate frontier

    public Frontier calculateFrontier() {
        ArrayList toRemove = new ArrayList();
        for(PolyTile t : tileSystem.getTileTypes()){
            checkMatchingGlues(t);
        }
        for(FrontierElement fe : possibleAttach) {
            if(checkStability(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y) &&
                    geometryCheckSuccess(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y)) {

                if(!frontier.contains(fe)){
                    frontier.add(fe);
                }
                toRemove.add(fe);
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

    public void cleanUp() {
        frontier.clear();
        possibleAttach.clear();
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
    }

    public double attach(){
        FrontierElement fe = frontier.get(frontier.randomSelect());

        fe.setAttachTime(getDistribution(frontier.getTotalConcentration()));
        placePolytile(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y);
        frontier.remove(fe);
        attached.add(fe);

        cleanUp();
        getOpenGlues();
        return fe.getAttachTime();
    }

    public double attach(FrontierElement fe){
        fe.setAttachTime(0);
        placePolytile(fe.getPolyTile().getCopy(), fe.getOffset().x, fe.getOffset().y);
        frontier.remove(fe);
        attached.add(fe);
        cleanUp();
        getOpenGlues();
        return fe.getAttachTime();
    }

    public void detach(){
        if(!attached.isEmpty()) {
            FrontierElement last = attached.remove(attached.size() - 1);
            removePolytile(last.getPolyTile(), last.getOffset().x, last.getOffset().y);
            cleanUp();
            getOpenGlues();
        }
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



    public double getDistribution(double rate){
        Random rand = new Random();
        double randNum = rand.nextDouble();
        double logger = Math.log(1-randNum);
        double time = logger/(-rate);
        return time;

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
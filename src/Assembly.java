/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembly {
    // tile system, it can be changed so it needs its own class
    private TileSystem tileSystem;
    // placeholder for the grid
    private HashMap<Point, Tile> Grid = new HashMap();
    // frontier list: calculated, increased, decreased, and changed here.
    private List<PolyTile> frontier = new ArrayList<PolyTile>();

    //Open glue ends stored by their coordinate
    HashMap<Point, String> openNorthGlues = new HashMap();
    HashMap<Point, String> openEastGlues = new HashMap();
    HashMap<Point, String> openSouthGlues = new HashMap();
    HashMap<Point, String> openWestGlues = new HashMap();
    HashMap<Point, PolyTile> possibleAttach = new HashMap();

    public Assembly(){
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2);

        //tileSystem.addPolyTile();
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
            if (!glueLabels[0].equals("") && Grid.get(new Point(t.getKey().x, t.getKey().y + 1)) == null) {
                openNorthGlues.put(t.getKey(), glueLabels[0]);
            }
            if (!glueLabels[1].equals("") && Grid.get(new Point(t.getKey().x + 1, t.getKey().y)) == null) {
                openEastGlues.put(t.getKey(), glueLabels[1]);
            }
            if (!glueLabels[2].equals("") && Grid.get(new Point(t.getKey().x, t.getKey().y - 1)) == null) {
                openSouthGlues.put(t.getKey(), glueLabels[2]);
            }
            if (!glueLabels[3].equals("") && Grid.get(new Point(t.getKey().x - 1, t.getKey().y)) == null) {
                openWestGlues.put(t.getKey(), glueLabels[3]);
            }
        }
    }

    private void placePolytile(PolyTile p, int x, int y) {
        for(Tile t : p.tiles) {
            Grid.put(new Point(x+t.getLocation().x,  y+t.getLocation().y), t);
        }
    }

    public Assembly(TileSystem ts){
        tileSystem = ts;
    }

    //Adds coordinate of placement of certain polytile to a grid location iff the polytile has a matching glue with assembly
    public void checkMatchingGlues( PolyTile t ) {
        for (Point ptPoint : t.southGlues.keySet()) {
            for (Point aPoint : openNorthGlues.keySet()) {
                if (t.southGlues.get(ptPoint) == openNorthGlues.get(aPoint)) {
                    Point tmp = new Point();
                    tmp.setLocation(aPoint.getX() - ptPoint.getX(), aPoint.getY() + 1 - ptPoint.getY());
                    possibleAttach.put(tmp, t);
                }
            }
        }
        for (Point ptPoint : t.westGlues.keySet()) {
            for (Point aPoint : openEastGlues.keySet()) {
                if (t.westGlues.get(ptPoint) == openEastGlues.get(aPoint)) {
                    Point tmp = new Point();
                    tmp.setLocation(aPoint.getX() + 1 - ptPoint.getX(), aPoint.getY() - ptPoint.getY());
                    possibleAttach.put(tmp, t);
                }
            }
        }
        for (Point ptPoint : t.northGlues.keySet()) {
            for (Point aPoint : openSouthGlues.keySet()) {
                if (t.northGlues.get(ptPoint) == openSouthGlues.get(aPoint)) {
                    Point tmp = new Point();
                    tmp.setLocation(aPoint.getX() - ptPoint.getX(), aPoint.getY() - 1 - ptPoint.getY());
                    possibleAttach.put(tmp, t);
                }
            }
        }
        for (Point ptPoint : t.eastGlues.keySet()) {
            for (Point aPoint : openWestGlues.keySet()) {
                if (t.eastGlues.get(ptPoint) == openWestGlues.get(aPoint)) {
                    Point tmp = new Point();
                    tmp.setLocation(aPoint.getX() - 1 - ptPoint.getX(), aPoint.getY() - ptPoint.getY());
                    possibleAttach.put(tmp, t);
                }
            }
        }
    }
    // calculate frontier


    // delete from frontier


    // add to frontier

}
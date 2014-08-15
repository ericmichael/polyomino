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

    // calculate frontier

    // delete from frontier

    // add to frontier

}
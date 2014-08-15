/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
import java.util.ArrayList;
import java.util.List;

public class Assembly {
    // tile system, it can be changed so it needs its own class
    private TileSystem tileSystem;
    // placeholder for the grid
    private HashMap<String, Tile> Grid = new HashMap();
    // frontier list: calculated, increased, decreased, and changed here.
    private List<PolyTile> frontier = new ArrayList<PolyTile>();

    HashMap<String, String> openNorthGlues = new HashMap();
    HashMap<String, String> openEastGlues = new HashMap();
    HashMap<String, String> openSouthGlues = new HashMap();
    HashMap<String, String> openWestGlues = new HashMap();

    public Assembly(){
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2);

        tileSystem.addPolyTile();
    }

    //Finds open glues on assembly grid and puts them in 4 maps.
    private void getOpenGlues() {
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
        for (Map.Entry<String, Tile> t : Grid.entrySet()) {
            String[] glueLabels = t.getValue().getGlueLabels();
            if (!glueLabels[0].equals("")) {
                openNorthGlues.put(t.getKey(), glueLabels[0]);
            }
            if (!glueLabels[1].equals("")) {
                openEastGlues.put(t.getKey(), glueLabels[1]);
            }
            if (!glueLabels[2].equals("")) {
                openSouthGlues.put(t.getKey(), glueLabels[2]);
            }
            if (!glueLabels[3].equals("")) {
                openWestGlues.put(t.getKey(), glueLabels[3]);
            }
        }
    }

    public Assembly(TileSystem ts){
        tileSystem = ts;
    }

    // calculate frontier

    // delete from frontier

    // add to frontier

}
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

    private boolean geometryCheckSuccess(PolyTile p, int x, int y){
        for(Tile t : p.tiles) {
            if(Grid.containsKey(new Point(x+t.getLocation().x,  y+t.getLocation().y))){
                return false;
            }
        }
        return true;
    }

    // calculate frontier

    private boolean checkStability(PolyTile p, int x, int y) {
        int totalStrength = 0;
        //For all tiles and their edges, check the tile they would possibly bond with for the strength
        //Example: a tile's north glue needs to see the above tile's south glue
        for(Tile t : p.tiles) {
            String nPolytileGlue = t.getGlueN();
            if(!nPolytileGlue.equals("")) {
                Tile nAssemblyTile = Grid.get(new Point(x, y+1));
                if(nAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(nPolytileGlue, nAssemblyTile.getGlueS());
                if(totalStrength >= tileSystem.getTemperature())
                    return true;
            }

            String ePolytileGlue = t.getGlueE();
            if(!ePolytileGlue.equals("")) {
                Tile eAssemblyTile = Grid.get(new Point(x+1, y));
                if(eAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(ePolytileGlue, eAssemblyTile.getGlueW());
                if(totalStrength >= tileSystem.getTemperature())
                    return true;
            }

            String sPolytileGlue = t.getGlueS();
            if(!sPolytileGlue.equals("")) {
                Tile sAssemblyTile = Grid.get(new Point(x, y-1));
                if(sAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(sPolytileGlue, sAssemblyTile.getGlueN());
                if(totalStrength >= tileSystem.getTemperature())
                    return true;
            }

            String wPolytileGlue = t.getGlueW();
            if(!wPolytileGlue.equals("")) {
                Tile wAssemblyTile = Grid.get(new Point(x-1, y));
                if(wAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(wPolytileGlue, wAssemblyTile.getGlueE());
                if(totalStrength >= tileSystem.getTemperature())
                    return true;
            }
        }
        //Should the total strength never reach the temperature of the system
        return false;
    }

    // delete from frontier

    // add to frontier

}
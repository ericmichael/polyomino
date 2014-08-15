/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
import java.util.ArrayList;
import java.util.List;

public class Assembly {
    // tile system, it can be changed so it needs its own class
    private TileSystem tileSystem;
    // placeholder for the grid
    private Object Grid;
    // frontier list: calculated, increased, decreased, and changed here.
    private List<PolyTile> frontier = new ArrayList<PolyTile>();

    public Assembly(){
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2);

        tileSystem.addPolyTile();
    }

    private void getOpenGlues() {
        for(PolyTile p : )
    }

    // calculate frontier

    // delete from frontier

    // add to frontier

}
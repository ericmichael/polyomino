package com.asarg.polysim.models.StagedTwoHAM;

import com.asarg.polysim.models.TwoHAM.TwoHAMAssembly;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.TileConfiguration;
import com.asarg.polysim.models.base.TileSystem;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ericmartinez on 6/27/15.
 */
public class Bin extends TwoHAMAssembly {
    private final ArrayList<Bin> edges = new ArrayList<Bin>();
    private final int i;
    public Stage stage;

    public Bin(int i) {
        super();
        this.i = i;
    }

    public Bin(int i, int temperature) {
        super(new TileSystem(temperature));
        this.i = i;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addEdge(Bin b) {
        edges.add(b);
    }

    public void addTile(PolyTile pt) {
        getTileSystem().getTileTypes().add(pt);
    }

    public void addTiles(List<PolyTile> polyTileArrayList) {
        getTileSystem().getTileTypes().addAll(polyTileArrayList);
    }

    public void addGlueFunction(HashMap<Pair<String, String>, Integer> glueFunction) {
        for (Map.Entry<Pair<String, String>, Integer> entry : glueFunction.entrySet()) {
            Pair<String, String> glues = entry.getKey();
            Integer strength = entry.getValue();
            getTileSystem().addGlueFunction(glues.getKey(), glues.getValue(), strength);
        }
    }

    public void addTileConfiguration(TileConfiguration tc) {
        addTiles(tc.getTiletypes());
        addGlueFunction(tc.getGlueFunction());
    }

    public TileConfiguration getTerminalTileConfiguration() {
        TileConfiguration tc = new TileConfiguration();
        for (PolyTile pt : getTerminalSet()) {
            tc.addTileType(pt);
        }

        tc.getGlueFunction().putAll(getTileSystem().getGlueFunction());
        return tc;
    }

    public void start() {
        System.out.println("Starting with " + this.getTileSystem().getTileTypes().size() + " tile types");
        //pass info to connected bins
        for (Bin bin : edges) {
            try {
                int max = 0;
                for (PolyTile pt : getTerminalSet()) if (pt.getTiles().size() > max) max = pt.getTiles().size();

                int min = getTerminalSet().get(0).getTiles().size();
                for (PolyTile pt : getTerminalSet()) if (pt.getTiles().size() < min) min = pt.getTiles().size();

                System.out.println("Passing from Stage " + stage.num + " Bin " + i + " to Stage " + bin.stage.num + " Bin " + bin.i);
                System.out.println("Passing " + getTerminalSet().size() + " tiles");
                System.out.println("Passing down tiles of max size: " + max);
                System.out.println("Passing down tiles of min size: " + min);
            } catch (Exception ignored) {
            }

            bin.addTiles(getTerminalSet());
            bin.addGlueFunction(getTileSystem().getGlueFunction());
        }
    }
}

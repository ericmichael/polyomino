package com.asarg.polysim.models.StagedTwoHAM;

import com.asarg.polysim.models.TwoHAM.TwoHAMAssembly;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.TileSystem;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericmartinez on 6/27/15.
 */
public class Bin extends TwoHAMAssembly {
    public Stage stage;
    ArrayList<Bin> edges = new ArrayList<Bin>();
    public int i;

    public Bin(int i){
        super();
        this.i = i;
    }

    public Bin(int i, int temperature){
        super(new TileSystem(temperature));
        this.i=i;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void addEdge(Bin b){ edges.add(b); }

    public void addTile(PolyTile pt){ getTileSystem().getTileTypes().add(pt); }

    public void addTiles(ArrayList<PolyTile> polyTileArrayList){ getTileSystem().getTileTypes().addAll(polyTileArrayList); }

    public void addGlueFunction(HashMap<Pair<String, String>, Integer> glueFunction){
        for(Map.Entry<Pair<String, String>, Integer> entry : glueFunction.entrySet()){
            Pair<String, String> glues = entry.getKey();
            Integer strength = entry.getValue();
            getTileSystem().addGlueFunction(glues.getKey(), glues.getValue(), strength);
        }
    }

    public void start(){
        System.out.println("Starting with " + this.getTileSystem().getTileTypes().size() + " tile types");
        //pass info to connected bins
        for(Bin bin : edges){
            try {
                int max = 0;
                for (PolyTile pt : getTerminalSet()) if (pt.getTiles().size() > max) max = pt.getTiles().size();

                int min = getTerminalSet().get(0).getTiles().size();
                for (PolyTile pt : getTerminalSet()) if (pt.getTiles().size() < min) min = pt.getTiles().size();

                System.out.println("Passing from Stage " + stage.num + " Bin " + i + " to Stage " + bin.stage.num + " Bin " + bin.i);
                System.out.println("Passing " + getTerminalSet().size() + " tiles");
                System.out.println("Passing down tiles of max size: " + max);
                System.out.println("Passing down tiles of min size: " + min);
            }catch(Exception e){}

            for(PolyTile pt : getTerminalSet()){
                for(int i = 0; i < 4; i ++){
                    String glue = pt.getTiles().get(0).getGlueLabels()[i];
                    if(glue!=null || !glue.trim().isEmpty())
                        System.out.println("" + i + ": " + glue);
                }
            }
            bin.addTiles(getTerminalSet());
            bin.addGlueFunction(getTileSystem().getGlueFunction());
        }
    }
}

/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import javafx.util.Pair;
import java.util.HashMap;

public class TileSystem {
    // temperature of the system, bonds must be of at least this value or they break.
    private int temperature;
    // placeholder for the gluefunction
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap();
    // list of polytiles: data structure should be changed to something that would be of better performance
    private Set<PolyTile> tileTypes = new HashSet<PolyTile>();

    public TileSystem(int temp){
        System.out.print(" in tilesystem with temp: "+temp+"\n");
        temperature = temp;
    }

    public void addGlueFunction(String label1, String label2, int temp) {
        glueFunction.put(new Pair(label1, label2), temp);
        if(!label1.equals(label2)) {
            glueFunction.put(new Pair(label2, label1), temp);
        }
    }

    public void removeGlueFunction(String label1, String label2) {
        glueFunction.remove(new Pair(label1, label2));
        if(!label1.equals(label2)) {
            glueFunction.remove(new Pair(label2, label1));
        }
    }

    public int getStrength(String label1, String label2) {
        return glueFunction.get(new Pair(label1, label2));
    }

    // add polytile to tiletypes
    public void addPolyTile(PolyTile p){
        tileTypes.add(p);
    }

//    public void removePolyTile(int id){
//        for (PolyTile tile : tileTypes){
//            if ( tile.);
//            }
//        }
//    }
}

/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class TileSystem {
    // temperature of the system, bonds must be of at least this value or they break.
    private int temperature;
    // placeholder for the gluefunction
    private Map<Pair<String, String>, Integer> glueFunction = new HashMap();
    // list of polytiles: data structure should be changed to something that would be of better performance
    private List<PolyTile> tileTypes = new ArrayList<PolyTile>();

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
    public void addPolyTile(){

        PolyTile poly = new PolyTile();
        String[] label = new String[4];
        label[0] = "glueN";
        label[1] = "glueE";
        label[2] = "glueS";
        label[3] = "glueW";
        String[] label2 = {"N", "E", "S", "W"};

        poly.addTile(2,3,label);
        poly.addTile(1,2, label2);

        PolyTile poly2 = new PolyTile("tetris piece1", 100);

        poly2.addTile(23,23, label2);

        Tile t1 = poly2.getTile(23, 23);
        System.out.println(t1.getID());
        poly.removeTile(2,3);
    }

//    public void removePolyTile(int id){
//        for (PolyTile tile : tileTypes){
//            if ( tile.);
//            }
//        }
//    }
}

/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TileSystem {
    // temperature of the system, bonds must be of at least this value or they break.
    private int temperature;
    // placeholder for the gluefunction
    private Object glueFunction;
    // list of polytiles: data structure should be changed to something that would be of better performance
    private List<PolyTile> tileTypes = new ArrayList<PolyTile>();

    public TileSystem(int temp){
        System.out.print(" in tilesystem with temp: "+temp+"\n");
        temperature = temp;
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

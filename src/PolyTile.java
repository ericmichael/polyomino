/* polytile class
    polytile with shape, glues, concentration, & label are defined.
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolyTile {
    // tiles that make up the shape of the polytile.
    List<Tile> tiles = new ArrayList<Tile>();
    // polytiles have a concentration or a count.
    private double concentration;
    private int count;
    // polytiles can be labeled and can have a unique name/id. (id might be useless)
    private String polyName;
    private int polyID;
    // tileID increases everytime a new tile is created, changes are not accounted for.
    //          (delete one, that number is lost forever)
    private static int tileID = 0;


    public PolyTile() {
        System.out.println("polytile with no name and infinite counts");
    }
    public PolyTile(String n) {
        polyName = n;
        System.out.println("polytile "+polyName+" with infinite counts");
    }
    public PolyTile(String n, double conc){
        polyName = n;
        concentration = conc;
        System.out.println("polytile "+polyName+" created");
    }
    public PolyTile(String n, int c){
        polyName = n;
        count = c;
        System.out.println("polytile "+polyName+" created");
    }

    // add tile, increases the size the polytile by creating a tile with the given data
    public void addTile(int x, int y, String[] gl) {
        Tile tile = new Tile(x, y, gl, this);
        tiles.add(tile);
    }

    // deletes tile at the specified location
    public void removeTile(int x, int y){
        for (Tile tile : tiles){
            if (tile.getLocation().equals(new Point(x,y))) {
                System.out.println("Tile found in polyTile! Removing...");
                tiles.remove(tile);
                return;
            }
        }
        System.out.println("Tile not found, could not remove");
    }

    // returns the tile at specified location.
    public Tile getTile(int x,int y) {
        for (Tile tile : tiles){
            if (tile.getLocation().equals(new Point(x, y))) {
                System.out.println("Tile found in polyTile!");
                return tile;
            }
        }
        System.out.println("Tile not found.");
        return null;
    }

    // returns true if the given tileID exists in the polytile
    //public boolean hasTileID(int id){

   //}

    public void changeConcentration(double c){
        concentration = c;
        count = 0;
        System.out.println("concentration changed to "+c);
    }

    public void changeCount(int c){
        concentration = 0;
        count = c;
        System.out.println("concentration changed to "+c);
    }

    public void changeName(String n){
        polyName = n;
    }

}
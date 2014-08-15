/*Tile class
information for single tiles (not to be confused with polytiles).
Should store a location and an id.
*/
import java.awt.*;

public class Tile {
    // each point in coordinates has 4 edges with a label and a direction
    private Point tileLocation = new Point();
    private String[] glueLabels = new String[4];
    // tiles have an id that references the polyTile they belong to.
    private int tileID;

    public Tile( int x, int y, String[] gl, int id ) {
        tileLocation.setLocation(x, y);
        glueLabels = gl;
        tileID = id;
    }

    public Point getLocation(){
        return tileLocation;
    }

    public int getID(){
        return tileID;
    }

    public String[] getGlueLabels() {
        return glueLabels;
    }

    public String getGlueN() {
        return glueLabels[0];
    }

    public String getGlueE() {
        return glueLabels[1];
    }

    public String getGlueS() {
        return glueLabels[2];
    }

    public String getGlueW() {
        return glueLabels[3];
    }
}
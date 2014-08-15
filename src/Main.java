
public class Main {
    public static void main(String args[]) {
        TileSystem ts = new TileSystem(2);
        PolyTile poly = new PolyTile();
        String[] label = new String[4];
        label[0] = "glueN";
        label[1] = "glueE";
        label[2] = "glueS";
        label[3] = "glueW";
        String[] label2 = {"N", "E", "S", "W"};

        poly.addTile(2,3,label);
        poly.addTile(1,2, label2);

        ts.addPolyTile(poly);

        PolyTile poly2 = new PolyTile("tetris piece1", 100);

        poly2.addTile(23,23, label2);

        Tile t1 = poly2.getTile(23, 23);
        //System.out.println(t1.getID());
        poly.removeTile(2,3);

        ts.addPolyTile(poly2);

        Assembly ass1 = new Assembly(ts);
    }
}
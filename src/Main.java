
public class Main {
    public String[] blankGlues(){
        String label[] = new String[4];
        label[0] = null;
        label[1] = null;
        label[2] = null;
        label[3] = null;
    }

    public PolyTile tetrisU(){
        String blank[] = blankGlues();
        String glue[] = {"e", null, null, null};

        PolyTile poly = new PolyTile();

        poly.addTile(0,0, glue);
        poly.addTile(-1,0, blank);
        poly.addTile(-1,1, blank);
        poly.addTile(1,0, blank);
        poly.addTile(1,1, blank);

        return poly;
    }

    public PolyTile tetrisV(){
        String blank[] = blankGlues();
        String glue[] = {null, null, null, "a"};

        PolyTile poly = new PolyTile();
        poly.addTile(0,0, blank);
        poly.addTile(-1,0, blank);
        poly.addTile(-1,1, blank);
        poly.addTile(-1,2, glue);
        poly.addTile(1,0, blank);

        return poly;
    }

    public PolyTile tetrisX(){
        String blank[] = blankGlues();
        String glue[] = {null, null, "e", null};

        PolyTile poly = new PolyTile();
        poly.addTile(0,0, label);
        poly.addTile(-1,0, label);
        poly.addTile(1,0, label);
        poly.addTile(0,1, label);
        poly.addTile(0,-1, glue);

        return poly;
    }

    public static void main(String args[]) {
        TileSystem ts = new TileSystem(2);

        String[] label = new String[4];


        ts.addPolyTile(poly2);

        Assembly ass1 = new Assembly(ts);
    }
}
package com.asarg.polysim.examples;

import com.asarg.polysim.*;
import com.asarg.polysim.models.atam.ATAMTile;
import javafx.application.Application;

/**
 * Created by ericmartinez on 3/16/15.
 */
public class Rectangles extends SimulationApplication {
    private TileSystem ts;

    public static ATAMTile normal(int i) {
        ATAMTile poly = new ATAMTile(""+i);
        poly.setGlues("u_"+i,"g", "u_"+i, "g");
        poly.setColor("ecf0f1");
        return poly;
    }

    public static ATAMTile hairpin_1(int i) {
        ATAMTile poly = new ATAMTile("HR"+i);
        poly.setGlues("u_"+i, "g", "h_"+i, "r");
        poly.setColor("95a5a6");
        return poly;
    }

    public static ATAMTile hairpin_2(int i) {
        ATAMTile poly = new ATAMTile("HP"+i);
        poly.setGlues("h_"+i, "g", "u_"+(i-1), "p");
        poly.setColor("7f8c8d");
        return poly;
    }

    public static ATAMTile return_probe() {
        ATAMTile poly = new ATAMTile("R");
        poly.setGlues("u_0", "r", "d_0", "r");
        poly.setColor("34495e");
        return poly;
    }

    public static ATAMTile probe(int m) {
        ATAMTile poly = new ATAMTile("P");
        poly.setGlues("d_0", "p", "u_"+(m-1), "p");
        poly.setColor("c0392b");
        return poly;
    }

    public static ATAMTile seed(int l, int k) {
        ATAMTile poly = new ATAMTile("S_"+l);
        if(l==0)
            poly.setGlues("c_1", "s_"+(l+1), "", "");
        else if(l==k-1)
            poly.setGlues("u_0", "vn", "", "s_"+l);
        else
            poly.setGlues("u_0", "s_"+(l+1), "", "s_"+l);
        return poly;
    }

    public static ATAMTile chain(int l, int m){
        ATAMTile poly = new ATAMTile("C_"+l);
        if(l==0)
            poly.setGlues("c_1", "r", "c_0", "");
        else if(l==m-1)
            poly.setGlues("c_0", "p", "c_"+l, "");
        else
            poly.setGlues("c_"+(l+1), "g", "c_"+l, "");
        poly.setColor("bdc3c7");
        return poly;
    }

    public static ATAMTile reflectY(ATAMTile at){
//        Tile t = at.getTile();
//        String north = t.getGlueN();
//        String east = t.getGlueE();
//        String south = t.getGlueS();
//        String west = t.getGlueW();
//        at.setGlues(south, east, north, west);
        return at;
    }

    //start unbounded tiles

    public static ATAMTile generateATAMTile(String label, String north, String east, String south, String west){
        ATAMTile at = new ATAMTile(label);
        at.setGlues(north, east, south,west);
        return at;
    }


    public static ATAMTile generateATAMTile(String label, String north, String east, String south, String west, String color){
        ATAMTile at = new ATAMTile(label);
        at.setGlues(north, east, south,west);
        at.setColor(color);
        return at;
    }

    public Rectangles() {
        super();
        ts = new TileSystem(2);
        int k = 5;
        int n = 5;
        int m = (int) Math.ceil(Math.pow(n, 1.0/k));
        System.out.println("k x m^k");
        System.out.println(k + " x " + m + "^" +k);

        for(int i = 0; i < m; i++){
            ts.addPolyTile(reflectY(normal(i)));
            ts.addGlueFunction("u_"+i,"u_"+i,1);
        }
        ts.addGlueFunction("g", "g", 1);
        ts.addGlueFunction("r", "r", 1);
        ts.addGlueFunction("p", "p", 1);
        ts.addGlueFunction("d_0", "d_0", 1);


        for(int i = 1; i < m; i++){
            ts.addPolyTile(reflectY(hairpin_1(i)));
            ts.addPolyTile(reflectY(hairpin_2(i)));
            ts.addGlueFunction("h_"+i,"h_"+i,2);
        }

        ts.addPolyTile(reflectY(return_probe()));
        ts.addPolyTile(reflectY(probe(m)));

        for(int i = 0; i < k; i++){
            ts.addPolyTile(reflectY(seed(i, k)));
            ts.addGlueFunction("s_"+i, "s_"+i, 2);
        }

        for(int i = 0; i < m; i++){
            ts.addPolyTile(reflectY(chain(i, m)));
            if(i!=0)
            ts.addGlueFunction("c_"+i, "c_"+i, 2);

        }



        ts.addGlueFunction("c_0", "c_0", 1);
        ts.addGlueFunction("a", "a", 2);



        assembly = new Assembly(ts);

    }

    public static void main(String args[]) {
        Application.launch(Rectangles.class, (java.lang.String[]) null);
    }

}

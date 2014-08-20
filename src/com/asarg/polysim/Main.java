package com.asarg.polysim;

import java.util.Random;

public class Main {

    public static double calculateExpDistribution(Random r, double p) {
        return -(Math.log(r.nextDouble()) / p);
    }
    public static String[] blankGlues() {
        String label[] = new String[4];
        label[0] = null;
        label[1] = null;
        label[2] = null;
        label[3] = null;
        return label;
    }

    public static PolyTile tetrisU() {
        String blank[] = blankGlues();
        String glue[] = {"e", null, null, null};

        PolyTile poly = new PolyTile("U");

        poly.setColor("008299");

        poly.addTile(0, 0, glue);
        poly.addTile(-1, 0, blank);
        poly.addTile(-1, 1, blank);
        poly.addTile(1, 0, blank);
        poly.addTile(1, 1, blank);

        return poly;
    }

    public static PolyTile tetrisV() {
        String blank[] = blankGlues();
        String glue[] = {null, null, null, "a"};

        PolyTile poly = new PolyTile("V");

        poly.setColor("2672EC");

        poly.addTile(0, 0, blank);
        poly.addTile(-1, 0, blank);
        poly.addTile(-1, 1, blank);
        poly.addTile(-1, 2, glue);
        poly.addTile(1, 0, blank);

        return poly;
    }

    public static PolyTile tetrisX() {
        String blank[] = blankGlues();
        String glue[] = {null, null, "e", null};
        String glue2[] = {null, null, null, "ddeFE"};

        PolyTile poly = new PolyTile("X");

        poly.setColor("8C0095");

        poly.addTile(0, 0, blankGlues());
        poly.addTile(-1, 0, blankGlues());
        poly.addTile(1, 0, blankGlues());
        poly.addTile(0, 1, glue2);
        poly.addTile(0, -1, glue);

        return poly;
    }

    public static PolyTile tetrisF() {
        String[] gleft = {null, null, null, "b"};
        String[] gright = {null, "ddeFE", null, null};

        PolyTile tetrisF = new PolyTile("F");

        tetrisF.setColor("5133AB");

        tetrisF.addTile(0, 0, blankGlues());
        tetrisF.addTile(0, -1, blankGlues());
        tetrisF.addTile(-1, 0, blankGlues());
        tetrisF.addTile(0, 1, gleft);
        tetrisF.addTile(1, 1, gright);
        return tetrisF;
    }

    public static PolyTile tetrisI() {
        String[] gtop = {null, "a", null, null};
        String[] gbottom = {null, "b", null, null};

        PolyTile tetris = new PolyTile("I");

        tetris.setColor("AC193D");


        tetris.addTile(0, 0, blankGlues());
        tetris.addTile(0, 1, blankGlues());
        tetris.addTile(0, -1, blankGlues());
        tetris.addTile(0, 2, gtop);
        tetris.addTile(0, -2, gbottom);
        return tetris;
    }

    public static PolyTile tetrisL() {
        String[] gtop = {null, "c", null, "a"};

        PolyTile tetris = new PolyTile("L");

        tetris.setColor("D24726");

        tetris.addTile(0, 0, blankGlues());
        tetris.addTile(0, 1, blankGlues());
        tetris.addTile(0, 2, blankGlues());
        tetris.addTile(0, 3, gtop);
        tetris.addTile(1, 0, blankGlues());
        return tetris;
    }


    public static void main(String args[]) {

        TileSystem ts = new TileSystem(2);

        ts.addPolyTile(tetrisF());
        ts.addPolyTile(tetrisI());
        ts.addPolyTile(tetrisL());
        ts.addPolyTile(tetrisU());
        ts.addPolyTile(tetrisV());
        ts.addPolyTile(tetrisX());

        ts.addGlueFunction("a","a",2);
        ts.addGlueFunction("b","b",2);
        ts.addGlueFunction("c","c",2);
        ts.addGlueFunction("d","d",2);
        ts.addGlueFunction("e","e",2);
        ts.addGlueFunction("f","f",2);
        ts.addGlueFunction("ddeFE","ddeFE",2);

        Assembly assembly = new Assembly(ts);
        assembly.placeSeed(tetrisF());

        TestCanvasFrame tcf = new TestCanvasFrame(800,600,assembly);

    }
}
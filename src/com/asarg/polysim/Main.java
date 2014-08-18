package com.asarg.polysim;

public class Main {

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

        PolyTile poly = new PolyTile();

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

        PolyTile poly = new PolyTile();
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

        PolyTile poly = new PolyTile();
        poly.addTile(0, 0, blankGlues());
        poly.addTile(-1, 0, blankGlues());
        poly.addTile(1, 0, blankGlues());
        poly.addTile(0, 1, blankGlues());
        poly.addTile(0, -1, glue);

        return poly;
    }

    public static PolyTile tetrisF() {
        String[] gleft = {null, null, null, "b"};
        String[] gright = {null, "d", null, null};
        PolyTile tetrisF = new PolyTile();
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
        PolyTile tetris = new PolyTile();
        tetris.addTile(0, 0, blankGlues());
        tetris.addTile(0, 1, blankGlues());
        tetris.addTile(0, -1, blankGlues());
        tetris.addTile(0, 2, gtop);
        tetris.addTile(0, -2, gbottom);
        return tetris;
    }

    public static PolyTile tetrisL() {
        String[] gtop = {null, "c", null, "a"};
        PolyTile tetris = new PolyTile();
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

        Assembly assembly = new Assembly(ts);
        assembly.placeSeed(tetrisI());

        for (int i =0; i < 4; i++){
            // Dom stuff here:
            assembly.attach();
        }
    }
}
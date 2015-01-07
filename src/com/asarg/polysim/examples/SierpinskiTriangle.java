package com.asarg.polysim.examples;

import com.asarg.polysim.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SierpinskiTriangle {

    public TileSystem ts;
    public Assembly assembly;

    public SierpinskiTriangle(int temperature) throws JAXBException {
        assembly = loadAssembly();
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
        String glue2[] = {null, null, null, "d"};

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
        String[] gright = {null, "d", null, null};

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

    public static void main(String args[]) throws JAXBException {

        SierpinskiTriangle tetris = new SierpinskiTriangle(2);
        Workspace w = new Workspace(tetris.assembly);
        //SimulationWindow tcf = new SimulationWindow(800,600, tetris.assembly);
    }

    public Assembly loadAssembly() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Assembly a = (Assembly) unmarshaller.unmarshal(new File("./Examples/TETRIS_ATAM/assembly.xml"));
        jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        TileConfiguration tc = (TileConfiguration) unmarshaller.unmarshal(new File("./Examples/TETRIS_ATAM/tileconfig.xml"));

        TileSystem ts = new TileSystem(2);
        ts.loadTileConfiguration(tc);

        for (PolyTile p : ts.getTileTypes())
            p.setGlues();

        a.changeTileSystem(ts);

        return a;
    }
}

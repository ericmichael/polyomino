package com.asarg.polysim.examples;

import com.asarg.polysim.SimulationApplication;
import com.asarg.polysim.models.atam.ATAMTile;
import com.asarg.polysim.models.base.Assembly;
import com.asarg.polysim.models.base.TileConfiguration;
import com.asarg.polysim.models.base.TileSystem;
import javafx.application.Application;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by ericmartinez on 8/19/14.
 */
public class RNGUnbounded2 extends SimulationApplication {
    private TileSystem ts;

    public RNGUnbounded2() throws JAXBException {
        super();
        //ts = new TileSystem(temperature, TileSystem.CONCENTRATION);
        TileConfiguration tc;

        JAXBContext jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        tc = (TileConfiguration) unmarshaller.unmarshal(new File("./Examples/RNG_ATAM/tileconfig.xml"));

        jaxbContext = JAXBContext.newInstance(Assembly.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        assembly = (Assembly) unmarshaller.unmarshal(new File("./Examples/RNG_ATAM/assembly.xml"));

        assembly.changeTileConfiguration(tc);
    }

    public static ATAMTile rngWL() {
        ATAMTile poly = new ATAMTile("Wl");
        poly.setGlues("wl", "ul", null, null);
        return poly;
    }

    public static ATAMTile rngL() {
        ATAMTile poly = new ATAMTile("L");
        poly.setGlues("p", "bind", "ul", "l");
        return poly;
    }

    public static ATAMTile rngUL() {
        ATAMTile poly = new ATAMTile("UL");
        poly.setGlues("ul", "floor-left", null, "ul");
        return poly;
    }

    public static ATAMTile rngFloor() {
        ATAMTile poly = new ATAMTile("floor");
        poly.setGlues(null, "floor-right", null, "floor-left");
        return poly;
    }

    public static ATAMTile rngUR() {
        ATAMTile poly = new ATAMTile("UR");
        poly.setGlues("ur", null, null, "floor-right");
        return poly;
    }

    public static ATAMTile rngR() {
        ATAMTile poly = new ATAMTile("R");
        poly.setGlues("p", "r", "ur", "bind");
        return poly;
    }

    public static ATAMTile rngBind() {
        ATAMTile poly = new ATAMTile("bind");
        poly.setGlues("connector", "bind", null, "bind");
        return poly;
    }

    public static ATAMTile rngConnector() {
        ATAMTile poly = new ATAMTile("connector");
        poly.setGlues("extension", "p", "connector", "p");
        return poly;
    }

    public static ATAMTile rngT() {
        ATAMTile poly = new ATAMTile("T");
        poly.setGlues("1-t", "p", "p", "p");
        return poly;
    }

    public static ATAMTile rngWL2() {
        ATAMTile poly = new ATAMTile("WL2");
        poly.setGlues("wl2", null, "wl", null);
        return poly;
    }

    public static ATAMTile rngWL3() {
        ATAMTile poly = new ATAMTile("WL3");
        poly.setGlues("wl3", null, "wl2", null);
        return poly;
    }

    public static ATAMTile rngSW() {
        ATAMTile poly = new ATAMTile("SW");
        poly.setGlues("sw", "sw", "wl3", null);
        return poly;
    }

    public static ATAMTile rng1T() {
        ATAMTile poly = new ATAMTile("1-t");
        poly.setGlues("reset", "1-t", "1-t", "sw");
        return poly;
    }

    public static ATAMTile rng1B() {
        ATAMTile poly = new ATAMTile("1-b");
        poly.setGlues("reset", "1-b", "1-b", "sw");
        return poly;
    }

    public static ATAMTile rng1TX() {
        ATAMTile poly = new ATAMTile("1-t ext");
        poly.setGlues("reset", "1-t", "extension", "1-t");
        return poly;
    }

    public static ATAMTile rng1BX() {
        ATAMTile poly = new ATAMTile("1-b ext");
        poly.setGlues("reset", "1-b", "extension", "1-b");
        return poly;
    }

    public static ATAMTile rng2T() {
        ATAMTile poly = new ATAMTile("2-t");
        poly.setGlues("reset-2", "2-t", "1-t", "1-t");
        return poly;
    }

    public static ATAMTile rng2B() {
        ATAMTile poly = new ATAMTile("2-b");
        poly.setGlues("reset-2", "2-b", "1-b", "1-b");
        return poly;
    }

    public static ATAMTile rngResetRight() {
        ATAMTile poly = new ATAMTile("reset right");
        poly.setGlues(null, "reset", "reset-2", "reset");
        return poly;
    }

    public static ATAMTile rngResetLeft() {
        ATAMTile poly = new ATAMTile("reset left");
        poly.setGlues("master reset", "reset", "sw", null);
        return poly;
    }

    public static ATAMTile rngReset() {
        ATAMTile poly = new ATAMTile("reset");
        poly.setGlues(null, "reset", "reset", "reset");
        return poly;
    }

    public static ATAMTile rngWLRepeat() {
        ATAMTile poly = new ATAMTile("Wl Repeat");
        poly.setGlues("wl", "ul", "master reset", null);
        return poly;
    }

    public static ATAMTile rngB() {
        ATAMTile poly = new ATAMTile("B");
        poly.setGlues("1-b", "p", "p", "p");
        return poly;
    }

    public static ATAMTile rng1T1B() {
        ATAMTile poly = new ATAMTile("1-t 1-b");
        poly.setGlues(null, null, "1-b", "1-t");
        return poly;
    }

    public static ATAMTile rng1B1T() {
        ATAMTile poly = new ATAMTile("1-b 1-t");
        poly.setGlues(null, null, "1-t", "1-b");
        return poly;
    }

    public static void main(String args[]) throws JAXBException {
        Application.launch(RNGUnbounded2.class, (java.lang.String[]) null);

    }
}

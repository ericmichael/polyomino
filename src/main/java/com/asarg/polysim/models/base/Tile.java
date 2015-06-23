package com.asarg.polysim.models.base;/*Tile class
information for single tiles (not to be confused with polytiles).
Should store a location and an id.
*/

import com.asarg.polysim.xml.PointXmlAdapter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.Arrays;

@XmlRootElement
@XmlType(propOrder = {"location", "color", "glueN", "glueE", "glueS", "glueW", "label"})
public class Tile {

    public static Coordinate NORTH = new Coordinate(0, 1);
    public static Coordinate EAST = new Coordinate(1, 0);
    public static Coordinate SOUTH = new Coordinate(0, -1);
    public static Coordinate WEST = new Coordinate(-1, 0);
    // each point in coordinates has 4 edges with a label and a direction
    private Coordinate tileLocation;
    // glue labels are 4 field array of strings: 0=N, 1=E, 2=S, 3=W

    private String glueN;
    private String glueE;
    private String glueS;
    private String glueW;

    private String label = new String();
    // tiles have an id that references the polyTile they belong to.
    private PolyTile polyTile;

    public Tile() {

    }


    public Tile(int x, int y, PolyTile parent) {
        glueN = null;
        glueE = null;
        glueS = null;
        glueW = null;
        tileLocation = new Coordinate(x, y);
        polyTile = parent;

    }

    public Tile(int x, int y, String[] gl, PolyTile parent) {
        tileLocation = new Coordinate(x, y);
        changeTileGlue(gl);
        polyTile = parent;
    }

    public String getLabel() {
        if (label.isEmpty() && polyTile!=null) return polyTile.getPolyName();
        else return label;
    }

    public void setLabel(String Label) {
        this.label = Label;
    }

    @XmlTransient
    public PolyTile getParent() {
        return polyTile;
    }

    public void setParent(PolyTile p) {
        polyTile = p;
    }

    @XmlElement(name = "Color")
    public String getColor() {
        return polyTile.getColor();
    }

    public boolean samePolyTile(PolyTile p) {
        return p.equals(polyTile);
    }

    public void changeTileGlue(String[] gl) {
        setGlueN(gl[0]);
        setGlueE(gl[1]);
        setGlueS(gl[2]);
        setGlueW(gl[3]);
    }

    public void setTileLocation(int x, int y) {
        tileLocation = new Coordinate(x, y);
    }

    @XmlElement(name = "Location")
    @XmlJavaTypeAdapter(PointXmlAdapter.class)
    public Coordinate getLocation() {
        return tileLocation;
    }

    public void setLocation(Coordinate p) {
        if (tileLocation != null)
            tileLocation = p;
        else tileLocation = new Coordinate(p.getX(), p.getY());
    }

    public String[] getGlueLabels() {
        String glues[] = new String[4];
        glues[0] = glueN;
        glues[1] = glueE;
        glues[2] = glueS;
        glues[3] = glueW;
        return glues;
    }

    @XmlElement(name = "NorthGlue")
    public String getGlueN() {
        return glueN;
    }

    public void setGlueN(String g) {
        glueN = g;
    }

    @XmlElement(name = "EastGlue")
    public String getGlueE() {
        return glueE;
    }

    public void setGlueE(String g) {
        glueE = g;
    }

    @XmlElement(name = "SouthGlue")
    public String getGlueS() {
        return glueS;
    }

    public void setGlueS(String g) {
        glueS = g;
    }

    @XmlElement(name = "WestGlue")
    public String getGlueW() {
        return glueW;
    }

    public void setGlueW(String g) {
        glueW = g;
    }

    public boolean equals(Tile toCompare) {
        // check if coordinates are the same.
        if (!tileLocation.equals(toCompare.getLocation()))
            return false;
        //if (!glueLabels.equals(toCompare.getGlueLabels()))
        if (!Arrays.deepEquals(toCompare.getGlueLabels(), getGlueLabels()))
            return false;

        return true;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {

        try {
            this.polyTile = (PolyTile) parent;
        } catch (Exception e) {
        }
    }
}
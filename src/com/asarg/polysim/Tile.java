package com.asarg.polysim;/*Tile class
information for single tiles (not to be confused with polytiles).
Should store a location and an id.
*/
import com.asarg.polysim.xml.PointXmlAdapter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.Arrays;

@XmlRootElement
@XmlType(propOrder = {"location", "color", "glueN", "glueE", "glueS", "glueW"})
public class Tile {
    // each point in coordinates has 4 edges with a label and a direction
    private Point tileLocation = new Point();
    // glue labels are 4 field array of strings: 0=N, 1=E, 2=S, 3=W
    private String[] glueLabels = new String[4];
    // tiles have an id that references the polyTile they belong to.
    private PolyTile polyTile;

    public Tile() {

    }

    public Tile( int x, int y, String[] gl, PolyTile parent) {
        tileLocation.setLocation(x, y);
        glueLabels = gl;
        polyTile = parent;
    }

    public String getLabel(){
        return polyTile.getPolyName();
    }

    @XmlElement(name = "Color")
    public String getColor(){
        return polyTile.getColor();
    }

    public boolean samePolyTile(PolyTile p){
        return p == polyTile;
    }

    public void changeTileGlue(String[] gl){
        glueLabels = gl;
    }

    public void setTileLocation(int x, int y){
        tileLocation.setLocation(x, y);
    }
    @XmlElement(name = "Location")
    @XmlJavaTypeAdapter(PointXmlAdapter.class)
    public Point getLocation(){
        return tileLocation;
    }
    public void setLocation(Point p) {
        tileLocation = p;
    }

    public String[] getGlueLabels() {
        return glueLabels;
    }
    @XmlElement(name = "NorthGlue")
    public String getGlueN() {
        return glueLabels[0];
    }
    public void setGlueN(String g) {
        glueLabels[0] = g;
    }
    @XmlElement(name = "EastGlue")
    public String getGlueE() {
        return glueLabels[1];
    }
    public void setGlueE(String g) {
        glueLabels[1] = g;
    }
    @XmlElement(name = "SouthGlue")
    public String getGlueS() {
        return glueLabels[2];
    }
    public void setGlueS(String g) {
        glueLabels[2] = g;
    }
    @XmlElement(name = "WestGlue")
    public String getGlueW() {
        return glueLabels[3];
    }
    public void setGlueW(String g) {
        glueLabels[3] = g;
    }

    public boolean equals(Tile toCompare){
        // check if coordinates are the same.
        if (!tileLocation.equals(toCompare.getLocation()))
            return false;
        //if (!glueLabels.equals(toCompare.getGlueLabels()))
        if (!Arrays.deepEquals(toCompare.getGlueLabels(), glueLabels))
            return false;

        return true;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.polyTile = (PolyTile) parent;
    }
}
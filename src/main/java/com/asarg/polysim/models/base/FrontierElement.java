package com.asarg.polysim.models.base;

/* Frontier Element
    Will be used for the frontier list of polytiles.
     It should contain the coordinates of the tile with the glue that the polytile is being matched to,
     the the offset of the polytile to be moved to the correct position, the polytile itself,
     and the the direction the tile to be matched is facing.
*/

import com.asarg.polysim.xml.PointXmlAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "FrontierElement")
public class FrontierElement {
    @XmlElement(name = "Location")
    @XmlJavaTypeAdapter(PointXmlAdapter.class)
    private Coordinate tileWithGlue;

    @XmlElement(name = "Offset")
    @XmlJavaTypeAdapter(PointXmlAdapter.class)
    private Coordinate polytileOffset;

    @XmlElement(name = "PolyTile")
    private PolyTile polyTile;

    @XmlElement(name = "Direction")
    private int direction;

    @XmlElement(name = "IsAttachment")
    private boolean attachment;

    @XmlElement(name = "AttachTime")
    private double attachTime = -1;

    public FrontierElement() {
    }

    public FrontierElement(Coordinate tg, Coordinate poff, PolyTile p, int d) {
        tileWithGlue = tg;
        polytileOffset = poff;
        polyTile = p;
        direction = d;
        attachment = true;
    }

    public FrontierElement(Coordinate tg, Coordinate poff, PolyTile p, int d, boolean a) {
        tileWithGlue = tg;
        polytileOffset = poff;
        polyTile = p;
        direction = d;
        attachment = a;
    }

    public Coordinate getLocation() {
        return tileWithGlue;
    }

    public Coordinate getOffset() {
        return polytileOffset;
    }

    public PolyTile getPolyTile() {
        return polyTile;
    }

    public int getDirection() {
        return direction;
    }

    public boolean checkAttachment() {
        return attachment;
    }

    public boolean equals(FrontierElement ele2) {
        // check offset to see if they are the same.
        if (!ele2.getOffset().equals(polytileOffset))
            return false;

        // check polytile to see if they are the same.
        if (!ele2.getPolyTile().equals(polyTile))
            return false;

        // if both the offset and polytile match, it's the same placement.
        return true;
    }

    @XmlTransient
    public double getAttachTime() {
        return attachTime;
    }

    public void setAttachTime(double t) {
        attachTime = t;
    }
}

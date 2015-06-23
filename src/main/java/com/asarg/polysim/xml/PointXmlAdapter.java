package com.asarg.polysim.xml;

import com.asarg.polysim.models.base.Coordinate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

public final class PointXmlAdapter extends XmlAdapter<PointXmlAdapter.PointXml, Coordinate> {

    @Override
    public PointXml marshal(Coordinate p) throws Exception {
        PointXml pointXml = new PointXml();
        pointXml.x = p.getX();
        pointXml.y = p.getY();
        return pointXml;
    }

    @Override
    public Coordinate unmarshal(PointXml p) throws Exception {
        Coordinate point = new Coordinate(p.x, p.y);
        return point;
    }

    public static class PointXml {
        @XmlAttribute
        public int x;
        @XmlAttribute
        public int y;
    }
}

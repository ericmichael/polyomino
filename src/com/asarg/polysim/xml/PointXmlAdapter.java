package com.asarg.polysim.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.*;

public final class PointXmlAdapter extends XmlAdapter<PointXmlAdapter.PointXml, Point> {

    @Override
    public PointXml marshal(Point p) throws Exception {
        PointXml pointXml = new PointXml();
        pointXml.x = p.x;
        pointXml.y = p.y;
        return pointXml;
    }

    @Override
    public Point unmarshal(PointXml p) throws Exception {
        Point point = new Point();
        point.x = p.x;
        point.y = p.y;
        return point;
    }

    public static class PointXml {
        @XmlAttribute
        public int x;
        @XmlAttribute
        public int y;
    }
}

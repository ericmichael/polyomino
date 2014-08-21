package com.asarg.polysim.xml;

import com.asarg.polysim.PolyTile;
import com.asarg.polysim.Tile;
import javafx.util.Pair;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridXmlAdapter extends XmlAdapter<GridXmlAdapter.HashMapXml, HashMap<Point, Tile>> {

    @Override
    public HashMap<Point, Tile> unmarshal(HashMapXml v) throws Exception {
        HashMap<Point, Tile> hashMap = new HashMap<Point, Tile>();
        for(EntryXml entry : v.entries) {
            entry.tile.setParent(entry.parent);
            hashMap.put(entry.point, entry.tile);
        }
        return hashMap;
    }

    @Override
    public HashMapXml marshal(HashMap<Point, Tile> v) throws Exception {
        HashMapXml hashMapXml = new HashMapXml();
        for(Map.Entry<Point, Tile> entry : v.entrySet()) {
            EntryXml entryXml = new EntryXml();
            entryXml.point = entry.getKey();
            entryXml.tile = entry.getValue();
            entryXml.parent = entry.getValue().getParent();
            hashMapXml.entries.add(entryXml);
        }
        return hashMapXml;
    }

    @XmlType(name = "GridHashMapXml")
    public static class HashMapXml {
        public List<EntryXml> entries = new ArrayList<EntryXml>();
    }

    @XmlType(name = "GridEntryXml")
    public static class EntryXml {
        @XmlElement
        @XmlJavaTypeAdapter(PointXmlAdapter.class)
        public Point point;
        @XmlElement
        public Tile tile;
        @XmlElement
        public PolyTile parent;
    }
}

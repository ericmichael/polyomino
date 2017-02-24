package com.asarg.polysim.xml;

import com.asarg.polysim.models.base.ActiveGrid;
import com.asarg.polysim.models.base.Coordinate;
import com.asarg.polysim.models.base.PolyTile;
import com.asarg.polysim.models.base.Tile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridXmlAdapter extends XmlAdapter<GridXmlAdapter.HashMapXml, HashMap<Coordinate, Tile>> {

    @Override
    public ActiveGrid unmarshal(HashMapXml v) throws Exception {
        ActiveGrid ag = new ActiveGrid();
        System.out.println("In GRIDXML");
        System.out.println(v.GridTile.size());
        for (EntryXml entry : v.GridTile) {
            entry.tile.setParent(entry.parent);
            ag.put(entry.point, entry.tile);
        }
        return ag;
    }

    @Override
    public HashMapXml marshal(HashMap<Coordinate, Tile> v) throws Exception {
        HashMapXml hashMapXml = new HashMapXml();
        for (Map.Entry<Coordinate, Tile> entry : v.entrySet()) {
            EntryXml entryXml = new EntryXml();
            entryXml.point = entry.getKey();
            entryXml.tile = entry.getValue();
            entryXml.parent = entry.getValue().getParent();
            hashMapXml.GridTile.add(entryXml);
        }
        return hashMapXml;
    }

    @XmlType(name = "GridHashMapXml")
    public static class HashMapXml {
        public final List<EntryXml> GridTile = new ArrayList<EntryXml>();
    }

    @XmlType(name = "GridEntryXml")
    public static class EntryXml {
        @XmlElement(name = "GridLocation")
        @XmlJavaTypeAdapter(PointXmlAdapter.class)
        public Coordinate point;
        @XmlElement(name = "Tile")
        public Tile tile;
        @XmlElement(name = "ParentPolyTile")
        public PolyTile parent;
    }
}

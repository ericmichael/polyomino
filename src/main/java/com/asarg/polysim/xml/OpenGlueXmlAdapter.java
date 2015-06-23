package com.asarg.polysim.xml;

import com.asarg.polysim.models.base.Coordinate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenGlueXmlAdapter extends XmlAdapter<OpenGlueXmlAdapter.HashMapXml, HashMap<Coordinate, String>> {

    @Override
    public HashMap<Coordinate, String> unmarshal(HashMapXml v) throws Exception {
        HashMap<Coordinate, String> hashMap = new HashMap<Coordinate, String>();
        for (EntryXml entry : v.Glue) {
            hashMap.put(entry.point, entry.glue);
        }
        return hashMap;
    }

    @Override
    public HashMapXml marshal(HashMap<Coordinate, String> v) throws Exception {
        HashMapXml hashMapXml = new HashMapXml();
        for (Map.Entry<Coordinate, String> entry : v.entrySet()) {
            EntryXml entryXml = new EntryXml();
            entryXml.point = entry.getKey();
            entryXml.glue = entry.getValue();
            hashMapXml.Glue.add(entryXml);
        }
        return hashMapXml;
    }

    @XmlType(name = "OpenGlueHashMapXml")
    public static class HashMapXml {
        public List<EntryXml> Glue = new ArrayList<EntryXml>();
    }

    @XmlType(name = "OpenGlueEntryXml")
    public static class EntryXml {
        @XmlElement(name = "Location")
        @XmlJavaTypeAdapter(PointXmlAdapter.class)
        public Coordinate point;
        @XmlElement(name = "GlueLabel")
        public String glue;
    }
}

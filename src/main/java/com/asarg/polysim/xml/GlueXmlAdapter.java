package com.asarg.polysim.xml;

import javafx.util.Pair;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlueXmlAdapter extends XmlAdapter<GlueXmlAdapter.HashMapXml, HashMap<Pair<String, String>, Integer>> {

    @Override
    public HashMap<Pair<String, String>, Integer> unmarshal(HashMapXml v) throws Exception {
        HashMap<Pair<String, String>, Integer> hashMap = new HashMap<Pair<String, String>, Integer>();
        for (EntryXml entry : v.Function) {
            hashMap.put(entry.labels, entry.strength);
        }
        return hashMap;
    }

    @Override
    public HashMapXml marshal(HashMap<Pair<String, String>, Integer> v) throws Exception {
        HashMapXml hashMapXml = new HashMapXml();
        for (Map.Entry<Pair<String, String>, Integer> entry : v.entrySet()) {
            EntryXml entryXml = new EntryXml();
            entryXml.labels = entry.getKey();
            entryXml.strength = entry.getValue();
            hashMapXml.Function.add(entryXml);
        }
        return hashMapXml;
    }

    public static class HashMapXml {
        public final List<EntryXml> Function = new ArrayList<EntryXml>();
    }

    public static class EntryXml {
        @XmlElement(name = "Labels")
        @XmlJavaTypeAdapter(PairXmlAdapter.class)
        public Pair<String, String> labels;
        @XmlElement(name = "Strength")
        public Integer strength;
    }
}

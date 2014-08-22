package com.asarg.polysim.xml;

import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PairXmlAdapter extends XmlAdapter<PairXmlAdapter.PairXml, Pair<String, String>> {

    @Override
    public Pair<String, String> unmarshal(PairXml v) throws Exception {
        Pair<String, String> pair;
        pair = new Pair<String, String>(v.label1, v.label2);
        return pair;
    }

    @Override
    public PairXml marshal(Pair<String, String> v) throws Exception {
        PairXml pairXml = new PairXml();
        pairXml.label1 = v.getKey();
        pairXml.label2 = v.getValue();
        return pairXml;
    }

    public static class PairXml {
        @XmlAttribute(name = "L1")
        public String label1;
        @XmlAttribute(name = "L2")
        public String label2;
    }
}

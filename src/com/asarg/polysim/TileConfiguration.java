package com.asarg.polysim;

import com.asarg.polysim.xml.GlueXmlAdapter;
import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "TileConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileConfiguration {

    //Glue Function stores the strength of attachment between two labels
    @XmlElement(name = "GlueFunction")
    @XmlJavaTypeAdapter(GlueXmlAdapter.class)
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap<Pair<String, String>, Integer>();

    //Tile Types is the list of PolyTiles
    @XmlElement(name = "TileType")
    private Set<PolyTile> tileTypes = new HashSet<PolyTile>();

    public TileConfiguration() { }

    public void addGlueFunction(String l1, String l2, Integer s) {
        glueFunction.put(new Pair<String, String>(l1, l2), s);
    }

    public void addTileType(PolyTile p) {
        tileTypes.add(p);
    }

    public HashMap<Pair<String, String>, Integer> getGlueFunction() { return glueFunction; }
    public Set<PolyTile> getTiletypes() { return tileTypes; }
}

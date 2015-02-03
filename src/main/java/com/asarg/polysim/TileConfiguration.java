package com.asarg.polysim;

import com.asarg.polysim.xml.GlueXmlAdapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement(name = "TileConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileConfiguration {

    //Glue Function stores the strength of attachment between two labels
    @XmlElement(name = "GlueFunction")
    @XmlJavaTypeAdapter(GlueXmlAdapter.class)
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap<Pair<String, String>, Integer>();

    //Tile Types is the list of PolyTiles
    private List<PolyTile> tileTypes = new ArrayList<PolyTile>();
    @XmlElement(name = "TileType")
    private ObservableList<PolyTile> observableTileTypes = FXCollections.observableArrayList(tileTypes);

    public TileConfiguration() {
    }

    public void addGlueFunction(String l1, String l2, Integer s) {
        glueFunction.put(new Pair<String, String>(l1, l2), s);
    }

    public void addTileType(PolyTile p) {
        if (!observableTileTypes.contains(p))
            observableTileTypes.add(p);
    }

    public HashMap<Pair<String, String>, Integer> getGlueFunction() {
        return glueFunction;
    }

    public ObservableList<PolyTile> getTiletypes() {
        return observableTileTypes;
    }
}

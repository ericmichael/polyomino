package com.asarg.polysim.models.base;/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */

import com.asarg.polysim.xml.GlueXmlAdapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "TileSystem")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileSystem {
    public static final int UNIFORMDISTRIBUTION = 0;
    public static final int CONCENTRATION = 1;
    public static final int COUNT = 2;
    // list of polytiles: data structure should be changed to something that would be of better performance
    private final List<PolyTile> tileTypes = new ArrayList<PolyTile>();
    @XmlElement(name = "TileType")
    private final ObservableList<PolyTile> observableTileTypes = FXCollections.observableArrayList(tileTypes);
    // temperature of the system, bonds must be of at least this value or they break.
    @XmlAttribute(name = "Temperature")
    private int temperature;
    // glue function to determine strength between two labels
    @XmlElement(name = "GlueFunction")
    @XmlJavaTypeAdapter(GlueXmlAdapter.class)
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap<Pair<String, String>, Integer>();
    // used to set weight option: 0 = none (assumed equal concentrations), 1 = concentration, 2 = tile count
    @XmlAttribute(name = "WeightingOption")
    private int weightOption;
    // total count of all tiles in tile system; used for count-based attachment
    @XmlTransient
    private int totalCount = 0;

    public TileSystem() {
    }

    public TileSystem(int temp) {
        temperature = temp;
        weightOption = UNIFORMDISTRIBUTION;
    }

    public TileSystem(int temp, int wO) {
        temperature = temp;
        weightOption = wO;
    }

    public void addGlueFunction(String label1, String label2, int temp) {
        glueFunction.put(new Pair<String, String>(label1, label2), temp);
        if (!label1.equals(label2)) {
            glueFunction.put(new Pair<String, String>(label2, label1), temp);
        }
    }

    public void removeGlueFunction(String label1, String label2) {
        glueFunction.remove(new Pair<String, String>(label1, label2));
        if (!label1.equals(label2)) {
            glueFunction.remove(new Pair<String, String>(label2, label1));
        }
    }

    public HashMap<Pair<String, String>, Integer> getGlueFunction() {
        return glueFunction;
    }

    // needs some work, it's not maintaining the new glue values further down the process.
    public void setGlueFunction(HashMap<Pair<String, String>, Integer> newGlueFunction) {
        System.out.println("Changing glue function");
        glueFunction.clear();
        for (Map.Entry<Pair<String, String>, Integer> glF : newGlueFunction.entrySet()) {
//            System.out.println(glF.getKey().getKey() + " " + glF.getKey().getValue()+" "+ glF.getValue()+"added.");
//            glueFunction.put( new Pair(glF.getKey().getKey(), glF.getKey().getValue()), glF.getValue() );
        }
    }

    public int getStrength(String label1, String label2) {
        Pair key1 = new Pair<String, String>(label1, label2);
        Pair key2 = new Pair<String, String>(label2, label1);

        try {
            if (glueFunction.containsKey(key1)) {
                return glueFunction.get(key1);
            } else if (glueFunction.containsKey(key2)) {
                return glueFunction.get(key2);
            } else {
                return 0;
            }
        } catch (NullPointerException npe) {
            return 0;
        }
    }

    // add polytile to tiletypes
    public void addPolyTile(PolyTile p) throws IllegalStateException {
        // check that the polytile to be added fits in with the weight model.
        // if equal concentration, nothing needs to be done, as all tiles will be assumed to be of equal
        // concentration.

        if (weightOption == CONCENTRATION) {
            if (p.getConcentration() > -1)
                if (!observableTileTypes.contains(p))
                    observableTileTypes.add(p);
                else {
                    throw new IllegalStateException("polytile does not fit weight model, " +
                            "You must set a concentration for it.");
                }
        } else if (weightOption == COUNT) {
            if (p.getCount() > -1) {
                if (!observableTileTypes.contains(p)) {
                    observableTileTypes.add(p);
                    totalCount += p.getCount();
                }
            } else {
                throw new IllegalStateException("polytile does not fit weight model, " +
                        "You must set a concentration for it.");
            }
        } else {
            if (!observableTileTypes.contains(p))
                observableTileTypes.add(p);
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int s) {
        temperature = s;
    }

    public int getWeightOption() {
        return weightOption;
    }

    public void setWeightOption(int x) throws InvalidObjectException {
        // when the weight option is changed, a check should be made on all polytiles and see that they all
        //  have the required variable set. Otherwise, pop up an error telling the user to fix their variables.
        if (x == CONCENTRATION) {
            for (PolyTile p : observableTileTypes) {
                if (p.getConcentration() < 0) {
                    // TODO: we can either change the value to a default or return all polytiles that do not match
                    //  the weight option.
                    throw new InvalidObjectException("Polytile " + p.getPolyName() + " does not have a set concentration.");
                }
            }
        } else if (x == COUNT) {
            for (PolyTile p : observableTileTypes) {
                if (p.getCount() < 0) {
                    // TODO: we can either change the value to a default or return all polytiles that do not match
                    //  the weight option.
                    throw new InvalidObjectException("Polytile " + p.getPolyName() + " does not have a set count.");
                }
            }
        }
        weightOption = x;
    }

    // loops over the polytiles in the system, reads their concentration and count, and gets
    // their total.
    public int getTotalCount() {
        int tCount = 0;
        for (PolyTile p : observableTileTypes) {
            tCount += p.getCount();
        }
        totalCount = tCount;
        return totalCount;
    }

    public double getTotalConcentration() {
        double tConc = 0;
        for (PolyTile p : observableTileTypes) {
            tConc += p.getConcentration();
        }
        double totalConcentration = tConc;
        return totalConcentration;
    }

    public ObservableList<PolyTile> getTileTypes() {
        return observableTileTypes;
    }

    public boolean loadTileConfiguration(TileConfiguration t) {
        System.out.println("Option in load tile config " + weightOption);
        if (t == null)
            return false;
        else if (t.getGlueFunction() == null || t.getTiletypes() == null)
            return false;

        glueFunction = t.getGlueFunction();

        observableTileTypes.addAll(t.getTiletypes());

        System.out.println("Size in load tile config " + observableTileTypes.size());

        for (PolyTile p : observableTileTypes) {
            p.setGlues();
        }
        System.out.println("Size after loading tile config " + observableTileTypes.size());

        return true;
    }

    public TileConfiguration getTileConfiguration() {
        TileConfiguration tileConfig = new TileConfiguration();
        for (PolyTile polyTile : observableTileTypes) {
            tileConfig.addTileType(polyTile);
        }

        for (Map.Entry<Pair<String, String>, Integer> glF : glueFunction.entrySet()) {
            String gLabelL = glF.getKey().getKey();
            String gLabelR = glF.getKey().getValue();
            int strength = glF.getValue();
            tileConfig.addGlueFunction(gLabelL, gLabelR, strength);
        }
        return tileConfig;
    }

    public void printDebugInformation() {
        System.out.println("--- Debug Information for TS----");
        System.out.println("Temperature: " + temperature);
        System.out.println("Weight Option: " + weightOption);
        System.out.println("Polytile Types Size: " + observableTileTypes.size());
        System.out.println("Glue Function Size:  " + glueFunction.size());
        System.out.println("--------------------------");
    }
}
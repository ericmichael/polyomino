package com.asarg.polysim;/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */

import java.util.Set;
import java.util.HashSet;
import javafx.util.Pair;
import java.util.HashMap;

public class TileSystem {
    // temperature of the system, bonds must be of at least this value or they break.
    private int temperature;
    // glue function to determine strength between two labels
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap();
    // list of polytiles: data structure should be changed to something that would be of better performance
    private Set<PolyTile> tileTypes = new HashSet<PolyTile>();
    // used to set weight option: 0 = none (assumed equal concentrations), 1 = concentration, 2 = tile count
    private int weightOption;
    // total count of all tiles in tile system; used for count-based attachment
    private int totalCount = 0;

    public TileSystem(int temp){ temperature = temp; weightOption = 0; }

    public TileSystem(int temp, int wO){ temperature = temp; weightOption = wO; }

    public void addGlueFunction(String label1, String label2, int temp) {
        glueFunction.put(new Pair(label1, label2), temp);
        if(!label1.equals(label2)) {
            glueFunction.put(new Pair(label2, label1), temp);
        }
    }

    public void removeGlueFunction(String label1, String label2) {
        glueFunction.remove(new Pair(label1, label2));
        if(!label1.equals(label2)) {
            glueFunction.remove(new Pair(label2, label1));
        }
    }

    public int getStrength(String label1, String label2) {
        Pair key = new Pair(label1, label2);

        if(glueFunction.containsKey(key)){
            return glueFunction.get(key);
        }
        else return 0;
    }

    // add polytile to tiletypes
    public void addPolyTile(PolyTile p){
        tileTypes.add(p);
    }

    public int getTemperature(){
        return temperature;
    }
    public void setTemperature(int s){
        temperature = s;
    }

    public int getWeightOption() { return weightOption; }
    public void setWeightOption(int x) { weightOption = x; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount( int x ) { totalCount = x; }

    public Set<PolyTile> getTileTypes() {
        return tileTypes;
    }
}

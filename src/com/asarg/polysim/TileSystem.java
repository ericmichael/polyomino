package com.asarg.polysim;/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */

import java.util.Set;
import java.util.HashSet;
import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement(name = "TileSystem")
public class TileSystem {
    // temperature of the system, bonds must be of at least this value or they break.
    private int temperature;
    // glue function to determine strength between two labels
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap();
    // list of polytiles: data structure should be changed to something that would be of better performance
    private Set<PolyTile> tileTypes = new HashSet<PolyTile>();

    public TileSystem() {

    }

    public TileSystem(int temp){
        temperature = temp;
    }

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

    @XmlAttribute(name = "Temperature")
    public int getTemperature(){
        return temperature;
    }
    public void setTemperature(int s){
        temperature = s;
    }

    @XmlElement(name = "TileType")
    public Set<PolyTile> getTileTypes() {
        return tileTypes;
    }


}

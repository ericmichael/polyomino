package com.asarg.polysim;/*
Tile system is meant to be the container where all different types of polytiles are.
 should be able to create polytiles and keep a list of all polytiles in the system.
 Glue function is also called from here.
 */

import java.io.InvalidObjectException;
import java.util.Set;
import java.util.HashSet;
import javafx.util.Pair;
import javax.xml.bind.annotation.*;
import java.util.HashMap;

@XmlRootElement(name = "TileSystem")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileSystem {
    public static final int UNIFORMDISTRIBUTION = 0;
    public static final int CONCENTRATION = 1;
    public static final int COUNT = 2;

    // temperature of the system, bonds must be of at least this value or they break.
    //@XmlElement(name = "Temperature")
    @XmlAttribute(name = "Temperature")
    private int temperature;
    // glue function to determine strength between two labels
    @XmlTransient
    private HashMap<Pair<String, String>, Integer> glueFunction = new HashMap<Pair<String, String>, Integer>();
    // list of polytiles: data structure should be changed to something that would be of better performance
    @XmlTransient
    private Set<PolyTile> tileTypes = new HashSet<PolyTile>();
    // used to set weight option: 0 = none (assumed equal concentrations), 1 = concentration, 2 = tile count
    @XmlAttribute(name = "WeightingOption")
    private int weightOption;
    // total count of all tiles in tile system; used for count-based attachment
    @XmlTransient
    private int totalCount = 0;

    public TileSystem() { }

    public TileSystem(int temp){ temperature = temp; weightOption = UNIFORMDISTRIBUTION; }

    public TileSystem(int temp, int wO){ temperature = temp; weightOption = wO; }

    public void addGlueFunction(String label1, String label2, int temp) {
        glueFunction.put(new Pair<String, String>(label1, label2), temp);
        if(!label1.equals(label2)) {
            glueFunction.put(new Pair<String, String>(label2, label1), temp);
        }
    }

    public void removeGlueFunction(String label1, String label2) {
        glueFunction.remove(new Pair<String, String>(label1, label2));
        if(!label1.equals(label2)) {
            glueFunction.remove(new Pair<String, String>(label2, label1));
        }
    }

    public int getStrength(String label1, String label2) {
        Pair key = new Pair<String, String>(label1, label2);

        if(glueFunction.containsKey(key)){
            return glueFunction.get(key);
        }
        else return 0;
    }

    // add polytile to tiletypes
    public void addPolyTile(PolyTile p) throws IllegalStateException{
        // check that the polytile to be added fits in with the weight model.
            // if equal concentration, nothing needs to be done, as all tiles will be assumed to be of equal
            // concentration.
  
        if ( weightOption == CONCENTRATION ){
            if ( p.getConcentration() > -1)
                tileTypes.add(p);
            else {
                throw new IllegalStateException("polytile does not fit weight model, " +
                        "You must set a concentration for it.");
            }
        }
        else if (weightOption == COUNT){
            if (p.getCount() > -1) {
                tileTypes.add(p);
                totalCount += p.getCount();
            }
            else {
                throw new IllegalStateException("polytile does not fit weight model, " +
                        "You must set a concentration for it.");
            }
        }

        else
            tileTypes.add(p);
    }

    public int getTemperature(){
        return temperature;
    }
    public void setTemperature(int s){
        temperature = s;
    }

    public int getWeightOption() { return weightOption; }
    public void setWeightOption(int x) throws InvalidObjectException{
        // when the weight option is changed, a check should be made on all polytiles and see that they all
        //  have the required variable set. Otherwise, pop up an error telling the user to fix their variables.
        if ( x == CONCENTRATION) {
            for (PolyTile p : tileTypes){
                if ( p.getConcentration() < 0) {
                    // TODO: we can either change the value to a default or return all polytiles that do not match
                    //  the weight option.
                    throw new InvalidObjectException("Polytile " + p.getPolyName() + " does not have a set concentration.");
                }
            }
        }
        else if ( x == COUNT) {
            for (PolyTile p : tileTypes){
                if ( p.getCount() < 0) {
                    // TODO: we can either change the value to a default or return all polytiles that do not match
                    //  the weight option.
                    throw new InvalidObjectException("Polytile " + p.getPolyName() + " does not have a set count.");
                }
            }
        }
        weightOption = x;
    }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount( int x ) { totalCount = x; }

    public Set<PolyTile> getTileTypes() {
        return tileTypes;
    }

    public boolean loadTileConfiguration(TileConfiguration t) {
        if(t == null)
            return false;
        else if(t.getGlueFunction() == null || t.getTiletypes() == null)
            return false;

        glueFunction = t.getGlueFunction();
        tileTypes = t.getTiletypes();
        return true;
    }
}
package com.asarg.polysim.models.StagedTwoHAM;

import java.util.ArrayList;

/**
 * Created by ericmartinez on 6/27/15.
 */
public class Stage{
    public final int num;
    public Stage(int i){
        num = i;
    }
    final ArrayList<Bin> bins = new ArrayList<Bin>();

    public void addBin(Bin bin){bins.add(bin); bin.stage=this;}

    public Bin getBin(int i){return bins.get(i);}

    public int size(){return bins.size();}

    public void start(){
        int i = 0;
        for(Bin bin : bins){
            bin.start();
        }
    }
}


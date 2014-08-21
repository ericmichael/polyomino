package com.asarg.polysim;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ctchalk on 8/21/2014.
 */
public class Frontier extends ArrayList<FrontierElement> {
    private int totalConcentration;
    private TileSystem tileSystem;

    Frontier( TileSystem parent ) {
        totalConcentration = 0;
        tileSystem = parent;
    }

    ;

    @Override
    public boolean add(FrontierElement frontierElement) {
        if( tileSystem.getWeightOption() == 1 )
            totalConcentration += frontierElement.getPolyTile().getConcentration();
        else if( tileSystem.getWeightOption() == 2 )
            totalConcentration += frontierElement.getPolyTile().getCount()/tileSystem.getTotalCount();
        return super.add(frontierElement);
    }

    @Override
    public boolean remove(Object o) {
        FrontierElement frontierElement = (FrontierElement) o;
        if( tileSystem.getWeightOption() == 1 )
            totalConcentration -= frontierElement.getPolyTile().getConcentration();
        else if( tileSystem.getWeightOption() == 2 )
            totalConcentration -= frontierElement.getPolyTile().getCount()/tileSystem.getTotalCount();
        return super.remove(o);
    }

    @Override
    public void clear() {
        totalConcentration = 0;
        super.clear();
    }

    public int randomSelect(){
        if( tileSystem.getWeightOption() == 1 )
            return concentrationSelectHelper();
        else if( tileSystem.getWeightOption() == 2 )
            return countSelectHelper();
        return selectHelper();
    }

    private int concentrationSelectHelper() {
        //generate cumulative density list
        ArrayList<Double> cdList = new ArrayList();
        double totalConcentration = 0.0;
        for (FrontierElement p : this) {
            PolyTile pt = p.getPolyTile();
            totalConcentration += pt.getConcentration();
            cdList.add(totalConcentration);
        }
        Random rn = new Random();
        double x = rn.nextDouble() * totalConcentration;
        //Binary search for random number in cdList
        return weightedBinarySearchHelper(cdList, x);
    }

    private int countSelectHelper(){
        //generate cumulative density list
        ArrayList<Double> cdList = new ArrayList();
        double totalConcentration = 0.0;
        for (FrontierElement p : this) {
            PolyTile pt = p.getPolyTile();
            totalConcentration += pt.getCount()/tileSystem.getTotalCount();
            cdList.add(totalConcentration);
        }
        Random rn = new Random();
        double x = rn.nextDouble() * totalConcentration;
        //Binary search for random number in cdList
        return weightedBinarySearchHelper(cdList, x);
    }

    private int selectHelper(){
        Random rn = new Random();
        return rn.nextInt(this.size());
    }

    private static int weightedBinarySearchHelper(ArrayList<Double> cdList, double x) {
        int lo = 0;
        int hi = cdList.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (mid == 0)
                return mid;
            if (x < cdList.get(mid) && x >= cdList.get(mid - 1))
                return mid;
            else if (x < cdList.get(mid))
                hi = mid - 1;
            else if (x > cdList.get(mid))
                lo = mid + 1;
        }
        return -1;
    }

    private double getProbability( FrontierElement frontierElement ){
        if( tileSystem.getWeightOption() == 1 )
            return frontierElement.getPolyTile().getConcentration()/totalConcentration;
        else if( tileSystem.getWeightOption() == 2 )
            return ( frontierElement.getPolyTile().getCount()/tileSystem.getTotalCount() ) / totalConcentration;
        return 1/size();
    }
}

package com.asarg.polysim.models.base;

import java.util.ArrayList;
import java.util.Random;

public class Frontier extends ArrayList<FrontierElement> {
    private TileSystem tileSystem;

    public Frontier(TileSystem parent) {
        super();
        tileSystem = parent;
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

    public void changeTileSystem(TileSystem newts) {
        tileSystem = newts;
    }

    @Override
    public boolean add(FrontierElement frontierElement) {
        return super.add(frontierElement);
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public void clear() {
        super.clear();
    }

    public int randomSelect() {
        if (tileSystem.getWeightOption() == TileSystem.CONCENTRATION)
            return concentrationSelectHelper();
        else if (tileSystem.getWeightOption() == TileSystem.COUNT)
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

    private int countSelectHelper() {
        //generate cumulative density list
        ArrayList<Double> cdList = new ArrayList();
        double totalConcentration = 0.0;
        for (FrontierElement p : this) {
            PolyTile pt = p.getPolyTile();
            totalConcentration += pt.getCount() / tileSystem.getTotalCount();
            cdList.add(totalConcentration);
        }
        Random rn = new Random();
        double x = rn.nextDouble() * totalConcentration;
        //Binary search for random number in cdList
        return weightedBinarySearchHelper(cdList, x);
    }

    private int selectHelper() {
        Random rn = new Random();
        return rn.nextInt(this.size());
    }

    public double getProbability(FrontierElement frontierElement) {
        if (tileSystem.getWeightOption() == TileSystem.CONCENTRATION)
            return frontierElement.getPolyTile().getConcentration() / getTotalConcentration();
        else if (tileSystem.getWeightOption() == TileSystem.COUNT)
            return (frontierElement.getPolyTile().getCount() / tileSystem.getTotalCount()) / getTotalConcentration();
        return 1.0 / size();
    }

    // Returns the sum of all polytile concentrations in the frontier.
    //  It should be calculated when asked, not saved in the class.
    //  value will be used to calculate the time it took for a single attachment to happen.
    public double getTotalConcentration() {
        double totalConcentration = 0;

        if (tileSystem.getWeightOption() == TileSystem.UNIFORMDISTRIBUTION) {
            int polytile_count = tileSystem.getTileTypes().size();
            double uniform_concentration = 1.0 / polytile_count;
            totalConcentration = this.size() * uniform_concentration;
        } else {
            for (FrontierElement fe : this) {
                totalConcentration += fe.getPolyTile().getConcentration();
            }
        }
        return totalConcentration;
    }

    public void printDebugInformation() {
        System.out.println("### Frontier Information");
        System.out.println("Frontier Size: " + this.size());
        this.tileSystem.printDebugInformation();
    }
}
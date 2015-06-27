package com.asarg.polysim.examples;

import com.asarg.polysim.models.StagedTwoHAM.Bin;
import com.asarg.polysim.models.StagedTwoHAM.Stage;
import com.asarg.polysim.models.atam.ATAMTile;
import com.asarg.polysim.models.base.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ericmartinez on 6/16/15.
 */



public class GeometricAlgorithms{

    public static void main(String args[]){
        int number = 16;
        int logn = 5;
        ArrayList<Stage> stages = new ArrayList<Stage>(5);
        for(int i = 0; i < logn; i++){
            System.out.println("Creating Stage: " + i);
            Stage stage = new Stage(i+1);
            stages.add(stage);

            HashMap<Pair<String,String>, Integer> glueFunction = new HashMap<Pair<String, String>, Integer>();
            glueFunction.put(new Pair<String, String>("a", "a"), 1);
            glueFunction.put(new Pair<String, String>("b", "b"), 1);
            glueFunction.put(new Pair<String, String>("c", "c"), 1);

            Bin b1 = new Bin(1,1);
            Bin b2 = new Bin(2,1);
            Bin b3 = new Bin(3,1);
            Bin b4 = new Bin(4,1);
            Bin b5 = new Bin(5,1);
            Bin b6 = new Bin(6,1);

            stage.addBin(b1);
            stage.addBin(b2);
            stage.addBin(b3);
            stage.addBin(b4);
            stage.addBin(b5);
            stage.addBin(b6);

            if(i==0){
                ATAMTile pt1 = new ATAMTile("");
                ATAMTile pt2 = new ATAMTile("");
                ATAMTile pt3 = new ATAMTile("");
                ATAMTile pt4 = new ATAMTile("");
                ATAMTile pt5 = new ATAMTile("");
                ATAMTile pt6 = new ATAMTile("");
                pt1.setGlues("","c","","a");
                pt2.setGlues("","b","","c");
                pt3.setGlues("","a","","b");
                pt4.setGlues("","a","","c");
                pt5.setGlues("","c","","b");
                pt6.setGlues("","b","","a");

                b1.addTile(pt1);
                b2.addTile(pt2);
                b3.addTile(pt3);
                b4.addTile(pt4);
                b5.addTile(pt5);
                b6.addTile(pt6);

                b1.addGlueFunction(glueFunction);
                b2.addGlueFunction(glueFunction);
                b3.addGlueFunction(glueFunction);
                b4.addGlueFunction(glueFunction);
                b5.addGlueFunction(glueFunction);
                b6.addGlueFunction(glueFunction);
            }else{
                Stage previousStage = stages.get(i-1);
                Bin bb1 = previousStage.getBin(0);
                Bin bb2 = previousStage.getBin(1);
                Bin bb3 = previousStage.getBin(2);
                Bin bb4 = previousStage.getBin(3);
                Bin bb5 = previousStage.getBin(4);
                Bin bb6 = previousStage.getBin(5);

                bb5.addEdge(b1);
                bb6.addEdge(b1);

                bb4.addEdge(b2);
                bb6.addEdge(b2);

                bb5.addEdge(b3);
                bb4.addEdge(b3);

                bb2.addEdge(b4);
                bb3.addEdge(b4);

                bb3.addEdge(b5);
                bb1.addEdge(b5);

                bb1.addEdge(b6);
                bb2.addEdge(b6);
            }
        }
        for(int i = 0; i < stages.size(); i ++) {
            Stage stage = stages.get(i);
            stage.start();


            for (int k = 0; k < stage.size(); k++) {

                int stagenum=i+1;
                int binnum = k+1;
                System.out.println("Writing Terminal Tiles for Stage: " + stagenum + " Bin: " + binnum);

                Bin bin = stage.getBin(k);

                writeTerminalTileSet(bin, "/Users/ericmartinez/stage_"+stagenum+"_bin_" + binnum + ".xml");
            }
        }
    }

    public static void writeTileConfig(Bin bin, String filename){

        try {
            TileConfiguration tc = new TileConfiguration();

            for (PolyTile pt : bin.tileSystem.getTileTypes())
                tc.addTileType(pt);

            tc.getGlueFunction().putAll(bin.tileSystem.getGlueFunction());
            File f = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tc, f);
        } catch (JAXBException jxb) {
            jxb.printStackTrace();
        }
    }

    public static void writeTerminalTileSet(Bin bin, String filename){

        try {
            TileConfiguration tc = new TileConfiguration();

            for (PolyTile pt : bin.getTerminalSet())
                tc.addTileType(pt);

            tc.getGlueFunction().putAll(bin.tileSystem.getGlueFunction());
            File f = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(TileConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tc, f);
        } catch (JAXBException jxb) {
            jxb.printStackTrace();
        }
    }
}

package com.asarg.polysim.examples;

import com.asarg.polysim.models.TwoHAM.TwoHAMAssembly;
import com.asarg.polysim.models.atam.ATAMTile;
import com.asarg.polysim.models.base.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericmartinez on 6/16/15.
 */
class Bin extends TwoHAMAssembly{
    public Stage stage;
    ArrayList<Bin> edges = new ArrayList<Bin>();
    public int i;

    public Bin(int i){
        super();
        this.i = i;
    }

    public Bin(int i, int temperature){
        super(new TileSystem(temperature));
        this.i=i;
    }

    void setStage(Stage stage){
        this.stage = stage;
    }

    void addEdge(Bin b){ edges.add(b); }

    void addTile(PolyTile pt){ tileSystem.getTileTypes().add(pt); }

    void addTiles(ArrayList<PolyTile> polyTileArrayList){ tileSystem.getTileTypes().addAll(polyTileArrayList); }

    void addGlueFunction(HashMap<Pair<String, String>, Integer> glueFunction){
        for(Map.Entry<Pair<String, String>, Integer> entry : glueFunction.entrySet()){
            Pair<String, String> glues = entry.getKey();
            Integer strength = entry.getValue();
            tileSystem.addGlueFunction(glues.getKey(), glues.getValue(), strength);
        }
    }

    void start(){
        System.out.println("Starting with " + this.tileSystem.getTileTypes().size() + " tile types");
        //pass info to connected bins
        for(Bin bin : edges){
            int max = 0;
            for(PolyTile pt : getTerminalSet()) if(pt.getTiles().size() > max) max=pt.getTiles().size();

            int min = getTerminalSet().get(0).getTiles().size();
            for(PolyTile pt : getTerminalSet()) if(pt.getTiles().size() < min) min=pt.getTiles().size();

            System.out.println("Passing from Stage " + stage.num + " Bin " + i + " to Stage " + bin.stage.num + " Bin " + bin.i);
            System.out.println("Passing " + getTerminalSet().size() + " tiles");
            System.out.println("Passing down tiles of max size: " + max);
            System.out.println("Passing down tiles of min size: " + min);

            if(getTerminalSet().size()==2){
                int i = 0;
                for(PolyTile pt : getTerminalSet()){
                    Assembly ptAsm = polyTileToAssembly(pt);
                    if(isTerminalAssembly(ptAsm)){
                        PolyTile problem = new PolyTile();
                        problem.getTile(0,0).setGlueW("a");
                        problem.getTile(0,0).setGlueE("c");
                        String glues[] = {"", "b", "", "c"};
                        problem.addTile(1,0, glues);
                        problem.setConcentration(-1);
                        problem.setCount(-1);
                        problem.getTile(1,0).setLabel("New");

                        try {
                            JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
                            Marshaller marshaller = jaxbContext.createMarshaller();
                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                            marshaller.marshal(ptAsm, new File("/Users/ericmartinez/error_asm_" + i + ".xml"));
                        } catch (JAXBException jaxbe) {

                        }

                        if(pt.equals(problem)) {
                            System.out.println("equal");
                        }else{
                            System.out.println("not equal");
                        }
                    }
                    i++;
                }


                System.out.println("crashing and burning");
                GeometricAlgorithms.writeTileConfig(this,"/Users/ericmartinez/error_stage_"+this.stage.num+"_bin_" + this.i + ".xml");
                System.exit(0);
            }

            for(PolyTile pt : getTerminalSet()){
                for(int i = 0; i < 4; i ++){
                    String glue = pt.getTiles().get(0).getGlueLabels()[i];
                    if(glue!=null || !glue.trim().isEmpty())
                        System.out.println("" + i + ": " + glue);
                }
            }
            bin.addTiles(getTerminalSet());
            bin.addGlueFunction(tileSystem.getGlueFunction());


        }
    }
}

class Stage{
    public int num;
    public Stage(int i){
        num = i;
    }
    ArrayList<Bin> bins = new ArrayList<Bin>();

    void addBin(Bin bin){bins.add(bin); bin.stage=this;}

    Bin getBin(int i){return bins.get(i);}

    int size(){return bins.size();}

    void start(){
        int i = 0;
        for(Bin bin : bins){
            bin.start();
        }
    }
}


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

                writeTileConfig(bin, "/Users/ericmartinez/stage_"+stagenum+"_bin_" + binnum + ".xml");
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
}

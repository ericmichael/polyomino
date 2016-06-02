package com.asarg.polysim.utils;

import com.asarg.polysim.models.base.*;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Eric Martinez on 6/1/2016.
 */
public class Utilities {
    private static Pair<TileConfiguration, HashMap<Point, PolyTile>> LoadISUTASTileConfiguration(File f, HashMap<Point, String> seeds) throws FileNotFoundException {
        TileConfiguration tc = new TileConfiguration();
        Scanner reader = new Scanner(f);
        HashMap<Point, PolyTile> seedPolyTiles = new HashMap<Point, PolyTile>();

        Tile t = new Tile();
        int northStrength = 0;
        int eastStrength = 0;
        int southStrength = 0;
        int westStrength = 0;
        Color c = null;
        String name = null;

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("LABEL ")) {
                String tokens[] = line.split(" ");
                if (tokens.length > 1) t.setLabel(tokens[1]);
                else t.setLabel("");
            } else if (line.startsWith("TILENAME ")) {
                name = line.split(" ")[1];
            } else if (line.contains("NORTHLABEL ")) {
                t.setGlueN(line.split(" ")[1]);
            } else if (line.contains("EASTLABEL ")) {
                t.setGlueE(line.split(" ")[1]);
            } else if (line.contains("SOUTHLABEL ")) {
                t.setGlueS(line.split(" ")[1]);
            } else if (line.contains("WESTLABEL ")) {
                t.setGlueW(line.split(" ")[1]);
            } else if (line.contains("NORTHBIND ")) {
                northStrength = Integer.parseInt(line.split(" ")[1]);
            } else if (line.contains("EASTBIND ")) {
                eastStrength = Integer.parseInt(line.split(" ")[1]);
            } else if (line.contains("SOUTHBIND ")) {
                southStrength = Integer.parseInt(line.split(" ")[1]);
            } else if (line.contains("WESTBIND ")) {
                westStrength = Integer.parseInt(line.split(" ")[1]);
            } else if (line.contains("TILECOLOR ")) {
                String colorStr = line.split(" ")[1];
                if (colorStr.toLowerCase().contains("rgb(")) {
                    colorStr = colorStr.substring(4, colorStr.length() - 1);
                    String rgbs[] = colorStr.split(",");
                    int red = Integer.parseInt(rgbs[0]);
                    int green = Integer.parseInt(rgbs[1]);
                    int blue = Integer.parseInt(rgbs[2]);
                    c = new Color(red, green, blue);
                } else {
                    javafx.scene.paint.Color c2 = javafx.scene.paint.Color.web(colorStr);
                    c = new Color((int) (c2.getRed() * 255), (int) (c2.getGreen() * 255), (int) (c2.getBlue() * 255));
                }
            } else if (line.contains("CREATE")) {
                if (t.getGlueN() != null) {
                    t.setGlueN(t.getGlueN() + northStrength);
                    tc.addGlueFunction(t.getGlueN(), t.getGlueN(), northStrength);
                }
                if (t.getGlueE() != null) {
                    t.setGlueE(t.getGlueE() + eastStrength);
                    tc.addGlueFunction(t.getGlueE(), t.getGlueE(), eastStrength);
                }
                if (t.getGlueS() != null) {
                    t.setGlueS(t.getGlueS() + southStrength);
                    tc.addGlueFunction(t.getGlueS(), t.getGlueS(), southStrength);
                }
                if (t.getGlueW() != null) {
                    t.setGlueW(t.getGlueW() + westStrength);
                    tc.addGlueFunction(t.getGlueW(), t.getGlueW(), westStrength);
                }
                PolyTile pt;
                pt = new PolyTile(t.getLabel());

                pt.addTile(0, 0, t.getGlueLabels());
                if (c != null) {
                    try {
                        String rgb = Integer.toHexString(c.getRGB());
                        rgb = rgb.substring(2, rgb.length());
                        pt.setColor(rgb);
                    } catch (NullPointerException ignored) {
                    }
                }
                for (Map.Entry<Point, String> entry : seeds.entrySet()) {
                    if (entry.getValue().equals(name)) {
                        seedPolyTiles.put(entry.getKey(), pt);
                    }
                }
                tc.addTileType(pt);
                t = new Tile();
                northStrength = 0;
                eastStrength = 0;
                southStrength = 0;
                westStrength = 0;
                c = null;
            }
        }
        return new Pair<TileConfiguration, HashMap<Point, PolyTile>>(tc, seedPolyTiles);
    }

    private static Assembly LoadISUTASAssembly(File f) throws FileNotFoundException {
        /*
            Load ISU TAS
            Reverse Engineered from File Format
            Code was never looked at such as to not inherit GPL
            */

        String tileSetFile = null;
        String mode = "atam";
        int temperature = 2;
        HashMap<Point, String> seeds = new HashMap<Point, String>();
        HashMap<Point, PolyTile> seedsPt = new HashMap<Point, PolyTile>();


        boolean three_dimensional = false;

        Scanner reader = new Scanner(f);

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String lowerline = line.toLowerCase();

            if (line.contains(".")) tileSetFile = line;
            else if (lowerline.contains("mode="))
                mode = lowerline.split("mode=")[1];
            else if (lowerline.contains("temperature="))
                temperature = Integer.parseInt(lowerline.split("temperature=")[1]);
            else if (line.split(" ").length >= 3) {
                String tokens[] = line.split(" ");
                String tileLabel = tokens[0];
                int x = Integer.parseInt(tokens[1]);
                int y = Integer.parseInt(tokens[2]);
                int z = tokens.length > 3 ? Integer.parseInt(tokens[3]) : 0;
                if (z != 0) three_dimensional = true;
                seeds.put(new Point(x, y), tileLabel);
            }
        }

        TileConfiguration tc = null;
        if (tileSetFile != null) {
            File tileConfig = new File(f.getParent() + "/" + tileSetFile);
            Pair<TileConfiguration, HashMap<Point, PolyTile>> p = LoadISUTASTileConfiguration(tileConfig, seeds);
            tc = p.getKey();
            seedsPt = p.getValue();
        }

        if (mode.equals("atam") && !three_dimensional) {
            TileSystem ts = new TileSystem(temperature);
            if (tc != null) ts.loadTileConfiguration(tc);
            Assembly assembly = new Assembly(ts);
            for (Map.Entry<Point, PolyTile> entry : seedsPt.entrySet()) {
                Point location = entry.getKey();
                PolyTile pt = entry.getValue();

                //check if passes geometry check
                if (assembly.Grid.geometryCheckSuccess(pt, (int) location.getX(), (int) location.getY())) {
                    assembly.Grid.placePolytile(pt, (int) location.getX(), (int) location.getY());
                }

            }
            return assembly;
        } else {
            //cannot use ktam, 2ham, or 3d
            return null;
        }
    }

    public static Assembly LoadAssembly(File f) throws JAXBException, FileNotFoundException {
        String extension = Utilities.getFileExtension(f);
        if (extension.equals("tdp")) {
            Assembly assembly = LoadISUTASAssembly(f);
            assembly.getOpenGlues();
            assembly.calculateFrontier();
            return assembly;
        } else { //load VersaTILE
            JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
            Unmarshaller unmarshaller;
            unmarshaller = jaxbContext.createUnmarshaller();
            Assembly assembly = (Assembly) unmarshaller.unmarshal(f);
            assembly.getOpenGlues();
            assembly.calculateFrontier();
            return assembly;
        }
    }


    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        else return "";
    }
}

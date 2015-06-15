/*
"execution" place. takes care of the tile system, its changes, the grid, the frontier, and the open glue list.
 */
package com.asarg.polysim.models.base;

import com.asarg.polysim.xml.GridXmlAdapter;
import javafx.util.Pair;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

@XmlRootElement(name = "Assembly")
@XmlAccessorType(XmlAccessType.FIELD)
public class Assembly extends Observable {
    static final int NORTH = 0;
    static final int EAST = 1;
    static final int SOUTH = 2;
    static final int WEST = 3;
    // placeholder for the grid
    @XmlElement(name = "AssemblyGrid")
    @XmlJavaTypeAdapter(GridXmlAdapter.class)
    public HashMap<Point, Tile> Grid = new HashMap<Point, Tile>();
    //Open glue ends stored by their coordinate
    @XmlTransient
    HashMap<Point, String> openNorthGlues = new HashMap<Point, String>();
    @XmlTransient
    HashMap<Point, String> openEastGlues = new HashMap<Point, String>();
    @XmlTransient
    HashMap<Point, String> openSouthGlues = new HashMap<Point, String>();
    @XmlTransient
    HashMap<Point, String> openWestGlues = new HashMap<Point, String>();
    @XmlTransient
    ArrayList<FrontierElement> possibleAttach = new ArrayList<FrontierElement>();
    // tile system, it can be changed so it needs its own class
    @XmlElement(name = "TileSystem")
    private TileSystem tileSystem;
    // frontier list: calculated, increased, decreased, and changed here.
    @XmlTransient
    private Frontier frontier;
    @XmlElement(name = "History")
    private History attached = new History();

    public Assembly() {
        System.out.print("in assembly,");
        tileSystem = new TileSystem(2, 0);
        frontier = new Frontier(tileSystem);
    }

    public Assembly(TileSystem ts) {
        tileSystem = ts;
        frontier = new Frontier(tileSystem);
    }

    private static Pair<TileConfiguration,  HashMap<Point, PolyTile>> LoadISUTASTileConfiguration(File f, HashMap<Point, String> seeds) throws FileNotFoundException{
        TileConfiguration tc = new TileConfiguration();
        Scanner reader = new Scanner(f);
        HashMap<Point, PolyTile> seedPolyTiles = new  HashMap<Point, PolyTile>();

        Tile t = new Tile();
        int northStrength = 0;
        int eastStrength = 0;
        int southStrength = 0;
        int westStrength = 0;
        Color c = null;
        String name = null;

        while(reader.hasNextLine()){
            String line = reader.nextLine();
            if(line.startsWith("LABEL ")){
                String tokens[] = line.split(" ");
                if(tokens.length>1) t.setLabel(tokens[1]);
                else t.setLabel("");
            }else if(line.startsWith("TILENAME ")){
                name = line.split(" ")[1];
            }else if(line.contains("NORTHLABEL ")){
                t.setGlueN(line.split(" ")[1]);
            }else if(line.contains("EASTLABEL ")){
                t.setGlueE(line.split(" ")[1]);
            }else if(line.contains("SOUTHLABEL ")){
                t.setGlueS(line.split(" ")[1]);
            }else if(line.contains("WESTLABEL ")){
                t.setGlueW(line.split(" ")[1]);
            }else if(line.contains("NORTHBIND ")){
                northStrength = Integer.parseInt(line.split(" ")[1]);
            }else if(line.contains("EASTBIND ")){
                eastStrength = Integer.parseInt(line.split(" ")[1]);
            }else if(line.contains("SOUTHBIND ")){
                southStrength = Integer.parseInt(line.split(" ")[1]);
            }else if(line.contains("WESTBIND ")){
                westStrength = Integer.parseInt(line.split(" ")[1]);
            }else if(line.contains("TILECOLOR ")){
                String colorStr = line.split(" ")[1];
                if(colorStr.toLowerCase().contains("rgb(")) {
                    colorStr = colorStr.substring(4, colorStr.length() - 1);
                    String rgbs[] = colorStr.split(",");
                    int red = Integer.parseInt(rgbs[0]);
                    int green = Integer.parseInt(rgbs[1]);
                    int blue = Integer.parseInt(rgbs[2]);
                    c = new Color(red, green, blue);
                }else{
                    javafx.scene.paint.Color c2 = javafx.scene.paint.Color.web(colorStr);
                    c = new Color((int) (c2.getRed()*255), (int) (c2.getGreen()*255), (int) (c2.getBlue()*255));
                }
            }
            else if(line.contains("CREATE")){
                if(t.getGlueN()!=null) {
                    t.setGlueN(t.getGlueN()+northStrength);
                    tc.addGlueFunction(t.getGlueN(), t.getGlueN(), northStrength);
                }
                if(t.getGlueE()!=null) {
                    t.setGlueE(t.getGlueE()+eastStrength);
                    tc.addGlueFunction(t.getGlueE(), t.getGlueE(), eastStrength);
                }
                if(t.getGlueS()!=null) {
                    t.setGlueS(t.getGlueS()+southStrength);
                    tc.addGlueFunction(t.getGlueS(), t.getGlueS(), southStrength);
                }
                if(t.getGlueW()!=null) {
                    t.setGlueW(t.getGlueW()+westStrength);
                    tc.addGlueFunction(t.getGlueW(), t.getGlueW(), westStrength);
                }
                PolyTile pt;
                pt = new PolyTile(t.getLabel());

                pt.addTile(0,0, t.getGlueLabels());
                if(c!=null) {
                    try {
                        String rgb = Integer.toHexString(c.getRGB());
                        rgb = rgb.substring(2, rgb.length());
                        pt.setColor(rgb);
                    } catch (NullPointerException npe) {
                    }
                }
                for(Map.Entry<Point, String> entry : seeds.entrySet()){
                    if(entry.getValue().equals(name)){
                        seedPolyTiles.put(entry.getKey(), pt);
                    }
                }
                tc.addTileType(pt);
                t = new Tile();
                northStrength=0;
                eastStrength=0;
                southStrength=0;
                westStrength=0;
                c=null;
            }
        }
        return new Pair<TileConfiguration,  HashMap<Point, PolyTile>>(tc, seedPolyTiles);
    }

    private static Assembly LoadISUTASAssembly(File f) throws FileNotFoundException{
        /*
            Load ISU TAS
            Reverse Engineered from File Format
            Code was never looked at such as to not inherit GPL
            */

        String tileSetFile=null;
        String mode = "atam";
        int temperature = 2;
        HashMap<Point, String> seeds = new HashMap<Point, String>();
        HashMap<Point, PolyTile> seedsPt = new HashMap<Point, PolyTile>();


        boolean three_dimensional = false;

        Scanner reader = new Scanner(f);

        while(reader.hasNextLine()){
            String line = reader.nextLine();
            String lowerline = line.toLowerCase();

            if(line.contains(".")) tileSetFile=line;
            else if(lowerline.contains("mode="))
                mode = lowerline.split("mode=")[1];
            else if(lowerline.contains("temperature="))
                temperature = Integer.parseInt(lowerline.split("temperature=")[1]);
            else if(line.split(" ").length>=3){
                String tokens[] = line.split(" ");
                String tileLabel = tokens[0];
                int x = Integer.parseInt(tokens[1]);
                int y = Integer.parseInt(tokens[2]);
                int z = tokens.length>3 ? Integer.parseInt(tokens[3]) : 0;
                if(z!=0) three_dimensional = true;
                seeds.put(new Point(x, y), tileLabel);
            }
        }

        TileConfiguration tc = null;
        if(tileSetFile!=null) {
            File tileConfig = new File(f.getParent() + "/" + tileSetFile);
            Pair<TileConfiguration, HashMap<Point, PolyTile>> p = LoadISUTASTileConfiguration(tileConfig, seeds);
            tc = p.getKey();
            seedsPt = p.getValue();
        }

        if(mode.equals("atam") && !three_dimensional){
            TileSystem ts = new TileSystem(temperature);
            if(tc!=null) ts.loadTileConfiguration(tc);
            Assembly assembly = new Assembly(ts);
            for(Map.Entry<Point, PolyTile> entry : seedsPt.entrySet()){
                Point location = entry.getKey();
                PolyTile pt = entry.getValue();

                //check if passes geometry check
                if (assembly.geometryCheckSuccess(pt, (int) location.getX(), (int) location.getY())) {
                    assembly.placePolytile(pt, (int) location.getX(), (int) location.getY());
                }

            }
            return assembly;
        }else{
            //cannot use ktam, 2ham, or 3d
            return null;
        }
    }

    public static Assembly LoadAssembly(File f) throws JAXBException, FileNotFoundException{
        String extension = Assembly.getFileExtension(f);
        if(extension.equals("tdp")){
            Assembly assembly = LoadISUTASAssembly(f);
            assembly.getOpenGlues();
            assembly.calculateFrontier();
            return assembly;
        }else { //load VersaTILE
            JAXBContext jaxbContext = JAXBContext.newInstance(Assembly.class);
            Unmarshaller unmarshaller;
            unmarshaller = jaxbContext.createUnmarshaller();
            Assembly assembly = (Assembly) unmarshaller.unmarshal(f);
            assembly.getOpenGlues();
            assembly.calculateFrontier();
            return assembly;
        }
    }

    //change tile system stub
    public void changeTileSystem(TileSystem newTS) {
        System.out.println("WARNING: CHANGING THE TILE SYSTEM, PREPARE FOR ERRORS!");
        tileSystem = newTS;
        cleanUp();
        getOpenGlues();
        frontier.changeTileSystem(newTS);
        frontier.clear();
        calculateFrontier();
        printDebugInformation();
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("Tile System", null));
        System.out.println("Frontier: " + frontier.size());
    }

    public void changeTemperature(int temperature) {
        tileSystem.setTemperature(temperature);
        changeTileSystem(tileSystem);
    }

    public void changeTileConfiguration(TileConfiguration tc) {
        tileSystem.loadTileConfiguration(tc);
        frontier.changeTileSystem(tileSystem);
    }

    public TileSystem getTileSystem() {
        return tileSystem;
    }

    //Finds open glues on assembly grid and puts them in 4 maps.
    public void getOpenGlues() {
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
        for (Map.Entry<Point, Tile> t : Grid.entrySet()) {
            String[] glueLabels = t.getValue().getGlueLabels();
            //Check if glues are open by checking if their corresponding adjacent point is open
            if (glueLabels[0] != null && Grid.get(new Point(t.getKey().x, t.getKey().y + 1)) == null) {
                openNorthGlues.put(t.getKey(), glueLabels[0]);
            }
            if (glueLabels[1] != null && Grid.get(new Point(t.getKey().x + 1, t.getKey().y)) == null) {
                openEastGlues.put(t.getKey(), glueLabels[1]);
            }
            if (glueLabels[2] != null && Grid.get(new Point(t.getKey().x, t.getKey().y - 1)) == null) {
                openSouthGlues.put(t.getKey(), glueLabels[2]);
            }
            if (glueLabels[3] != null && Grid.get(new Point(t.getKey().x - 1, t.getKey().y)) == null) {
                openWestGlues.put(t.getKey(), glueLabels[3]);
            }
        }
    }

    public void placePolytile(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Grid.put(tmp, t);
        }
    }

    public void removePolytile(PolyTile p, int x, int y) {
        boolean polytilePresent = true;
        for (Tile t : p.tiles) {
            Point tmp = new Point(t.getLocation());
            tmp.translate(x, y);
            Tile existing = Grid.get(tmp);

            if (existing == null) polytilePresent = false;
        }
        if (polytilePresent) {
            for (Tile t : p.tiles) {
                Point tmp = new Point(t.getLocation());
                tmp.translate(x, y);
                Grid.remove(tmp);
            }
        }
    }

    public boolean geometryCheckSuccess(PolyTile p, int x, int y) {
        for (Tile t : p.tiles) {
            if (Grid.containsKey(new Point(x + t.getLocation().x, y + t.getLocation().y))) {
                return false;
            }
        }
        return true;
    }

    private Pair<Point, Point> getOffset(Point aPoint, Point ptPoint, int offsetX, int offsetY) {
        Point tmp = new Point(aPoint);
        tmp.translate(offsetX, offsetY);
        Point placement = new Point(tmp);
        int xOffset = (int) (tmp.getX() - ptPoint.getX());
        int yOffset = (int) (tmp.getY() - ptPoint.getY());
        tmp.setLocation(xOffset, yOffset);
        return new Pair<Point, Point>(placement, tmp);
    }

    private void fillPossibleList(PolyTile pt, int direction) {
        HashMap<Point, String> ptGlues;
        HashMap<Point, String> glues;
        int offsetX;
        int offsetY;

        switch (direction) {
            case NORTH:
                ptGlues = pt.southGlues;
                glues = openNorthGlues;
                offsetX = 0;
                offsetY = 1;
                break;
            case EAST:
                ptGlues = pt.westGlues;
                glues = openEastGlues;
                offsetX = 1;
                offsetY = 0;
                break;
            case SOUTH:
                ptGlues = pt.northGlues;
                glues = openSouthGlues;
                offsetX = 0;
                offsetY = -1;
                break;
            default:
                ptGlues = pt.eastGlues;
                glues = openWestGlues;
                offsetX = -1;
                offsetY = 0;
                break;
        }

        String glue1;
        String glue2;
        for (Point ptPoint : ptGlues.keySet()) {
            for (Point aPoint : glues.keySet()) {
                glue1 = ptGlues.get(ptPoint);
                glue2 = glues.get(aPoint);
                if (tileSystem.getStrength(glue1, glue2) > 0) {
                    Pair<Point, Point> locAndOffset = getOffset(aPoint, ptPoint, offsetX, offsetY);
                    FrontierElement fe = new FrontierElement(locAndOffset.getKey(), locAndOffset.getValue(), pt, direction);
                    possibleAttach.add(fe);
                }
            }
        }
    }

    //Adds coordinate of placement of certain polytile to a grid location iff the polytile has a matching glue with assembly
    private void checkMatchingGlues(PolyTile t) {
        fillPossibleList(t, NORTH);
        fillPossibleList(t, EAST);
        fillPossibleList(t, SOUTH);
        fillPossibleList(t, WEST);
    }

    public List<FrontierElement> getAttached() {
        return attached.getHistory();
    }

    public Frontier getFrontier() {
        return frontier;
    }
    // calculate frontier

    public Frontier calculateFrontier() {
        ArrayList toRemove = new ArrayList();
        for (PolyTile t : tileSystem.getTileTypes()) {
            checkMatchingGlues(t);
        }
        for (FrontierElement fe : possibleAttach) {
            if (checkStability(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y) &&
                    geometryCheckSuccess(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y)) {

                if (!frontier.contains(fe)) {
                    frontier.add(fe);
                }
                toRemove.add(fe);
            }
        }
        for (Object e : toRemove) {
            possibleAttach.remove(e);
        }
        return frontier;
    }

    private boolean checkStability(PolyTile p, int x, int y) {
        int totalStrength = 0;
        //For all tiles and their edges, check the tile they would possibly bond with for the strength
        //Example: a tile's north glue needs to see the above tile's south glue
        for (Tile t : p.tiles) {
            String nPolytileGlue = t.getGlueN();
            if (nPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x, y + 1);
                Tile nAssemblyTile = Grid.get(pt);
                if (nAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(nPolytileGlue, nAssemblyTile.getGlueS());

            }

            String ePolytileGlue = t.getGlueE();
            if (ePolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x + 1, y);
                Tile eAssemblyTile = Grid.get(pt);
                if (eAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(ePolytileGlue, eAssemblyTile.getGlueW());

            }

            String sPolytileGlue = t.getGlueS();
            if (sPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x, y - 1);
                Tile sAssemblyTile = Grid.get(pt);
                if (sAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(sPolytileGlue, sAssemblyTile.getGlueN());

            }

            String wPolytileGlue = t.getGlueW();
            if (wPolytileGlue != null) {
                Point pt = new Point(t.getLocation());
                pt.translate(x - 1, y);
                Tile wAssemblyTile = Grid.get(pt);
                if (wAssemblyTile != null)
                    totalStrength += tileSystem.getStrength(wPolytileGlue, wAssemblyTile.getGlueE());

            }
        }
        if (totalStrength >= tileSystem.getTemperature())
            return true;
        else
            return false;
    }

    // delete from frontier

    public void cleanUp() {
        frontier.clear();
        possibleAttach.clear();
        openNorthGlues.clear();
        openEastGlues.clear();
        openSouthGlues.clear();
        openWestGlues.clear();
    }

    public double attach(boolean notify) {
        if (!frontier.isEmpty()) {
            FrontierElement fe = frontier.get(frontier.randomSelect());
            attachP(fe);
            calculateFrontier();
            if (notify) {
                setChanged();
                notifyObservers(new Pair<String, FrontierElement>("attach", fe));
            }
            return fe.getAttachTime();
        } else return -1.0;
    }

    public double attach(FrontierElement fe) {
        double time = attachP(fe);
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("attach", fe));
        return time;
    }

    private double attachP(FrontierElement fe) {
        fe.setAttachTime(getDistribution(frontier.getTotalConcentration()));
        placePolytile(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y);
        frontier.remove(fe);
        attached.getHistory().add(fe);
        cleanUp();
        getOpenGlues();
        return fe.getAttachTime();
    }

    public double attachAll(boolean notify) {
        double last = -1.0;
        while (!frontier.isEmpty()) {
            FrontierElement fe = frontier.get(frontier.randomSelect());
            last = attachP(fe);
            calculateFrontier();
        }
        if (notify) {
            setChanged();
            notifyObservers(new Pair<String, FrontierElement>("refresh", null));
        }
        return last;
    }


    public void detach(boolean notify) {
        if (!attached.getHistory().isEmpty()) {
            FrontierElement last = attached.getHistory().remove(attached.getHistory().size() - 1);
            detach(last);
            calculateFrontier();
            if (notify) {
                setChanged();
                notifyObservers(new Pair<String, FrontierElement>("detach", last));
            }
        }
    }

    private void detach(FrontierElement fe) {
        removePolytile(fe.getPolyTile(), fe.getOffset().x, fe.getOffset().y);
        cleanUp();
        getOpenGlues();
    }

    public void detachAll(boolean notify) {
        while (!attached.getHistory().isEmpty()) {
            FrontierElement last = attached.getHistory().remove(attached.getHistory().size() - 1);
            detach(last);

        }
        if (notify) {
            setChanged();
            notifyObservers(new Pair<String, FrontierElement>("refresh", null));
        }
    }

    public void placeSeed(PolyTile t) {
        if (Grid.size() == 0)
            placePolytile(t, 0, 0);
        else
            System.out.println("Grid not empty");

        getOpenGlues();
    }

    public void clearSeed(){
        detachAll(false);
        Grid.clear();
        setChanged();
        notifyObservers(new Pair<String, FrontierElement>("refresh", null));
    }

    public Set<Point> pointsInGrid() {
        return Grid.keySet();
    }


    public double getDistribution(double rate) {
        Random rand = new Random();
        double randNum = rand.nextDouble();
        double logger = Math.log(1 - randNum);
        double time = logger / (-rate);
        return time;

    }

    //Prints assembly as grid, with the number being the number of tiles in a spot
    //For debugging purposes
    @Override
    public String toString() {
        int minimumX, maximumX, minimumY, maximumY;
        Point[] tiles = Grid.keySet().toArray(new Point[Grid.keySet().size()]);
        Point[] tiles2 = new Point[Grid.keySet().size()];
        for (int i = 0; i < tiles.length; i++)
            tiles2[i] = new Point(tiles[i]);
        minimumX = tiles2[0].x;
        maximumX = tiles2[0].x;
        minimumY = tiles2[0].y;
        maximumY = tiles2[0].y;

        //Find minimum and maximum of assembly coordinates
        for (Point p : tiles2) {
            if (p.x < minimumX)
                minimumX = p.x;
            else if (p.x > maximumX)
                maximumX = p.x;

            if (p.y < minimumY)
                minimumY = p.y;
            else if (p.y > maximumY)
                maximumY = p.y;
        }

        //Shift everything to 0,0 as bottom left
        for (int i = 0; i < tiles2.length; i++) {
            tiles2[i].x += (-1) * minimumX;
            tiles2[i].y += (-1) * minimumY;
        }
        maximumX += (-1) * minimumX;
        maximumY += (-1) * minimumY;

        int[][] assemblyMatrix = new int[maximumY + 1][maximumX + 1];
        for (Point p : tiles2) {
            assemblyMatrix[maximumY - p.y][p.x]++;
        }

        StringBuilder matrixString = new StringBuilder();

        for (int i = 0; i < assemblyMatrix.length; i++) {
            for (int j = 0; j < assemblyMatrix[i].length; j++) {
                matrixString.append(assemblyMatrix[i][j] + " ");
            }
            matrixString.append("\n");
        }

        return matrixString.toString();
    }

    public PolyTile toPolyTile(){
        PolyTile assemblyPT = new PolyTile();
        assemblyPT.getTiles().clear();
        for (Map.Entry<Point, Tile> t : Grid.entrySet()) {
            Point location = t.getKey();
            Tile tile = t.getValue();
            Tile tile_copy = new Tile((int) location.getX(), (int) location.getY(), tile.getGlueLabels(), assemblyPT);
            assemblyPT.addTile(tile_copy);
        }
        return assemblyPT;
    }

    public void printDebugInformation() {
        System.out.println("--- Debug Information ----");
        System.out.println("Grid Size: " + Grid.size());
        System.out.println("Frontier Size: " + frontier.size());
        System.out.println("Attached List Size: " + attached.getHistory().size());
        System.out.println("Possible Attached List Size: " + possibleAttach.size());
        System.out.println("Open North Glues: " + openNorthGlues.size());
        System.out.println("Open East Glues: " + openEastGlues.size());
        System.out.println("Open South Glues: " + openSouthGlues.size());
        System.out.println("Open West Glues: " + openWestGlues.size());


        System.out.println("--------------------------");

        tileSystem.printDebugInformation();
        frontier.printDebugInformation();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        changeTileSystem(getTileSystem());
        getOpenGlues();
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        else return "";
    }
}
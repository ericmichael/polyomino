package com.asarg.polysim.models.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
    public List<Vertex> vertices = new ArrayList<>();
    public List<Vertex> faceVertices = new ArrayList<>();

    public class Vertex{
        public String tileLabel = new String();;
        public List<Edge> neighbors = new ArrayList<>();
        public Coordinate location;
        public int mark = 0;
        public int pathMark = 0;
        public int edgeAmount = 0;


        public Edge northNeighbor;
        public Edge southNeighbor;
        public Edge westNeighbor;
        public Edge eastNeighbor;

        public String N;
        public String W;
        public String S;
        public String E;

        public void addEdge(Vertex b, int strength, String where){
            neighbors.add(new Edge(b , strength, where));
            Edge e = new Edge(b, strength, where);
            edgeAmount = edgeAmount + 1;
            if (where == "N"){
                northNeighbor = e;
            }
            else if (where == "S"){
                southNeighbor = e;
            }
            else if (where == "W"){
                westNeighbor = e;
            }
            else if (where == "E"){
                eastNeighbor = e;
            }
        }
    }

    public class Edge{
        public Vertex toVertex = new Vertex();
        public int bondStrength = 0;
        public String direction = "X";


        public Edge(Vertex b, int strength, String where){
            toVertex = b;
            bondStrength = strength;
            direction = where;
        }
    }
    public void printVertices(){
        for (Vertex v : vertices){
            System.out.print("Label: " + v.tileLabel);
            System.out.print(" edgeAmount: " + v.edgeAmount);
            System.out.print("-> ");
            if (v.northNeighbor != null){
                System.out.print(v.northNeighbor.direction);
            }
            if(v.eastNeighbor != null){
                System.out.print(v.eastNeighbor.direction);
            }
            if (v.westNeighbor != null){
                System.out.print(v.westNeighbor.direction);
            }
            if (v.southNeighbor != null){
                System.out.print(v.southNeighbor.direction);
            }
            System.out.println("\n");
        }
    }

    public void addVertex(String label, Coordinate pt, String N, String S, String W, String E){
        Vertex v = new Vertex();
        v.tileLabel = label;
        v.location = pt;
        v.N = N;
        v.S = S;
        v.W = W;
        v.E = E;
        vertices.add(v);
    }

    public void makeDualGraph(){
        //mark all hairs
        int amount = 0;
        while(true){
            amount = markHairs();
            if (amount >= 2){
                break;
            }
        }
        getFaces();
    }

    public void getFaces(){
        for (Vertex v : vertices){
            if (v.neighbors.size() == 1){
                System.out.print(" " + v.tileLabel + " A LONER TILE\n ");
            }
            else{
                for (Edge e : v.neighbors){
                    if (e.direction.equals("N")){
                        theNorthernTrials(v, e);
                    }
                }
            }
        }
    }
    public int markHairs(){
        int minimum_edge_amount = 5;
        for (Vertex v : vertices){
            if (v.edgeAmount == 1 && v.mark == 0){
                v.mark = 1;
                for (Edge neighbor : v.neighbors){
                    if (neighbor.toVertex.mark == 0){
                        neighbor.toVertex.edgeAmount -= 1;
                    }
                }
            }
        }
        for (Vertex v : vertices){
            if (v.edgeAmount <= minimum_edge_amount && v.mark != 1){
                minimum_edge_amount = v.edgeAmount;
                System.out.print(minimum_edge_amount);
            }
            System.out.print("} THIS: " + v.tileLabel + ", #" + v.edgeAmount + " {");
        }

        //print the mark ones
        for (Vertex v : vertices){
            if (v.mark == 1){
                System.out.print("} THIS GUY IS MARKED: {" + v.tileLabel + "} ");
            }
        }
        return minimum_edge_amount;
    }


    public void removePathMarkings(){
        for (Vertex v : vertices){
            v.pathMark = 0;
        }
    }


    public void theNorthernTrials(Vertex v, Edge start){
        Vertex transv = new Vertex();
        Vertex endOfStack = v;
        //north trial = 0
        //west trial = 1
        //south trial = 2
        //east trial = 3
        int trial = 0;
        List<Vertex> northTrials_path = new ArrayList<Vertex>();
        List<Vertex> westTrials_path = new ArrayList<Vertex>();
        List<Vertex> southTrials_path = new ArrayList<Vertex>();
        List<Vertex> eastTrials_path = new ArrayList<Vertex>();

        transv = start.toVertex;
        String direction = "N";
        System.out.print("\nI am target tile: " + v.tileLabel + " and I'm at " + transv.tileLabel+ " ");



        //this is the north trial
        //if both a west, east, and north, save east and north trial but go to the west trial
        //if west trial fails, return to east or north, preferably north, saved and continue NORTH trial
        int i = 0;
        do {
            System.out.print(" " + i + " ");
            //check to see if multiple paths exist in the current vertex
            //if all exist

            if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && !transv.northNeighbor.direction.isEmpty()
                    && transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && !transv.westNeighbor.direction.isEmpty()
                    && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && !transv.eastNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.toVertex.mark != 1
                    && transv.westNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.mark != 1
                    && transv.eastNeighbor.toVertex.pathMark != 1
                    && transv.westNeighbor.toVertex.pathMark != 1
                    && transv.northNeighbor.toVertex.pathMark != 1) {
                System.out.print(" a ");
                northTrials_path.add(transv.northNeighbor.toVertex);
                northTrials_path.add(transv.eastNeighbor.toVertex);
                transv = transv.westNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && !transv.northNeighbor.direction.isEmpty()
                    && transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && !transv.westNeighbor.direction.isEmpty()
                    && transv.westNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.mark != 1
                    && transv.westNeighbor.toVertex.pathMark != 1
                    && transv.northNeighbor.toVertex.pathMark != 1){
                System.out.print(" b ");
                Vertex really = new Vertex();
                really = transv.northNeighbor.toVertex;
                northTrials_path.add(really);
                transv = transv.westNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && !transv.northNeighbor.direction.isEmpty()
                    && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && !transv.eastNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.mark != 1
                    && transv.eastNeighbor.toVertex.pathMark != 1
                    && transv.northNeighbor.toVertex.pathMark != 1){
                System.out.print(" c ");
                northTrials_path.add(transv.eastNeighbor.toVertex);
                transv = transv.northNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && !transv.westNeighbor.direction.isEmpty()
                    && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && !transv.eastNeighbor.direction.isEmpty()
                    && transv.southNeighbor != null && transv.southNeighbor.direction != null
                    && !transv.southNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.toVertex.mark != 1
                    && transv.westNeighbor.toVertex.mark != 1
                    && transv.southNeighbor.toVertex.mark != 1
                    && transv.eastNeighbor.toVertex.pathMark != 1
                    && transv.westNeighbor.toVertex.pathMark != 1){
                System.out.print(" c ");
                northTrials_path.add(transv.eastNeighbor.toVertex);
                transv = transv.westNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && !transv.northNeighbor.direction.isEmpty()
                    && transv.northNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.pathMark != 1){
                transv = transv.northNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && !transv.westNeighbor.direction.isEmpty()
                    && transv.westNeighbor.toVertex.mark != 1
                    && transv.westNeighbor.toVertex.pathMark != 1) {
                System.out.print(" d ");
                transv = transv.westNeighbor.toVertex;
                transv.pathMark = 1;
            }
            else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && !transv.eastNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.toVertex.mark != 1) {
                System.out.print(" e ");
                System.out.print("{Didn't move}");
                int count = 0;
                if (!northTrials_path.isEmpty()){
                    for (Vertex ok : northTrials_path){
                        System.out.print(ok.tileLabel + " ");
                        count = count + 1;
                    }
                    if (count >= 1){
                        transv = northTrials_path.get(0);
                        transv.pathMark = 1;
                        northTrials_path.remove(0);
                    }
                }
            }

            /*
            if (!transv.eastNeighbor.direction.isEmpty()
                    && !transv.northNeighbor.direction.isEmpty()
                    && !transv.westNeighbor.direction.isEmpty()

                    && transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && transv.westNeighbor.toVertex.mark != 1 && transv.westNeighbor.toVertex.mark != 1
                    && transv.eastNeighbor.toVertex.mark != 1 && transv.eastNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.mark != 1 && transv.northNeighbor.toVertex.mark != 1
                    ) {
                System.out.print(" \na ");
                System.out.print(transv.tileLabel + " ");
                northTrials_path.add(transv.northNeighbor.toVertex);
                northTrials_path.add(transv.eastNeighbor.toVertex);
                transv = transv.westNeighbor.toVertex;

            }
            //if N and W exist
            else if (!transv.northNeighbor.direction.isEmpty()
                    && !transv.westNeighbor.direction.isEmpty()

                    && transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && transv.northNeighbor.toVertex.mark != 1 && transv.westNeighbor.toVertex.mark != 1
                    && transv.westNeighbor.toVertex.mark != 1 && transv.westNeighbor.toVertex.mark != 1) {
                System.out.print(" \nb ");
                northTrials_path.add(transv.northNeighbor.toVertex);
                System.out.print(transv.tileLabel + " ");
                transv = transv.westNeighbor.toVertex;
            }
            //if N and E exist
            else if (!transv.northNeighbor.direction.isEmpty()
                    && !transv.eastNeighbor.direction.isEmpty()

                    && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                    && transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && transv.eastNeighbor.toVertex.mark != 1 && transv.eastNeighbor.toVertex.mark != 1
                    && transv.northNeighbor.toVertex.mark != 1 && transv.northNeighbor.toVertex.mark != 1 ) {
                System.out.print(" \nc ");
                System.out.print(transv.tileLabel + " ");
                northTrials_path.add(transv.northNeighbor.toVertex);
                transv = transv.eastNeighbor.toVertex;
            //if N and E exist

            }
            else if (!transv.eastNeighbor.direction.isEmpty()
                    && !transv.northNeighbor.direction.isEmpty()
                    && !transv.westNeighbor.direction.isEmpty()

                    && transv.northNeighbor != null && transv.northNeighbor.direction != null
                    && transv.northNeighbor.toVertex.mark != 1
                    && transv.westNeighbor == null && transv.westNeighbor.direction == null
                    && transv.eastNeighbor == null && transv.eastNeighbor.direction == null){
                System.out.print("NORTH ONLI");
            }
            else if (!transv.eastNeighbor.direction.isEmpty()
                    && !transv.northNeighbor.direction.isEmpty()
                    && !transv.westNeighbor.direction.isEmpty()

                    && transv.westNeighbor != null && transv.westNeighbor.direction != null
                    && transv.westNeighbor.toVertex.mark != 1
                    && transv.northNeighbor == null && transv.northNeighbor.direction == null
                    && transv.eastNeighbor == null && transv.eastNeighbor.direction == null) {
                System.out.print(" \nd ");
                System.out.print(transv.tileLabel + " ");
                System.out.print("TIME TO WEST TRIALZ");
            }
            else{
                System.out.print(" \ne ");
                System.out.print(transv.tileLabel + " ");
                if (!northTrials_path.isEmpty()){
                    transv = northTrials_path.get(0);
                    northTrials_path.remove(0);
                }
            }
            System.out.print(" \ni ");
            //IF ONLY WEST DIRECTION EXIST, GO TO WEST TRIALZ
            i++;
            if (i > 10){
                System.out.print("END ");
                break;
            }
            */
            System.out.print("{" + transv.tileLabel + "}, ");
            i++;
            if (i > 20){
                System.out.print(" END ");
                removePathMarkings();
                break;
            }
        }while(!transv.equals(v));






        /*
        do{
            if (transv.westNeighbor != null && transv.westNeighbor.direction != null && !transv.westNeighbor.direction.isEmpty()
                    && transv.westNeighbor.direction.equals("W") && direction.equals("N") && transv.westNeighbor.toVertex.mark != 1){
                direction = "W";
                System.out.print(" Go West ");
                transv = transv.westNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else if (transv.southNeighbor != null && transv.southNeighbor.direction != null && !transv.southNeighbor.direction.isEmpty()
                    && transv.southNeighbor.direction.equals("S") && direction.equals("W") && transv.southNeighbor.toVertex.mark != 1){
                direction = "S";
                System.out.print(" Go South ");
                transv = transv.southNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null && !transv.eastNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.direction.equals("E") && direction.equals("S") && transv.eastNeighbor.toVertex.mark != 1){
                direction = "E";
                System.out.print(" Go East ");
                transv = transv.eastNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            //this next half moves it forward more->
            else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null && !transv.eastNeighbor.direction.isEmpty()
                    && transv.eastNeighbor.direction.equals("E") && direction.equals("E") && transv.eastNeighbor.toVertex.mark != 1){
                direction = "E";
                System.out.print(" Go East ");
                transv = transv.eastNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else if (transv.southNeighbor != null && transv.southNeighbor.direction != null && !transv.southNeighbor.direction.isEmpty()
                    && transv.southNeighbor.direction.equals("S") && direction.equals("S") && transv.southNeighbor.toVertex.mark != 1){
                direction = "S";
                System.out.print(" Go South ");
                transv = transv.southNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else if (transv.westNeighbor != null && transv.westNeighbor.direction != null && !transv.westNeighbor.direction.isEmpty()
                    && transv.westNeighbor.direction.equals("W") && direction.equals("W") && transv.westNeighbor.toVertex.mark != 1){
                direction = "W";
                System.out.print(" Go West ");
                transv = transv.westNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else if (transv.northNeighbor != null && transv.northNeighbor.direction != null && !transv.northNeighbor.direction.isEmpty()
                    && transv.northNeighbor.direction.equals("W") && direction.equals("W") && transv.northNeighbor.toVertex.mark != 1){
                direction = "W";
                System.out.print(" Go North ");
                transv = transv.northNeighbor.toVertex;
                System.out.print(transv.tileLabel + " ");
            }
            else{
                System.out.print(" Landed on: " + transv.tileLabel);
                return;
            }
        }while(!transv.equals(v));
        */
    }
}

package com.asarg.polysim.models.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Graph {
    public List<Vertex> vertices = new ArrayList<>();
    public ArrayList<ArrayList<Vertex>> faceVertices = new ArrayList<ArrayList<Vertex>>();
    public int face_amount = 0;
    public class Vertex {
        public String tileLabel = new String();
        ;
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

        public void addEdge(Vertex b, int strength, String where) {
            neighbors.add(new Edge(b, strength, where));
            Edge e = new Edge(b, strength, where);
            edgeAmount = edgeAmount + 1;
            if (where == "N") {
                northNeighbor = e;
            } else if (where == "S") {
                southNeighbor = e;
            } else if (where == "W") {
                westNeighbor = e;
            } else if (where == "E") {
                eastNeighbor = e;
            }
        }
    }

    public class Edge {
        public Vertex toVertex = new Vertex();
        public int bondStrength = 0;
        public String direction = "X";


        public Edge(Vertex b, int strength, String where) {
            toVertex = b;
            bondStrength = strength;
            direction = where;
        }
    }

    public void printVertices() {
        for (Vertex v : vertices) {
            System.out.print("Label: " + v.tileLabel);
            System.out.print(" edgeAmount: " + v.edgeAmount);
            System.out.print("-> ");
            if (v.northNeighbor != null) {
                System.out.print(v.northNeighbor.direction);
            }
            if (v.eastNeighbor != null) {
                System.out.print(v.eastNeighbor.direction);
            }
            if (v.westNeighbor != null) {
                System.out.print(v.westNeighbor.direction);
            }
            if (v.southNeighbor != null) {
                System.out.print(v.southNeighbor.direction);
            }
            System.out.println("\n");
        }
    }

    public void addVertex(String label, Coordinate pt, String N, String S, String W, String E) {
        Vertex v = new Vertex();
        v.tileLabel = label;
        v.location = pt;
        v.N = N;
        v.S = S;
        v.W = W;
        v.E = E;
        vertices.add(v);
    }

    public void makeDualGraph() {
        //mark all hairs
        int amount = 0;
        while (true) {
            amount = markHairs();
            if (amount >= 2) {
                break;
            }
        }
        getFaces();
    }

    public void getFaces() {
        ArrayList<Vertex> buffer = new ArrayList<Vertex>();
        for (Vertex v : vertices) {
            if (v.neighbors.size() == 1) {
                //System.out.print(" " + v.tileLabel + " A LONER TILE\n ");
            } else {
                for (Edge e : v.neighbors) {
                    if (e.direction.equals("N")) {
                        buffer = theTrials(v, e);
                        if (!buffer.isEmpty()){
                            faceVertices.add(buffer);
                        }
                    }
                }
            }
        }
        //remove redundant
        if (!faceVertices.isEmpty()){
            int flag = 0;
            ArrayList<ArrayList<Vertex>> path = new ArrayList<ArrayList<Vertex>>();
            path.add(faceVertices.get(0));
            for (ArrayList<Vertex> firstPath : faceVertices){
                for (ArrayList<Vertex> secondPath : path){
                    if (listEqualsIgnoreOrder(secondPath, firstPath)){
                        flag = 1;
                    }
                }
                if (flag == 0){
                    path.add(firstPath);
                }
                else{
                    flag = 0;
                }
            }
            faceVertices = path;
        }
        printPath();
    }
    public static <T> boolean listEqualsIgnoreOrder(ArrayList<T> list1, ArrayList<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }
    public void printPath(){
        if (!faceVertices.isEmpty()){
            for (ArrayList<Vertex> p : faceVertices) {
                System.out.print("Path for {" + p.get(0).tileLabel + "} = { ");
                for (Vertex v : p){
                    System.out.print(v.tileLabel + ", ");
                }
                System.out.println("};");
            }
        }
        System.out.println("Face amount: " + faceVertices.size());
    }
    public void printAmount(){
        System.out.print("Amount of faces:" + face_amount);
    }
    public int markHairs() {
        int minimum_edge_amount = 5;
        for (Vertex v : vertices) {
            if (v.edgeAmount == 1 && v.mark == 0) {
                v.mark = 1;
                for (Edge neighbor : v.neighbors) {
                    if (neighbor.toVertex.mark == 0) {
                        neighbor.toVertex.edgeAmount -= 1;
                    }
                }
            }
        }
        for (Vertex v : vertices) {
            if (v.edgeAmount <= minimum_edge_amount && v.mark != 1) {
                minimum_edge_amount = v.edgeAmount;
            }
        }

        return minimum_edge_amount;
    }


    public void removePathMarkings() {
        for (Vertex v : vertices) {
            v.pathMark = 0;
        }
    }


    public ArrayList<Vertex> theTrials(Vertex v, Edge start) {
        Vertex transv = new Vertex();
        Vertex notTransv = new Vertex();
        int round = 0;
        //north trial = 0
        //west trial = 1
        //south trial = 2
        //east trial = 3
        int flag = 0;
        int trial = 0;
        List<Vertex> northTrials_path = new ArrayList<Vertex>();
        List<Vertex> westTrials_path = new ArrayList<Vertex>();
        List<Vertex> southTrials_path = new ArrayList<Vertex>();
        List<Vertex> eastTrials_path = new ArrayList<Vertex>();

        ArrayList<Vertex> finalPath = new ArrayList<Vertex>();
        finalPath.add(v);
        transv = start.toVertex;
        finalPath.add(transv);
        notTransv = v;
        String direction = "N";
        //System.out.print("\nI am target tile: " + v.tileLabel + " and I'm at " + transv.tileLabel + " ");


        //this is the north trial
        //if both a west, east, and north, save east and north trial but go to the west trial
        //if west trial fails, return to east or north, preferably north, saved and continue NORTH trial
        int i = 0;
       // System.out.print("\nFirst Tile " + v.tileLabel + " -> ");
        do {
            //System.out.print(" AT: {" + transv.tileLabel + "}<-R: " + round + " T: " + trial + "}");
            //System.out.print(" " + i + " ");
            ////////////////////////////////////////////////////////////
            //THE NORTH TRIALS//////////////////////////////////////////
            if (trial == 0) {
                //System.out.print("North Trials\n");
                ////////////////////////////////////////////////////////////
                //////////////////
                //     |
                //    -o-
                ////////////////////////////////////////////////////////////
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
                    northTrials_path.add(transv.northNeighbor.toVertex);
                    northTrials_path.add(transv.eastNeighbor.toVertex);
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 1;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //   -o
                ////////////////////////////////////////////////////////////
                else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1) {
                    northTrials_path.add(transv.northNeighbor.toVertex);
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 1;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o-
                ////////////////////////////////////////////////////////////
                else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1) {
                    northTrials_path.add(transv.eastNeighbor.toVertex);
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o-
                //    |
                ////////////////////////////////////////////////////////////
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
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    northTrials_path.add(transv.eastNeighbor.toVertex);
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 1;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o
                ////////////////////////////////////////////////////////////
                else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1) {
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o
                //
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                    trial = 1;
                }
                ////////////////////////////////////////////////////////////
                //    o-
                //
                ////////////////////////////////////////////////////////////
                else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.pathMark != 1
                        && round == 0) {
                        int count = 0;
                        if (!northTrials_path.isEmpty()) {
                            for (Vertex ok : northTrials_path) {
                                //System.out.print(ok.tileLabel + " ");
                                count = count + 1;
                            }
                            if (count >= 1) {
                                transv = northTrials_path.get(0);
                                finalPath.remove(finalPath.size() - 1);
                                finalPath.add(transv);
                                transv.pathMark = 1;
                                northTrials_path.remove(0);
                            } else {
                                notTransv = transv;
                            }
                        }
                }
                else if (round != 0 && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.eastNeighbor.toVertex.mark != 1) {
                    transv = transv.eastNeighbor.toVertex;
                    trial = 3;
                }

                ////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////
            } else if (trial == 1) {
                //System.out.print("West Trials\n");
                ////////////////////////////////////////////////////////////
                //THE WEST TRIALS//////////////////////////////////////////
                ////////////////////////////////////////////////////////////
                if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()

                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.mark != 1

                        && transv.southNeighbor.toVertex.pathMark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1) {
                    westTrials_path.add(transv.westNeighbor.toVertex);
                    westTrials_path.add(transv.northNeighbor.toVertex);
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 2;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //   -o
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()

                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.mark != 1

                        && transv.southNeighbor.toVertex.pathMark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    westTrials_path.add(transv.westNeighbor.toVertex);
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 2;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o-
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    westTrials_path.add(transv.northNeighbor.toVertex);
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o-
                //    |
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()

                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.mark != 1

                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    westTrials_path.add(transv.northNeighbor.toVertex);
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 2;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    transv = transv.westNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o
                //
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                    trial = 2;
                }
                ////////////////////////////////////////////////////////////
                //    o-
                //
                ////////////////////////////////////////////////////////////
                else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.pathMark != 1) {
                    int count = 0;
                    if (!westTrials_path.isEmpty()) {
                        for (Vertex ok : westTrials_path) {
                           // System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            transv = westTrials_path.get(0);
                            finalPath.remove(finalPath.size() - 1);
                            finalPath.add(transv);
                            transv.pathMark = 1;
                            westTrials_path.remove(0);
                        } else {
                            notTransv = transv;
                        }
                    }
                }
                ////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////
            } else if (trial == 2) {
                //System.out.print("South Trials\n");
                ////////////////////////////////////////////////////////////
                //THE SOUTH TRIALS//////////////////////////////////////////
                ////////////////////////////////////////////////////////////
                //////////////////
                //
                //
                ////////////////////////////////////////////////////////////
                if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()

                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.mark != 1

                        && transv.eastNeighbor.toVertex.pathMark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    southTrials_path.add(transv.westNeighbor.toVertex);
                    southTrials_path.add(transv.southNeighbor.toVertex);
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 3;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //   -o
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()

                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.mark != 1

                        && transv.eastNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    southTrials_path.add(transv.southNeighbor.toVertex);
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 3;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o-
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()

                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.mark != 1

                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    southTrials_path.add(transv.northNeighbor.toVertex);
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o-
                //    |
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()

                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.mark != 1

                        && transv.eastNeighbor.toVertex.pathMark != 1
                        && transv.westNeighbor.toVertex.pathMark != 1) {
                    southTrials_path.add(transv.westNeighbor.toVertex);
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 3;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    transv = transv.southNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o
                //
                ////////////////////////////////////////////////////////////
                else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1) {
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 3;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    o-
                //
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.westNeighbor.toVertex.mark != 1
                        && transv.pathMark != 1) {
                    int count = 0;
                    if (!southTrials_path.isEmpty()) {
                        for (Vertex ok : southTrials_path) {
                            //System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            transv = southTrials_path.get(0);
                            finalPath.remove(finalPath.size() - 1);
                            finalPath.add(transv);
                            transv.pathMark = 1;
                            southTrials_path.remove(0);
                        } else {
                            notTransv = transv;
                        }
                    }
                }
                ////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////
            } else if (trial == 3) {
                round++;
                //System.out.print("East Trials\n");
                ////////////////////////////////////////////////////////////
                //THE EAST TRIALS//////////////////////////////////////////
                ////////////////////////////////////////////////////////////
                //////////////////
                //     |
                //    -o-
                ////////////////////////////////////////////////////////////
                if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()

                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.mark != 1

                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1) {
                    eastTrials_path.add(transv.eastNeighbor.toVertex);
                    eastTrials_path.add(transv.southNeighbor.toVertex);
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 0;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //   -o
                ////////////////////////////////////////////////////////////
                else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()

                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.mark != 1

                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1) {
                    eastTrials_path.add(transv.eastNeighbor.toVertex);
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 0;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o-
                ////////////////////////////////////////////////////////////
                else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1) {
                    eastTrials_path.add(transv.southNeighbor.toVertex);
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o-
                //    |
                ////////////////////////////////////////////////////////////
                else if (transv.westNeighbor != null && transv.westNeighbor.direction != null
                        && !transv.westNeighbor.direction.isEmpty()
                        && transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()

                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.southNeighbor.toVertex.mark != 1

                        && transv.northNeighbor.toVertex.pathMark != 1
                        && transv.southNeighbor.toVertex.pathMark != 1) {
                    eastTrials_path.add(transv.southNeighbor.toVertex);
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 0;
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //    |
                //    o
                ////////////////////////////////////////////////////////////
                else if (transv.eastNeighbor != null && transv.eastNeighbor.direction != null
                        && !transv.eastNeighbor.direction.isEmpty()
                        && transv.eastNeighbor.toVertex.mark != 1
                        && transv.eastNeighbor.toVertex.pathMark != 1) {
                    transv = transv.eastNeighbor.toVertex;
                    finalPath.add(transv);
                    transv.pathMark = 1;
                }
                ////////////////////////////////////////////////////////////
                //   -o
                //
                ////////////////////////////////////////////////////////////
                else if (transv.northNeighbor != null && transv.northNeighbor.direction != null
                        && !transv.northNeighbor.direction.isEmpty()
                        && transv.northNeighbor.toVertex.mark != 1
                        && transv.northNeighbor.toVertex.pathMark != 1) {
                    transv = transv.northNeighbor.toVertex;
                    finalPath.add(transv);
                    trial = 0;
                    transv.pathMark = 1;
                    trial = 0;
                }
                ////////////////////////////////////////////////////////////
                //    o-
                //
                ////////////////////////////////////////////////////////////
                else if (transv.southNeighbor != null && transv.southNeighbor.direction != null
                        && !transv.southNeighbor.direction.isEmpty()
                        && transv.southNeighbor.toVertex.mark != 1
                        && transv.pathMark != 1) {
                    int count = 0;
                    if (!eastTrials_path.isEmpty()) {
                        for (Vertex ok : eastTrials_path) {
                            //System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            transv = eastTrials_path.get(0);
                            finalPath.remove(finalPath.size() - 1);
                            finalPath.add(transv);
                            transv.pathMark = 1;
                            eastTrials_path.remove(0);
                        } else {
                            notTransv = transv;
                        }
                    }
                }
                ////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////
            }

            if (notTransv == transv && !transv.equals(v)) {
               // System.out.print("Equal");
                int count = 0;
                if (trial == 0) {
                    if (!eastTrials_path.isEmpty()) {
                        for (Vertex ok : eastTrials_path) {
                           // System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            trial = 3;
                            transv = eastTrials_path.get(0);
                            notTransv = transv;
                            transv.pathMark = 1;
                            eastTrials_path.remove(0);
                        }
                    }
                } else if (trial == 1) {
                    if (!northTrials_path.isEmpty()) {
                        for (Vertex ok : northTrials_path) {
                           // System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            trial = 0;
                            transv = northTrials_path.get(0);
                            notTransv = transv;
                            transv.pathMark = 1;
                            northTrials_path.remove(0);
                        }
                    }
                } else if (trial == 2) {
                    if (!westTrials_path.isEmpty()) {
                        for (Vertex ok : westTrials_path) {
                           // System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            trial = 1;
                            transv = westTrials_path.get(0);
                            notTransv = transv;
                            transv.pathMark = 1;
                            westTrials_path.remove(0);
                        }
                    }
                } else if (trial == 3) {
                    if (!southTrials_path.isEmpty()) {
                        for (Vertex ok : southTrials_path) {
                           // System.out.print(ok.tileLabel + " ");
                            count = count + 1;
                        }
                        if (count >= 1) {
                            trial = 2;
                            transv = southTrials_path.get(0);
                            notTransv = transv;
                            transv.pathMark = 1;
                            southTrials_path.remove(0);
                        }
                    }
                }
            }
            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            //System.out.print("{" + transv.tileLabel + "}, ");
            i++;
            if (i > vertices.size()) {
                flag = 1;
                //System.out.print(" END ");
                removePathMarkings();
                break;
            }
        } while (!transv.equals(v));
        if (flag == 1) {
            System.out.print("did not find path\n" + ", trial ended= " + trial + ", round =" + round);
            for (Vertex d : finalPath){
                System.out.print(" " + d.tileLabel + " " );
            }
            removePathMarkings();
            finalPath.clear();
            return finalPath;
        } else {
            //printPath(finalPath);
            removePathMarkings();
            //face_amount++;
            //System.out.print("FOUND PATH\n");
            return finalPath;
        }


    }

}
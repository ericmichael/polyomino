package com.asarg.polysim;

public class Main {

    public static void main(String args[]) {

        TetrisSimulation tetris = new TetrisSimulation(2);
        TestCanvasFrame tcf = new TestCanvasFrame(800,600, tetris.assembly);
    }
}
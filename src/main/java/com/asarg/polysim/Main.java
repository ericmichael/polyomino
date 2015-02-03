package com.asarg.polysim;

import com.asarg.polysim.utils.AutoUpdate;
import javafx.application.Application;


public class Main {


    public static void main(String args[]) {
        AutoUpdate.checkForUpdates();
        Application.launch(SimulationApplication.class, (java.lang.String[]) null);
    }

}
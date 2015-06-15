package com.asarg.polysim;

import com.asarg.polysim.controllers.SimulationController;
import com.asarg.polysim.models.base.Assembly;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ericmartinez on 12/11/14.
 */

public class SimulationApplication extends Application {
    static{
        try {
            Font.loadFont(Main.class.getResource("/fontawesome.ttf").toExternalForm(), 10);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    protected Assembly assembly;
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        //Workspace w = new Workspace();
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/mainwindow.fxml")
            );

            BorderPane page = (BorderPane) loader.load();
            SimulationController controller = (SimulationController) loader.getController();
            controller.setStage(primaryStage);
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("VersaTILE Simulator");
            primaryStage.show();

            if (assembly != null) {
                controller.loadAssembly(assembly);
            }

        } catch (IOException ie) {
            System.out.println(ie.getMessage());
            ie.printStackTrace();
        }
    }
}

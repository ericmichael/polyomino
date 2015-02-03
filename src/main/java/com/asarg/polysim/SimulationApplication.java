package com.asarg.polysim;

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
    protected Assembly assembly;
    static {
        Font.loadFont(Main.class.getResource("resources/fontawesome.ttf").toExternalForm(), 10);
    }
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        //Workspace w = new Workspace();
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("mainwindow.fxml")
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
        }
    }
}

package com.asarg.polysim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Font;
import javafx.application.Application;

public class Main extends Application {
    static {
        Font.loadFont(Main.class.getResource("resources/fontawesome.ttf").toExternalForm(), 10);
    }

    public static void main(String args[]) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        //Workspace w = new Workspace();
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("mainwindow.fxml")
            );
            BorderPane page = (BorderPane) loader.load();
            SimulationController controller = (SimulationController) loader.getController();
            controller.setStage(primaryStage);
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Untitled");
            primaryStage.show();

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
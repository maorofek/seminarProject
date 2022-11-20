package com.seminarproject;

import com.seminarproject.controller.Controller;
import com.seminarproject.View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Program extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        new Controller(new View(stage));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Program.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
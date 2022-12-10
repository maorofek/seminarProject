package com.seminarproject;

import com.seminarproject.controller.Controller;
import com.seminarproject.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Program extends Application {

    @Override
    public void start(Stage stage) {
        new Controller(new View(stage));
    }


    public static void main(String[] args) {
        launch();
    }
}
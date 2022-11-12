package com.seminarproject;

import com.seminarproject.View.View;
import com.seminarproject.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new Controller(new View(stage));
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.seminarproject.View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View {
    Stage primaryStage;

    public View(Stage _primaryStage) {
        primaryStage = _primaryStage;
        BorderPane root = new BorderPane();

        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        HBox hBox = new HBox();
        HBox hBox2 = new HBox();
        VBox buttonsVBox = new VBox();
        VBox radioButtonsVBox = new VBox();
        VBox spinnerVBox = new VBox();
        VBox sliderVBox = new VBox();
        Slider slider = new Slider(0, 60, 7);
        slider.setPrefWidth(350);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(1); //use the arrow keys to change the value
        Label sliderValue = new Label("7");
        Label startAt = new Label("start location");
        Label survivesNumber = new Label("How many will survive");

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sliderValue.setText(String.format("%.0f", t1));
            }
        });
        SpinnerValueFactory<Integer> startingAt = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        startingAt.setValue(1);
        SpinnerValueFactory<Integer> howManySurvive = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        howManySurvive.setValue(1);


        rightVBox.getChildren().addAll(hBox2, hBox);
        sliderVBox.getChildren().addAll(sliderValue, slider);
        sliderVBox.setAlignment(Pos.CENTER);

        hBox2.getChildren().addAll(sliderVBox);
        hBox2.setAlignment(Pos.TOP_CENTER);
        addButtonToVbox(buttonsVBox, "start");
        addButtonToVbox(buttonsVBox, "stop");

        addRadioButtonToVbox(radioButtonsVBox, "clockwise");
        addRadioButtonToVbox(radioButtonsVBox, "counter-clockwise");


        spinnerVBox.getChildren().add(startAt);
        spinnerVBox.getChildren().add(new Spinner<>(startingAt));
        spinnerVBox.getChildren().add(survivesNumber);
        spinnerVBox.getChildren().add(new Spinner<>(howManySurvive));

        hBox.setSpacing(25);
        hBox.getChildren().addAll(buttonsVBox, radioButtonsVBox, spinnerVBox);

        buttonsVBox.setSpacing(10);
        radioButtonsVBox.setSpacing(10);

        rightVBox.setAlignment(Pos.CENTER);

        hBox.setAlignment(Pos.BOTTOM_CENTER);


        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPositions(0.66f, 0.33f);


        root.setCenter(splitPane);


        Scene scene = new Scene(root, 1350, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Elazar shtok");
        primaryStage.show();
    }

    public Button addButtonToVbox(VBox vBox, String text) {
        Button button = new Button(text);
        vBox.getChildren().add(button);
        return button;
    }

    public Button addButtonToHBox(HBox hbox, String text) {
        Button button = new Button(text);
        hbox.getChildren().add(button);
        return button;
    }

    public RadioButton addRadioButtonToVbox(VBox vBox, String text) {
        RadioButton radioButton = new RadioButton(text);
        vBox.getChildren().add(radioButton);
        return radioButton;
    }

    public RadioButton addRadioButtonToHBox(HBox hbox, String text) {
        RadioButton radioButton = new RadioButton(text);
        hbox.getChildren().add(radioButton);
        return radioButton;
    }

}

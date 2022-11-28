package com.seminarproject.View;

import com.seminarproject.Program;
import com.seminarproject.model.Person;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class View {
    Stage primaryStage;
    Button startButton;
    Button stopButton;
    Slider slider;
    SpinnerValueFactory<Integer> startingAt;
    SpinnerValueFactory<Integer> howManySurvive;
    RadioButton clockwise;

    Image image;

    AnchorPane circleSpace;
    public int defaultSize;
    private int radio;
    private int radioLbl;
    private int winnerNumber;

    private List<Person> people;

    public View(Stage _primaryStage) {
        primaryStage = _primaryStage;
        BorderPane root = new BorderPane();

        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        VBox buttonsVBox = new VBox();
        VBox radioButtonsVBox = new VBox();
        VBox spinnerVBox = new VBox();
        VBox sliderVBox = new VBox();
        HBox hBox = new HBox();
        HBox hBox2 = new HBox();

        rightVBox.setStyle("-fx-background-color: #9698a4 ;");
        leftVBox.setStyle("-fx-background-color: #212121;");


        people = new ArrayList<>();
        circleSpace = new AnchorPane();
        circleSpace.setPrefHeight(leftVBox.getPrefHeight());
        circleSpace.setPrefWidth(leftVBox.getPrefWidth());
        circleSpace.setStyle("-fx-background-color: #212121;");
        leftVBox.getChildren().add(circleSpace);
        leftVBox.setAlignment(Pos.CENTER);

        slider = new Slider(0, 60, 7);
        slider.setPrefWidth(350);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(1);

        Label sliderValue = new Label("7");
        Label startAt = new Label("start location");
        Label survivesNumber = new Label("How many will survive");

        slider.valueProperty().addListener((observableValue, number, t1) -> {
            sliderValue.setText(String.format("%.0f", t1.floatValue()));
            init();
            updateCircle();
        });

        startingAt = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        startingAt.setValue(1);

        howManySurvive = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        howManySurvive.setValue(1);

        rightVBox.getChildren().addAll(hBox2, hBox);
        sliderVBox.getChildren().addAll(sliderValue, slider);
        sliderVBox.setAlignment(Pos.CENTER);

        hBox2.getChildren().addAll(sliderVBox);
        hBox2.setAlignment(Pos.TOP_CENTER);

        startButton = new Button("Start");
        stopButton = new Button("Stop");

        buttonsVBox.getChildren().addAll(startButton, stopButton);

        clockwise = new RadioButton("clockwise");
        clockwise.setSelected(true);
        RadioButton counterClockwise = new RadioButton("counter-clockwise");
        ToggleGroup toggleGroup = new ToggleGroup();
        clockwise.setToggleGroup(toggleGroup);
        counterClockwise.setToggleGroup(toggleGroup);
        radioButtonsVBox.getChildren().addAll(clockwise, counterClockwise);

        Spinner<Integer> spinner = new Spinner<>(startingAt);
        spinnerVBox.getChildren().add(startAt);
        spinnerVBox.getChildren().add(spinner);
        spinnerVBox.getChildren().add(survivesNumber);
        spinnerVBox.getChildren().add(new Spinner<>(howManySurvive));

        hBox.setSpacing(25);
        hBox.getChildren().addAll(buttonsVBox, radioButtonsVBox, spinnerVBox);

        buttonsVBox.setSpacing(10);
        radioButtonsVBox.setSpacing(10);

        rightVBox.setAlignment(Pos.CENTER);

        hBox.setAlignment(Pos.BOTTOM_CENTER);

        init();

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPositions(0.66f, 0.33f);

        root.setCenter(splitPane);

        Scene scene = new Scene(root, 1350, 579);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Elazar shtok");
        primaryStage.show();
    }

    private void init() {
        image = new Image(Objects.requireNonNull(Program.class.getResourceAsStream("/images/odin.png")),
                50, 50, true, true);
        defaultSize = getSliderValue();
        radio = 230;
        radioLbl = 40;
        people.clear();
        for (int i = 0; i < defaultSize; i++) {
            addSoldier();
        }
    }

    public void addSoldier() {
        people.add(new Person());
        circleSpace.getChildren().add(makeBox(image, 300, 240));
        updateCircle();
    }

    public void updateCircle() {
        for (int i = 0; i < people.size(); i++) {
            double angle = (((double) i) / people.size()) * 2 * Math.PI;
            double xpos = radio * Math.cos(angle) + 300;
            double ypos = radio * Math.sin(angle) + 260;
            double lblxpos = radioLbl * Math.cos(angle) + 15;
            double lblypos = radioLbl * Math.sin(angle) + 20;
            circleSpace.getChildren().get(i).setLayoutX(xpos);
            circleSpace.getChildren().get(i).setLayoutY(ypos);

            if (people.get(i).isAlive()) {
                circleSpace.getChildren().get(i).setOpacity(1);
            } else {
                circleSpace.getChildren().get(i).setOpacity(0.5);
            }

            AnchorPane plx = (AnchorPane) circleSpace.getChildren().get(i);
            plx.getChildren().get(0).setLayoutX(lblxpos);
            plx.getChildren().get(0).setLayoutY(lblypos);
        }
    }

    public void markSurvivor(int number) {
        int index = number - 1;
        for (int i = 0; i < people.size(); i++) {
            if (i != index) {
                people.get(i).kill();
            }
        }
        updateCircle();
    }

    public AnchorPane makeBox(Image img, double xpos, double ypos) {
        ImageView personImage = new ImageView(img);
        Label lblHead = new Label("" + people.size());
        lblHead.setStyle("-fx-text-fill: white;");
        lblHead.setFont(Font.font("", FontWeight.BOLD, 12));
        AnchorPane anchorSoldier = new AnchorPane();
        anchorSoldier.setLayoutX(xpos);
        anchorSoldier.setLayoutY(ypos);
        anchorSoldier.getChildren().addAll(lblHead, personImage);
        return anchorSoldier;
    }

    public boolean isClockWiseSelected() {
        return clockwise.isSelected();
    }

    public int getSliderValue() {
        return Integer.parseInt(String.format("%.0f", slider.getValue()));
    }

    public Integer getSurvivesNumber() {
        return howManySurvive.getValue();
    }

    public Integer getStartingAtValue() {
        return startingAt.getValue();
    }

    public void addEventHandlerToStartButton(EventHandler<MouseEvent> eventHandler) {
        startButton.setOnMouseClicked(eventHandler);
    }

    public void addEventHandlerToStopButton(EventHandler<MouseEvent> eventHandler) {
        stopButton.setOnMouseClicked(eventHandler);
    }

    public void setWinnerNumber(int winnerNumber) {
        this.winnerNumber = winnerNumber;
        System.out.println("winner number is " + winnerNumber);
    }
}

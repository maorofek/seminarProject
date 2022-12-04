package com.seminarproject.View;

import com.seminarproject.Program;
import com.seminarproject.model.Person;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class View {
    Stage primaryStage;
    Button startButton;
    Button stopButton;
    Slider slider;
    SpinnerValueFactory<Integer> startingAt;
    SpinnerValueFactory<Integer> howManySurvive;
    RadioButton clockwise;

    Image soldierImage;

    AnchorPane circleSpace;
    private final int imageRadius;
    private final int labelRadius;

    private List<Person> people;
    private Runnable gameInitializer;

    // ################### getters and setters ###################
    public boolean isClockWiseSelected() {
        return clockwise.isSelected();
    }

    public int getSliderValue() {
        return Integer.parseInt(String.format("%.0f", slider.getValue()));
    }

    public Integer getHowManySurvive() {
        return howManySurvive.getValue();
    }

    public Integer getStartingAtValue() {
        return startingAt.getValue();
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public void setGameInitializer(Runnable gameInitializer) {
        this.gameInitializer = gameInitializer;
    }

    // ##########################################################

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
            onSliderChanged();
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

        buttonsVBox.getChildren().addAll(
                startButton
//                stopButton // only in the next version... :)
        );

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

        soldierImage = new Image(Objects.requireNonNull(Program.class.getResourceAsStream("/images/gimli.png")),
                                 50, 50, true, true);
        imageRadius = 230;
        labelRadius = 50;

        initSoldierImages();

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPositions(0.66f, 0.33f);

        root.setCenter(splitPane);

        Scene scene = new Scene(root, 1350, 579);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Elazar shtok");
        primaryStage.show();
    }

    /**
     * When slider value is changed we need to update the current state accordingly.
     */
    public void onSliderChanged() {
        gameInitializer.run();
        initSoldierImages();
        updateCircle();
    }

    private void initSoldierImages() {
        circleSpace.getChildren().clear();
        for (int i = 0; i < getSliderValue(); i++) {
            circleSpace.getChildren().add(makeBox(i + 1, 300, 240));
        }
    }

    /**
     * Use the current people list in the game state to draw them as a circle.
     */
    public void updateCircle() {
        if (people == null) {
            return;
        }
        for (int i = 0; i < people.size(); i++) {
            double angle = (((double) i) / people.size()) * 2 * Math.PI;
            double xPos = imageRadius * Math.cos(angle) + 300;
            double yPos = imageRadius * Math.sin(angle) + 260;
            double labelXPos = labelRadius * Math.cos(angle) + 15;
            double labelYPos = labelRadius * Math.sin(angle) + 20;

            circleSpace.getChildren().get(i).setLayoutX(xPos);
            circleSpace.getChildren().get(i).setLayoutY(yPos);

            if (people.get(i).isAlive()) {
                circleSpace.getChildren().get(i).setOpacity(1);
            } else {
                circleSpace.getChildren().get(i).setOpacity(0.5);
            }

            AnchorPane plx = (AnchorPane) circleSpace.getChildren().get(i);
            plx.getChildren().get(0).setLayoutX(labelXPos);
            plx.getChildren().get(0).setLayoutY(labelYPos);
        }
    }

    /**
     * draw a box with an image and a label for a person.
     * @param personNumber the number of the person (1-based index).
     * @param xPos x position of the box
     * @param yPos y position of the box
     * @return the box
     */
    public AnchorPane makeBox(int personNumber, double xPos, double yPos) {
        ImageView personImage = new ImageView(soldierImage);
        Label lblHead = new Label("" + personNumber);
        lblHead.setStyle("-fx-text-fill: white;");
        lblHead.setFont(Font.font("", FontWeight.BOLD, 12));
        AnchorPane anchorSoldier = new AnchorPane();
        anchorSoldier.setLayoutX(xPos);
        anchorSoldier.setLayoutY(yPos);
        anchorSoldier.getChildren().addAll(lblHead, personImage);
        return anchorSoldier;
    }

    /**
     * Add event handler to start button
     * @param eventHandler the event handler
     */
    public void addEventHandlerToStartButton(EventHandler<MouseEvent> eventHandler) {
        startButton.setOnMouseClicked(eventHandler);
    }

    /**
     * Add event handler to stop button
     * @param eventHandler the event handler
     */
    public void addEventHandlerToStopButton(EventHandler<MouseEvent> eventHandler) {
        stopButton.setOnMouseClicked(eventHandler);
    }

}

package com.seminarproject.view;

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

    Spinner<Integer> startingAtSpinner;
    Spinner<Integer> howManySurviveSpinner;
    RadioButton clockwise;

    ComboBox<Integer> iterationDelayComboBox;

    Label messageLbl;

    Image soldierImage;

    AnchorPane circleSpace;
    private static final int IMAGE_RADIUS = 230;
    private static final int LABEL_RADIUS = 50;

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
        return howManySurviveSpinner.getValue();
    }

    public Integer getStartingAtValue() {
        return startingAtSpinner.getValue();
    }

    public int getIterationDelay() {
        return iterationDelayComboBox.getValue();
    }

    public void setMessage(String message) {
        this.messageLbl.setText(message);
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

        messageLbl = new Label();
        messageLbl.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));

        iterationDelayComboBox = new ComboBox<>();
        iterationDelayComboBox.getItems().addAll(100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000);
        iterationDelayComboBox.setValue(1000);


        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox(25);

        VBox buttonsVBox = new VBox(10);
        VBox radioButtonsVBox = new VBox(10);
        VBox spinnerVBox = new VBox();

        VBox sliderVBox = new VBox();
        HBox buttonsHbox = new HBox(25);
        HBox sliderHbox = new HBox();
        VBox iterationDelayVBox = new VBox(10);

        iterationDelayVBox.getChildren().addAll(new Label("Time between iterations (ms):"), iterationDelayComboBox);
        buttonsHbox.getChildren().addAll(buttonsVBox, radioButtonsVBox, spinnerVBox);
        rightVBox.getChildren().addAll(iterationDelayVBox, sliderHbox, buttonsHbox, messageLbl);
        sliderHbox.getChildren().addAll(sliderVBox);

        buttonsVBox.setAlignment(Pos.CENTER);
        radioButtonsVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER);
        leftVBox.setAlignment(Pos.CENTER);
        buttonsHbox.setAlignment(Pos.BOTTOM_CENTER);
        sliderVBox.setAlignment(Pos.CENTER);
        sliderHbox.setAlignment(Pos.TOP_CENTER);
        iterationDelayVBox.setAlignment(Pos.CENTER);

        rightVBox.setStyle("-fx-background-color: #9698a4 ;");
        leftVBox.setStyle("-fx-background-color: #212121;");

        circleSpace = new AnchorPane();
        circleSpace.setPrefHeight(leftVBox.getPrefHeight());
        circleSpace.setPrefWidth(leftVBox.getPrefWidth());
        circleSpace.setStyle("-fx-background-color: #212121;");
        leftVBox.getChildren().add(circleSpace);

        slider = new Slider(0, 50, 7);
        slider.setPrefWidth(350);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);

        Label sliderValue = new Label("Number of people: 7");

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderValue.setText(String.format("Number of people: %.0f", newValue.floatValue()));
            onSliderChanged();
        });

        sliderVBox.getChildren().addAll(sliderValue, slider);

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

        startingAtSpinner = new Spinner<>(1, 10, 1);
        howManySurviveSpinner = new Spinner<>(1, 10, 1);

        spinnerVBox.getChildren().addAll(
                new Label("Starting number:"),
                startingAtSpinner,
                new Label("How many will survive:"),
                howManySurviveSpinner
        );

        soldierImage = new Image(
                Objects.requireNonNull(Program.class.getResourceAsStream("/images/gimli.png")),
                50,
                50,
                true,
                true
        );

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftVBox, rightVBox);
        splitPane.setDividerPositions(0.66f, 0.33f);

        Scene scene = new Scene(splitPane, 1350, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Josephus");
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        initSoldierImages();
    }

    /**
     * @return get an updated spinner value factory in accordance to the new number of people set by the slider.
     */
    private SpinnerValueFactory<Integer> getUpdatedSpinnerValueFactory() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Math.max(1,people.size()), 1);
    }

    /**
     * When slider value is changed we need to update the current state accordingly.
     */
    public void onSliderChanged() {
        gameInitializer.run();

        setMessage("");

        startingAtSpinner.setValueFactory(getUpdatedSpinnerValueFactory());
        howManySurviveSpinner.setValueFactory(getUpdatedSpinnerValueFactory());

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
            double xPos = IMAGE_RADIUS * Math.cos(angle) + 300;
            double yPos = IMAGE_RADIUS * Math.sin(angle) + 260;
            double labelXPos = LABEL_RADIUS * Math.cos(angle) + 15;
            double labelYPos = LABEL_RADIUS * Math.sin(angle) + 20;

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
     *
     * @param personNumber the number of the person (1-based index).
     * @param xPos         x position of the box
     * @param yPos         y position of the box
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
     *
     * @param eventHandler the event handler
     */
    public void addEventHandlerToStartButton(EventHandler<MouseEvent> eventHandler) {
        startButton.setOnMouseClicked(eventHandler);
    }

    /**
     * Add event handler to stop button
     *
     * @param eventHandler the event handler
     */
    public void addEventHandlerToStopButton(EventHandler<MouseEvent> eventHandler) {
        stopButton.setOnMouseClicked(eventHandler);
    }

}

package com.seminarproject.controller;

import com.seminarproject.view.View;
import com.seminarproject.model.Game;
import com.seminarproject.model.Game.GameBuilder;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    Game game;
    Timer timer;
    boolean running = false;

    /**
     * Our app's main controller, which is responsible for connecting the model and the view.
     *
     * @param view The view managing class of the app.
     */
    public Controller(View view) {
        timer = new Timer();

        /*
               TODO for next version:
               1. make spinners max value dynamic.
               2. make sleep between iterations a user input.

         */

        Runnable updatePeopleInView = () -> view.setPeople(game.getPeople());

        Runnable gameInitializer = () -> {
            // update model
            game = GameBuilder.aGameWith()
                    .numberOfPeople(view.getSliderValue())
                    .startingAt(view.getStartingAtValue())
                    .howManyToKeep(view.getHowManySurvive())
                    .clockwise(view.isClockWiseSelected())
                    .build();

            // update view
            updatePeopleInView.run();
        };

        Runnable play = () -> {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.killNext();
                    updatePeopleInView.run();
                    view.updateCircle();
                    if (game.isGameOver()) {
                        timer.cancel();
                        Platform.runLater(() -> view.setMessage("Game Over!\nThe survivor numbers are: " + game.getSurvivorNumbers()));
                    }
                }
            }, 0, 200);
        };

        view.setGameInitializer(gameInitializer);

        view.addEventHandlerToStartButton(event -> {
            gameInitializer.run();
            play.run();
        });

//        view.addEventHandlerToStopButton(event -> { // only in the next version... :)
//            // TODO add logic
//            System.out.println("stop");
//        });


        view.onSliderChanged();
    }

}

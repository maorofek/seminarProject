package com.seminarproject.controller;

import com.seminarproject.view.View;
import com.seminarproject.model.Game;
import com.seminarproject.model.Game.GameBuilder;
import javafx.application.Platform;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    Game game;
    Timer timer;
    int iterationDelay = 1000;
    boolean running = false;

    /**
     * Our app's main controller, which is responsible for connecting the model and the view.
     *
     * @param view The view managing class of the app.
     */
    public Controller(View view) {
        /*
            TODO: maybe add stop / pause button.
         */

        timer = new Timer();

        Runnable updatePeopleInView = () -> view.setPeople(game.getPeople());

        Runnable gameInitializer = () -> {
            // update model
            timer.cancel();
            iterationDelay = view.getIterationDelay();
            game = GameBuilder.aGameWith()
                    .numberOfPeople(view.getSliderValue())
                    .startingAt(view.getStartingAtValue())
                    .howManyToKeep(view.getHowManySurvive())
                    .clockwise(view.isClockWiseSelected())
                    .build();

            // update view
            view.setMessage("");
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

                        List<Integer> survivorNumbers = game.getSurvivorNumbers();
                        String survivorNumbersString = String.format(
                                "The survivor number%s %s",
                                survivorNumbers.size() > 1 ? "s are" : " is",
                                survivorNumbers.size() > 1 ? survivorNumbers.toString() : survivorNumbers.get(0)
                        );

                        Platform.runLater(() -> view.setMessage("Game Over!\n" + survivorNumbersString));
                    }
                }
            }, 0, iterationDelay);
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

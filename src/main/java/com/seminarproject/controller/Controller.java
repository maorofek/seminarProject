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

    /**
     * Our app's main controller, which is responsible for connecting the model and the view.
     *
     * @param view The view managing class of the app.
     */
    public Controller(View view) {
        timer = new Timer();

        // a runnable that updates the view with the current state of the circle.
        Runnable viewPeopleUpdater = () -> view.setPeople(game.getPeople());

        // a runnable that initializes the game in the model and view according to the user's input.
        Runnable gameInitializer = () -> {
            timer.cancel();

            // update model
            iterationDelay = view.getIterationDelay();
            game = GameBuilder.aGameWith()
                    .numberOfPeople(view.getSliderValue())
                    .startingAt(view.getStartingAtValue())
                    .howManyToKeep(view.getHowManySurvive())
                    .clockwise(view.isClockWiseSelected())
                    .build();

            // update view
            view.setMessage("");
            viewPeopleUpdater.run();
        };

        view.setGameInitializer(gameInitializer);

        view.addEventHandlerToStartButton(event -> {
            gameInitializer.run();
            view.updateCircle();
            view.disableInput();

            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.killNext();
                    viewPeopleUpdater.run();
                    view.updateCircle();
                    if (game.isGameOver()) {
                        timer.cancel();

                        List<Integer> survivorNumbers = game.getSurvivorNumbers();
                        String survivorNumbersString = String.format(
                                "The survivor number%s: %s",
                                survivorNumbers.size() > 1 ? "s are" : " is",
                                survivorNumbers.size() > 1 ? survivorNumbers.toString() : survivorNumbers.get(0)
                        );

                        Platform.runLater(() -> {
                            view.setMessage("Game Over!\n" + survivorNumbersString);
                            view.enableInput();
                        });
                    }
                }
            }, iterationDelay, iterationDelay);
        });

        view.addEventHandlerToResetButton(event -> {
            timer.cancel();
            view.enableInput();
            gameInitializer.run();
            view.updateCircle();
        });

        view.onSliderChanged();
    }

}

package com.seminarproject.controller;

import com.seminarproject.view.View;
import com.seminarproject.model.Game;
import com.seminarproject.model.Game.GameBuilder;

public class Controller {
    Game game;

    /**
     * Our app's main controller, which is responsible for connecting the model and the view.
     * @param view The view managing class of the app.
     */
    public Controller(View view) {
        /*
               TODO for next version:
               1. put sleep and view update on every single game iteration.
               2. make spinners max value dynamic.
               3. add support for jumpSize as input from user.

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

        view.setGameInitializer(gameInitializer);

        view.addEventHandlerToStartButton(event -> {
            gameInitializer.run();
            game.play();
            updatePeopleInView.run();
            view.updateCircle();
        });

//        view.addEventHandlerToStopButton(event -> { // only in the next version... :)
//            // TODO add logic
//            System.out.println("stop");
//        });


        view.onSliderChanged();
    }

}

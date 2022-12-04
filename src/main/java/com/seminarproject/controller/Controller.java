package com.seminarproject.controller;

import com.seminarproject.View.View;
import com.seminarproject.model.Game;
import com.seminarproject.model.Game.GameBuilder;

public class Controller {
    Game game;

    public Controller(View view) {
        /*
               TODO:
               1. write single iteration of the game (model + view)
               2. write game loop (model + view)
               3. make spinners max value dynamic

         */

        Runnable gameInitializer = () -> {
            // update model
            game = GameBuilder.aGameWith()
                    .numberOfPeople(view.getSliderValue())
                    .startingAt(view.getStartingAtValue())
                    .howManyToKeep(view.getSurvivesNumber())
                    .clockwise(view.isClockWiseSelected())
                    .build();

            // update view
            view.setPeople(game.getPeople());
        };

        view.setGameInitializer(gameInitializer);

        view.addEventHandlerToStartButton(event -> {
            // TODO should start the game loop now

            System.out.println(game);
            int winner = game.getLastSurvivor(view.getStartingAtValue() + 1);
            System.out.println("winner number is " + winner);

            game.markSurvivor(winner);
            view.updateCircle();
        });

        view.addEventHandlerToStopButton(event -> {
            // TODO add logic
            System.out.println("stop");
        });


        view.onSliderChanged();
    }

}

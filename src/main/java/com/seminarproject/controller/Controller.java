package com.seminarproject.controller;

import com.seminarproject.View.View;
import com.seminarproject.model.Game;
import javafx.scene.layout.VBox;

import java.util.stream.IntStream;

public class Controller {
    Game game;

    public Controller(View view) {

        view.addEventHandlerToStartButton(event -> {
            game = new Game.GameBuilder()
                    .numberOfPeople(view.getSliderValue())
                    .startingAt(view.getStartingAtValue())
                    .howManySurvive(view.getSurvivesNumber())
                    .clockwise(view.isClockWiseSelected())
                    .build();

            System.out.println(game);
            int winner = game.getLastSurvivor(view.getStartingAtValue() + 1);
            view.setWinnerNumber(winner);
            view.markSurvivor(winner);
        });
        view.addEventHandlerToStopButton(event -> {
            System.out.println("stop");
        });

    }


}

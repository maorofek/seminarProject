package com.seminarproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
    int numberOfPeople;
    int startingAt;
    int howManySurvive;
    boolean clockwise;
    List<Person> people;

    private Game() {

    }

    public int getLastSurvivor() {
        return IntStream.range(1, people.size() + 1).reduce(0, (x, y) -> (x + 1) % y) + 1;
    }

    @Override
    public String toString() {
        return "Game{" +
                "numberOfPeople=" + numberOfPeople +
                ", startingAt=" + startingAt +
                ", howManySurvive=" + howManySurvive +
                ", clockwise=" + clockwise +
                ", people=" + people +
                '}';
    }

    public static final class GameBuilder {
        private int numberOfPeople;
        private int startingAt;
        private int howManySurvive;
        private boolean clockwise;
        private List<Person> people;

        public GameBuilder() {
        }


        public static GameBuilder aGame() {
            return new GameBuilder();
        }

        public GameBuilder numberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
            return this;
        }

        public GameBuilder startingAt(int startingAt) {
            this.startingAt = startingAt;
            return this;
        }

        public GameBuilder howManySurvive(int howManySurvive) {
            this.howManySurvive = howManySurvive;
            return this;
        }

        public GameBuilder clockwise(boolean clockwise) {
            this.clockwise = clockwise;
            return this;
        }

        public Game build() {
            Game game = new Game();
            game.clockwise = this.clockwise;
            game.numberOfPeople = this.numberOfPeople;
            game.startingAt = this.startingAt;
            game.howManySurvive = this.howManySurvive;

            game.people = new ArrayList<>();
            for (int i = 0; i < numberOfPeople; i++) {
                game.people.add(new Person());
            }
            return game;
        }
    }
}

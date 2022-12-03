package com.seminarproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
    private int startingAt;
    private int howManyToKeep;
    private int jumpSize;
    private boolean clockwise;
    private List<Person> people;

    public int getStartingAt() {
        return startingAt;
    }

    public int getHowManyToKeep() {
        return howManyToKeep;
    }

    public int getJumpSize() {
        return jumpSize;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public List<Person> getPeople() {
        return people;
    }


    private Game() {
    }

    public int getLastSurvivor(int k) {
        return IntStream.range(1, people.size() + 1).reduce(0, (x, y) -> (x + k) % y) + 1;
    }

    public void markSurvivor(int number) {
        int index = number - 1;
        for (int i = 0; i < people.size(); i++) {
            if (i != index) {
                people.get(i).kill();
            }
        }
    }

    public boolean isGameOver() {
        return people.stream().filter(Person::isAlive).count() <= howManyToKeep;
    }

    public void killNext() {
        if (isGameOver()) {
            return;
        }

        if (clockwise) {
            startingAt = (startingAt + jumpSize) % people.size();
        } else {
            startingAt = (startingAt - jumpSize) % people.size();
            if (startingAt < 0) {
                startingAt += people.size();
            }
        }

        people.get(startingAt).kill();
    }

    @Override
    public String toString() {
        return "Game{" +
                "numberOfPeople=" + people.size() +
                ", startingAt=" + startingAt +
                ", howManySurvive=" + howManyToKeep +
                ", clockwise=" + clockwise +
                ", people=" + people +
                '}';
    }

    public static final class GameBuilder {
        private int numberOfPeople;
        private int startingAt;
        private int howManyToKeep;
        private boolean clockwise;
        private int jumpSize;

        private List<Person> people;

        private GameBuilder() {
        }

        public static GameBuilder aGameWith() {
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

        public GameBuilder howManyToKeep(int howManyToKeep) {
            this.howManyToKeep = howManyToKeep;
            return this;
        }

        public GameBuilder clockwise(boolean clockwise) {
            this.clockwise = clockwise;
            return this;
        }

        public GameBuilder jumpSize(int jumpSize) {
            this.jumpSize = jumpSize;
            return this;
        }

        public Game build() {
            Game game = new Game();
            game.clockwise = this.clockwise;
            game.startingAt = this.startingAt;
            game.howManyToKeep = this.howManyToKeep;
            game.jumpSize = this.jumpSize;
            if (this.jumpSize == 0) {
                game.jumpSize = startingAt;
            }

            game.people = new ArrayList<>();
            for (int i = 0; i < numberOfPeople; i++) {
                game.people.add(new Person());
            }
            return game;
        }
    }
}

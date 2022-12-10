package com.seminarproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    private int currentPosition;
    private int howManyToKeep;
    private boolean clockwise;
    private List<Person> people;

    public List<Person> getPeople() {
        return people;
    }

    private Game() {} // private constructor to force using the builder


    /**
     * @return the list of indices (1-based) of the people who are alive.
     */
    public List<Integer> getSurvivorNumbers() {
        return IntStream.range(0, people.size())
                .filter(i -> people.get(i).isAlive())
                .mapToObj(i -> i + 1)
                .collect(Collectors.toList());
    }


    /**
     * Check if the game is over.
     *
     * @return if number of survivors is less than / equal to howManyToKeep.
     */
    public boolean isGameOver() {
        return people.stream().filter(Person::isAlive).count() <= howManyToKeep;
    }

    /**
     * kill the next person in the game.
     */
    public void killNext() {
        if (isGameOver()) {
            return;
        }
        people.get(getNextAliveIndex()).kill();
        currentPosition = getNextAliveIndex();
    }

    /**
     * @return index of the next alive person after jumping from the current position.
     */
    int getNextAliveIndex() {
        int index = currentPosition;
        do {
            if (clockwise) {
                index = (index + 1) % people.size();
            } else {
                index = (index - 1 + people.size()) % people.size();
            }
        } while (!people.get(index).isAlive());
        return index;
    }

    @Override
    public String toString() {
        return "Game{" +
                "numberOfPeople=" + people.size() +
                ", startingAt=" + currentPosition +
                ", howManySurvive=" + howManyToKeep +
                ", clockwise=" + clockwise +
                ", people=" + people +
                '}';
    }

    /**
     * Builder class for Game.
     * makes creating complex object much more readable and easier to maintain.
     */
    public static final class GameBuilder {
        private int numberOfPeople;
        private int startingAt;
        private int howManyToKeep;
        private boolean clockwise;

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

        /**
         * Assemble all given parameters and create a Game object.
         *
         * @return a Game object.
         */
        public Game build() {
            Game game = new Game();
            game.clockwise = this.clockwise;
            game.currentPosition = this.startingAt - 1;
            game.howManyToKeep = this.howManyToKeep;

            game.people = new ArrayList<>();
            for (int i = 0; i < numberOfPeople; i++) {
                game.people.add(new Person());
            }
            return game;
        }
    }
}

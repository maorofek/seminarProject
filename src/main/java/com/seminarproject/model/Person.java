package com.seminarproject.model;

public class Person {

    private boolean isAlive;

    public Person() {
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    //TODO add number to person
    @Override
    public String toString() {
        return "Person{" +
                "isAlive=" + isAlive +
                '}';
    }
}

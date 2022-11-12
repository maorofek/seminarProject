package com.seminarproject.model;

public class Person {

    private boolean isAlive;

    public Person() {
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill(Person s) {
        s.isAlive = false;
    }
}

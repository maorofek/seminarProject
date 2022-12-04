package com.seminarproject.model;

import static java.util.UUID.randomUUID;

public class Person {

    private final String id;
    private boolean isAlive;

    public Person() {
        isAlive = true;
        String uuid = randomUUID().toString(); // Generate random Universally Unique Identifier (UUID)
        id = uuid.substring(uuid.length() - 4); // last 4 chars of UUID
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    @Override
    public String toString() {
        return "Person{" +
                "isAlive=" + isAlive +
                ", id='" + id + '\'' +
                '}';
    }
}

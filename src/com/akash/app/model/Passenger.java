package com.akash.app.model;

public class Passenger {
    private String direction;
    private int count;

    public Passenger(String direction, int count) {
        this.direction = direction;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getDirection() {
        return direction;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

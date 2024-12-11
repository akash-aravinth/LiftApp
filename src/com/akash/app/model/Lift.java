package com.akash.app.model;

import java.util.ArrayList;
import java.util.List;

public class Lift {
    private static int no = 0;
    private int liftNo;
    private List<Integer> stations;
    private int increments;
    private int currentPos;
    private int destinationPos;
    private String status;
    private int noOfPassengers;

    public Lift(ArrayList<Integer> stations, int increments, int currentPos, int destinationPos, String status, int noOfPassengers) {
        this.liftNo = ++no;
        this.stations = stations;
        this.increments = increments;
        this.currentPos = currentPos;
        this.destinationPos = destinationPos;
        this.status = status;
        this.noOfPassengers = noOfPassengers;
    }

    public Lift(){
        stations = new ArrayList<>();
    }

    public int getLiftNo() {
        return liftNo;
    }

    public List<Integer> getStations() {
        return stations;
    }

    public void setStations(List<Integer> stations) {
        this.stations = stations;
    }

    public int getIncrements() {
        return increments;
    }

    public void setIncrements(int increments) {
        this.increments = increments;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getDestinationPos() {
        return destinationPos;
    }

    public void setDestinationPos(int destinationPos) {
        this.destinationPos = destinationPos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }
}

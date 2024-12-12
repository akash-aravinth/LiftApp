package com.akash.app.model;

import com.akash.app.db.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Requests implements Runnable {
    private Map<Integer,Integer> pasengersMap;
    private int source;
    private int destination;
    private String status;
    private Lift lift;
    public Requests(){

    }
    public Requests(int source, int destination, String status) {
        this.source = source;
        this.destination = destination;
        this.status = status;
        this.lift = null;
        pasengersMap = new HashMap<>();
    }

    public Map<Integer, Integer> getPasengersMap() {
        return pasengersMap;
    }

    public void setPasengersMap(Map<Integer, Integer> pasengersMap) {
        this.pasengersMap = pasengersMap;
    }

    public Lift getLift() {
        return lift;
    }

    public void setLift(Lift lift) {
        this.lift = lift;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void run() {
        if (source < destination){
            if (lift.getCurrentPos() > source){
                lift.setStatus("down");
                while (lift.getCurrentPos() > source){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (this.getPasengersMap().containsKey(-lift.getCurrentPos())){
                        lift.setNoOfPassengers(lift.getNoOfPassengers()-this.getPasengersMap().get(-lift.getCurrentPos()));
                        this.getPasengersMap().remove(-lift.getCurrentPos());
                    }
                    lift.setCurrentPos(lift.getCurrentPos() - lift.getIncrements());
                }
            }
            lift.setStatus("up");
            while (lift.getCurrentPos() < lift.getDestinationPos()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (this.getPasengersMap().containsKey(lift.getCurrentPos())){
                    lift.setNoOfPassengers(lift.getNoOfPassengers()-this.getPasengersMap().get(lift.getCurrentPos()));
                    this.getPasengersMap().remove(lift.getCurrentPos());
                }
                lift.setCurrentPos(lift.getCurrentPos()+ lift.getIncrements());
            }
            this.getPasengersMap().clear();
            Database.getDatabase().getCatchList().remove(this);
            lift.setNoOfPassengers(0);
            lift.setStatus("idle");
        }else {
            if (lift.getCurrentPos() < source){
                lift.setStatus("up");
                while (lift.getCurrentPos() < source){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (this.getPasengersMap().containsKey(lift.getCurrentPos())){
                        lift.setNoOfPassengers(lift.getNoOfPassengers()-this.getPasengersMap().get(lift.getCurrentPos()));
                        this.getPasengersMap().remove(lift.getCurrentPos());
                    }
                    lift.setCurrentPos(lift.getCurrentPos()+ lift.getIncrements());
                }
            }
            lift.setStatus("down");
            while (lift.getCurrentPos() > lift.getDestinationPos()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (this.getPasengersMap().containsKey(-lift.getCurrentPos())){
                    lift.setNoOfPassengers(lift.getNoOfPassengers()-this.getPasengersMap().get(-lift.getCurrentPos()));
                    this.getPasengersMap().remove(-lift.getCurrentPos());
                }
                lift.setCurrentPos(lift.getCurrentPos() - lift.getIncrements());
            }
            this.getPasengersMap().clear();
            Database.getDatabase().getCatchList().remove(this);
            lift.setStatus("idle");
            lift.setNoOfPassengers(0);
        }
    }
}

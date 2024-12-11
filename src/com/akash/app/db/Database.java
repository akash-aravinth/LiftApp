package com.akash.app.db;

import com.akash.app.model.Lift;
import com.akash.app.model.Passenger;
import com.akash.app.model.Requests;

import java.util.*;

public class Database {
    private static Database database;

    public static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private Deque<Requests> requests = new LinkedList<>();

    public synchronized Deque<Requests> getRequests() {
        return requests;
    }

    public synchronized void setRequests(Deque<Requests> requests) {
        this.requests = requests;
    }

    private List<Lift> lifts = new ArrayList<>();

    public synchronized List<Lift> getLifts() {
        return lifts;
    }

    public synchronized void setLifts(List<Lift> lifts) {
        this.lifts = lifts;
    }

    private List<Requests> catchList = new ArrayList<>();

    public synchronized List<Requests> getCatchList() {
        return catchList;
    }

    public synchronized void setCatchList(List<Requests> catchList) {
        this.catchList = catchList;
    }

    public void addLifts() {
        List<Integer> all = new ArrayList<>();
        List<Integer> odds = new ArrayList<>();
        List<Integer> evens = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            all.add(i);
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
        }
        Lift lift1 = new Lift(new ArrayList<>(all), 1, 0, 0, "idle", 0);
        Lift lift2 = new Lift(new ArrayList<>(all), 1, 0, 0, "idle", 0);
        Lift lift3 = new Lift(new ArrayList<>(odds), 2, 1, 1, "idle", 0);
        Lift lift4 = new Lift(new ArrayList<>(evens), 2, 0, 0, "idle", 0);
        Database.getDatabase().getLifts().add(lift1);
        Database.getDatabase().getLifts().add(lift2);
        Database.getDatabase().getLifts().add(lift3);
        Database.getDatabase().getLifts().add(lift4);
    }

    public void printLifts() {
        System.out.print("     No  ");
        for (int i = 0; i < 14; i++) {
            System.out.print("  " + i % 10);
        }
        System.out.println();
        for (Lift l : getLifts()) {
            System.out.print(l.getNoOfPassengers() + "  ");
            System.out.print("   " + l.getLiftNo() + "    ");
            for (int i = 0; i < l.getCurrentPos(); i++) {
                System.out.print("   ");
            }
            if (l.getStatus().equals("idle")) {
                System.out.print("\033[31m*\033[0m");
            } else if (l.getStatus().equals("up")) {
                System.out.print("\033[32m^\033[0m");
            } else if (l.getStatus().equals("down")) {
                System.out.print("\033[33mv\033[0m");
            } else {
                System.out.print("\033[34m-\033[0m");
            }
            System.out.println();
        }
    }

    public Lift isRequestAvailable(Requests request) {
        Lift lift = null;
        String movement = "";
        if (request.getSource() < request.getDestination()) {
            movement = "up";
        } else {
            movement = "down";
        }
        List<Lift> availableRequests = getAvailableRequests(request, movement);
        int steps = Integer.MAX_VALUE;
        if (!availableRequests.isEmpty()) {
            for (Requests r : getCatchList()) {
                Lift l = r.getLift();
                if (l.getStations().contains(r.getSource()) && l.getStations().contains(r.getDestination()) && l.getNoOfPassengers() + request.getPassengersMap().get(request.getDestination()).getCount() <= 10) {
                    int dis = ((Math.abs(l.getCurrentPos() - request.getSource())) % l.getIncrements());
                    if (dis < steps) {
                        steps = dis;
                        lift = l;
                    }
                }
            }
            if (lift != null) {
                lift.setNoOfPassengers(lift.getNoOfPassengers() + request.getPassengersMap().get(request.getDestination()).getCount());
            }
            if (lift != null) {
                if (lift.getStatus().equals("up")) {
                    if (lift.getDestinationPos() < request.getDestination()) {
                        lift.setDestinationPos(request.getDestination());
                    }
                } else if (lift.getStatus().equals("down")) {
                    if (lift.getDestinationPos() > request.getDestination()) {
                        lift.setDestinationPos(lift.getDestinationPos());
                    }
                }
            }
        }
        return lift;
    }

    private List<Lift> getAvailableRequests(Requests request, String movement) {
        List<Lift> availableRequests = new ArrayList<>();
        for (Requests r : getCatchList()) {
            Lift l = r.getLift();
            if (l.getStatus().equals(movement) && l.getStations().contains(request.getSource()) && l.getStations().contains(request.getDestination())) {
                if (movement.equals("up")) {
                    if (l.getCurrentPos() < request.getSource()) {
                        availableRequests.add(l);
                    }
                } else {
                    if (l.getCurrentPos() > request.getSource()) {
                        availableRequests.add(l);
                    }
                }
            }
        }
        return availableRequests;
    }

    public Lift isAvailableInIdle(Requests request) {
        Lift lift = null;
        List<Lift> liftList = new ArrayList<>();
        for (Lift l : getLifts()) {
            if (l.getStatus().equals("idle") && l.getStations().contains(request.getSource()) && l.getStations().contains(request.getDestination())) {
                liftList.add(l);
            }
        }
        int steps = Integer.MAX_VALUE;
        int speed = Integer.MAX_VALUE;
        for (Lift l : liftList) {
            int dis = ((Math.abs(l.getCurrentPos() - request.getSource())) % l.getIncrements());
            if (dis <= steps){
                steps = dis;
                lift = l;
            }
        }
        return lift;
    }

    public Lift getLiftNo(int no) {
        for (Lift l : getLifts()) {
            if (l.getLiftNo() == no) {
                return l;
            }
        }
        return null;
    }
}

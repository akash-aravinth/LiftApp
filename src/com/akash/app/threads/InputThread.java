package com.akash.app.threads;

import com.akash.app.db.Database;
import com.akash.app.model.Lift;
import com.akash.app.model.Passenger;
import com.akash.app.model.Requests;
import com.sun.net.httpserver.Request;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class InputThread implements Runnable {
    Scanner scanner = new Scanner(System.in);
    @Override
    public void run() {
        System.out.println("1 -> Take Lift\n2 -> Print Lift\n3 -> Add New Lift\n4 -> Edit Lift\n5 -> Stop");
        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    Requests request = getRequestFromUser();
                    boolean flag = processRequest(request);
                    while (!flag){
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Wait Some Time");
                        flag = processRequest(request);
                    }
                    break;
                }
                case 2: {
                    Database.getDatabase().printLifts();
                    break;
                }
                case 3: {
                    addNewLift();
                    break;
                }
                case 4: {
                    editLift();
                    break;
                }
                default: {
                    System.out.println("Enter a Valid Choice only on 1 -> 4");
                    break;
                }
            }
        }
    }

    private void editLift() {
        System.out.println("Enter the Lift No : ");
        int no = scanner.nextInt();
        Lift lift = Database.getDatabase().getLiftNo(no);
        if (lift == null){
            System.out.println("Enter Correct Lift No : ");
            return;
        }
        System.out.println("Do You Want to Edit stations then type yes else no");
        String yes = scanner.next();
        if (yes.equalsIgnoreCase(yes)){
            System.out.println("Enter Difference Between Two Lifts");
            int increment = scanner.nextInt();
            ArrayList<Integer> stations = new ArrayList<>();
            for (int i = 0;i<14;i++){
                if (i%increment == 0){
                    stations.add(i);
                }
            }
            lift.setStations(stations);
            lift.setIncrements(increment);
            lift.setNoOfPassengers(0);
            lift.setStatus("idle");
            lift.setCurrentPos(increment);
        }
        System.out.println("Do You Want to Stop The Lift Type yes else No");
        String terminate = scanner.next();
        if (terminate.equalsIgnoreCase("yes")){
            lift.setStatus("terminate");
        }
        System.out.println("Successfully Changed Lift Settings");
    }

    private void addNewLift() {
        System.out.println("Enter Difference Between Two Lifts");
        int increment = scanner.nextInt();
        ArrayList<Integer> stations = new ArrayList<>();
        for (int i = 0;i<14;i++){
            if (i%increment == 0){
                stations.add(i);
            }
        }
        Lift lift = new Lift(stations,increment,increment,0,"idle",0);
        Database.getDatabase().getLifts().add(lift);
        System.out.println("Successfully Added a new Lift");
    }

    private boolean processRequest(Requests request) {
        Lift requestLift = Database.getDatabase().isRequestAvailable(request);
        if (requestLift != null){
            System.out.println("Lift No : "+requestLift.getLiftNo()+" will take You.");
            return true;
        }
        Lift idleLift = Database.getDatabase().isAvailableInIdle(request);
        if (idleLift != null){
            idleLift.setNoOfPassengers(request.getPassengersMap().get(request.getDestination()).getCount());
            request.setLift(idleLift);
            idleLift.setDestinationPos(request.getDestination());
            Database.getDatabase().getRequests().addLast(request);
            System.out.println("Lift No : "+idleLift.getLiftNo()+" has assigned You.");
            return true;
        }
        return false;
    }


    private Requests getRequestFromUser() {
        System.out.println("Enter Source Floor : ");
        int source = scanner.nextInt();
        System.out.println("Enter Destination Floor : ");
        int destination = scanner.nextInt();
        System.out.println("Enter Number Of Passengers : ");
        int passengers = scanner.nextInt();
        String move = "";
        if (source < destination){
            move = "up";
        }else{
            move = "down";
        }
        Requests request = new Requests(source,destination,"");
        Passenger passenger = new Passenger(move,passengers);
        request.getPassengersMap().put(destination,passenger);
        return request;
    }
}

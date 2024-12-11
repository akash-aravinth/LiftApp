package com.akash.app;

import com.akash.app.db.Database;
import com.akash.app.model.Requests;
import com.akash.app.threads.InputThread;
import com.akash.app.threads.ProcessThread;

public class LiftApp {
    public static void main(String[] args) {
        Database.getDatabase().addLifts();
        Database.getDatabase().printLifts();
        Thread inputThread = new Thread(new InputThread());
        inputThread.start();
        Thread processThread = new Thread(new ProcessThread());
        processThread.start();
    }
}
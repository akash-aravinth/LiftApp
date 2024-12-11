package com.akash.app.threads;

import com.akash.app.db.Database;
import com.akash.app.model.Requests;

import java.util.concurrent.*;

public class ProcessThread implements Runnable {
    @Override
    public void run() {
        BlockingQueue<Runnable> requests = new LinkedBlockingQueue<>(10);
        ExecutorService service = new ThreadPoolExecutor(4,5,Long.MAX_VALUE, TimeUnit.SECONDS,requests);
        while (true) {
            if (!Database.getDatabase().getRequests().isEmpty()){
                int size = Database.getDatabase().getRequests().size();
                for (int i = 0;i<size;i++){
                    Requests task = Database.getDatabase().getRequests().poll();
                    Database.getDatabase().getCatchList().add(task);
                    service.execute(task);
                }
            }else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

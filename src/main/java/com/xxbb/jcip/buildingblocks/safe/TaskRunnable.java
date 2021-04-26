package com.xxbb.jcip.buildingblocks.safe;

import java.util.concurrent.BlockingQueue;

public class TaskRunnable implements Runnable {

    BlockingQueue<Task> queue;

    @Override
    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void processTask(Task t) {

    }

    interface Task {

    }
}

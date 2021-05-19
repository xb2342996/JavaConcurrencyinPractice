package com.xxbb.jcip.cancellationshutdown.safe;

import com.xxbb.jcip.annotation.GuardBy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogService {
    private final BlockingQueue<String> queue;
    @GuardBy("this")
    private boolean isShutdown;
    @GuardBy("this")
    private int reservations;
    private final PrintWriter writer;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;

    public LogService(Writer writer) {
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.writer = new PrintWriter(writer);
        this.logger = new LoggerThread();
    }

    public void start() {
        logger.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        logger.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) {
                throw new IllegalStateException();
            }
            ++reservations;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if (isShutdown && reservations == 0) {
                                break;
                            }
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            -- reservations;
                        }
                        writer.println(msg);
                    } catch (InterruptedException e) {
                        // retry
                    }
                }
            } finally {
                writer.close();
            }
        }
    }
}

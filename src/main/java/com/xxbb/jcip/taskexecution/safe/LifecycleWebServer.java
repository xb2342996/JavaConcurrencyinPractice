package com.xxbb.jcip.taskexecution.safe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LifecycleWebServer {
    private final ExecutorService exec = Executors.newCachedThreadPool();

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket connection = socket.accept();
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(connection);
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown())
                    log("task is rejected", e);
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    public void handleRequest(Socket socket) {
        Request req = readRequest(socket);
        if (isShutdownRequest(req)) {
            stop();
        } else {
            dispatchRequest(req);
        }
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }

    interface Request {

    }

    private Request readRequest(Socket s) {
        return null;
    }

    private void dispatchRequest(Request r) {
    }

    private boolean isShutdownRequest(Request r) {
        return false;
    }
}

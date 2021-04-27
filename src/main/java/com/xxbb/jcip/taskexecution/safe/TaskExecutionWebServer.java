package com.xxbb.jcip.taskexecution.safe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {
    private static final int N_THREADs = 100;
    private static final Executor ex = Executors.newFixedThreadPool(N_THREADs);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            ex.execute(task);
        }
    }

    public static void handleRequest(Socket socket) {

    }
}

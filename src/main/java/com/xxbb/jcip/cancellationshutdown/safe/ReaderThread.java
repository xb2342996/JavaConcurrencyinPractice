package com.xxbb.jcip.cancellationshutdown.safe;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReaderThread extends Thread {
    private static final int BUFSZ = 512;
    private final Socket socket;
    private final InputStream is;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = is.read(buf);
                if (count < 0) {
                    break;
                } else if (count > 0) {
                    processBuffer(count, buf);
                }
            }
        } catch (IOException e) {
            // allow quit thread
        }
    }

    public void processBuffer(int count, byte[] buf) {

    }

    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }
}

package com.xxbb.jcip.cancellationshutdown.safe;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IndexingService {

    private static final int CAPACITY = 1000;
    private final File POISON = new File("");
    private final File root;
    private final FileFilter fileFilter;
    private final BlockingQueue<File> queue;
    private final IndexThread consumer = new IndexThread();
    private final CawlerThread producer = new CawlerThread();

    public IndexingService(File root, final FileFilter fileFilter) {
        this.root = root;
        this.fileFilter = pathname -> pathname.isDirectory() || fileFilter.accept(pathname);
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    private class CawlerThread extends Thread {
        @Override
        public void run() {
            try {
                cawler(root);
            } catch (InterruptedException ignored) {
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e) {
                        // retry
                    }
                }
            }
        }

        private void cawler(File root) throws InterruptedException {

        }
    }

    private class IndexThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void indexFile(File file) {

        }
    }
}

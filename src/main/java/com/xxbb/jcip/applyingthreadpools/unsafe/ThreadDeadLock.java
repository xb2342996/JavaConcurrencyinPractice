package com.xxbb.jcip.applyingthreadpools.unsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadDeadLock {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public class renderPageTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // waiting for sub task, cause deadlock
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            return null;
        }
    }

    public class LoadFileTask implements Callable<String> {
        private final String fileName;
        public LoadFileTask(String s) {
            this.fileName = s;
        }

        @Override
        public String call() throws Exception {
            return fileName;
        }
    }
}

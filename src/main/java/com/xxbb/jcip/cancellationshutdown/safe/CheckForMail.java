package com.xxbb.jcip.cancellationshutdown.safe;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CheckForMail {
    public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewEmail = new AtomicBoolean(false);
        try {
            for (String host : hosts) {
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (checkMail(host)) {
                            hasNewEmail.set(true);
                        }
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }
        return hasNewEmail.get();
    }

    public boolean checkMail(String host) {
        return false;
    }
}

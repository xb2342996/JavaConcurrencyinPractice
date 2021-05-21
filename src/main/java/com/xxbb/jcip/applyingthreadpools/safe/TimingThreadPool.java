package com.xxbb.jcip.applyingthreadpools.safe;

import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class TimingThreadPool extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final Logger logger = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong(0);
    private final AtomicLong totalTime = new AtomicLong(0);

    public TimingThreadPool() {
        super(1, 1, 0L, TimeUnit.SECONDS, null);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        logger.fine(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long end = System.nanoTime();
            long period = end - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(period);
            logger.fine(String.format("Thread %s: end %s, time=%dns", t, r, period));
        } finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void terminated() {
        try {
            logger.info(String.format("Terminated: avg time=%dns", totalTime.get()/numTasks.get()));
        } finally {
            super.terminated();
        }
    }
}

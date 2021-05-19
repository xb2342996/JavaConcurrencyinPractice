package com.xxbb.jcip.cancellationshutdown.safe;

import com.xxbb.jcip.buildingblocks.safe.LaundryThrowable;

import java.util.concurrent.*;

public class TimeRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timeRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {

        } catch (ExecutionException e) {
            throw LaundryThrowable.launderThrowable(e.getCause());
        } finally {
            task.cancel(true);
        }
    }
}

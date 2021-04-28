package com.xxbb.jcip.taskexecution.safe;

import java.sql.Time;
import java.util.concurrent.*;

public class RenderWithTimeBudget {

    private final static Ad DEFAULT_AD = new Ad();
    private final static long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    public Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAd());
        Page page = renderPageBody();
        Ad ad;
        try {
            long time = endNanos - System.nanoTime();
            ad = f.get(time, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }
        page.setAd(ad);
        return page;
    }

    Page renderPageBody() {
        return new Page();
    }

    static class Ad {

    }

    static class Page {
        public void setAd(Ad ad) {

        }
    }

    static class FetchAd implements Callable<Ad> {

        @Override
        public Ad call() throws Exception {
            return new Ad();
        }
    }
}

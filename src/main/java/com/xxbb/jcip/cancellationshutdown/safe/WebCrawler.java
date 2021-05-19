package com.xxbb.jcip.cancellationshutdown.safe;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class WebCrawler {
    
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS; 
    
    private final Set<URL> urlsToCrawler = new HashSet<>();
    private volatile TrackingExecutor exec;
    private final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<>();

    public WebCrawler(URL startUrl) {
        urlsToCrawler.add(startUrl);
    }

    public void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawler) {
            submitCrawlTask(url);
        }
        urlsToCrawler.clear();
    }
    
    public void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT)) {
                saveUncrawled(exec.getCancelledTasks());
            }
        } finally {
            exec = null;
        }
    }
    
    private void saveUncrawled(List<Runnable> uncrawled) {
        for (Runnable task :
                uncrawled) {
            urlsToCrawler.add(((CrawlerTask) task).getPage());
        }
    }
    
    protected abstract List<URL> processPage(URL url);

    private void submitCrawlTask(URL u) {
        exec.submit(new CrawlerTask(u));
    }
    
    private class CrawlerTask implements Runnable {
        private final URL url;

        private int count = 1;
        
        public CrawlerTask(URL url) {
            this.url = url;
        }
        
        public boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }
        
        public void markUncrawler() {
            seen.remove(url);
            System.out.printf("marking %s uncrawled%n", url);
        }
        
        @Override
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                submitCrawlTask(link);
            }
        }
        
        public URL getPage() {
            return url;
        }
    }
}

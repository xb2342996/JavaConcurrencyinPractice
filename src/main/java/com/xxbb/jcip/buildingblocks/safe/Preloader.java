package com.xxbb.jcip.buildingblocks.safe;

import javax.xml.crypto.Data;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Preloader {
    private final FutureTask<ProductInfo> future = new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
        @Override
        public ProductInfo call() throws Exception {
            return loadProductInfo();
        }
    });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            } else {
                throw LaundryThrowable.launderThrowable(cause);
            }
        }
    }

    static class DataLoadException extends Exception { }

    private ProductInfo loadProductInfo() {
        return new ProductInfo();
    }

    public static class ProductInfo {

    }
}

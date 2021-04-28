package com.xxbb.jcip.taskexecution.safe;

import com.xxbb.jcip.buildingblocks.safe.LaundryThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureRender {
    private final ExecutorService exec = Executors.newCachedThreadPool();

    public void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = new Callable<List<ImageData>>() {
            @Override
            public List<ImageData> call() throws Exception {
                ArrayList<ImageData> imageData = new ArrayList<>();
                for (ImageInfo info :
                        imageInfos) {
                    imageData.add(info.downloadImage());
                }
                return imageData;
            }
        };
        Future<List<ImageData>> future = exec.submit(task);
        renderText(source);

        try {
            List<ImageData> imagedata = future.get();
            for (ImageData data :
                    imagedata) {
                renderImage(data);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
        } catch (ExecutionException e) {
            throw LaundryThrowable.launderThrowable(e.getCause());
        }
    }


    void renderImage(ImageData data) {

    }

    List<ImageInfo> scanForImageInfo(CharSequence source) {
        return new ArrayList<>();
    }

    void renderText(CharSequence source) {

    }

    static class ImageData {

    }

    static interface ImageInfo {
        ImageData downloadImage();
    }
}

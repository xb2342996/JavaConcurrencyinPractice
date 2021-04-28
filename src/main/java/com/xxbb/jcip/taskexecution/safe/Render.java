package com.xxbb.jcip.taskexecution.safe;

import com.xxbb.jcip.buildingblocks.safe.LaundryThrowable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Render {
    private final ExecutorService executor;

    public Render(ExecutorService executor) {
        this.executor = executor;
    }

    public void renderPage(CharSequence source) {
        final List<ImageInfo> infos = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
        for (ImageInfo info : infos) {
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {
                    return info.downloadImage();
                }
            });
        }

        try {
            for (ImageInfo info : infos) {
                Future<ImageData> f = completionService.take();
                ImageData data = f.get();
                renderImage(data);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

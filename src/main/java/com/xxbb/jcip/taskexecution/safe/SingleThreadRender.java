package com.xxbb.jcip.taskexecution.safe;

import java.util.ArrayList;
import java.util.List;

public class SingleThreadRender {
    void renderPage(CharSequence source) {
        renderText(source);
        List<ImageData> imageData = new ArrayList<>();
        for (ImageInfo info : scanForImageInfo(source)) {
            imageData.add(info.downloadImage());
        }

        for (ImageData data: imageData) {
            renderImage(data);
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

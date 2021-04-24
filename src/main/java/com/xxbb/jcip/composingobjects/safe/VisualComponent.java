package com.xxbb.jcip.composingobjects.safe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VisualComponent {

    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
    private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<>();


    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeKeyLister(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void removeMouseLister(MouseListener listener) {
        mouseListeners.remove(listener);
    }


    private static class KeyListener {

    }

    private static class MouseListener {

    }
}

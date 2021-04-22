package com.xxbb.jcip.sharingobjects.safe;

public class SafeListener {

    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    private void doSomething(Event e) {

    }

    private interface Event{

    }

    private interface EventSource {
        void registerListener(EventListener e);
    }

    private interface EventListener {
        void onEvent(Event e);
    }



}

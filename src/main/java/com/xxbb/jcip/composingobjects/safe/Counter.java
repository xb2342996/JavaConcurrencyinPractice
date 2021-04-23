package com.xxbb.jcip.composingobjects.safe;

import com.xxbb.jcip.annotation.GuardBy;

public final class Counter {
    @GuardBy("this")
    private long value;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {
        if (value == Long.MAX_VALUE) {
            throw new IllegalArgumentException("overflow");
        }
        return ++value;
    }
}

package com.xxbb.jcip.sharingobjects.safe;

import com.xxbb.jcip.annotation.GuardBy;
import com.xxbb.jcip.annotation.ThreadSafe;

@ThreadSafe
public class SynchronizedInteger {
    @GuardBy("this")
    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}

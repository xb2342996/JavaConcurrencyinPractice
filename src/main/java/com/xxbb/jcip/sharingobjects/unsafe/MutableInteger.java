package com.xxbb.jcip.sharingobjects.unsafe;

import com.xxbb.jcip.annotation.NotThreadSafe;

@NotThreadSafe
public class MutableInteger {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

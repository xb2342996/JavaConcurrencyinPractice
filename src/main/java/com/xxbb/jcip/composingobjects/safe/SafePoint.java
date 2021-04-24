package com.xxbb.jcip.composingobjects.safe;

import com.xxbb.jcip.annotation.GuardBy;
import com.xxbb.jcip.annotation.ThreadSafe;

@ThreadSafe
public class SafePoint {
    @GuardBy("this")
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint s) {
        this(s.get());
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized int[] get() {
        return new int[] {x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

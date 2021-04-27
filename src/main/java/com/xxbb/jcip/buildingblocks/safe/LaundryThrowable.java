package com.xxbb.jcip.buildingblocks.safe;

public class LaundryThrowable {
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException)t;
        } else if (t instanceof Error) {
            throw (Error) t;
        } else {
            throw new IllegalStateException("NOT CHECKED", t);
        }
    }
}

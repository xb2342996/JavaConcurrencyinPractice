package com.xxbb.jcip.applyingthreadpools.safe;

import com.xxbb.jcip.annotation.GuardBy;
import com.xxbb.jcip.annotation.ThreadSafe;

import java.util.concurrent.CountDownLatch;

@ThreadSafe
public class ValueLatch<T> {
    @GuardBy("this")
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T t) {
        if (!isSet()) {
            this.value = t;
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }
}

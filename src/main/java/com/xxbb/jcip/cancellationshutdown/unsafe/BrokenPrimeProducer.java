package com.xxbb.jcip.cancellationshutdown.unsafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            if (!cancelled) {
                queue.put(p.nextProbablePrime());
            }
        } catch (InterruptedException ignored) {

        }
    }

    public void cancel() {
        cancelled = true;
    }


}

package com.xxbb.jcip.buildingblocks.safe;

import java.util.concurrent.*;

public class Memorizer3<A, V> implements Computable<A, V> {

    private final ConcurrentHashMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memorizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run();
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw LaundryThrowable.launderThrowable(e.getCause());
        }
    }
}

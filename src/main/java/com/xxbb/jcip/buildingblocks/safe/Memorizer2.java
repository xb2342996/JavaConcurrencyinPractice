package com.xxbb.jcip.buildingblocks.safe;

import java.util.concurrent.ConcurrentHashMap;

public class Memorizer2<A, V> implements Computable<A, V> {

    private final ConcurrentHashMap<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memorizer2(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

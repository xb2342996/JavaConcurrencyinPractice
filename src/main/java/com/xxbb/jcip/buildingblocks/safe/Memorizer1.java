package com.xxbb.jcip.buildingblocks.safe;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import com.xxbb.jcip.annotation.GuardBy;

import java.util.HashMap;
import java.util.Map;

public class Memorizer1<A, V> implements Computable<A, V> {

    @GuardBy("this")
    private final Map<A, V> cache = new HashMap<A, V>();
    private final Computable<A, V> c;

    public Memorizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

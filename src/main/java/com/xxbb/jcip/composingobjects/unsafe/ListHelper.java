package com.xxbb.jcip.composingobjects.unsafe;

import com.xxbb.jcip.annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotThreadSafe
public class ListHelper<E> {
    private List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E e) {
        boolean abs = !list.contains(e);
        if (abs) {
            list.add(e);
        }
        return abs;
    }
}

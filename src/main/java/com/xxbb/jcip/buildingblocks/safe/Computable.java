package com.xxbb.jcip.buildingblocks.safe;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}

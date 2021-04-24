package com.xxbb.jcip.composingobjects.safe;

import com.xxbb.jcip.annotation.Immutable;

@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

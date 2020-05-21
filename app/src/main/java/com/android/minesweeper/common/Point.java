package com.android.minesweeper.common;

import com.google.gson.annotations.SerializedName;

public class Point {
    @SerializedName("x")
    public int x;
    @SerializedName("y")
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

package com.android.minesweeper.common;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

public class Cell extends Point {
    @SerializedName("revealed")
    boolean revealed = false;
    @SerializedName("flag")
    boolean flag = false;
    @SerializedName("bomb")
    transient boolean bomb = false;
    transient Integer hiddenNumber = 0;
    @SerializedName("number")
    Integer number = null;

    public transient Integer color;
    public transient Boolean lost;

    Cell(int x, int y) {
        super(x, y);
    }

    void setBomb() {
        this.bomb = true;
        this.hiddenNumber = null;
    }

    void reveal() {
        this.revealed = true;
        this.number = hiddenNumber;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public Integer getNumber() {
        return number;
    }

    public int getNumberValue() {
        return number == null ? -1 : number;
    }

    public Integer getColor() {
        return color;
    }

    public int getColorValue() {
        return color == null ? -1 : color;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isBomb() {
        return bomb;
    }

    public Point asPoint() {
        return new Point(x, y);
    }

    public boolean equals(Cell other) {
        return x == other.x && y == other.y &&
                isRevealed() == other.isRevealed() && isFlag() == other.isFlag() &&
                getNumberValue() == other.getNumberValue() && getColorValue() == other.getColorValue();
    }

    public boolean same(Cell other) {
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return "cell{ x: " + x + ", y: " + y + ", revealed: " + revealed + ", bomb: " + isBomb() + (number != null ? ", number: " + getNumber() : "") + " }";
    }
}

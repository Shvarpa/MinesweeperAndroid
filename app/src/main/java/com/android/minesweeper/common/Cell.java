package com.android.minesweeper.common;

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

    public boolean isFlag() {
        return flag;
    }

    public boolean isBomb() {
        return bomb;
    }

    public Point asPoint() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "cell{ x: " + x + ", y: " + y + ", revealed: " + revealed + ", bomb: " + isBomb() + (getNumber() != null ? ", number: " + getNumber() : "") + " }";
    }

//    public char toByte() {
//        char b = 0;
//        if (revealed) b |= 0x80;
//        if (revealed && bomb) b |= 0x40;
//        if (flag) b |= 0x20;
//        if (number != null) b |= (number & 0x1f);
//        return b;
//    }
//
//    public void fromByte(char b) {
//        revealed = ((b & 0x80) != 0);
//        bomb = ((b & 0x40) != 0);
//        flag = ((b & 0x20) != 0);
//        number = (b & 0x1f);
//    }
}

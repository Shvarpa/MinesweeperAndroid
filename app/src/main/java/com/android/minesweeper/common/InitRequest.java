package com.android.minesweeper.common;

import com.google.gson.annotations.SerializedName;

public class InitRequest {
    @SerializedName("rows")
    public int rows;
    @SerializedName("columns")
    public int columns;
    @SerializedName("bombs")
    public int bombs;

    public InitRequest(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
    }
}

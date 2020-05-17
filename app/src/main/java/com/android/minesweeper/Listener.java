package com.android.minesweeper;

public interface Listener<DataType> {
    void on(DataType data);
}

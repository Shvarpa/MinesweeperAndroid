package com.android.minesweeper.common;

import android.os.Build;
import android.util.Log;
import android.util.Range;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    @SerializedName(("grid"))
    List<List<Cell>> grid = new ArrayList<>();
    @SerializedName("rows")
    int rows;
    @SerializedName("columns")
    int columns;
    @SerializedName("won")
    Boolean won = null;
    @SerializedName("lost")
    Boolean lost = null;
    @SerializedName("bombs")
    Integer bombs = null;
    @SerializedName("flags")
    Integer flags = null;
    @SerializedName("left")
    Integer left = null;

    GameState(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            List<Cell> row = new ArrayList<>();
            for (int x = 0; x < columns; x++)
                row.add(new Cell(x, y));
            this.grid.add(row);
        }
    }

//    GameState(List<List<Cell>> grid) {
//        this.grid = grid;
//        this.rows = grid.size();
//        this.columns = grid.get(0).size();
//    }

    public Cell get(int x, int y) {
        try {
            return grid.get(y).get(x);
        } catch (Exception e) {
            return null;
        }
    }

    public Cell get(Point p) {
        return get(p.x, p.y);
    }

    public List<Cell> getNeighbours(Point point) {
        List<Cell> neighbours = new ArrayList<>();
        for (int x = point.x - 1; x <= point.x + 1; x++) {
            for (int y = point.y - 1; y <= point.y + 1; y++) {
                if (0 <= x && x < columns && 0 <= y && y < rows)
                    neighbours.add(get(x, y));
            }
        }
        return neighbours;
    }

    public int getFlagCount(Point p) {
        int count = 0;
        for (Cell c : getNeighbours(p)) {
            if (c.isFlag()) count++;
        }
        return count;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Boolean getLost() {
        return lost != null && lost;
    }

    public boolean getWon() {
        return won != null && won;
    }

    public List<List<Cell>> getGrid() {
        return grid;
    }

    public int getFlags() {
        return this.flags == null ? 0 : this.flags;
    }

    public int getBombs() {
        return this.bombs == null ? 0 : this.bombs;
    }

    @Override
    public String toString() {
        return "won: " + getWon() + ", lost: " + getLost() + ", " + this.grid.toString();
    }

    public int getRemaining() {
        return this.left == null ? 0 : this.left;
    }

//    private static int pre = 3;
//    public static GameState fromBytes(char[] bytes) throws Exception {
//        if (bytes.length < pre)
//            throw new Exception("bad|short gameState, won,lost,rows,columns missing, length: " + bytes.length);
//        Log.e("TAG", "fromBytes: " + bytes[0] + ", " + bytes[1] + ", " + bytes[2]);
//        int wl = bytes[0] >> 6;
//        Boolean won = (wl & 2) != 0;
//        Boolean lost = (wl & 1) != 0;
//        int rows = ((bytes[0] & 0x3f) << 5) | ((bytes[1] & 0xff) >> 3);
////        byte rows = bytes[0];
//        int columns = ((bytes[1] & 0x7) << 8) | (bytes[2] & 0xff);
////        byte columns = bytes[1];
//        Log.e("TAG", "fromBytes: " + won + ", " + lost + ", " + rows + ", " + columns);
////        if (columns < 0 || rows < 0)
////            throw new Exception("bad (rows, columns): (" + rows + ", " + columns + ")");
//        if (bytes.length < (rows * columns) + pre)
//            throw new Exception("bad gameState grid, should be " + ((rows * columns) + pre) + ", but is " + bytes.length + ".");
//        GameState gs = new GameState(rows, columns);
//        for (int i = pre; i < bytes.length; i++) {
//            gs.get(new Point((i - pre) % columns, (i - pre) / columns)).fromByte(bytes[i]);
//        }
//        gs.won = won;
//        gs.lost = lost;
//        return gs;
//    }
//
//    public char[] toBytes() {
//        int rows = this.rows & 0x03ff;
//        int columns = this.columns & 0x03ff;
//        char[] bytes = new char[(rows * columns) + pre];
//        bytes[0] = 0;
//        bytes[1] = 0;
//        bytes[2] = 0;
//        if (won != null) bytes[0] |= 0x80;
//        if (lost != null) bytes[0] |= 0x40;
//        bytes[0] |= ((rows >> 5) & 0x3f);
//        bytes[1] |= (((rows & 0x1f) << 3) & 0xf8) | ((columns >> 8) & 0x7);
//        bytes[2] |= ((columns & 0xff) & 0xff);
//        for (int i = pre; i < bytes.length; i++) {
//            bytes[i] = get(new Point((i - pre) % columns, (i - pre) / columns)).toByte();
//        }
//        return bytes;
//    }
}

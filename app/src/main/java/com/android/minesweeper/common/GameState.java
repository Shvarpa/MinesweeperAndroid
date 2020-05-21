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
    Point lost = null;
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

    public Cell get(int position) {
        return get(position % getColumns(), (int) (position / getColumns()));
    }

    public Cell get(Point p) {
        return get(p.x, p.y);
    }

    public int getSize() {
        return getRows() * getColumns();
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

    public List<Point> getNeighbouringPoints(Point point) {
        List<Point> neighbours = new ArrayList<>();
        for (int x = point.x - 1; x <= point.x + 1; x++) {
            for (int y = point.y - 1; y <= point.y + 1; y++) {
                if (0 <= x && x < columns && 0 <= y && y < rows)
                    neighbours.add(get(x, y).asPoint());
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

    public boolean getLost() {
        return lost != null;
    }

    public Point getLosingPoint() {
        return lost;
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

}

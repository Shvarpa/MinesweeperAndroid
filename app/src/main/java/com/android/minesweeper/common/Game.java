package com.android.minesweeper.common;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Game {
    GameState state;
    boolean started = false;
    Integer rows;
    Integer columns;
    int bombs;

    private void clear() {
        state = new GameState(rows, columns);
        started = false;
    }

    public void generateGame(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
        clear();
    }

    public boolean start(Point start) {
        if (rows == null || columns == null) return false;
        clear();
        this.generateBombs(bombs, start);
        started = true;
        return true;
    }

    public boolean reveal(Point p) {
        if (!started || state.won != null || state.lost != null) {
            start(p);
        }
        if (!started) return false;
        Log.e("TAG", "reveal: started:" + started + ", state:" + state);
        return revealAsStarted(p);
    }

    private boolean revealAsStarted(Point p) {
        Cell cell = state.get(p);
        if (cell.isRevealed()) return false;
        if (cell.isBomb()) {
            revealAll();
            state.lost = cell.asPoint();
            return true;
        }
        cell.reveal();
        state.left--;
        if (cell.hiddenNumber != null && cell.hiddenNumber == 0) {
            for (Cell neighbour : this.state.getNeighbours(cell)) {
                if (neighbour.isBomb()) break;
                if (neighbour.hiddenNumber != null && neighbour.hiddenNumber == 0) {
                    revealAsStarted(neighbour);
                } else {
                    if (!neighbour.isRevealed()) {
                        neighbour.reveal();
                        state.left--;
                    }
                }
            }
        }
        Log.e("TAG", "left: " + state.left);
        if (state.left == 0) {
            state.won = true;
            revealAll();
        }
        return true;
    }

    private void generateBombs(int count, Point start) {
        List<Point> bombs = new ArrayList<>();
        for (int x = 0; x < state.columns; x++) {
            for (int y = 0; y < state.rows; y++)
                if (start == null || start.x != x || start.y != y)
                    bombs.add(new Point(x, y));
        }
        while (bombs.size() > count) {
            bombs.remove((int) (Math.random() * bombs.size()));
        }

        for (Point p : bombs) {
            state.get(p).setBomb();
            for (Cell neighbour : this.state.getNeighbours(p)) {
                if (neighbour.hiddenNumber != null) neighbour.hiddenNumber++;
            }
        }

        this.state.bombs = this.bombs = bombs.size();
        state.left = state.rows * state.columns - this.bombs;
    }

    public boolean revealNeighbours(Point point) {
        GameActionValidator.neighbours(state, point);
        for (Cell neighbour : this.state.getNeighbours(point)) {
            if (!neighbour.isFlag()) revealAsStarted(neighbour);
        }
        return true;
    }

    public boolean flag(Point point) {
        Cell c = this.state.get(point);
        if (c.isRevealed()) return false;
        c.flag = !c.flag;
        state.flags = state.getFlags() + (c.flag ? 1 : -1);
        return true;
    }

    private void revealAll() {
        for (List<Cell> row : state.grid) {
            for (Cell cell : row)
                cell.reveal();
        }
    }

    public GameState getState() {
        return state;
    }

}

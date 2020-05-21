package com.android.minesweeper.common;

public class GameActionValidator {

    public static boolean reveal(GameState state, Point point) {
        if (state == null || point == null) return false;
        Cell cell = state.get(point);
        return (!cell.isRevealed() && !cell.isFlag()) || state.getWon() || state.getLost();
    }

    public static boolean flag(GameState state, Point point) {
        if (state == null || point == null) return false;
        if (state.getWon() || state.getLost()) return false;
        return !state.get(point).isRevealed();
    }

    public static boolean neighbours(GameState state, Point point) {
        if (state == null || point == null) return false;
        if (state.getWon() || state.getLost()) return false;
        Cell cell = state.get(point);
        int flagCount = state.getFlagCount(point);
        return cell.isRevealed() && cell.getNumber() != null && cell.getNumber() == flagCount;
    }
}

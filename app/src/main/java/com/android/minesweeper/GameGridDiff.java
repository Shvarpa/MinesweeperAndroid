package com.android.minesweeper;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import com.android.minesweeper.common.Cell;
import com.android.minesweeper.common.ClientMessage;
import com.android.minesweeper.common.GameState;

public class GameGridDiff extends DiffUtil.Callback {
    private GameState oldState, newState;

    public GameGridDiff(GameState oldState, GameState newState) {
        this.oldState = oldState;
        this.newState = newState;
    }

    @Override
    public int getOldListSize() {
        return oldState != null ? oldState.getSize() : 0;
    }

    @Override
    public int getNewListSize() {
        return newState != null ? newState.getSize() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldState.get(oldItemPosition).same(newState.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(!oldState.get(oldItemPosition).equals(newState.get(newItemPosition))) {
            Log.e("TAG", "not same!!! " + oldState.get(oldItemPosition) + ", " + newState.get(newItemPosition));
        }
        return oldState.get(oldItemPosition).equals(newState.get(newItemPosition));
    }
}

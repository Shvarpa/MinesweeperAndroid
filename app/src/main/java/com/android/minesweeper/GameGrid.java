package com.android.minesweeper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.minesweeper.common.Cell;
import com.android.minesweeper.common.GameState;
import com.android.minesweeper.common.Point;
import com.android.minesweeper.interfaces.Listener;

public class GameGrid extends RecyclerView.Adapter<CellViewHolder> {

    Context mContext;
    RecyclerView mView;
    GridLayoutManager mLayout;
    GameState state;
    Listener<Cell> onClick, onLongClick;

    GameGrid(Context context, RecyclerView view, Listener<Cell> onClick, Listener<Cell> onLongClick) {
        this.mContext = context;
        this.mView = view;
        this.mView.setLayoutManager(mLayout);
        this.mLayout = new GridLayoutManager(mContext, 1);
        this.mView.setAdapter(this);
        this.mView.setLayoutManager(mLayout);
        mView.addItemDecoration(new EqualSpacingItemDecoration(5, 5, 5, 5));
        this.onClick = onClick;
        this.onLongClick = onLongClick;
    }

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CellViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cell, parent, false), onClick, onLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        Point p = new Point(position % state.getColumns(), (int) (position / state.getColumns()));
//        Log.e("TAG", "onBindViewHolder: " + "grid:(" + state.getColumns() + "," + state.getRows() + ") position:" + position + " (" + p.x + "," + p.y + ")");
        holder.setCell(state.get(p));
    }

    @Override
    public int getItemCount() {
        return state == null ? 0 : state.getColumns() * state.getRows();
    }


    private void updateGrid(GameState newState, GameState oldState) {
        if (newState == null) return;
        if (oldState == null || newState.getColumns() != oldState.getColumns()) {
            this.mLayout.setSpanCount(newState.getColumns());
        }
    }

    void update(GameState state) {
        Log.e("TAG", "update: " + (state != null ? state.getGrid() : null));
        updateGrid(state, this.state);
        this.state = state;
        this.notifyDataSetChanged();
    }

    public GameState getState() {
        return this.state;
    }
}

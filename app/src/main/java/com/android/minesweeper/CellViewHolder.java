package com.android.minesweeper;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.minesweeper.common.Cell;
import com.android.minesweeper.interfaces.Listener;

import java.util.Locale;

public class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private Cell cell;
    private Button tvNumber;
    private View mView;
    private Listener<Cell> onClick, onLongClick;

    public CellViewHolder(@NonNull View itemView) {
        super(itemView);
        assignView(itemView);
    }

    public CellViewHolder(@NonNull View itemView, Listener<Cell> onClick, Listener<Cell> onLongClick) {
        super(itemView);
        assignView(itemView);
        this.onClick = onClick;
        this.onLongClick = onLongClick;
        tvNumber.setOnClickListener(this);
        tvNumber.setOnLongClickListener(this);
    }

    private void assignView(View itemView) {
        mView = itemView;
        tvNumber = itemView.findViewById(R.id.cell_number);
    }

    private @ColorInt
    int getColorFromAttr(@AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        mView.getContext().getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        if (cell.isRevealed()) {
            tvNumber.setBackgroundColor(getColorFromAttr(R.attr.colorBackgroundFloating));
            if (cell.getNumber() != null)
                tvNumber.setText(cell.getNumber() == 0 ? "" : cell.getNumber().toString());
            else
                tvNumber.setText("B");
        } else {
            tvNumber.setBackgroundColor(getColorFromAttr(R.attr.backgroundColor));
            if (cell.isFlag()) {
                tvNumber.setText("F");
            } else {
                tvNumber.setText("");
//            tvNumber.setVisibility(TextView.INVISIBLE);
            }
        }
    }

    public Cell getCell() {
        return this.cell;
    }

    @Override
    public void onClick(View v) {
        if (onClick != null) onClick.on(getCell());
    }

    @Override
    public boolean onLongClick(View v) {
        if (onLongClick != null) onLongClick.on(getCell());
        return true;
    }
}

package com.android.minesweeper;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.android.OutlineSpan;
import com.android.R;
import com.android.minesweeper.common.Cell;
import com.android.minesweeper.interfaces.Listener;

import java.util.Locale;

public class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private Cell cell;
    private Button item;
    private View mView;
    private Resources resources;
    private int defualtColor;
    private int[] colors;
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
        item.setOnClickListener(this);
        item.setOnLongClickListener(this);
    }

    private void assignView(View itemView) {
        mView = itemView;
        resources = mView.getResources();
        item = itemView.findViewById(R.id.cell_number);
        colors = resources.getIntArray(R.array.number_colors);
        defualtColor = getColorFromAttr(R.attr.colorPrimary);
    }

    private @ColorInt
    int getColorFromAttr(@AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        mView.getContext().getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    private int colorFromNumber(Integer number) {
        return number != null && number > 0 ? colors[number - 1] : defualtColor;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        int background;
        float scale;
        item.setTextColor(colorFromNumber(cell.getNumber()));
        if (cell.isRevealed()) {
            background = getColorFromAttr(R.attr.colorBackgroundFloating);
            scale = 0.8f;
            if (cell.getNumber() != null) {
                drawText(cell.getNumber() == 0 ? "" : cell.getNumber().toString());
//                item.setText(cell.getNumber() == 0 ? "" : cell.getNumber().toString());
            } else {
                if(cell.lost != null && cell.lost) drawText(resources.getString(R.string.boom));
                else drawText(resources.getString(R.string.bomb));
//                item.setText("B");
            }
        } else {
            background = getColorFromAttr(R.attr.backgroundColor);
            scale = 0.5f;
            if (cell.isFlag()) {
                drawText(resources.getString(R.string.flag));
//                item.setText("F");
            } else {
                drawText("");
//                item.setText("");
            }
        }
        if (cell.getColor() != null) {
            animateColorChange(background, ColorUtils.blendARGB(cell.getColor(), background, scale));
        } else {
            item.setBackgroundColor(background);
        }
    }

    private void drawText(String text) {
        OutlineSpan outlineSpan = new OutlineSpan(resources.getColor(R.color.white), 2f);
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(outlineSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    private void animateColorChange(int base, int color) {
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color, base);
        colorAnimation.setDuration(500); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                item.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
        cell.color = null;
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

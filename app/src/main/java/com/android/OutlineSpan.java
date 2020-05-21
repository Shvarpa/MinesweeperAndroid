package com.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OutlineSpan extends ReplacementSpan {

    int strokeColor;
    float strokeWidth;

    public OutlineSpan(@ColorInt int strokeColor, @Dimension float strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
//        float originalTextSize = paint.getTextSize();
//        paint.setTextSize(originalTextSize + (0.5f * strokeWidth));
        int size = (int) paint.measureText(text.toString().substring(start, end));
//        paint.setTextSize(originalTextSize);
        Log.e("TAG", "getSize: " + size);
        return size;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int originalTextColor = paint.getColor();
//        float originalTextSize = paint.getTextSize();

        Log.e("TAG", "original: " + originalTextColor);
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(this.strokeWidth);
//        paint.setTextSize(originalTextSize + (0.5f * strokeWidth));
        canvas.drawText(text, start, end, x, (float) y, paint);

        paint.setColor(originalTextColor);
        paint.setStyle(Paint.Style.FILL);
//        paint.setTextSize(originalTextSize);
        canvas.drawText(text, start, end, x, (float) y, paint);

    }
}

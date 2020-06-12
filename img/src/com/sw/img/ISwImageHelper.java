package com.sw.img;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.InputStream;

public interface ISwImageHelper {
    void postInvalidate();

    void setInputStream(InputStream is);

    int getBitmapWidth();

    int getBitmapHeight();

    int getWidth();

    int getHeight();

    Rect getDrawRect();

    Bitmap decodeRegion();

    void onMeasure(int widthMeasureSpec, int heightMeasureSpec);

    void onDraw(Canvas canvas);

    void onScale(float x, float y, float factor);

    void updateSampleSize();
}

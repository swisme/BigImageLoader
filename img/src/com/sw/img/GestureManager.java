package com.sw.img;

import android.content.Context;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class GestureManager extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    private final float SCROLL_FACTOR = 1.0f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private ISwImageHelper mSwImageHelper;
    private float mLastX, mLastY;

    public GestureManager(Context context, ISwImageHelper swImageHelper) {
        mGestureDetector = new GestureDetector(context, this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mSwImageHelper = swImageHelper;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        handleScroll(e2);
        return true;
    }


    private void handleScroll(MotionEvent e2) {
        float dx = (e2.getRawX() - mLastX) * SCROLL_FACTOR;
        float dy = (e2.getRawY() - mLastY) * SCROLL_FACTOR;
        if (mSwImageHelper.getBitmapWidth() > mSwImageHelper.getWidth()) {
            // Bitmap.Width > ImageView.Width
            // move draw-rect
            mSwImageHelper.getDrawRect().offset((int) -dx, 0);
        }
        if (mSwImageHelper.getBitmapHeight() > mSwImageHelper.getHeight()) {
            // Bitmap.Height > ImageView.Height
            // move draw-rect
            mSwImageHelper.getDrawRect().offset(0, (int) -dy);
        }
        adjustBounds();
        mSwImageHelper.postInvalidate();
    }

    private void adjustBounds() {
        Rect drawRect = mSwImageHelper.getDrawRect();
        int bitmapWidth = mSwImageHelper.getBitmapWidth();
        if (drawRect.right > bitmapWidth) {
            drawRect.right = bitmapWidth;
            drawRect.left = bitmapWidth - mSwImageHelper.getWidth();
        }
        if (drawRect.left < 0) {
            drawRect.left = 0;
            drawRect.right = mSwImageHelper.getWidth();
        }

        int bitmapHeight = mSwImageHelper.getBitmapHeight();
        if (drawRect.bottom > bitmapHeight) {
            drawRect.bottom = bitmapHeight;
            drawRect.top = bitmapHeight - mSwImageHelper.getHeight();
        }
        if (drawRect.top < 0) {
            drawRect.top = 0;
            drawRect.bottom = mSwImageHelper.getHeight();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        updatePosition(e);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        updatePosition(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        handleScroll(e2);
        return true;
    }

    private void updatePosition(MotionEvent e) {
        mLastX = e.getRawX();
        mLastY = e.getRawY();
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean scale = mScaleGestureDetector.onTouchEvent(event);
        boolean normal = mGestureDetector.onTouchEvent(event);
        return scale || normal;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Rect drawRect = mSwImageHelper.getDrawRect();
        float centerX = detector.getFocusX();
        float centerY = detector.getFocusY();
        float scaleFactor = detector.getScaleFactor();
        drawRect.left = (int) (centerX - Math.abs(centerX - drawRect.left) * scaleFactor);
        drawRect.top = (int) (centerY - Math.abs(centerY - drawRect.top) * scaleFactor);
        drawRect.right = (int) (drawRect.left + drawRect.width() * scaleFactor);
        drawRect.bottom = (int) (drawRect.top + drawRect.height() * scaleFactor);
        //TODO 判断MIN_SCALE_FACTOR <= scaleFactor <=MAX_SCALE_FACTOR
//        mSwImageHelper.onScale(detector.getFocusX(), detector.getFocusY(), detector.getScaleFactor());
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        adjustBounds();
        mSwImageHelper.postInvalidate();
    }
}

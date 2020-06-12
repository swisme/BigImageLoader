package com.sw.img;

import android.graphics.Bitmap;

public class BitmapUnit {
    private boolean mIsLoading = false;
    private int mCurrentSampleSize;
    private Bitmap mBitmap;
    private Bitmap mInitialThumbBitmap;

    public BitmapUnit(){}
    public boolean isLoading() {
        return mIsLoading;
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
    }

    public int getCurrentSampleSize() {
        return mCurrentSampleSize;
    }

    public void setSampleSize(int sampleSize) {
        mCurrentSampleSize = sampleSize;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setInitialThumbBitmap(Bitmap bitmap) {
        mInitialThumbBitmap = bitmap;
    }

    public Bitmap getInitialThumbBitmap(){
        return mInitialThumbBitmap;
    }

    public void recycle() {
        mBitmap = null;
        mInitialThumbBitmap = null;
        mCurrentSampleSize = 0;
    }
}

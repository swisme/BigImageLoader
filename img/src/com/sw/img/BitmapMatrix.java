package com.sw.img;

import android.graphics.Rect;

public class BitmapMatrix {
    private int mColumn;
    private int mRow;
    private BitmapUnit[][] mMatrix;
    private Rect mViewRect;
    private Rect mBitmapRect;
    private int mSampleSize;

    public BitmapMatrix(Rect viewRect, Rect bitmapRect, int sampleSize) {
        mViewRect = viewRect;
        mBitmapRect = bitmapRect;
        mSampleSize = sampleSize;
    }

    public void init() {
        if (mMatrix != null) {
            recycle();
        }

        int viewWidth = mViewRect.width();
        int viewHeight = mViewRect.height();
        int bitmapWidth = mBitmapRect.width();
        int bitmapHeight = mBitmapRect.height();
        mRow = bitmapWidth / viewWidth + (bitmapWidth % viewWidth == 0 ? 0 : 1);
        mColumn = bitmapHeight / viewHeight + (bitmapHeight % viewHeight == 0 ? 0 : 1);
        mMatrix = new BitmapUnit[mRow][mColumn];
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                mMatrix[i][j] = new BitmapUnit();
                mMatrix[i][j].setSampleSize(mSampleSize);
            }
        }
        //TODO 异步通知，开始loading
        if (mListener != null) {
            mListener.onInitComplete();
        }
    }

    public void recycle() {
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                mMatrix[i][j].recycle();
            }
        }
    }

    private IMatrixInitListener mListener;

    public void setMatrixInitListener(IMatrixInitListener listener) {
        mListener = listener;
    }

    public interface IMatrixInitListener {
        void onInitComplete();
    }
}

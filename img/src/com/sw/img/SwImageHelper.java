package com.sw.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.InputStream;

public class SwImageHelper implements ISwImageHelper {
    private final String THREAD_NAME = "SwLoadingThread";
    private final long DRAW_DELAY_TIME = -1;// unit: milliseconds
    private SwImageView mSwImageView;
    private BitmapFactory.Options mOptions;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private int mBitmapWidth, mBitmapHeight;
    private volatile Rect mDrawRect = new Rect();
    private volatile boolean mNeedInvalidate = true;
    private HandlerThread mLoadingThread;
    private Handler mLoadingHandler;
    private Handler mMainHandler = new Handler();
    private Bitmap mCurrentBitmap;

    private long mLastDrawTime;

    public SwImageHelper(SwImageView swImageView) {
        mSwImageView = swImageView;
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = true;
    }

    @Override
    public void postInvalidate() {
        mNeedInvalidate = true;
        mSwImageView.postInvalidate();
    }

    @Override
    public void setInputStream(InputStream is) {
        checkLoadingThread();
        try {
            is.reset();
            updateBitmapSize(is);
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkLoadingThread() {
        if (mLoadingThread == null || mLoadingThread.getState() == Thread.State.NEW) {
            mLoadingThread = new HandlerThread(THREAD_NAME + hashCode());
            mLoadingThread.start();
        }
        mLoadingHandler = new Handler(mLoadingThread.getLooper());
    }

    @Override
    public int getBitmapWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getBitmapHeight() {
        return mBitmapHeight;
    }

    @Override
    public int getWidth() {
        return mSwImageView.getWidth();
    }

    @Override
    public int getHeight() {
        return mSwImageView.getHeight();
    }

    @Override
    public Rect getDrawRect() {
        return mDrawRect;
    }

    @Override
    public Bitmap decodeRegion() {
        if (mBitmapRegionDecoder != null) {
            return mBitmapRegionDecoder.decodeRegion(mDrawRect, mOptions);
        }
        return null;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = mSwImageView.getMeasuredWidth();
        int measuredHeight = mSwImageView.getMeasuredHeight();
        mDrawRect.left = mBitmapWidth / 2 - measuredWidth / 2;
        mDrawRect.top = mBitmapHeight / 2 - measuredHeight / 2;
        mDrawRect.right = mDrawRect.left + measuredWidth;
        mDrawRect.bottom = mDrawRect.top + measuredHeight;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        //TODO 边滑动边加载，很卡
        if (mNeedInvalidate && System.currentTimeMillis() - mLastDrawTime > DRAW_DELAY_TIME) {
            mNeedInvalidate = false;
            mLastDrawTime = System.currentTimeMillis();
            Bitmap bitmap = decodeRegion();
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }
    }

    @Override
    public void onScale(float x, float y, float factor) {

    }

    @Override
    public void updateSampleSize() {
        //TODO scale end, update sample size
    }

    private void updateBitmapSize(InputStream is) {
        BitmapFactory.decodeStream(is, null, mOptions);
        mBitmapWidth = mOptions.outWidth;
        mBitmapHeight = mOptions.outHeight;
    }
}

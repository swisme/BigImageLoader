package com.sw.img;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SuppressLint("AppCompatCustomView")
public class SwImageView extends ImageView {
    private static final String TAG = "SwImageView";
    private GestureManager mGestureManager;
    private ISwImageHelper mSwImageHelper;

    public SwImageView(Context context) {
        this(context, null);
    }

    public SwImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnLongClickListener(v -> {
            return false;
        });
        mSwImageHelper = new SwImageHelper(this);
        mGestureManager = new GestureManager(getContext(), mSwImageHelper);
    }

    public void setImageResource(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.d(TAG, "file path is null");
            return;
        }
        setImageResource(new File(filePath));
    }

    public void setImageResource(File file) {
        if (file == null || !file.exists()) {
            Log.d(TAG, "file invalid");
            return;
        }
        try {
            setImageResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setImageResource(InputStream is) {
        if (is == null) {
            Log.d(TAG, "is is null");
            return;
        }
        mSwImageHelper.setInputStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSwImageHelper.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSwImageHelper.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO stop fling
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                interceptTouchEvent(false);
                break;
        }
        return mGestureManager.onTouchEvent(event);
    }

    public void interceptTouchEvent(boolean intercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(intercept);
        }
    }
}

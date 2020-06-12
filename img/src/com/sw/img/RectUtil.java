package com.sw.img;

import android.graphics.Rect;
import android.graphics.RectF;

public final class RectUtil {
    private RectUtil() {
    }

    public static RectF getRectF(Rect rect, float ratio) {
        return getScaledRectF(new RectF(rect), ratio);
    }

    public static RectF getScaledRectF(RectF rectF, float ratio) {
        float left = rectF.left * ratio;
        float top = rectF.top * ratio;
        float right = left + rectF.width() * ratio;
        float bottom = top + rectF.height() * ratio;
        return new RectF(left, top, right, bottom);
    }
}

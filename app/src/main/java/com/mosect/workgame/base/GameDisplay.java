package com.mosect.workgame.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class GameDisplay {

    private SurfaceHolder holder;
    private int width;
    private int height;
    private int contentWidth;
    private int contentHeight;
    private int canvasWidth;
    private int canvasHeight;
    private float canvasScaleX = 1f;
    private float canvasScaleY = 1f;
    private Canvas canvas;

    GameDisplay(SurfaceHolder holder, int width, int height) {
        this.holder = holder;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    void setContentSize(int width, int height) {
        if (this.contentWidth == width && this.contentHeight == height) return;

        this.canvasWidth = this.width;
        this.canvasHeight = this.height;
        this.canvasScaleX = 1f;
        this.canvasScaleY = 1f;
        this.contentWidth = width;
        this.contentHeight = height;
        if (width > 0 && height > 0) {
            float sw = width / (float) this.width;
            float sh = height / (float) this.height;
            float s = this.width / (float) this.height;
            if (sw > sh) {
                canvasWidth = width;
                canvasHeight = (int) (width / s);
            } else {
                canvasHeight = height;
                canvasWidth = (int) (height * s);
            }
            if (canvasWidth > 0 && canvasHeight > 0) {
                canvasScaleX = this.width / (float) canvasWidth;
                canvasScaleY = this.height / (float) canvasHeight;
            }
        }
    }

//    private Rect rect = new Rect(0, 100, 300, 400);
//    private Paint paint = new Paint();

    void destroy() {
        postCanvas();
    }

    Canvas lockCanvas() {
        try {
            if (null != holder && null == canvas) {
                canvas = holder.lockCanvas();
                canvas.scale(canvasScaleX, canvasScaleY);
            }
        } catch (Exception ignored) {
        }
        return canvas;
    }

    void postCanvas() {
        if (null != holder && null != canvas) {
            try {
//                rect.offset(10, 0);
//                paint.setAntiAlias(true);
//                paint.setColor(Color.RED);
//                paint.setStyle(Paint.Style.FILL);
//                canvas.drawRect(rect, paint);
                holder.unlockCanvasAndPost(canvas);
            } catch (Exception ignored) {
            }
            canvas = null;
        }
    }
}

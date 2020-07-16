package com.mosect.workgame.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class GameDisplay {

    private SurfaceHolder holder;
    private int width;
    private int height;
    private int contentWidth;
    private int contentHeight;
    private Bitmap bitmap;
    private Canvas canvas;
    private Canvas holderCanvas;

    private Rect src = new Rect();
    private RectF dst = new RectF();

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
        return null == bitmap ? 0 : bitmap.getWidth();
    }

    public int getCanvasHeight() {
        return null == bitmap ? 0 : bitmap.getHeight();
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    void setContentSize(int width, int height) {
        if (this.contentWidth == width && this.contentHeight == height) return;

        clearCanvas();
        this.contentWidth = width;
        this.contentHeight = height;
        if (width > 0 && height > 0) {
            float sw = width / (float) this.width;
            float sh = height / (float) this.height;
            float s = this.width / (float) this.height;
            int canvasWidth, canvasHeight;
            if (sw > sh) {
                canvasWidth = width;
                canvasHeight = (int) (width / s);
            } else {
                canvasHeight = height;
                canvasWidth = (int) (height * s);
            }
            if (canvasWidth > 0 && canvasHeight > 0) {
                bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
            }
        }
    }

    void destroy() {
        endDraw();
        clearCanvas();
    }

    Canvas getCanvas() {
        return canvas;
    }

    void startDraw() {
        if (null != holderCanvas) return;
        try {
            holderCanvas = holder.lockCanvas();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    void endDraw() {
        if (null == holderCanvas) return;
        try {
            if (null != bitmap && !bitmap.isRecycled()) {
                src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                dst.set(0, 0, width, height);
                holderCanvas.drawBitmap(bitmap, src, dst, null);
            }
            holder.unlockCanvasAndPost(holderCanvas);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void clearCanvas() {
        canvas = null;
        if (null != bitmap) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
    }
}

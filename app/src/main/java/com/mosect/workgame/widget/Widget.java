package com.mosect.workgame.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.base.GameTouchEvent;

public class Widget {

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean pressed;
    private boolean focused;
    private int touchPointerId = -1;

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    /**
     * 更新控件信息，当控件某些信息发生更改时，需要主动调用此方法更新其余信息
     */
    public void update() {
    }

    public boolean dispatchTouchEvent(GameTouchEvent event) {
        if (touchPointerId < 0) {
            // 未确定触摸手指
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (touchInWidget(event)) {
                    // 触摸到控件
                    touchPointerId = event.getPointerId();
                    onTouchEvent(event);
                    return true;
                }
            }
        } else if (touchPointerId == event.getPointerId()) {
            // 已确定触摸手指，需要处理事件
            onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                touchPointerId = -1;
            }
            return true;
        }
        return false;
    }

    public boolean dispatchKeyEvent(GameKeyEvent event) {
        if (focused) {
            return onKeyEvent(event);
        }
        return false;
    }

    public void dispatchDraw(Canvas canvas) {
        int sc = canvas.save();
        canvas.clipRect(x, y, x + width, y + height);
        canvas.translate(x, y);
        onDraw(canvas);
        canvas.restoreToCount(sc);
    }

    protected void onTouchEvent(GameTouchEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                onClick();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                if (touchInWidget(event)) {
                    onPressedClick();
                }
                break;
        }
    }

    protected boolean onKeyEvent(GameKeyEvent event) {
        return false;
    }

    protected boolean touchInWidget(GameTouchEvent event) {
        int right = x + width;
        int bottom = y + height;
        float tx = event.getX();
        float ty = event.getY();
        return tx >= x && tx < right && ty >= y && ty < bottom;
    }

    protected void onDraw(Canvas canvas) {
    }

    protected void onClick() {
    }

    protected void onPressedClick() {
    }
}

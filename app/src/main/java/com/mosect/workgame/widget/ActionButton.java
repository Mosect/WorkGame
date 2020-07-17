package com.mosect.workgame.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mosect.workgame.base.GameTouchEvent;
import com.mosect.workgame.graphics.Graphics;

public class ActionButton {

    private Graphics[] icons;
    private int x;
    private int y;
    private int width;
    private int height;
    private int padding;

    public void setIcons(Graphics[] icons) {
        this.icons = icons;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

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

    public boolean onTouchEvent(GameTouchEvent event) {
        return false;
    }

    public void draw(Canvas canvas) {
    }
}

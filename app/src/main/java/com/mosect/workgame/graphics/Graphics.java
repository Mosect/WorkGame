package com.mosect.workgame.graphics;

import android.graphics.Canvas;

public abstract class Graphics {

    private float width;
    private float height;

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public abstract void draw(Canvas canvas);
}

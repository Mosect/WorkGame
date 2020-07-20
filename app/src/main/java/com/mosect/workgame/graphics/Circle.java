package com.mosect.workgame.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Circle extends Graphics {

    private RectF region = new RectF();
    private Paint paint;

    public Circle() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(region, 0, 360, true, paint);
    }

    @Override
    public void update() {
        super.update();
        region.set(0, 0, getWidth(), getHeight());
    }
}

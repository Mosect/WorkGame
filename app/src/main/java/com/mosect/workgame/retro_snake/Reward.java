package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 奖励
 */
public class Reward {

    private final static long SHOWN_TIME = 400;
    private final static long HIDDEN_TIME = 300;

    private RetroSnakeWorld world;
    private int x;
    private int y;
    private long time;
    private Paint paint;

    public Reward(RetroSnakeWorld world) {
        this.world = world;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void tick(long time) {
        this.time += time;
        if (this.time > SHOWN_TIME + HIDDEN_TIME) {
            this.time = this.time % (SHOWN_TIME + HIDDEN_TIME);
        }
    }

    public void draw(Canvas canvas) {
        if (time < SHOWN_TIME && x >= 0 && y >= 0 &&
                x < world.getXBlocks() && y < world.getYBlocks()) {
            // 显示
            int bs = world.getBlockSize();
            int l = bs * x;
            int t = bs * y;
            int r = l + bs;
            int b = t + bs;
            canvas.drawRect(l, t, r, b, paint);
        }
    }
}

package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 蛇身体
 */
public class SnakeBody {

    private Rect rect = new Rect();
    private Direction direction;

    public void set(int left, int top, int right, int bottom, Direction direction) {
        rect.set(left, top, right, bottom);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLeft() {
        return rect.left;
    }

    public int getTop() {
        return rect.top;
    }

    public int getRight() {
        return rect.right;
    }

    public int getBottom() {
        return rect.bottom;
    }

    public int getLength() {
        switch (direction) {
            case LEFT:
            case RIGHT:
                return rect.width();
            case UP:
            case DOWN:
                return rect.height();
        }
        return 0;
    }

    public void addLength(int value) {
        switch (direction) {
            case DOWN:
                rect.bottom += value;
                break;
            case UP:
                rect.top -= value;
                break;
            case RIGHT:
                rect.right += value;
                break;
            case LEFT:
                rect.left -= value;
                break;
        }
    }

    public void reduceLength(int value) {
        switch (direction) {
            case LEFT:
                rect.right = Math.max(rect.left, rect.right - value);
                break;
            case RIGHT:
                rect.left = Math.min(rect.right, rect.left + value);
                break;
            case UP:
                rect.bottom = Math.max(rect.top, rect.bottom - value);
                break;
            case DOWN:
                rect.top = Math.min(rect.bottom, rect.top + value);
                break;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public void clear() {
        rect.setEmpty();
        direction = null;
    }

    @Override
    public String toString() {
        return String.format("{left=%s,top=%s,right=%s,bottom=%s,direction=%s}",
                rect.left, rect.top, rect.right, rect.bottom, direction);
    }
}

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

    public void addHead(int value) {
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

    public void reduceHead(int value) {
        switch (direction) {
            case UP:
                rect.top = Math.min(rect.bottom, rect.top + value);
                break;
            case DOWN:
                rect.bottom = Math.max(rect.top, rect.bottom - value);
                break;
            case RIGHT:
                rect.right = Math.max(rect.left, rect.right - value);
                break;
            case LEFT:
                rect.left = Math.min(rect.right, rect.left + value);
                break;
        }
    }

    public void reduceEnd(int value) {
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

    public boolean isEmpty() {
        return rect.isEmpty();
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public int getHeadX(int blockSize) {
        switch (direction) {
            case RIGHT:
                if (rect.right % blockSize == 0) {
                    return rect.right / blockSize - 1;
                } else {
                    return rect.right / blockSize;
                }
            case LEFT:
            case DOWN:
            case UP:
                return rect.left / blockSize;
        }
        return -1;
    }

    public int getHeadY(int blockSize) {
        switch (direction) {
            case DOWN:
                if (rect.bottom % blockSize == 0) {
                    return rect.bottom / blockSize - 1;
                } else {
                    return rect.bottom / blockSize;
                }
            case LEFT:
            case RIGHT:
            case UP:
                return rect.top / blockSize;
        }
        return -1;
    }

    public int getEndX(int blockSize) {
        switch (direction) {
            case LEFT:
                if (rect.right % blockSize == 0) {
                    return rect.right / blockSize - 1;
                } else {
                    return rect.right / blockSize;
                }
            case DOWN:
            case UP:
            case RIGHT:
                return rect.left / blockSize;
        }
        return -1;
    }

    public int getEndY(int blockSize) {
        switch (direction) {
            case UP:
                if (rect.bottom % blockSize == 0) {
                    return rect.bottom / blockSize - 1;
                } else {
                    return rect.bottom / blockSize;
                }
            case LEFT:
            case RIGHT:
            case DOWN:
                return rect.top / blockSize;
        }
        return -1;
    }

    public int getBlockOffset(int blockSize) {
        switch (direction) {
            case LEFT:
                return blockSize - rect.left % blockSize;
            case RIGHT:
                return rect.right % blockSize;
            case UP:
                return blockSize - rect.top % blockSize;
            case DOWN:
                return rect.bottom % blockSize;
        }
        return 0;
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

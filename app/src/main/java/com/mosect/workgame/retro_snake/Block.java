package com.mosect.workgame.retro_snake;

import android.util.Log;

/**
 * 面板格子
 */
public class Block {

    private int x;
    private int y;
    private boolean used; // 格子是否被占用
    private Direction direction; // 方向

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setUsed(boolean used) {
        if (this.used != used) {
            this.used = used;
            Log.d("Block", String.format("[%02d,%02d] used %s -> %s", x, y, !used, used));
        }
    }

    public boolean isUsed() {
        return used;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format("{x=%s,y=%s,used=%s,direction=%s}", x, y, used, direction);
    }
}

package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * 蛇身体区块
 */
public class SnakeBlock {

    private Rect rect = new Rect();
    private Direction direction = Direction.LEFT;
    private GamePanel panel;
    private ArrayList<Block> blocks; // 占用的格子

    public SnakeBlock(GamePanel panel) {
        this.panel = panel;
        int count = Math.max(panel.getXCount(), panel.getYCount());
        blocks = new ArrayList<>(count);
    }

    public void clear() {
        rect.setEmpty();
        clearBlocks();
    }

    public void setRect(int left, int top, int right, int bottom) {
        rect.set(left, top, right, bottom);
    }

    public void offset(int offset) {
        switch (direction) {
            case DOWN:
                if (offset < 0) {
                    rect.top = Math.min(rect.bottom, rect.top - offset);
                } else {
                    rect.bottom += offset;
                }
                break;
            case UP:
                if (offset < 0) {
                    rect.bottom = Math.max(rect.top, rect.bottom + offset);
                } else {
                    rect.top -= offset;
                }
                break;
            case LEFT:
                if (offset < 0) {
                    rect.right = Math.max(rect.left, rect.right + offset);
                } else {
                    rect.left -= offset;
                }
                break;
            case RIGHT:
                if (offset < 0) {
                    rect.left = Math.min(rect.left - offset, rect.right);
                } else {
                    rect.right += offset;
                }
                break;
        }
    }

    /**
     * 绘制
     *
     * @param canvas 画布
     * @param paint  画笔
     */
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * 占用格子
     */
    public void useBlocks() {
        clearBlocks();
        int left = Math.max(0, rect.left - panel.getX());
        int right = Math.max(0, rect.right - panel.getX());
        int sx = left / panel.getBlockSize();
        int ex = right / panel.getBlockSize();
        int top = Math.max(0, rect.top - panel.getY());
        int bottom = Math.max(0, rect.bottom - panel.getY());
        int sy = top / panel.getBlockSize();
        int ey = bottom / panel.getBlockSize();
        for (int x = sx; x <= ex; x++) {
            for (int y = sy; y < ey; y++) {
                Block block = panel.getBlock(x, y);
                block.setUsed(true);
                blocks.add(block);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("{rect:%s,direction:%s}", rect, direction);
    }

    private void clearBlocks() {
        if (blocks.isEmpty()) return;
        for (Block block : blocks) {
            block.setUsed(false);
        }
        blocks.clear();
    }
}

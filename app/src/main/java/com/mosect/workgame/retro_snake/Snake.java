package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

/**
 * 蛇
 */
public class Snake {

    private Direction direction; // 蛇的方向
    private LinkedList<SnakeBlock> blocks = new LinkedList<>(); // 蛇的身体
    private LinkedList<SnakeBlock> cacheBlocks = new LinkedList<>();
    private Paint paint = new Paint();
    private GamePanel panel;
    private long tickTime;
    private int speed; // 速度

    public Snake(GamePanel panel) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.panel = panel;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * 移动蛇
     *
     * @param offset 偏移量
     */
    private void move(int offset) {
        if (blocks.isEmpty()) return;
        SnakeBlock last = blocks.getLast();
        SnakeBlock first = blocks.getFirst();
        if (offset < 0) {
            // 倒退

            // 伸长尾部
            last.offset(offset);
            // 缩短头部
            first.offset(-offset);
        } else {
            // 前进

            // 伸长头部
            first.offset(offset);
            // 缩短尾部
            last.offset(-offset);
        }
        if (first == last) {
            first.useBlocks();
            Log.d("Snake", "move: first,last: " + first);
        } else {
            first.useBlocks();
            last.useBlocks();
            Log.d("Snake", "move: first: " + first);
            Log.d("Snake", "move: last: " + last);
        }
    }

    public void tick() {
        if (tickTime == 0) {
            tickTime = System.currentTimeMillis();
        } else {
            long now = System.currentTimeMillis();
            long part = now - tickTime;
            if (part > 0) {
                int offset = (int) (part * speed / 1000);
                if (offset != 0) {
                    move(offset);
                }
                tickTime = now;
            }
        }
    }

    /**
     * 绘制蛇
     *
     * @param canvas 画布
     */
    public void draw(Canvas canvas) {
        for (SnakeBlock block : blocks) {
            block.draw(canvas, paint);
        }
    }

    /**
     * 添加身体
     *
     * @param left      左
     * @param top       上
     * @param right     右
     * @param bottom    下
     * @param direction 方向
     */
    public void addBody(int left, int top, int right, int bottom, Direction direction) {
        SnakeBlock block = genBlock();
        block.setDirection(direction);
        block.setRect(left, top, right, bottom);
        block.useBlocks();
        blocks.addFirst(block);
        setDirection(direction);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (!blocks.isEmpty()) {
            SnakeBlock first = blocks.getFirst();
            switch (direction) {
                case RIGHT:
                    if (first.getDirection() == Direction.LEFT) {
                        this.direction = Direction.LEFT;
                    }
                    break;
                case LEFT:
                    if (first.getDirection() == Direction.RIGHT) {
                        this.direction = Direction.RIGHT;
                    }
                    break;
                case UP:
                    if (first.getDirection() == Direction.DOWN) {
                        this.direction = Direction.DOWN;
                    }
                    break;
                case DOWN:
                    if (first.getDirection() == Direction.UP) {
                        this.direction = Direction.UP;
                    }
                    break;
            }
        }
    }

    private SnakeBlock genBlock() {
        if (cacheBlocks.isEmpty()) {
            return new SnakeBlock(panel);
        }
        SnakeBlock block = cacheBlocks.removeFirst();
        block.clear();
        return block;
    }
}

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

    private RetroSnakeWorld world;
    private LinkedList<SnakeBody> bodies;
    private Paint paint;
    private int speed;

    public Snake(RetroSnakeWorld world) {
        this.world = world;
        this.bodies = new LinkedList<>();
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(Color.WHITE);
        this.paint.setStrokeWidth(0);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBody(SnakeBody body) {
        bodies.clear();
        bodies.addFirst(body);
    }

    public void draw(Canvas canvas) {
        if (!bodies.isEmpty()) {
            for (SnakeBody body : bodies) {
                body.draw(canvas, paint);
            }
        }
    }

    public void tick(long time) {
        if (bodies.isEmpty()) return;
        int offset = (int) (time * speed / 1000);
        if (offset <= 0) return;
        SnakeBody head = bodies.getFirst();
        Direction nextDirection = world.getNextDirection();

        // 增加头部长度
        if (head.getDirection() == nextDirection) {
            // 方向一样
            head.addHead(offset);
        } else {
            // 方向不一致
            Log.d("Snake", "==================================");
            int headOffset = head.getBlockOffset(world.getBlockSize());
            int x = head.getHeadX(world.getBlockSize());
            int y = head.getHeadY(world.getBlockSize());
            boolean addBody = false;
            int bodyOffset = 0;
            if (headOffset == 0) {
                addBody = true;
                bodyOffset = offset;
                Log.d("Snake", "full: " + head);
            } else {
                if (headOffset + offset > world.getBlockSize()) {
                    // 移动超过一格
                    int fullOffset = world.getBlockSize() - headOffset;
                    head.addHead(fullOffset);
                    Log.d("Snake", "addHead:" + head);
                    addBody = true;
                    bodyOffset = offset - fullOffset;
                } else {
                    // 移动未超过一格
                    head.addHead(offset);
                }
            }
            if (addBody) {
                // 需要新增身体
                SnakeBody body = nextBody(x, y, nextDirection, bodyOffset);
                if (null != body) {
                    // 缩短头部
                    Log.d("Snake", "beforeReduceHead:" + head);
                    head.reduceHead(world.getBlockSize());
                    Log.d("Snake", "reduceHead:" + head);
                    // 移除为空的身体
                    if (head.isEmpty()) {
                        bodies.removeFirst();
                    }
                    // 增加身体
                    bodies.addFirst(body);
                }
            }
        }

        // 缩短尾部长度
        int reduceValue = offset;
        while (reduceValue > 0 && bodies.size() > 0) {
            SnakeBody body = bodies.getLast();
            int length = body.getLength();
            if (length >= reduceValue) {
                body.reduceEnd(reduceValue);
                if (body.isEmpty()) {
                    bodies.removeLast();
                }
                reduceValue = 0;
            } else {
                body.reduceEnd(length);
                bodies.removeLast();
                reduceValue -= length;
            }
        }

        /*Log.d("Snake", "======================================");
        for (SnakeBody body : bodies) {
            Log.d("Snake", "length:" + body.getLength());
        }*/
    }

    public Direction getDirection() {
        if (!bodies.isEmpty()) {
            return bodies.getFirst().getDirection();
        }
        return null;
    }

    private SnakeBody nextBody(int x, int y, Direction direction, int length) {
        if (x >= 0 && x < world.getXBlocks() && y >= 0 && y < world.getYBlocks()) {
            int left = x * world.getBlockSize();
            int top = y * world.getBlockSize();
            int right = left + world.getBlockSize();
            int bottom = top + world.getBlockSize();
            SnakeBody body = new SnakeBody();
            body.set(left, top, right, bottom, direction);
            body.addHead(length);
            return body;
        }
        return null;
    }
}

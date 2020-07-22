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

    private int oldCount;

    public void draw(Canvas canvas) {
        if (!bodies.isEmpty()) {
            for (SnakeBody body : bodies) {
                body.draw(canvas, paint);
            }
        }
        if (bodies.size() != oldCount) {
            Log.d("Snake", "bodies==================================================");
            for (SnakeBody body : bodies) {
                Log.d("Snake", "body: " + body);
            }
        }
        oldCount = bodies.size();
    }

    public void tick(long time) {
        if (bodies.isEmpty()) return;
        int offset = (int) (time * speed / 1000);
        if (offset > 0) {
            // 增加头部身体长度
            Direction direction = world.getNextDirection();
            SnakeBody first = bodies.getFirst();
            if (direction == first.getDirection()) {
                first.addLength(offset);
            } else {
                int value;
                switch (first.getDirection()) {
                    case LEFT:
                    case RIGHT:
                        value = first.getLeft() % world.getBlockSize();
                        break;
                    case DOWN:
                    case UP:
                        value = first.getTop() % world.getBlockSize();
                        break;
                    default:
                        value = 0;
                        break;
                }
                if (value == 0) {
                    // 刚好填满格子
//                    Log.d("Snake", "value==0  >>>>  " + first);
                    SnakeBody body = nextBody(offset);
                    if (null != body) {
                        bodies.addFirst(body);
                    }
                } else {
                    if (value + offset > world.getBlockSize()) {
                        // 移动超过一个格子，先填满格子，再增加下一个身体
                        int fullOffset = world.getBlockSize() - value;
                        first.addLength(fullOffset);
//                        Log.d("Snake", "addLength:full" + first);
                        SnakeBody body = nextBody(offset - fullOffset);
                        if (null != body) {
                            bodies.addFirst(body);
                        }
                    } else {
                        // 移动少于一个格子
                        first.addLength(offset);
//                        Log.d("Snake", "first.addLength(offset)");
                    }
                }
            }
            int reduceValue = offset;
            while (reduceValue > 0) {
                SnakeBody last = bodies.getLast();
                int lastLength = last.getLength();
                if (lastLength > reduceValue) {
                    // 尾部长度足够
                    // 缩短身体长度
                    last.reduceLength(reduceValue);
                    reduceValue = 0;
                } else {
                    // 长度不够，移除身体
                    bodies.removeLast();
                    reduceValue -= lastLength;
                }
            }
        }
    }

    public Direction getDirection() {
        if (!bodies.isEmpty()) {
            return bodies.getFirst().getDirection();
        }
        return null;
    }

    private SnakeBody nextBody(int length) {
        Direction direction = world.getNextDirection();
        SnakeBody first = bodies.getFirst();
        int l = first.getLeft() / world.getBlockSize();
        int t = first.getTop() / world.getBlockSize();
        int r = first.getRight() / world.getBlockSize() +
                (first.getRight() % world.getBlockSize() == 0 ? 0 : 1);
        int b = first.getBottom() / world.getBlockSize() +
                (first.getBottom() % world.getBlockSize() == 0 ? 0 : 1);
        switch (first.getDirection()) {
            case LEFT:
                return genBody(l, t, length, direction);
            case RIGHT:
                return genBody(r - 1, t, length, direction);
            case UP:
                return genBody(l, t, length, direction);
            case DOWN:
                return genBody(l, b - 1, length, direction);
        }
        return null;
    }

    private SnakeBody genBody(int x, int y, int length, Direction direction) {
        int fx = x;
        int fy = y;
        switch (direction) {
            case LEFT:
                fx -= 1;
                break;
            case RIGHT:
                fx += 1;
                break;
            case DOWN:
                fy += 1;
                break;
            case UP:
                fy -= 1;
                break;
            default:
                fx = -1;
                fy = -1;
                break;
        }
        if (fx >= 0 && fx < world.getXBlocks() && y >= 0 && y < world.getYBlocks()) {
            SnakeBody body = new SnakeBody();
            int left = fx * world.getBlockSize();
            int top = fy * world.getBlockSize();
            int right = left + world.getBlockSize();
            int bottom = top + world.getBlockSize();
            switch (direction) {
                case UP:
                    body.set(left, bottom - length, right, bottom, direction);
                    break;
                case DOWN:
                    body.set(left, top, right, top + length, direction);
                    break;
                case RIGHT:
                    body.set(left, top, left + length, bottom, direction);
                    break;
                case LEFT:
                    body.set(right - length, top, right, bottom, direction);
                    break;
            }
            return body;
        }
        return null;
    }
}

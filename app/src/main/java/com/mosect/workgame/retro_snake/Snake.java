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
            int ox = head.getHeadX(world.getBlockSize());
            int oy = head.getHeadY(world.getBlockSize());
            head.addHead(offset);
            int nx = head.getHeadX(world.getBlockSize());
            int ny = head.getHeadY(world.getBlockSize());
            usedBlocks(ox, oy, nx, ny);
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
                    int nx = head.getHeadX(world.getBlockSize());
                    int ny = head.getHeadY(world.getBlockSize());
                    usedBlocks(x, y, nx, ny);
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

        SnakeBody first = bodies.getFirst();

        // 缩短尾部长度
        int reduceValue = offset;
        while (reduceValue > 0 && bodies.size() > 0) {
            SnakeBody body = bodies.getLast();
            int length = body.getLength();
            if (length >= reduceValue) {
                int ox = body.getEndX(world.getBlockSize());
                int oy = body.getEndY(world.getBlockSize());
                body.reduceEnd(reduceValue);
                int nx = body.getEndX(world.getBlockSize());
                int ny = body.getEndY(world.getBlockSize());
                unusedBlocks(ox, oy, nx, ny);
                if (body.isEmpty()) {
                    bodies.removeLast();
                }
                reduceValue = 0;
            } else {
                int ox = body.getEndX(world.getBlockSize());
                int oy = body.getEndY(world.getBlockSize());
                body.reduceEnd(length);
                int nx = body.getEndX(world.getBlockSize());
                int ny = body.getEndY(world.getBlockSize());
                unusedBlocks(ox, oy, nx, ny);
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

    /**
     * 获取蛇的格子长度
     *
     * @return 蛇的格子长度
     */
    public int getBlockLength() {
        int len = 0;
        for (SnakeBody body : bodies) {
            len += body.getLength();
        }
        return len / world.getBlockSize();
    }

    private SnakeBody nextBody(int x, int y, Direction direction, int length) {
        if (x >= 0 && x < world.getXBlocks() && y >= 0 && y < world.getYBlocks()) {
            int left = x * world.getBlockSize();
            int top = y * world.getBlockSize();
            int right = left + world.getBlockSize();
            int bottom = top + world.getBlockSize();
            SnakeBody body = new SnakeBody();
            body.set(left, top, right, bottom, direction);
            int ox = body.getHeadX(world.getBlockSize());
            int oy = body.getHeadY(world.getBlockSize());
            body.addHead(length);
            int nx = body.getHeadX(world.getBlockSize());
            int ny = body.getHeadY(world.getBlockSize());
            usedBlocks(ox, oy, nx, ny);
            return body;
        }
        return null;
    }

    /**
     * 占用格子
     *
     * @param ox 旧位置：X
     * @param oy 旧位置：Y
     * @param nx 新位置：X
     * @param ny 新位置：Y
     */
    private void usedBlocks(int ox, int oy, int nx, int ny) {
        if (ox == nx && oy != ny) {
            // 垂直
            if (oy > ny) {
                // 往上
                for (int y = oy - 1; y >= ny; y--) {
                    Block block = world.getBlock(ox, y);
                    if (usedBlockFailed(block)) break;
                }
            } else {
                // 往下
                for (int y = oy + 1; y <= ny; y++) {
                    Block block = world.getBlock(ox, y);
                    if (usedBlockFailed(block)) break;
                }
            }
        } else if (oy == ny && ox != nx) {
            // 水平
            if (ox > nx) {
                // 往左
                for (int x = ox - 1; x >= nx; x--) {
                    Block block = world.getBlock(x, oy);
                    if (usedBlockFailed(block)) break;
                }
            } else {
                // 往右
                for (int x = ox + 1; x <= nx; x++) {
                    Block block = world.getBlock(x, oy);
                    if (usedBlockFailed(block)) break;
                }
            }
        }
    }

    /**
     * 解除占用格子
     *
     * @param ox 旧位置：X
     * @param oy 旧位置：Y
     * @param nx 新位置：X
     * @param ny 新位置：Y
     */
    private void unusedBlocks(int ox, int oy, int nx, int ny) {
//        Log.d("Snake", String.format("unusedBlocks:ox=%s,oy=%s,nx=%s,ny=%s", ox, oy, nx, ny));
        if (ox == nx && oy != ny) {
            // 垂直
            if (oy > ny) {
                // 往上
                for (int y = oy; y > ny; y--) {
                    Block block = world.getBlock(ox, y);
                    if (null == block) continue;
                    block.setUsed(false);
                }
            } else {
                // 往下
                for (int y = oy; y < ny; y++) {
                    Block block = world.getBlock(ox, y);
                    if (null == block) continue;
                    block.setUsed(false);
                }
            }
        } else if (oy == ny && ox != nx) {
            // 水平
            if (ox > nx) {
                // 往左
                for (int x = ox; x > nx; x--) {
                    Block block = world.getBlock(x, oy);
                    if (null == block) continue;
                    block.setUsed(false);
                }
            } else {
                // 往右
                for (int x = ox; x < nx; x++) {
                    Block block = world.getBlock(x, oy);
                    if (null == block) continue;
                    block.setUsed(false);
                }
            }
        }
    }

    /**
     * 占用格子是否失败了
     *
     * @param block 格子
     * @return true，占用失败；false，占用成功
     */
    private boolean usedBlockFailed(Block block) {
        if (null == block || block.isUsed()) {
            // 占用格子失败，结束游戏
            world.overGame();
            return true;
        }
        block.setUsed(true);
        return false;
    }
}

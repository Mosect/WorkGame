package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 贪吃蛇游戏世界
 */
public class RetroSnakeWorld {

    /**
     * 边框宽度
     */
    private static final float EDGE_WIDTH = 5f;
    /**
     * 默认蛇长度
     */
    private static final int DEF_SNAKE_LENGTH = 4;

    private Snake snake; // 蛇
    private Reward reward; // 奖励
    private boolean over; // 表示游戏是否结束
    private Direction nextDirection;
    private boolean paused; // 表示游戏是否暂停
    private Rect panelBounds; // 面板边界
    private RectF panelEdges; // 面板边框
    private Paint paint; // 画笔
    private int blockSize;
    private int xBlocks;
    private int yBlocks;
    private Block[] blocks;
    private long tickTime;

    public RetroSnakeWorld(int blockSize, int xBlocks, int yBlocks, int panelX, int panelY) {
        this.blockSize = blockSize;
        this.xBlocks = xBlocks;
        this.yBlocks = yBlocks;
        // 计算面板边界
        this.panelBounds = new Rect(
                panelX, panelY,
                panelX + xBlocks * blockSize,
                panelY + yBlocks * blockSize
        );
        float edgeOffset = EDGE_WIDTH / 2;
        this.panelEdges = new RectF(
                this.panelBounds.left - edgeOffset,
                this.panelBounds.top - edgeOffset,
                this.panelBounds.right + edgeOffset,
                this.panelBounds.bottom + edgeOffset
        );

        // 产生画笔
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.WHITE);

        // 产生格子
        this.blocks = new Block[xBlocks * yBlocks];
        for (int i = 0; i < this.blocks.length; i++) {
            int x = i % xBlocks;
            int y = i / xBlocks;
            this.blocks[i] = new Block(x, y);
        }
    }

    public void init() {
        // 创建蛇
        snake = new Snake(this);
        int sx = (xBlocks - DEF_SNAKE_LENGTH) / 2;
        int sy = yBlocks / 2;
        SnakeBody body = new SnakeBody();
        int left = sx * blockSize;
        int top = sy * blockSize;
        int right = left + DEF_SNAKE_LENGTH * blockSize;
        int bottom = top + blockSize;
        body.set(left, top, right, bottom, Direction.RIGHT);
        snake.setBody(body);
        snake.setSpeed(300);
        moveSnake(Direction.RIGHT);

        reward = new Reward(this);
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getXBlocks() {
        return xBlocks;
    }

    public int getYBlocks() {
        return yBlocks;
    }

    /**
     * 获取蛇
     *
     * @return 蛇
     */
    public Snake getSnake() {
        return snake;
    }

    public Direction getNextDirection() {
        return nextDirection;
    }

    /**
     * 移动蛇
     *
     * @param direction 方向
     */
    public void moveSnake(Direction direction) {
        Direction snakeDirection = snake.getDirection();
        boolean reverse = direction.isReverse(snakeDirection);
        if (!reverse) {
            // 不是反方向
            nextDirection = direction;
        }
    }

    /**
     * 判断游戏是否结束
     *
     * @return true，游戏结束
     */
    public boolean isOver() {
        return over;
    }

    /**
     * 判断游戏是否暂停
     *
     * @return true，已经暂停
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * 设置游戏是否暂停
     *
     * @param paused 是否暂停
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * 推进游戏世界的时间
     */
    public void tick() {
        if (over) return;
        if (tickTime == 0) {
            tickTime = System.currentTimeMillis();
            return;
        }
        long now = System.currentTimeMillis();
        long time = now - tickTime;
        if (time <= 0) return;
        tickTime = now;

        if (null != snake) {
            snake.tick(time);
        }
        if (null != reward) {
            reward.tick();
        }
    }

    /**
     * 绘制游戏
     *
     * @param canvas 画布
     */
    public void draw(Canvas canvas) {
        // 绘制边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(EDGE_WIDTH);
        paint.setColor(Color.WHITE);
        canvas.drawRect(panelEdges, paint);
        // 绘制面板内容
        int sc = canvas.save();
        canvas.clipRect(panelBounds);
        canvas.translate(panelBounds.left, panelBounds.top);
        // 绘制蛇
        if (null != snake) {
            snake.draw(canvas);
        }
        // 绘制奖励
        if (null != reward) {
            reward.draw(canvas);
        }
        canvas.restoreToCount(sc);
    }

    /**
     * 获取指定位置格子
     *
     * @param x 位置：X
     * @param y 位置：Y
     * @return 格子对象
     */
    public Block getBlock(int x, int y) {
        if (x >= 0 && x < xBlocks && y >= 0 && y < yBlocks) {
            return blocks[x + y * xBlocks];
        }
        return null;
    }
}

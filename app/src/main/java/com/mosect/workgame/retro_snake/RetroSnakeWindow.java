package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

import com.mosect.workgame.base.GameContext;
import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.graphics.Circle;
import com.mosect.workgame.graphics.Graphics;
import com.mosect.workgame.graphics.RegularTriangle;
import com.mosect.workgame.ui.BaseWindow;
import com.mosect.workgame.ui.MainWindow;
import com.mosect.workgame.widget.ActionButton;

import java.util.LinkedList;

/**
 * 贪吃蛇
 */
public class RetroSnakeWindow extends BaseWindow {

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int COLOR_NORMAL = Color.WHITE;
    private static final int COLOR_PRESSED = Color.argb(0xFF, 0xCC, 0xCC, 0xCC);
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 120;
    private static final int BUTTON_PADDING = 20;
    private static final int CONTENT_MARGIN = 20;
    private static final int X_BLOCKS = 60;
    private static final int Y_BLOCKS = 40;
    private static final int LINE_WIDTH = 6;
    private static final int SNAKE_DEF_LENGTH = 4;
    private static final long TICK_TIME = 50;

    private Rect panelBounds; // 面板边界
    private int blockSize; // 块大小
    private int panelX; // 面板位置：X
    private int panelY; // 面板位置：Y
    private Paint paint; // 画笔
    private Block[] blocks; // 块列表
    private LinkedList<Block> snakeBody; // 蛇身体
    private int snakeOffset; // 蛇便宜量
    private Rect blockRect;
    private long lastTickTime;
    private int snakeSpeed; // 蛇速度
    private Direction snakeDirection; // 蛇方向

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        setSize(WIDTH, HEIGHT);
        addOrientationButton(0, 500, 0, new Runnable() {
            @Override
            public void run() {
                // 上
            }
        });
        addOrientationButton(0, 640, 270, new Runnable() {
            @Override
            public void run() {
                // 左
            }
        });
        addOrientationButton(0, 780, 180, new Runnable() {
            @Override
            public void run() {
                // 下
            }
        });
        addOrientationButton(1800, 640, 90, new Runnable() {
            @Override
            public void run() {
                // 右
            }
        });

        // 添加加速按钮
        Graphics[] icons = {
                newCircle(COLOR_NORMAL),
                newCircle(COLOR_PRESSED),
        };
        addButton(1800, 780, icons, new Runnable() {
            @Override
            public void run() {
                // 加速
            }
        });

        // 计算面板边界
        int contentX = CONTENT_MARGIN + BUTTON_WIDTH;
        int contentY = CONTENT_MARGIN;
        int contentWidth = getWidth() - CONTENT_MARGIN * 2 - BUTTON_WIDTH * 2;
        int contentHeight = getHeight() - CONTENT_MARGIN * 2;
        int blockWidth = contentWidth / X_BLOCKS;
        int blockHeight = contentHeight / Y_BLOCKS;
        blockSize = Math.min(blockWidth, blockHeight);
        int panelWidth = blockSize * X_BLOCKS;
        int panelHeight = blockSize * Y_BLOCKS;
        panelX = (contentWidth - panelWidth) / 2 + contentX;
        panelY = (contentHeight - panelHeight) / 2 + contentY;
        panelBounds = new Rect();
        panelBounds.left = panelX - LINE_WIDTH / 2;
        panelBounds.top = panelY - LINE_WIDTH / 2;
        panelBounds.right = panelX + panelWidth + LINE_WIDTH / 2;
        panelBounds.bottom = panelY + panelHeight + LINE_WIDTH / 2;

        // 创建画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_WIDTH);

        // 产生块
        blocks = new Block[X_BLOCKS * Y_BLOCKS];
        for (int i = 0; i < blocks.length; i++) {
            int x = i % X_BLOCKS;
            int y = i / X_BLOCKS;
            blocks[i] = new Block(x, y);
        }
        // 产生蛇身体
        snakeBody = new LinkedList<>();
        blockRect = new Rect();
        int x = (X_BLOCKS - SNAKE_DEF_LENGTH) / 2;
        int y = Y_BLOCKS / 2;
        for (int i = 0; i < SNAKE_DEF_LENGTH; i++) {
            addSnakeBody(x + i, y, Direction.RIGHT);
        }
        snakeOffset = 0;
        snakeSpeed = 2;
        snakeDirection = Direction.RIGHT;
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    // 上
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // 下
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    // 左
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    // 右
                    break;
                case KeyEvent.KEYCODE_Z:
                case KeyEvent.KEYCODE_X:
                case KeyEvent.KEYCODE_C:
                case KeyEvent.KEYCODE_V:
                    // 加速
                    break;
                case KeyEvent.KEYCODE_ESCAPE:
                case KeyEvent.KEYCODE_E:
                    // 退出
                    getContext().setWindow(new MainWindow());
                    break;
            }
        }
    }

    @Override
    protected void onDrawWindowContent(Canvas canvas) {
        super.onDrawWindowContent(canvas);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_WIDTH);
        canvas.drawRect(panelBounds, paint);
        // 绘制格子
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        for (int x = 1; x < X_BLOCKS; x++) {
            float lx = panelX + x * blockSize;
            canvas.drawLine(lx, panelY, lx, panelY + blockSize * Y_BLOCKS, paint);
        }
        for (int y = 1; y < Y_BLOCKS; y++) {
            float ly = panelY + y * blockSize;
            canvas.drawLine(panelX, ly, panelX + blockSize * X_BLOCKS, ly, paint);
        }

        // 绘制蛇
        Block first = snakeBody.getFirst();
        Block last = snakeBody.getLast();
        for (Block block : snakeBody) {
            if (snakeOffset == 0) {
                drawBlock(canvas, block, 0);
            } else {
                if (block == first) {
                    drawBlock(canvas, block, snakeOffset - blockSize);
                } else if (block == last) {
                    drawBlock(canvas, block, snakeOffset);
                } else {
                    drawBlock(canvas, block, 0);
                }
            }
        }
    }

    @Override
    protected void onTick() {
        super.onTick();
        long now = System.currentTimeMillis();
        if (lastTickTime == 0) {
            onTickGame();
            lastTickTime = now;
        } else {
            while (now - lastTickTime >= TICK_TIME) {
                lastTickTime += TICK_TIME;
                onTickGame();
            }
        }
    }

    private void addOrientationButton(int x, int y, int angle, Runnable action) {
        Graphics[] icons = {
                newRegularTriangle(angle, COLOR_NORMAL),
                newRegularTriangle(angle, COLOR_PRESSED),
        };
        addButton(x, y, icons, action);
    }

    private void addButton(int x, int y, Graphics[] icons, Runnable action) {
        ActionButton button = new ActionButton();
        button.setIcons(icons);
        button.setPadding(BUTTON_PADDING);
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setLocation(x, y);
        button.update();
        button.setAction(action);
        addWidget(button);
    }

    private Graphics newRegularTriangle(int angle, int color) {
        RegularTriangle regularTriangle = new RegularTriangle();
        regularTriangle.setAngle(angle);
        regularTriangle.setColor(color);
        return regularTriangle;
    }

    private Graphics newCircle(int color) {
        Circle circle = new Circle();
        circle.setColor(color);
        return circle;
    }

    /**
     * 获取块
     *
     * @param x 位置：X
     * @param y 位置：Y
     * @return 块
     */
    private Block getBlock(int x, int y) {
        if (x >= 0 && x < X_BLOCKS && y >= 0 && y < Y_BLOCKS) {
            return blocks[y * X_BLOCKS + x];
        }
        return null;
    }

    /**
     * 添加蛇身体
     *
     * @param x         位置：X
     * @param y         位置：Y
     * @param direction 方向
     */
    private void addSnakeBody(int x, int y, Direction direction) {
        Block block = getBlock(x, y);
        block.setState(Block.State.SNAKE);
        block.setDirection(direction);
        snakeBody.addFirst(block);
    }

    private void onTickGame() {
        int oldOffset = snakeOffset;
        snakeOffset += snakeSpeed;
        if (oldOffset == 0) {
            if (snakeOffset != 0) {
                int moveCount = snakeOffset / blockSize;
                snakeOffset = snakeOffset % blockSize;
            }
        }
        while (snakeOffset > blockSize) {
            Block first = snakeBody.getFirst();
            Block next = null;
            switch (snakeDirection) {
                case LEFT:
                    next = getBlock(first.getX() - 1, first.getY());
                    break;
                case UP:
                    next = getBlock(first.getX(), first.getY() - 1);
                    break;
                case RIGHT:
                    next = getBlock(first.getX() + 1, first.getY());
                    break;
                case DOWN:
                    next = getBlock(first.getX(), first.getY() + 1);
                    break;
            }
            if (null == next) {
                // 不存在下一块，游戏结束
            } else {
                addSnakeBody(next.getX(), next.getY(), snakeDirection);
            }
        }
    }

    private void drawBlock(Canvas canvas, Block block, int offset) {
        int left = panelX + block.getX() * blockSize;
        int top = panelY + block.getY() * blockSize;
        int right = left + blockSize;
        int bottom = top + blockSize;
        blockRect.set(left, top, right, bottom);
        switch (block.getDirection()) {
            case UP:
            case DOWN:
                blockRect.offset(0, offset);
                break;
            case LEFT:
            case RIGHT:
                blockRect.offset(offset, 0);
                break;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawRect(blockRect, paint);
    }
}

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

/**
 * 贪吃蛇
 */
public class RetroSnakeWindow extends BaseWindow {

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int COLOR_NORMAL = Color.WHITE;
    private static final int COLOR_PRESSED = Color.argb(0xFF, 0xCC, 0xCC, 0xCC);
    private static final int BUTTON_WIDTH = 180;
    private static final int BUTTON_HEIGHT = 180;
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
    private GamePanel panel;
    private Snake snake;

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        context.setFps(0);
        setSize(WIDTH, HEIGHT);
        addOrientationButton(20, 400, 0, new Runnable() {
            @Override
            public void run() {
                // 上
                snake.setDirection(Direction.UP);
            }
        });
        addOrientationButton(20, 600, 270, new Runnable() {
            @Override
            public void run() {
                // 左
                snake.setDirection(Direction.LEFT);
            }
        });
        addOrientationButton(20, 800, 180, new Runnable() {
            @Override
            public void run() {
                // 下
                snake.setDirection(Direction.DOWN);
            }
        });
        addOrientationButton(1720, 600, 90, new Runnable() {
            @Override
            public void run() {
                // 右
                snake.setDirection(Direction.RIGHT);
            }
        });

        // 添加加速按钮
        Graphics[] icons = {
                newCircle(COLOR_NORMAL),
                newCircle(COLOR_PRESSED),
        };
        addButton(1720, 800, icons, new Runnable() {
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

        panel = new GamePanel(panelX, panelY, X_BLOCKS, Y_BLOCKS, blockSize);
        snake = new Snake(panel);
        int bx = (panel.getXCount() - SNAKE_DEF_LENGTH) / 2;
        int by = panel.getYCount() / 2;
        int bl = bx * blockSize + panelX;
        int br = bl + blockSize * SNAKE_DEF_LENGTH;
        int bt = by * blockSize + panelY;
        int bb = bt + blockSize;
        snake.addBody(bl, bt, br, bb, Direction.RIGHT);
        snake.setSpeed(blockSize * 10);
        snake.setDirection(Direction.RIGHT);
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    // 上
                    snake.setDirection(Direction.UP);
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // 下
                    snake.setDirection(Direction.DOWN);
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    // 左
                    snake.setDirection(Direction.LEFT);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    // 右
                    snake.setDirection(Direction.RIGHT);
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
        /*paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        for (int x = 1; x < X_BLOCKS; x++) {
            float lx = panelX + x * blockSize;
            canvas.drawLine(lx, panelY, lx, panelY + blockSize * Y_BLOCKS, paint);
        }
        for (int y = 1; y < Y_BLOCKS; y++) {
            float ly = panelY + y * blockSize;
            canvas.drawLine(panelX, ly, panelX + blockSize * X_BLOCKS, ly, paint);
        }*/

        // 绘制蛇
        snake.draw(canvas);
    }

    @Override
    protected void onTick() {
        super.onTick();
        snake.tick();
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

}

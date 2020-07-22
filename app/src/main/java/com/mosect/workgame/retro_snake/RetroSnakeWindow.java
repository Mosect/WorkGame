package com.mosect.workgame.retro_snake;

import android.graphics.Canvas;
import android.graphics.Color;
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

    private RetroSnakeWorld world;

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        context.setFps(0);
        setSize(WIDTH, HEIGHT);
        addOrientationButton(20, 400, 0, new Runnable() {
            @Override
            public void run() {
                // 上
                world.moveSnake(Direction.UP);
            }
        });
        addOrientationButton(20, 600, 270, new Runnable() {
            @Override
            public void run() {
                // 左
                world.moveSnake(Direction.LEFT);
            }
        });
        addOrientationButton(20, 800, 180, new Runnable() {
            @Override
            public void run() {
                // 下
                world.moveSnake(Direction.DOWN);
            }
        });
        addOrientationButton(1720, 600, 90, new Runnable() {
            @Override
            public void run() {
                // 右
                world.moveSnake(Direction.RIGHT);
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

        // 创建游戏世界
        int contentX = CONTENT_MARGIN + BUTTON_WIDTH;
        int contentY = CONTENT_MARGIN;
        int contentWidth = getWidth() - CONTENT_MARGIN * 2 - BUTTON_WIDTH * 2;
        int contentHeight = getHeight() - CONTENT_MARGIN * 2;
        int blockWidth = contentWidth / X_BLOCKS;
        int blockHeight = contentHeight / Y_BLOCKS;
        int blockSize = Math.min(blockWidth, blockHeight);
        int panelWidth = blockSize * X_BLOCKS;
        int panelHeight = blockSize * Y_BLOCKS;
        int panelX = (contentWidth - panelWidth) / 2 + contentX;
        int panelY = (contentHeight - panelHeight) / 2 + contentY;
        world = new RetroSnakeWorld(blockSize, X_BLOCKS, Y_BLOCKS, panelX, panelY);
        world.init();
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    // 上
                    world.moveSnake(Direction.UP);
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // 下
                    world.moveSnake(Direction.DOWN);
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    // 左
                    world.moveSnake(Direction.LEFT);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    // 右
                    world.moveSnake(Direction.RIGHT);
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
        world.draw(canvas);
    }

    @Override
    protected void onTick() {
        super.onTick();
        world.tick();
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

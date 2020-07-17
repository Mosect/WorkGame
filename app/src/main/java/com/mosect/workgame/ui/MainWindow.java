package com.mosect.workgame.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;

import com.mosect.workgame.base.GameContext;
import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.util.DataBuffer;

/**
 * 主窗口
 */
public class MainWindow extends BaseWindow {

    private static final String TAG = "MainWindow";
    private static final int COLUMNS = 6;
    private static final int ROWS = 3;
    private static final int SPACING = 10;

    private MainMenu focusedMenu;

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        Log.d(TAG, "onCreate");
        setSize(1920, 1080);
        context.setFps(60);
        addMenu("贪吃蛇", null, new Runnable() {
            @Override
            public void run() {
                getContext().setWindow(new RetroSnakerWindow());
            }
        });
        addMenu("打砖块", null, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void addMenu(String name, Bitmap icon, Runnable action) {
        MainMenu menu = new MainMenu(name, icon);
        menu.setAction(action);
        addWidget(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onTick() {
        super.onTick();
//        Log.d(TAG, "onTick");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d(TAG, "onDraw");
    }

    @Override
    protected void onDisplayConfigured() {
        super.onDisplayConfigured();
        Log.d(TAG, "onDisplayConfigured");
        int menuWidth = (getWidth() - (COLUMNS + 1) * SPACING) / COLUMNS;
        int menuHeight = (getHeight() - (ROWS + 1) * SPACING) / ROWS;
        int count = getWidgetCount();
        for (int i = 0; i < count; i++) {
            int row = i / COLUMNS;
            int col = i % COLUMNS;
            MainMenu menu = (MainMenu) getWidget(i);
            menu.setPadding(10);
            menu.setSize(menuWidth, menuHeight);
            int x = (col + 1) * SPACING + col * menuWidth;
            int y = (row + 1) * SPACING + row * menuHeight;
            menu.setLocation(x, y);
        }
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        Log.d(TAG, "onKeyEvent:" + event);
        switch (event.getCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    nextFocused(0, -1);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    nextFocused(0, 1);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    nextFocused(-1, 0);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    nextFocused(1, 0);
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (null == focusedMenu) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        nextFocused(1, 0);
                    }
                }
                break;
            case KeyEvent.KEYCODE_ESCAPE:
                getContext().exit();
                break;
        }
    }

    @Override
    protected void onEvent(DataBuffer event) {
        super.onEvent(event);
        event.resetPosition();
        int type = event.getInt();
        Log.d(TAG, "onEvent:" + type);
    }

    private void nextFocused(int x, int y) {
        int count = getWidgetCount();
        if (count <= 0) return;
        if (x == 0 && y == 0) return;
        if (null == focusedMenu) {
            focusedMenu = (MainMenu) getWidget(0);
            focusedMenu.setFocused(true);
        } else {
            focusedMenu.setFocused(false);
            boolean pressed = focusedMenu.isPressed();
            focusedMenu.setPressed(false);

            int index = indexOfWidget(focusedMenu);
            int rows = count / COLUMNS +
                    (count % COLUMNS == 0 ? 0 : 1);
            int col = index % COLUMNS;
            int row = index / COLUMNS;
            int nextCol = col + x;
            if (nextCol < 0) {
                nextCol = COLUMNS - 1;
            } else if (nextCol >= COLUMNS) {
                nextCol = 0;
            }
            int nextRow = row + y;
            if (nextRow < 0) {
                nextRow = rows - 1;
            } else if (nextRow >= rows) {
                nextRow = 0;
            }
            int nextIndex = nextCol + nextRow * COLUMNS;
            if (nextIndex >= count) {
                nextIndex = 0;
            }
            focusedMenu = (MainMenu) getWidget(nextIndex);
            focusedMenu.setFocused(true);
            focusedMenu.setPressed(pressed);
        }
    }

    private boolean isMenuPointer(float x, float y, MainMenu menu) {
        float left = menu.getX();
        float top = menu.getY();
        float right = left + menu.getWidth();
        float bottom = top + menu.getHeight();
        return x >= left && x < right && y >= top && y < bottom;
    }
}

package com.mosect.workgame.base;

import android.graphics.Canvas;

import com.mosect.workgame.util.DataBuffer;

/**
 * 游戏窗口
 */
public class GameWindow {

    private int width;
    private int height;
    private int x;
    private int y;
    private GameContext context;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * 设置窗口
     *
     * @param width  窗口宽
     * @param height 窗口高
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (null != context) {
            context.updateWindowSize();
        }
    }

    /**
     * 设置窗口位置
     *
     * @param x 位置：X
     * @param y 位置：Y
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameContext getContext() {
        return context;
    }

    protected void onCreate(GameContext context) {
        this.context = context;
    }

    protected void onPause() {
    }

    protected void onResume() {
    }

    protected void onDestroy() {
    }

    protected void onTick() {
    }

    protected void onDraw(Canvas canvas) {
        int sc = canvas.save();
        canvas.clipRect(x, y, x + width, y + height);
        canvas.translate(x, y);
        onDrawWindowContent(canvas);
        canvas.restoreToCount(sc);
    }

    /**
     * 显示器已经配置完成，应该在此处通过显示器信息设置窗口位置，默认居中显示窗口
     */
    protected void onDisplayConfigured() {
        GameDisplay display = getContext().getDisplay();
        int canvasWidth = display.getCanvasWidth();
        int canvasHeight = display.getCanvasHeight();
        int contentWidth = display.getContentWidth();
        int contentHeight = display.getContentHeight();
        int x = (canvasWidth - contentWidth) / 2;
        int y = (canvasHeight - contentHeight) / 2;
        setLocation(x, y);
    }

    protected void onDrawWindowContent(Canvas canvas) {
    }

    protected void onTouchEvent(GameTouchEvent event) {
        event.offset(x, y);
    }

    protected void onKeyEvent(GameKeyEvent event) {
    }

    protected void onEvent(DataBuffer event) {
    }
}

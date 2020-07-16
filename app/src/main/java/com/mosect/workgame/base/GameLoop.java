package com.mosect.workgame.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.mosect.workgame.util.DataBuffer;

import java.util.LinkedList;

/**
 * 游戏循环
 */
class GameLoop implements GameContext {

    private static final int EVENT_CHANGE_WINDOW = 1;
    private static final int EVENT_CHANGE_FPS = 2;
    private static final int EVENT_CHANGE_HOLDER = 3;
    private static final int EVENT_CHANGE_DISPLAY_SIZE = 4;
    private static final int EVENT_PAUSE = 5;
    private static final int EVENT_RESUME = 6;
    private static final int EVENT_DESTROY = 7;
    private static final int EVENT_TOUCH = 8;
    private static final int EVENT_KEY = 9;
    private static final int EVENT_CHANGE_WINDOW_SIZE = 10;

    private Context context;
    private SurfaceHolder holder;
    private GameDisplay display;
    private int displayWidth;
    private int displayHeight;
    private int fps;
    private long drawWaitTime;
    private long drawTime;
    private boolean loop;
    private boolean started;
    private GameWindow window;
    private boolean resumed;

    private final byte[] eventLock = new byte[0];
    private LinkedList<DataBuffer> eventDataCacheList;
    private LinkedList<DataBuffer> events; // 事件列表
    private GameTouchEvent touchEvent = new GameTouchEvent();
    private GameKeyEvent keyEvent = new GameKeyEvent();

    GameLoop(Context context) {
        this.context = context;
    }

    void setHolder(SurfaceHolder holder) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_CHANGE_HOLDER);
        eventData.putObject(holder);
        postEvent(eventData);
    }

    void setDisplaySize(int width, int height) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_CHANGE_DISPLAY_SIZE);
        eventData.putInt(width);
        eventData.putInt(height);
        postEvent(eventData);
    }

    void start() {
        if (started) return;
        if (loop) return;
        started = true;
        loop = true;
        // 开始游戏循环
        new Thread("GameLoop") {
            @Override
            public void run() {
                while (loop) {
                    // 先处理事件
                    DataBuffer event;
                    while (loop && (event = takeEvent()) != null) {
                        beforeHandleEvent(event);
                        event.resetPosition();
                        int type = event.getInt();
                        switch (type) {
                            case EVENT_CHANGE_WINDOW:
                                changeWindow((GameWindow) event.getObject());
                                break;
                            case EVENT_CHANGE_FPS:
                                changeFps(event.getInt());
                                break;
                            case EVENT_CHANGE_HOLDER:
                                changeHolder((SurfaceHolder) event.getObject());
                                break;
                            case EVENT_CHANGE_DISPLAY_SIZE:
                                changeDisplaySize(event.getInt(), event.getInt());
                                break;
                            case EVENT_PAUSE:
                                onPause();
                                break;
                            case EVENT_RESUME:
                                onResume();
                                break;
                            case EVENT_DESTROY:
                                onDestroy();
                                break;
                            case EVENT_TOUCH:
                                onTouch(event);
                                break;
                            case EVENT_KEY:
                                onKey(event);
                                break;
                            case EVENT_CHANGE_WINDOW_SIZE:
                                changeWindowSize(event.getInt(), event.getInt());
                                break;
                            default:
                                onOtherEvent(event);
                                break;
                        }
                        releaseEventData(event);
                    }

                    // 处理数据
                    onTick();

                    // 绘制
                    onDraw();
                }
            }
        }.start();
    }

    void pause() {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_PAUSE);
        postEvent(eventData);
    }

    void resume() {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_RESUME);
        postEvent(eventData);
    }

    void destroy() {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_DESTROY);
        postEvent(eventData);
    }

    void dispatchTouchEvent(MotionEvent event) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_TOUCH);
        eventData.putInt(event.getAction());
        int count = event.getPointerCount();
        eventData.putInt(count);
        for (int i = 0; i < count; i++) {
            float x = event.getX(i);
            float y = event.getY(i);
            eventData.putFloat(x);
            eventData.putFloat(y);
        }
        postEvent(eventData);
    }

    void dispatchKeyEvent(KeyEvent event) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_KEY);
        eventData.putInt(event.getAction());
        eventData.putInt(event.getKeyCode());
        eventData.putInt(event.getUnicodeChar());
        postEvent(eventData);
    }

    @Override
    public void setWindow(GameWindow window) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_CHANGE_WINDOW);
        eventData.putObject(window);
        postEvent(eventData);
    }

    @Override
    public void updateWindowSize() {
        if (null == window) return;
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_CHANGE_WINDOW_SIZE);
        eventData.putInt(window.getWidth());
        eventData.putInt(window.getHeight());
        postEvent(eventData);
    }

    @Override
    public void setFps(int fps) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_CHANGE_FPS);
        eventData.putInt(fps);
        postEvent(eventData);
    }

    @Override
    public int getFps() {
        return fps;
    }

    @Override
    public GameDisplay getDisplay() {
        return display;
    }

    private void postEvent(DataBuffer eventData) {
        synchronized (eventLock) {
            if (null == events) {
                events = new LinkedList<>();
            }
            eventData.resetPosition();
            events.add(eventData);
        }
    }

    private DataBuffer takeEvent() {
        synchronized (eventLock) {
            if (null != events && events.size() > 0) {
                DataBuffer event = events.removeFirst();
                event.resetPosition();
                return event;
            }
            return null;
        }
    }

    private DataBuffer createEventData() {
        synchronized (eventLock) {
            DataBuffer eventData;
            if (null == eventDataCacheList || eventDataCacheList.isEmpty()) {
                eventData = new DataBuffer();
            } else {
                eventData = eventDataCacheList.removeFirst();
            }
            eventData.resetPosition();
            return eventData;
        }
    }

    private void releaseEventData(DataBuffer eventData) {
        synchronized (eventLock) {
            if (null == eventDataCacheList) {
                eventDataCacheList = new LinkedList<>();
            }
            eventData.resetPosition();
            eventDataCacheList.add(eventData);
        }
    }

    protected void beforeHandleEvent(DataBuffer event) {
    }

    protected void changeWindow(GameWindow window) {
        if (null != this.window) {
            this.window.onDestroy();
        }
        this.window = window;
        window.onCreate(this);
        if (resumed) {
            window.onResume();
        }
    }

    protected void changeFps(int fps) {
        if (fps > 0) {
            drawWaitTime = 1000 / fps;
        } else {
            drawWaitTime = 0;
        }
        this.fps = fps;
    }

    protected void changeHolder(SurfaceHolder holder) {
        this.holder = holder;
        destroyDisplay();
        createDisplay();
    }

    protected void changeDisplaySize(int width, int height) {
        this.displayWidth = width;
        this.displayHeight = height;
        destroyDisplay();
        createDisplay();
    }

    protected void onPause() {
        resumed = false;
        if (null != window) {
            window.onPause();
        }
    }

    protected void onResume() {
        resumed = true;
        if (null != window) {
            window.onResume();
        }
    }

    protected void onDestroy() {
        loop = false;
        if (null != window) {
            window.onDestroy();
        }
        destroyDisplay();
    }

    protected void onTick() {
        if (null != window) {
            window.onTick();
        }
    }

    protected void onDraw() {
        if (null == display) return;
        Canvas canvas = display.getCanvas();
        if (null == canvas) return;
        long now = System.currentTimeMillis();
        long time = now - drawTime;
        if (time < drawWaitTime) return;

        drawTime = now;
        display.startDraw();
        canvas.drawColor(Color.BLACK);
        if (null != window) {
            window.onDraw(canvas);
        }
        display.endDraw();
    }

    protected void onTouch(DataBuffer event) {
        if (null == display) return;
        if (null == window) return;
        int cw = display.getCanvasWidth();
        int ch = display.getCanvasHeight();
        int dw = display.getWidth();
        int dh = display.getHeight();
        if (cw <= 0 || ch <= 0 || dw <= 0 || dh <= 0) return;
        float sw = cw / (float) dw;
        float sh = ch / (float) dh;

        int action = event.getInt();
        touchEvent.setAction(action);
        int pointerCount = event.getInt();
        touchEvent.setPointerCount(pointerCount);
        float[] pointers = touchEvent.getPointers();
        for (int i = 0; i < pointerCount; i++) {
            float x = event.getFloat() * sw;
            float y = event.getFloat() * sh;
            pointers[i * 2] = x;
            pointers[i * 2 + 1] = y;
        }
        window.onTouchEvent(touchEvent);
    }

    protected void onKey(DataBuffer event) {
        if (null == window) return;
        keyEvent.setAction(event.getInt());
        keyEvent.setCode(event.getInt());
        keyEvent.setUnicode(event.getInt());
        window.onKeyEvent(keyEvent);
    }

    protected void changeWindowSize(int width, int height) {
        if (null == display) return;
        display.setContentSize(width, height);
        if (null != window) {
            window.onDisplayConfigured();
        }
    }

    protected void onOtherEvent(DataBuffer event) {
        if (null != window) {
            window.onEvent(event);
        }
    }

    private void destroyDisplay() {
        if (null != display) {
            display.destroy();
            display = null;
        }
    }

    private void createDisplay() {
        if (null != holder && displayWidth > 0 && displayHeight > 0) {
            display = new GameDisplay(holder, displayWidth, displayHeight);
            if (null != window) {
                display.setContentSize(window.getWidth(), window.getHeight());
                window.onDisplayConfigured();
            }
        }
    }
}

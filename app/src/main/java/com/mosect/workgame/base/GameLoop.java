package com.mosect.workgame.base;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.mosect.workgame.util.DataBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    private enum TouchState {
        UP, DOWN, MOVE;
    }

    private Activity activity;
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
    private List<TouchState> touchStates = new ArrayList<>();

    GameLoop(Activity activity) {
        this.activity = activity;
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
        Log.d("GameLoop", "dispatchTouchEvent:" + event.getPointerCount());
        int index = event.getActionIndex();
        int action = event.getAction();
        for (int i = 0; i < ; i++) {
            
        }
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clearTouchStates();
                break;
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int count = event.getPointerCount();
                for (int i = 0; i < count; i++) {
                    int pointerId = event.getPointerId(i);
                    float x = event.getX(i);
                    float y = event.getY(i);
                    postTouchEvent(action, pointerId, x, y);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerId = event.getPointerId(index);
                float x = event.getX(index);
                float y = event.getY(index);
                postTouchEvent(MotionEvent.ACTION_UP, pointerId, x, y);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerId = event.getPointerId(index);
                float x = event.getX(index);
                float y = event.getY(index);
                postTouchEvent(MotionEvent.ACTION_DOWN, pointerId, x, y);
                break;
            }
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                postTouchEvent(action, event.getPointerId(0), event.getX(), event.getY());
                break;
        }
    }

    private void postTouchEvent(int action, int pointerId, float x, float y) {
        DataBuffer eventData = createEventData();
        eventData.putInt(EVENT_TOUCH);
        eventData.putInt(action);
        eventData.putInt(pointerId);
        eventData.putFloat(x);
        eventData.putFloat(y);
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
    public void exit() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        });
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
        canvas.drawColor(Color.BLACK);
        if (null != window) {
            window.onDraw(canvas);
        }
        display.flush();
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
        int pointerId = event.getInt();
        touchEvent.setPointerId(pointerId);
        float x = event.getFloat();
        float y = event.getFloat();
        touchEvent.setX(x * sw);
        touchEvent.setY(y * sh);
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

    private TouchState getTouchState(int id) {
        if (null != touchStates && id >= 0 && id < touchStates.size()) {
            return touchStates.get(id);
        }
        return TouchState.UP;
    }

    private void setTouchState(int id, TouchState state) {
        if (id < 0) return;
        if (null == touchStates) touchStates = new ArrayList<>();
        if (id < touchStates.size()) {
            touchStates.set(id, state);
        } else {
            while (touchStates.size() < id) {
                touchStates.add(TouchState.UP);
            }
            touchStates.add(state);
        }
    }

    private void clearTouchStates() {
        if (null != touchStates) {
            for (int i = 0; i < touchStates.size(); i++) {
                touchStates.set(i, TouchState.UP);
            }
        }
    }
}

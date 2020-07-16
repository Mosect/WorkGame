package com.mosect.workgame.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.mosect.workgame.base.GameContext;
import com.mosect.workgame.base.GameKeyEvent;
import com.mosect.workgame.base.GameTouchEvent;
import com.mosect.workgame.base.GameWindow;
import com.mosect.workgame.util.DataBuffer;

public class MainWindow extends GameWindow {

    private static final String TAG = "MainWindow";

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        setSize(640, 480);
        context.setFps(60);
        Log.d(TAG, "onCreate");
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
    }

    @Override
    protected void onDrawWindowContent(Canvas canvas) {
        super.onDrawWindowContent(canvas);
        canvas.drawColor(Color.RED);
//        Log.d(TAG, "onDrawWindowContent");
    }

    @Override
    protected void onTouchEvent(GameTouchEvent event) {
        super.onTouchEvent(event);
        Log.d(TAG, "onTouchEvent:" + event);
    }

    @Override
    protected void onKeyEvent(GameKeyEvent event) {
        super.onKeyEvent(event);
        Log.d(TAG, "onKeyEvent:" + event);
    }

    @Override
    protected void onEvent(DataBuffer event) {
        super.onEvent(event);
        event.resetPosition();
        int type = event.getInt();
        Log.d(TAG, "onEvent:" + type);
    }
}

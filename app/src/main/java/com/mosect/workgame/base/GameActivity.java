package com.mosect.workgame.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mosect.workgame.ui.MainWindow;

/**
 * 游戏页面
 */
public class GameActivity extends Activity {

    private GameLoop gameLoop; // 游戏循环

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置游戏视图
        SurfaceView view = new SurfaceView(this);
        setContentView(view);
        view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoop.setHolder(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                gameLoop.setDisplaySize(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                gameLoop.setHolder(null);
            }
        });
        // 开启游戏线程
        gameLoop = new GameLoop(this);
        gameLoop.start();
        gameLoop.setWindow(new MainWindow());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameLoop.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        gameLoop.dispatchKeyEvent(event);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        gameLoop.dispatchKeyEvent(event);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameLoop.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameLoop.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameLoop.destroy();
    }
}

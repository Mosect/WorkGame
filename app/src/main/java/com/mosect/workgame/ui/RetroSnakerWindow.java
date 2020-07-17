package com.mosect.workgame.ui;

import android.graphics.Canvas;

import com.mosect.workgame.base.GameContext;
import com.mosect.workgame.base.GameWindow;
import com.mosect.workgame.graphics.RegularTriangle;

/**
 * 贪吃蛇
 */
public class RetroSnakerWindow extends GameWindow {

    private RegularTriangle regularTriangle;

    @Override
    protected void onCreate(GameContext context) {
        super.onCreate(context);
        setSize(1920, 1080);
        regularTriangle = new RegularTriangle();
        regularTriangle.setAngle(90);
        regularTriangle.setSize(120, 120);
        regularTriangle.update();
    }

    @Override
    protected void onDrawWindowContent(Canvas canvas) {
        super.onDrawWindowContent(canvas);
        regularTriangle.draw(canvas);
    }

}

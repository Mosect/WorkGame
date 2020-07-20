package com.mosect.workgame.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mosect.workgame.base.GameTouchEvent;
import com.mosect.workgame.graphics.Graphics;

public class ActionButton extends Widget {

    private Graphics[] icons;
    private int padding;
    private boolean pressed;
    private Runnable action;

    public void setIcons(Graphics[] icons) {
        this.icons = icons;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void update() {
        super.update();
        if (null != icons) {
            for (Graphics icon : icons) {
                int width = Math.max(0, getWidth() - padding * 2);
                int height = Math.max(0, getHeight() - padding * 2);
                icon.setSize(width, height);
                icon.update();
            }
        }
    }

    @Override
    protected void onTouchEvent(GameTouchEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressed = true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                pressed = false;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int iconIndex = pressed ? 1 : 0;
        if (null != icons && iconIndex < icons.length) {
            int sc = canvas.save();
            Graphics icon = icons[iconIndex];
            canvas.clipRect(padding, padding,
                    padding + icon.getWidth(), padding + icon.getHeight());
            canvas.translate(padding, padding);
            icon.draw(canvas);
            canvas.restoreToCount(sc);
        }
    }

    @Override
    protected void onClick() {
        super.onClick();
        if (null != action) {
            action.run();
        }
    }
}

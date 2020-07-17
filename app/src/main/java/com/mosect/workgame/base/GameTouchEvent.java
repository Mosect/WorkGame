package com.mosect.workgame.base;

public class GameTouchEvent {

    private int action;
    private int pointerId;
    private float x;
    private float y;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getPointerId() {
        return pointerId;
    }

    public void setPointerId(int pointerId) {
        this.pointerId = pointerId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void offset(float x, float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return String.format("action=%s,pointerId=%s,x=%s,y=%s",
                action, pointerId, x, y);
    }
}

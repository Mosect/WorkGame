package com.mosect.workgame.base;

import java.util.Arrays;

public class GameTouchEvent {

    private int action;
    private int pointerCount;
    private float[] pointers;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public float getX(int index) {
        return pointers[index * 2];
    }

    public float getY(int index) {
        return pointers[index * 2 + 1];
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public void setPointerCount(int pointerCount) {
        if (pointerCount < 0) {
            throw new IllegalArgumentException("pointerCount < 0");
        }
        this.pointerCount = pointerCount;
        if (null == pointers) {
            int count = Math.max(8, pointerCount * 2);
            pointers = new float[count];
        } else if (pointers.length / 2 < pointerCount) {
            pointers = Arrays.copyOf(pointers, pointers.length * 2);
        }
    }

    public float[] getPointers() {
        return pointers;
    }

    public void offset(float x, float y) {
        for (int i = 0; i < pointerCount; i++) {
            pointers[i * 2] -= x;
            pointers[i * 2 + 1] -= y;
        }
    }

    @Override
    public String toString() {
        return String.format("action=%s,pointerCount=%s,pointers=%s",
                action, pointerCount, Arrays.toString(pointers));
    }
}

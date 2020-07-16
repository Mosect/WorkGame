package com.mosect.workgame.base;

public class GameKeyEvent {

    private int action;
    private int code;
    private int unicode;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUnicode() {
        return unicode;
    }

    public void setUnicode(int unicode) {
        this.unicode = unicode;
    }

    @Override
    public String toString() {
        return String.format("action=%s,code=%s,unicode=%s", action, code, unicode);
    }
}

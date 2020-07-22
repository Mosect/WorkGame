package com.mosect.workgame.retro_snake;

/**
 * 方向
 */
public enum Direction {
    LEFT(-1), UP(-2), RIGHT(1), DOWN(2);

    private int value;

    Direction(int value) {
        this.value = value;
    }

    public boolean isReverse(Direction other) {
        return value + other.value == 0;
    }
}

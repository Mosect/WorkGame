package com.mosect.workgame.retro_snake;

/**
 * 面板格子
 */
public class Block {

    public enum State {
        /**
         * 空白格子
         */
        EMPTY,
        /**
         * 蛇身占用格子
         */
        SNAKE,
        /**
         * 奖励占用的格子
         */
        REWARD;
    }

    private int x;
    private int y;
    private State state = State.EMPTY;
    private Direction direction; // 方向

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}

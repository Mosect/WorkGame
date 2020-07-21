package com.mosect.workgame.retro_snake;

/**
 * 面板格子
 */
public class Block {

    private int x;
    private int y;
    private boolean used; // 格子是否被占用
    private boolean reward; // 格子是否有奖励

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

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public void setReward(boolean reward) {
        this.reward = reward;
    }

    public boolean isReward() {
        return reward;
    }
}

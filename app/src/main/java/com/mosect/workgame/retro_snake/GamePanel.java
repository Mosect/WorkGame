package com.mosect.workgame.retro_snake;

/**
 * 游戏面板
 */
public class GamePanel {

    private Block[] blocks;
    private int x;
    private int y;
    private int xCount;
    private int yCount;
    private int blockSize;
    private int width;
    private int height;
    private int right;
    private int bottom;

    public GamePanel(int x, int y, int xCount, int yCount, int blockSize) {
        this.x = x;
        this.y = y;
        this.xCount = xCount;
        this.yCount = yCount;
        this.blockSize = blockSize;
        blocks = new Block[xCount * yCount];
        for (int i = 0; i < blocks.length; i++) {
            int bx = i % xCount;
            int by = i / xCount;
            blocks[i] = new Block(bx, by);
        }
        this.width = xCount * blockSize;
        this.height = yCount * blockSize;
        this.right = x + this.width;
        this.bottom = y + this.height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXCount() {
        return xCount;
    }

    public int getYCount() {
        return yCount;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public Block getBlock(int x, int y) {
        return blocks[x + y * xCount];
    }
}

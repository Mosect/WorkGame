package com.mosect.workgame.base;

/**
 * 游戏上下文
 */
public interface GameContext {

    /**
     * 设置游戏窗口
     *
     * @param window 窗口
     */
    void setWindow(GameWindow window);

    /**
     * 更新窗口大小
     */
    void updateWindowSize();

    /**
     * 设置帧率
     *
     * @param fps 帧率
     */
    void setFps(int fps);

    /**
     * 获取帧率
     *
     * @return 帧率
     */
    int getFps();

    /**
     * 退出游戏
     */
    void exit();

    /**
     * 获取刷新的fps值
     *
     * @return 刷新的fps值，没1秒钟计算一次
     */
    int getRefreshFps();

    GameDisplay getDisplay();
}

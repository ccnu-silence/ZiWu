package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 4/22/17.
 */

public abstract class ITask {
    protected TaskManager taskManager;

    /**
     * 初始化
     */
    abstract void onInit(TaskManager taskManager);

    abstract void onLoginSuccess();

}

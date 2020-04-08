package com.lineage.data.executor;

import com.lineage.server.templates.L1Event;

/**
 * Event(活动) 设置执行
 */
public abstract class EventExecutor {

    /**
     * Event(活动) 设置执行
     * 
     * @param event
     */
    public abstract void execute(L1Event event);
}

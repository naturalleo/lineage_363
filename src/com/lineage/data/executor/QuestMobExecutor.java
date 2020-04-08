package com.lineage.data.executor;

import com.lineage.server.templates.L1QuestUser;

/**
 * Quest(任务) 怪物剩余0设置执行
 */
public abstract class QuestMobExecutor {

    /**
     * 任务终止(怪物剩余0)
     * 
     * @param quest
     */
    public abstract void stopQuest(L1QuestUser quest);
}

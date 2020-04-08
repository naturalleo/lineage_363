package com.lineage.data.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Quest;

/**
 * Quest(任务) 设置执行
 */
public abstract class QuestExecutor {

    /**
     * Quest(任务) 开机设置启用执行<BR>
     * 召唤相关物件
     * 
     * @param quest
     */
    public abstract void execute(L1Quest quest);

    /**
     * Quest(任务) 设置执行<BR>
     * 设置任务开始执行(任务进度提升为1)<BR>
     * 相关NPC可与执行者进行对话<BR>
     * 
     * @param pc
     */
    public abstract void startQuest(final L1PcInstance pc);

    /**
     * Quest(任务) 设置结束<BR>
     * 设置任务结束(任务进度提升为255)<BR>
     * 假设该任务可以重复执行<BR>
     * 在此设置任务状态移除<BR>
     * 
     * @param pc
     */
    public abstract void endQuest(final L1PcInstance pc);

    /**
     * 展示任务说明
     * 
     * @param pc
     */
    public abstract void showQuest(final L1PcInstance pc);

    /**
     * 任务终止
     * 
     * @param pc
     */
    public abstract void stopQuest(L1PcInstance pc);
}

package com.lineage.server.model.skill;

/**
 * 技能效果时间轴
 * 
 * @author daien
 * 
 */
public interface L1SkillTimer {

    /**
     * 剩余时间(秒)
     * 
     * @return
     */
    public int getRemainingTime();

    /**
     * 效果开始执行
     */
    public void begin();

    /**
     * 效果结束
     */
    public void end();

    /**
     * 效果时间轴终止
     */
    public void kill();
}

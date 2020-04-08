package com.lineage.server.datatables.storage;

import java.util.ArrayList;

import com.lineage.server.templates.L1UserSkillTmp;

/**
 * 人物技能纪录
 * 
 * @author dexc
 * 
 */
public interface CharSkillStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 取回该人物技能列表
     * 
     * @param pc
     * @return
     */
    public ArrayList<L1UserSkillTmp> skills(int playerobjid);

    /**
     * 增加技能
     */
    public void spellMastery(int playerobjid, int skillid, String skillname,
            int active, int time);

    /**
     * 删除技能
     */
    public void spellLost(int playerobjid, int skillid);

    /**
     * 检查技能是否重复
     */
    public boolean spellCheck(int playerobjid, int skillid);

    /**
     * 设置自动技能状态
     */
    public void setAuto(final int mode, final int objid, final int skillid);

}

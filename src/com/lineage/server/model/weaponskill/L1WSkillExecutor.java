package com.lineage.server.model.weaponskill;

import com.lineage.server.model.L1Character;

/**
 * 武器各项能力设置抽象接口
 * 
 * @author daien
 * 
 */
public abstract class L1WSkillExecutor {

    /**
     * 设置武器技能设定值
     * 
     * @param int1
     *            应用设定1
     * @param int2
     *            应用设定2
     * @param range
     *            范围输出距离 设定质0无范围输出
     * @param range_mode
     *            0:不排除物件 1:排除盟友队友 2:排除人物 3:排除NPC(range设置大于0本项才有作用)
     */
    public abstract void set_power(int int1, int int2, int range, int range_mode);

    /**
     * 武器技能发动(1)
     * 
     * @param cha
     * @return
     */
    public abstract void start_power_skill_1(L1Character cha);
}

package com.lineage.server.model.weaponskill;

import com.lineage.server.model.L1Character;

/**
 * 武器各项动画设置抽象接口
 * 
 * @author daien
 * 
 */
public abstract class L1WSprExecutor {

    /**
     * 设置武器技能设定值
     * 
     * @param spr
     *            动画编号
     * @param istg
     *            动画是否施展在目标 true:动画在目标身上 false:动画在施展者身上
     * @param direction
     *            动画方向(设制动画方向istg设置将无作用)
     * @param range
     *            发动距离(具有动画方向本设置才有作用)
     * @param sleep
     *            延迟时间 0:不延迟
     */
    public abstract void set_power(int spr, int direction, int range, int sleep);

    /**
     * 武器技能发动动画(1)
     * 
     * @param cha
     * @return
     */
    public abstract void start_power_skill_1(L1Character cha);
}

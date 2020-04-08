package com.lineage.data.executor;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * NPC对话执行
 * 
 * @author dexc
 * 
 */
public abstract class NpcExecutor {

    /**
     * 具有的方法数组<BR>
     * 32:NPC召唤<BR>
     * 16:NPC工作时间<BR>
     * 8:NPC死亡<BR>
     * 4:NPC受到攻击<BR>
     * 2:NPC对话执行<BR>
     * 1:NPC对话判断<BR>
     */
    public abstract int type();

    /**
     * NPC对话判断(1)
     * 
     * @param pc
     * @param npc
     */
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }

    /**
     * NPC对话执行(2)
     * 
     * @param pc
     * @param npc
     * @param cmd
     */
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        // TODO Auto-generated method stub
    }

    /**
     * NPC受到攻击(4)<BR>
     * 任务NPC作为抵达目标检查的方法
     * 
     * @param pc
     * @param npc
     */
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }

    /**
     * NPC死亡(8)<BR>
     * 任务NPC作为给予任务道具的判断
     * 
     * @param lastAttacker
     *            攻击者
     * @param npc
     */
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        return null;
    }

    /**
     * NPC工作(16)<BR>
     * 工作重复时间(秒)
     */
    public int workTime() {
        return 0;
    }

    /**
     * NPC工作执行
     * 
     * @param mode
     */
    public void work(final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }

    /**
     * NPC召唤(32)
     * 
     * @param mode
     */
    public void spawn(final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }

    public String[] get_set() {
        return null;
    }

    public void set_set(String[] set) {
    }
}

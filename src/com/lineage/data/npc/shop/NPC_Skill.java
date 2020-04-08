package com.lineage.data.npc.shop;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 全职技能导师<BR>
 * 70754
 * 
 * @author dexc
 * 
 */
public class NPC_Skill extends NpcExecutor {

    /**
	 *
	 */
    private NPC_Skill() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new NPC_Skill();
    }

    @Override
    public int type() {
        return 17;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_skill_01"));
    }

    @Override
    public int workTime() {
        return 11;
    }

    @Override
    public void work(final L1NpcInstance npc) {
        // 4396 水(有文字)
        npc.broadcastPacketX8(new S_SkillSound(npc.getId(), 4396));
    }
}

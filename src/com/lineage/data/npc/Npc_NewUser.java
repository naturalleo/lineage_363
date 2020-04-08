package com.lineage.data.npc;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;

/**
 * 象牙塔 助手<BR>
 * 86121
 * 
 * @author dexc
 * 
 */
public class Npc_NewUser extends NpcExecutor {

    /**
	 *
	 */
    private Npc_NewUser() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_NewUser();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        if (pc.hasSkillEffect(ADVANCE_SPIRIT)) {
            return;
        }
        final int[] allBuffSkill = new int[] { ADVANCE_SPIRIT, BLESS_WEAPON,
                PHYSICAL_ENCHANT_STR, PHYSICAL_ENCHANT_DEX };

        for (int i = 0; i < allBuffSkill.length; i++) {
            final int skillid = allBuffSkill[i];
            this.startSkill(pc, npc, skillid);
        }
    }

    private void startSkill(final L1PcInstance pc, final L1NpcInstance npc,
            final int skillid) {
        final int objid = pc.getId();
        final int x = pc.getX();
        final int y = pc.getY();
        final L1SkillUse skillUse = new L1SkillUse();
        skillUse.handleCommands(pc, skillid, objid, x, y, 0,
                L1SkillUse.TYPE_NPCBUFF, npc);
    }
}

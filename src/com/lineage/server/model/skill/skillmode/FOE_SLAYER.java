package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 屠宰者
 * 
 * @author dexc
 * 
 */
public class FOE_SLAYER extends SkillMode {

    public FOE_SLAYER() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        srcpc.isFoeSlayer(true);

        for (int i = 0; i < 3; i++) {
            cha.onAction(srcpc);
        }
        // 屠宰者 加速封包
        srcpc.sendPacketsX8(new S_SkillSound(srcpc.getId(), 7020));
        srcpc.sendPacketsX8(new S_SkillSound(cha.getId(), 6509));

        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        for (int i = 0; i < 3; i++) {
            npc.attackTarget(cha);
        }
        npc.broadcastPacketX8(new S_SkillSound(cha.getId(), 6509));
        npc.broadcastPacketX8(new S_SkillSound(cha.getId(), 7020));
        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {

    }
}

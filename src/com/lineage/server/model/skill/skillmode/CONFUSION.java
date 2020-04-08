package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 混乱
 * 
 * @author dexc
 * 
 */
public class CONFUSION extends SkillMode {

    public CONFUSION() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = magic.calcMagicDamage(L1SkillId.CONFUSION);
        if (!cha.hasSkillEffect(L1SkillId.SILENCE)) {
            cha.setSkillEffect(L1SkillId.SILENCE, integer * 1000);
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                // 1339：突然感觉到混乱。
                // 1364：制作残象让敌方混乱。
                pc.sendPackets(new S_ServerMessage(1339));
            }
        }
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;

        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        // TODO Auto-generated method stub
    }
}

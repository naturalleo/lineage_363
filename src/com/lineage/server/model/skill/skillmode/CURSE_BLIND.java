package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.DARKNESS;
import static com.lineage.server.model.skill.L1SkillId.STATUS_FLOATING_EYE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CurseBlind;

/**
 * 闇盲咒术20 黑闇之影40 暗黑盲咒103
 * 
 * @author dexc
 * 
 */
public class CURSE_BLIND extends SkillMode {

    public CURSE_BLIND() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
                pc.sendPackets(new S_CurseBlind(2));

            } else {
                pc.sendPackets(new S_CurseBlind(1));
            }
            //pc.setSkillEffect(DARKNESS, integer * 1000);
        }
        cha.setSkillEffect(DARKNESS, integer * 1000);
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            if (pc.hasSkillEffect(STATUS_FLOATING_EYE)) {
                pc.sendPackets(new S_CurseBlind(2));

            } else {
                pc.sendPackets(new S_CurseBlind(1));
            }
        }
        cha.setSkillEffect(DARKNESS, integer * 1000);
        return dmg;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_CurseBlind(0));
        }
    }
}

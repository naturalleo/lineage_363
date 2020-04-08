package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;

/**
 * 幻觉：化身
 * 
 * @author dexc
 * 
 */
public class ILLUSION_AVATAR extends SkillMode {

    public ILLUSION_AVATAR() {
    }

    private static final byte DMG = 10;
    //private static final byte SP = 8;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (cha instanceof L1PcInstance) {
            if (!cha.hasSkillEffect(L1SkillId.ILLUSION_AVATAR)) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.addDmgup(DMG);
                //pc.addSp(SP);
                //pc.sendPackets(new S_SPMR(pc));
                cha.setSkillEffect(L1SkillId.ILLUSION_AVATAR, integer * 1000);
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
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.addDmgup(-DMG);
            //pc.addSp(-SP);
            //pc.sendPackets(new S_SPMR(pc));
        }
    }
}

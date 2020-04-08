package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;

/**
 * 武器破坏者
 * 
 * @author dexc
 * 
 */
public class ARM_BREAKER extends SkillMode {

    public ARM_BREAKER() {
    }

    private static final byte DMGUP = 2;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = magic.calcMagicDamage(L1SkillId.ARM_BREAKER);

        if (!cha.hasSkillEffect(L1SkillId.DMGUP2)) {
            cha.setSkillEffect(L1SkillId.DMGUP2, integer * 1000);
            cha.addDmgup(-DMGUP);
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
        cha.addDmgup(DMGUP);
    }
}

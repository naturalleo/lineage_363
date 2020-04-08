package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZING;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 圣洁之光
 * 
 * @author dexc
 * 
 */
public class REMOVE_CURSE extends SkillMode {

    public REMOVE_CURSE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        cha.curePoison();
        if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING)
                || cha.hasSkillEffect(STATUS_CURSE_PARALYZED)) {
            cha.cureParalaysis();
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

package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;

/**
 * 魔法效果:诅咒(双脚被困)
 * 
 * @author dexc
 * 
 */
public class STATUS_FREEZE extends SkillMode {

    public STATUS_FREEZE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.STATUS_FREEZE);
        if (!cha.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.setSkillEffect(L1SkillId.STATUS_FREEZE, integer * 1000);
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));

            } else if ((cha instanceof L1MonsterInstance)
                    || (cha instanceof L1SummonInstance)
                    || (cha instanceof L1PetInstance)) {
                final L1NpcInstance tgnpc = (L1NpcInstance) cha;
                tgnpc.setParalyzed(true);
            }
        }
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.STATUS_FREEZE);
        if (!cha.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.setSkillEffect(L1SkillId.STATUS_FREEZE, integer * 1000);
                pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));

            } else if ((cha instanceof L1MonsterInstance)
                    || (cha instanceof L1SummonInstance)
                    || (cha instanceof L1PetInstance)) {
                final L1NpcInstance tgnpc = (L1NpcInstance) cha;
                tgnpc.setParalyzed(true);
            }
        }
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
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance npc = (L1NpcInstance) cha;
            npc.setParalyzed(false);
        }
    }
}

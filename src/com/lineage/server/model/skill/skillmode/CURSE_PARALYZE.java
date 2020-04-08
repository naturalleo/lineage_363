package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BLIZZARD;
import static com.lineage.server.model.skill.L1SkillId.FREEZING_BREATH;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1CurseParalysis;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;

/**
 * 木乃伊的诅咒
 * 
 * @author dexc
 * 
 */
public class CURSE_PARALYZE extends SkillMode {

    public CURSE_PARALYZE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);

        if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE)
                && !cha.hasSkillEffect(FREEZING_BLIZZARD)
                && !cha.hasSkillEffect(FREEZING_BREATH)) {
            if (cha instanceof L1PcInstance) {
                L1CurseParalysis.curse(cha, 8000, 16000, 1);

            } else if (cha instanceof L1MonsterInstance) {
                L1CurseParalysis.curse(cha, 0, 16000, 0);
            }
        }
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;

        if (!cha.hasSkillEffect(EARTH_BIND) && !cha.hasSkillEffect(ICE_LANCE)
                && !cha.hasSkillEffect(FREEZING_BLIZZARD)
                && !cha.hasSkillEffect(FREEZING_BREATH)) {
            if (cha instanceof L1PcInstance) {
                L1CurseParalysis.curse(cha, 8000, 16000, 1);

            } else if (cha instanceof L1MonsterInstance) {
                L1CurseParalysis.curse(cha, 0, 16000, 0);
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
            pc.sendPacketsAll(new S_Poison(cha.getId(), 0));
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance npc = (L1NpcInstance) cha;
            npc.broadcastPacketAll(new S_Poison(cha.getId(), 0));
            npc.setParalyzed(false);
        }
    }
}

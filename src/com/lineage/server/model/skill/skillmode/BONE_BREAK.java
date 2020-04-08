package com.lineage.server.model.skill.skillmode;

import java.util.Random;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 骷髅毁坏
 * 
 * @author dexc
 * 
 */
public class BONE_BREAK extends SkillMode {

    public BONE_BREAK() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = magic.calcMagicDamage(L1SkillId.BONE_BREAK);

        if (!cha.hasSkillEffect(L1SkillId.BONE_BREAK)) {
            final Random random = new Random();
            final int rad = random.nextInt(1000) + 1;

            if (rad > 700) { //800更改为700 hjx1000
                final int time = 2;
                // System.out.println("骷髅毁坏:" + integer);
                cha.setSkillEffect(L1SkillId.BONE_BREAK, time * 1000);
                L1SpawnUtil.spawnEffect(86123, time, cha.getX(), cha.getY(),
                        cha.getMapId(), cha, 0);

                if (cha instanceof L1PcInstance) {
                    final L1PcInstance pc = (L1PcInstance) cha;
                    pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

                } else if ((cha instanceof L1MonsterInstance)
                        || (cha instanceof L1SummonInstance)
                        || (cha instanceof L1PetInstance)) {
                    final L1NpcInstance npc = (L1NpcInstance) cha;
                    npc.setParalyzed(true);
                }
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
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance npc = (L1NpcInstance) cha;
            npc.setParalyzed(false);
        }
    }
}

package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;

/**
 * 心灵破坏
 * 
 * @author dexc
 * 
 */
public class MIND_BREAK extends SkillMode {

    public MIND_BREAK() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        int dmg = 0;
        //final Random random = new Random();
        final int reMp = 5;

        if (cha.getCurrentMp() > reMp) {
            dmg = magic.calcMagicDamage(L1SkillId.MIND_BREAK) << 2;

            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                int newMp = pc.getCurrentMp() - reMp;
                if (newMp < 0) {
                    newMp = 0;
                }
                pc.setCurrentMp(newMp);

            } else if ((cha instanceof L1MonsterInstance)
                    || (cha instanceof L1SummonInstance)
                    || (cha instanceof L1PetInstance)) {
                final L1NpcInstance npc = (L1NpcInstance) cha;
                int newMp = npc.getCurrentMp() - reMp;
                if (newMp < 0) {
                    newMp = 0;
                }
                npc.setCurrentMp(newMp);
            }
        }
        // System.out.println("心灵破坏送出伤害: " + dmg);
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

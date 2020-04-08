package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharStatus2;

/**
 * 恐慌
 * 
 * @author dexc
 * 
 */
public class PANIC extends SkillMode {

    public PANIC() {
    }

    private static final byte STR = 1;
    private static final byte CON = 1;
    private static final byte DEX = 1;
    private static final byte WIS = 1;
    private static final byte INT = 1;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (!cha.hasSkillEffect(L1SkillId.PANIC)) {
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                pc.addStr((byte) -STR);
                pc.addCon((byte) -CON);
                pc.addDex((byte) -DEX);
                pc.addWis((byte) -WIS);
                pc.addInt((byte) -INT);

                pc.setSkillEffect(L1SkillId.PANIC, integer * 1000);
                // pc.sendPackets(new S_OwnCharStatus(pc));
                pc.sendPackets(new S_OwnCharStatus2(pc));

            } else if ((cha instanceof L1MonsterInstance)
                    || (cha instanceof L1SummonInstance)
                    || (cha instanceof L1PetInstance)) {
                final L1NpcInstance tgnpc = (L1NpcInstance) cha;
                tgnpc.addStr((byte) -STR);
                tgnpc.addCon((byte) -CON);
                tgnpc.addDex((byte) -DEX);
                tgnpc.addWis((byte) -WIS);
                tgnpc.addInt((byte) -INT);

                tgnpc.setSkillEffect(L1SkillId.PANIC, integer * 1000);
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
            pc.addStr(STR);
            pc.addCon(CON);
            pc.addDex(DEX);
            pc.addWis(WIS);
            pc.addInt(INT);

        } else if ((cha instanceof L1MonsterInstance)
                || (cha instanceof L1SummonInstance)
                || (cha instanceof L1PetInstance)) {
            final L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.addStr(STR);
            tgnpc.addCon(CON);
            tgnpc.addDex(DEX);
            tgnpc.addWis(WIS);
            tgnpc.addInt(INT);
        }
    }
}

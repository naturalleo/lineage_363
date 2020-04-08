package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.world.World;

/**
 * 呼唤盟友
 * 
 * @author dexc
 * 
 */
public class CALL_CLAN extends SkillMode {

    public CALL_CLAN() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        final L1PcInstance pc = (L1PcInstance) cha;
        final L1PcInstance clanPc = (L1PcInstance) World.get().findObject(
                integer);
        if (clanPc != null) {
            clanPc.setTempID(pc.getId()); // 暂存盟主ID
            // 729 盟主正在呼唤你，你要接受他的呼唤吗？(Y/N)
            clanPc.sendPackets(new S_Message_YN(729));
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

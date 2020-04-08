package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 世界树的呼唤131
 * 
 * @author dexc
 * 
 */
public class TELEPORT_TO_MATHER extends SkillMode {

    public TELEPORT_TO_MATHER() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        final L1PcInstance pc = (L1PcInstance) cha;
        if (pc.getMap().isEscapable() || pc.isGm()) {
            L1Teleport.teleport(pc, 33051, 32337, (short) 4, 5, true);

        } else {
            // 276 \f1在此无法使用传送。
            pc.sendPackets(new S_ServerMessage(276));
            pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
                    false));
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

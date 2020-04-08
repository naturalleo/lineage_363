package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 援护盟友118
 * 
 * @author dexc
 * 
 */
public class RUN_CLAN extends SkillMode {

    public RUN_CLAN() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        final L1PcInstance pc = (L1PcInstance) cha;
        final L1PcInstance clanPc = (L1PcInstance) World.get().findObject(
                integer);
        if (clanPc != null) {
            if (pc.getMap().isEscapable() || pc.isGm()) {
                // 是否在战争旗范围内
                final boolean castle_area = L1CastleLocation.checkInAllWarArea(
                        clanPc.getX(), clanPc.getY(), clanPc.getMapId());

                if (!castle_area) {
                    if ((clanPc.getMapId() == 0) || (clanPc.getMapId() == 4)
                            || (clanPc.getMapId() == 304)) {
                        L1Teleport.teleport(pc, clanPc.getX(), clanPc.getY(),
                                clanPc.getMapId(), 5, true);
                        return dmg;
                    }

                }
                // 1,192：目前无法移动，请稍后再使用。
                pc.sendPackets(new S_ServerMessage(1192));
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));

            } else {
                // 647：这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
                pc.sendPackets(new S_ServerMessage(647));
                pc.sendPackets(new S_Paralysis(
                        S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
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
        // TODO Auto-generated method stub
    }
}

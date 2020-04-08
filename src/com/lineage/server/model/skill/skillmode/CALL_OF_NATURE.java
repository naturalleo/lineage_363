package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;

/**
 * 生命呼唤
 * 
 * @author dexc
 * 
 */
public class CALL_OF_NATURE extends SkillMode {

    public CALL_OF_NATURE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.CURE_POISON);
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            if (srcpc.getId() != pc.getId()) {
                if (World.get().getVisiblePlayer(pc, 0).size() > 0) {
                    for (final L1PcInstance visiblePc : World.get()
                            .getVisiblePlayer(pc, 0)) {
                        if (!visiblePc.isDead()) {
                            // 592 复活失败，因为这个位置已被占据
                            srcpc.sendPackets(new S_ServerMessage(592));
                            return 0;
                        }
                    }
                }
                //城战不能复活 == hjx1000
                boolean isNowWar = false;
                final int castleId = L1CastleLocation.getCastleIdByArea(pc);
                if (castleId > 0) {
                    isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                }
                if (isNowWar) {
                	return 0;
                }
              //城战不能复活 end  ==hjx1000
                if (pc.isDead()) {
                    pc.setTempID(srcpc.getId());
                    // 322 是否要复活？ (Y/N)
                    pc.sendPackets(new S_Message_YN(322));
                }
            }
        }
        if (cha instanceof L1NpcInstance) {
            if (!(cha instanceof L1TowerInstance)) {
                final L1NpcInstance npc = (L1NpcInstance) cha;
                // 不允许复活
                if (npc.getNpcTemplate().isCantResurrect()) {
                    return 0;
                }
                //城战不能复活 == hjx1000
                boolean isNowWar = false;
                final int castleId = L1CastleLocation.getCastleIdByArea(npc);
                if (castleId > 0) {
                    isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                }
                if (isNowWar) {
                	return 0;
                }
              //城战不能复活 end  ==hjx1000
                if ((npc instanceof L1PetInstance)
                        && (World.get().getVisiblePlayer(npc, 0).size() > 0)) {
                    for (final L1PcInstance visiblePc : World.get()
                            .getVisiblePlayer(npc, 0)) {
                        if (!visiblePc.isDead()) {
                            // 592 复活失败，因为这个位置已被占据
                            srcpc.sendPackets(new S_ServerMessage(592));
                            return 0;
                        }
                    }
                }
                if (npc.isDead()) {
                    npc.resurrect(cha.getMaxHp());// HP全回复
                    // npc.resurrect(cha.getMaxHp() / 4);// MP 0
                    npc.setResurrect(true);
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
        // TODO Auto-generated method stub
    }
}

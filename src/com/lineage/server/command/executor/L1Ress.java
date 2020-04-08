package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;

/**
 * GM治疗结界
 * 
 * @author dexc
 * 
 */
public class L1Ress implements L1CommandExecutor {

    private static final Log _log = LogFactory.getLog(L1Ress.class);

    private L1Ress() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Ress();
    }

    @Override
    public void execute(final L1PcInstance pc, final String cmdName,
            final String arg) {
        try {
            final int objid = pc.getId();
            pc.sendPacketsX8(new S_SkillSound(objid, 761));

            pc.setCurrentHp(pc.getMaxHp());
            pc.setCurrentMp(pc.getMaxMp());

            if (pc.isDead()) {
                pc.setTempID(objid);
                // 322 是否要复活？ (Y/N)
                pc.sendPackets(new S_Message_YN(322));
            }

            for (final L1PcInstance tg : World.get().getVisiblePlayer(pc)) {
                if (tg.isDead()) {
                    tg.setTempID(objid);
                    // 322 是否要复活？ (Y/N)
                    tg.sendPackets(new S_Message_YN(322));

                } else {
                    tg.setCurrentHp(tg.getMaxHp());
                    tg.setCurrentMp(tg.getMaxMp());
                }
            }

            // 宠物 召唤兽 未死亡状态 补满HP MP
            for (final L1Object obj : World.get().getVisibleObjects(pc)) {
                // 宠物 未死亡
                if (obj instanceof L1PetInstance) {
                    final L1PetInstance tg = (L1PetInstance) obj;
                    if (!tg.isDead()) {
                        tg.setCurrentHp(tg.getMaxHp());
                        tg.setCurrentMp(tg.getMaxMp());
                    }
                }
                // 召唤兽 未死亡
                if (obj instanceof L1SummonInstance) {
                    final L1SummonInstance tg = (L1SummonInstance) obj;
                    if (!tg.isDead()) {
                        tg.setCurrentHp(tg.getMaxHp());
                        tg.setCurrentMp(tg.getMaxMp());
                    }
                }
            }

        } catch (final Exception e) {
            _log.error("错误的GM指令格式: " + this.getClass().getSimpleName()
                    + " 执行的GM:" + pc.getName());
            // 261 \f1指令错误。
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}

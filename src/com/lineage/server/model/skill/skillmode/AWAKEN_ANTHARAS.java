package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus;

/**
 * 觉醒：安塔瑞斯
 * 
 * @author dexc
 * 
 */
public class AWAKEN_ANTHARAS extends SkillMode {

    public AWAKEN_ANTHARAS() {
    }

    private static final byte MAX_HP = 127;
    private static final byte AC = 12;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.AWAKEN_ANTHARAS);
        if (srcpc.getAwakeSkillId() == 0) {// 未变身状态
            srcpc.addMaxHp(MAX_HP);
            srcpc.sendPackets(new S_HPUpdate(srcpc.getCurrentHp(), srcpc
                    .getMaxHp()));

            if (srcpc.isInParty()) { // 队伍状态
                srcpc.getParty().updateMiniHP(srcpc);
            }
            srcpc.addAc(-AC);

            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(L1SkillId.AWAKEN_ANTHARAS);

            L1BuffUtil.doPoly(srcpc);

            srcpc.startMpReductionByAwake();

        } else {
            if (srcpc.getAwakeSkillId() == L1SkillId.AWAKEN_ANTHARAS) { // 解除
                this.stop(srcpc);
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
            pc.addMaxHp(-MAX_HP);
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));

            if (pc.isInParty()) { // 队伍状态
                pc.getParty().updateMiniHP(pc);
            }
            pc.addAc(AC);

            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);

            L1BuffUtil.undoPoly(pc);

            pc.stopMpReductionByAwake();
        }
    }
}

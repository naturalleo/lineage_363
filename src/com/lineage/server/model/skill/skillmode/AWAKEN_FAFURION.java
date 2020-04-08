package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;

/**
 * 觉醒：法利昂
 * 
 * @author dexc
 * 
 */
public class AWAKEN_FAFURION extends SkillMode {

    public AWAKEN_FAFURION() {
    }

    private static final byte MR = 30;
    private static final byte WIND = 30;
    private static final byte WATER = 30;
    private static final byte FIRE = 30;
    private static final byte EARTH = 30;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.AWAKEN_ANTHARAS);
        if (srcpc.getAwakeSkillId() == 0) {// 未变身状态
            srcpc.addMr(MR);
            srcpc.sendPackets(new S_SPMR(srcpc));
            srcpc.addWind(WIND);
            srcpc.addWater(WATER);
            srcpc.addFire(FIRE);
            srcpc.addEarth(EARTH);
            srcpc.sendPackets(new S_OwnCharAttrDef(srcpc));

            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(L1SkillId.AWAKEN_FAFURION);

            L1BuffUtil.doPoly(srcpc);

            srcpc.startMpReductionByAwake();

        } else {
            if (srcpc.getAwakeSkillId() == L1SkillId.AWAKEN_FAFURION) { // 解除
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
            pc.addMr(-MR);
            pc.sendPackets(new S_SPMR(pc));
            pc.addWind(-WIND);
            pc.addWater(-WATER);
            pc.addFire(-FIRE);
            pc.addEarth(-EARTH);
            pc.sendPackets(new S_OwnCharAttrDef(pc));

            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);

            L1BuffUtil.undoPoly(pc);

            pc.stopMpReductionByAwake();
        }
    }
}

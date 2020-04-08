package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharStatus;

/**
 * 觉醒：巴拉卡斯
 * 
 * @author dexc
 * 
 */
public class AWAKEN_VALAKAS extends SkillMode {

    public AWAKEN_VALAKAS() {
    }

    private static final byte STR = 5;
    private static final byte CON = 5;
    private static final byte DEX = 5;
    private static final byte CHA = 5;
    private static final byte INT = 5;
    private static final byte WIS = 5;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.AWAKEN_ANTHARAS);
        if (srcpc.getAwakeSkillId() == 0) {// 未变身状态
            srcpc.addStr(STR);
            srcpc.addCon(CON);
            srcpc.addDex(DEX);
            srcpc.addCha(CHA);
            srcpc.addInt(INT);
            srcpc.addWis(WIS);

            srcpc.sendPackets(new S_OwnCharStatus(srcpc));
            srcpc.setAwakeSkillId(L1SkillId.AWAKEN_VALAKAS);

            L1BuffUtil.doPoly(srcpc);

            srcpc.startMpReductionByAwake();

        } else {
            if (srcpc.getAwakeSkillId() == L1SkillId.AWAKEN_VALAKAS) { // 解除
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
            pc.addStr(-STR);
            pc.addCon(-CON);
            pc.addDex(-DEX);
            pc.addCha(-CHA);
            pc.addInt(-INT);
            pc.addWis(-WIS);

            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.setAwakeSkillId(0);

            L1BuffUtil.undoPoly(pc);

            pc.stopMpReductionByAwake();
        }
    }
}

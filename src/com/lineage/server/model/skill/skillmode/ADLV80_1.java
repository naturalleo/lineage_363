package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;

/**
 * 卡瑞的祝福(地龙副本) HP+100 MP+50 体力恢复量+3 魔力恢复量+3 地属性魔防+30 额外攻击点数+1 攻击成功+5 ER+30 现有负重
 * / 1.04
 * 
 * @author dexc
 * 
 */
public class ADLV80_1 extends SkillMode {

    public ADLV80_1() {
    }

    private static final byte MAX_HP = 100;
    private static final byte MAX_MP = 50;
    private static final byte EARTH = 30;
    private static final byte DMGUP = 1;
    private static final byte BOW_DMGUP = 1;
    private static final byte HITUP = 5;
    private static final byte BOW_HITUP = 5;
    private static final byte WEIGHT_REDUCTION = 4;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (!srcpc.hasSkillEffect(L1SkillId.ADLV80_1)) {
            srcpc.addMaxHp(MAX_HP);
            srcpc.addMaxMp(MAX_MP);

            srcpc.addEarth(EARTH);

            srcpc.addDmgup(DMGUP);
            srcpc.addBowDmgup(BOW_DMGUP);

            srcpc.addHitup(HITUP);
            srcpc.addBowHitup(BOW_HITUP);

            srcpc.addWeightReduction(WEIGHT_REDUCTION);

            srcpc.setSkillEffect(L1SkillId.ADLV80_1, integer * 1000);

            srcpc.sendPackets(new S_HPUpdate(srcpc.getCurrentHp(), srcpc
                    .getMaxHp()));
            srcpc.sendPackets(new S_MPUpdate(srcpc));
            srcpc.sendPackets(new S_OwnCharAttrDef(srcpc));

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
        cha.addMaxHp(-MAX_HP);
        cha.addMaxMp(-MAX_MP);

        cha.addEarth(-EARTH);

        cha.addDmgup(-DMGUP);
        cha.addBowDmgup(-BOW_DMGUP);

        cha.addHitup(-HITUP);
        cha.addBowHitup(-BOW_HITUP);

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.addWeightReduction(-WEIGHT_REDUCTION);

            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            pc.sendPackets(new S_MPUpdate(pc));
            pc.sendPackets(new S_OwnCharAttrDef(pc));
        }
    }
}

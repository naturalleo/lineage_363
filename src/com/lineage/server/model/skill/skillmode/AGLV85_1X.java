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
 * 莎尔的祝福(水龙副本-强化) HP+150 MP+50
 * 
 * 4属性魔防+30 额外攻击点数+1 攻击成功+5 防御力-10
 * 
 * @author dexc
 * 
 */
public class AGLV85_1X extends SkillMode {

    public AGLV85_1X() {
    }

    private static final Short MAX_HP = 150;
    private static final Short MAX_MP = 50;
    private static final Short WATER = 30;
    private static final Short EARTH = 30;
    private static final Short FIRE = 30;
    private static final Short WIND = 30;
    private static final Short DMGUP = 1;
    private static final Short BOW_DMGUP = 1;
    private static final Short HITUP = 5;
    private static final Short BOW_HITUP = 5;
    private static final Short AC = 10;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (!srcpc.hasSkillEffect(L1SkillId.AGLV85_1X)) {
            srcpc.addMaxHp(MAX_HP);
            srcpc.addMaxMp(MAX_MP);

            srcpc.addWater(WATER);
            srcpc.addEarth(EARTH);
            srcpc.addFire(FIRE);
            srcpc.addWind(WIND);

            srcpc.addDmgup(DMGUP);
            srcpc.addBowDmgup(BOW_DMGUP);

            srcpc.addHitup(HITUP);
            srcpc.addBowHitup(BOW_HITUP);
            srcpc.addAc(AC);

            srcpc.setSkillEffect(L1SkillId.AGLV85_1X, integer * 1000);

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

        cha.addWater(-WATER);
        cha.addEarth(-EARTH);
        cha.addFire(-FIRE);
        cha.addWind(-WIND);

        cha.addDmgup(-DMGUP);
        cha.addBowDmgup(-BOW_DMGUP);

        cha.addHitup(-HITUP);
        cha.addBowHitup(-BOW_HITUP);
        cha.addAc(-AC);

        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            pc.sendPackets(new S_MPUpdate(pc));
            pc.sendPackets(new S_OwnCharAttrDef(pc));
        }
    }
}

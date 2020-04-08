package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;

/**
 * 灵魂升华
 * 
 * @author dexc
 * 
 */
public class ADVANCE_SPIRIT extends SkillMode {

    public ADVANCE_SPIRIT() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        // System.out.println("灵魂升华");
        if (!cha.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
            final L1PcInstance pc = (L1PcInstance) cha;
            // System.out.println("灵魂升华:" + pc.getName());
            pc.setAdvenHp(pc.getBaseMaxHp() / 5);
            pc.setAdvenMp(pc.getBaseMaxMp() / 5);
            pc.addMaxHp(pc.getAdvenHp());
            pc.addMaxMp(pc.getAdvenMp());
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) { // 队伍状态
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }

        cha.setSkillEffect(L1SkillId.ADVANCE_SPIRIT, integer * 1000);

        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final int dmg = 0;
        // System.out.println("灵魂升华");
        if (!cha.hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
            final L1PcInstance pc = (L1PcInstance) cha;
            // System.out.println("灵魂升华:" + pc.getName());
            pc.setAdvenHp(pc.getBaseMaxHp() / 5);
            pc.setAdvenMp(pc.getBaseMaxMp() / 5);
            pc.addMaxHp(pc.getAdvenHp());
            pc.addMaxMp(pc.getAdvenMp());
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) { // 队伍状态
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }

        cha.setSkillEffect(L1SkillId.ADVANCE_SPIRIT, integer * 1000);

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
            pc.addMaxHp(-pc.getAdvenHp());
            pc.addMaxMp(-pc.getAdvenMp());
            pc.setAdvenHp(0);
            pc.setAdvenMp(0);
            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) { // 队伍状态
                pc.getParty().updateMiniHP(pc);
            }
            pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
        }
    }
}

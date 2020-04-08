package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.ENTANGLE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.MASS_SLOW;
import static com.lineage.server.model.skill.L1SkillId.SLOW;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillHaste;

/**
 * 加速术
 * 
 * @author dexc
 * 
 */
public class HASTE extends SkillMode {

    public HASTE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.BONE_BREAK);
        if (cha.getMoveSpeed() != 2) { // 减速以外
            if (cha instanceof L1PcInstance) {
                final L1PcInstance pc = (L1PcInstance) cha;
                if (pc.getHasteItemEquipped() > 0) {
                    return dmg;
                }
                pc.setDrink(false);
                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, integer));
            }
            cha.setSkillEffect(L1SkillId.HASTE, integer * 1000);
            cha.broadcastPacketAll(new S_SkillHaste(cha.getId(), 1, 0));
            cha.setMoveSpeed(1);

        } else { // 减速中
            int skillNum = 0;
            if (cha.hasSkillEffect(SLOW)) {
                skillNum = SLOW;

            } else if (cha.hasSkillEffect(MASS_SLOW)) {
                skillNum = MASS_SLOW;

            } else if (cha.hasSkillEffect(ENTANGLE)) {
                skillNum = ENTANGLE;
            }

            if (skillNum != 0) {
                cha.removeSkillEffect(skillNum);
                cha.removeSkillEffect(HASTE);
                cha.setMoveSpeed(0);
                return dmg;
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
            pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0));
        }
        cha.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
        cha.setMoveSpeed(0);
    }
}

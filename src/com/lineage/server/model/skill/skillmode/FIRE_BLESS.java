package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillBrave;

/**
 * 烈炎气息
 * 
 * @author hjx1000
 * 
 */
public class FIRE_BLESS extends SkillMode {

    public FIRE_BLESS() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {

        final L1PcInstance pc = (L1PcInstance) cha;
        if (pc.getInventory().getTypeEquipped(1, 4) >= 1) {
        	return 0;
        }
        if (pc.getInventory().getTypeEquipped(1, 13) >= 1) {
        	return 0;
        }
        if (pc.getWeapon() == null) { 
        	return 0;
        }
        pc.setSkillEffect(L1SkillId.FIRE_BLESS, integer * 1000);
        L1BuffUtil.braveStart(pc);

        pc.setSkillEffect(L1SkillId.STATUS_BRAVE, integer * 1000);

		pc.sendPackets(new S_SkillBrave(pc.getId(), 1, integer));
		pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 6, 0));
		pc.setBraveSpeed(1);

        return 0;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        return 0;
    }

    @Override
    public void start(final L1PcInstance srcpc, final Object obj)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final L1Character cha) throws Exception {
        cha.setBraveSpeed(0);
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
        }
    }
}

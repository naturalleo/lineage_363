package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.WIND_SHACKLE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxWindShackle;

/**
 * 风之枷锁
 * 
 * @author dexc
 * 
 */
public class WIND_SHACKLE extends SkillMode {

    public WIND_SHACKLE() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (cha.hasSkillEffect(WIND_SHACKLE)) {
            return dmg;
        }
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), integer));
        }
        cha.setSkillEffect(WIND_SHACKLE, integer * 1000);
        return dmg;
    }

    @Override
    public int start(final L1NpcInstance npc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (cha.hasSkillEffect(WIND_SHACKLE)) {
            return dmg;
        }
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), integer));
        }
        cha.setSkillEffect(WIND_SHACKLE, integer * 1000);
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
            pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), 0));
        }
    }
}

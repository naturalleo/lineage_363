package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.RESIST_FEAR;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;

/**
 * 镜像201
 * 
 * @author loli
 * 
 */
public class RESIST_FEAR extends SkillMode {

    public RESIST_FEAR() {
    }

    private static final byte DODGE = 5;

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;// magic.calcMagicDamage(L1SkillId.UNCANNY_DODGE);
        if (!srcpc.hasSkillEffect(RESIST_FEAR)) {
            srcpc.setSkillEffect(RESIST_FEAR, integer * 1000);
            srcpc.add_dodge_down(DODGE); // 闪避率 - 50%
            // 更新闪避率显示
            srcpc.sendPackets(new S_PacketBoxIcon1(false, srcpc.get_dodge()));
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
        cha.add_dodge_down(-DODGE); // 闪避率+ 50%
        if (cha instanceof L1PcInstance) {
            final L1PcInstance pc = (L1PcInstance) cha;
            // 更新闪避率显示
            pc.sendPackets(new S_PacketBoxIcon1(false, pc.get_dodge()));
        }
    }
}

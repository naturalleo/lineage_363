package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;

/**
 * 水:魔法伤害减免 寒冰耐心+3，持续1200秒
 * 
 * @author dexc
 * 
 */
public class DRAGON3 extends SkillMode {

    public DRAGON3() {
    }

    @Override
    public int start(final L1PcInstance srcpc, final L1Character cha,
            final L1Magic magic, final int integer) throws Exception {
        final int dmg = 0;
        if (!srcpc.hasSkillEffect(L1SkillId.DRAGON3)) {
            srcpc.add_regist_freeze(+3);
            srcpc.setSkillEffect(L1SkillId.DRAGON3, integer * 1000);
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
        cha.add_regist_freeze(-3);
    }
}

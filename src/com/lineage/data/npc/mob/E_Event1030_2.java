package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 91056 南瓜小偷 91057 南瓜小偷
 * 
 * @author dexc
 * 
 */
public class E_Event1030_2 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(E_Event1030_2.class);

    private E_Event1030_2() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new E_Event1030_2();
    }

    @Override
    public int type() {
        return 4;
    }

    private static Random _random = new Random();

    /**
     * NPC受到攻击(4)<BR>
     * 任务NPC作为抵达目标检查的方法
     * 
     * @param pc
     * @param npc
     */
    @Override
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            int i = _random.nextInt(100);
            if (i >= 0 && i <= 1) {
                if (npc.isremovearmor()) {
                    return;
                }
                if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
                    return;
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7782));
                pc.setSkillEffect(L1SkillId.ADLV80_2_2, 12 * 1000);

                npc.set_removearmor(true);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

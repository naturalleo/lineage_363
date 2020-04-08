package com.lineage.data.npc.quest2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 卡瑞<BR>
 * 70670<BR>
 * HP+100 MP+50 体力恢复量+3 魔力恢复量+3 地属性魔防+30 额外攻击点数+1 攻击成功+5 ER+30 现有负重 / 1.04
 * 
 * @author dexc
 * 
 */
public class Npc_Cray extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Cray.class);

    private Npc_Cray() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Cray();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.hasSkillEffect(L1SkillId.AGLV85_1X)) {
                pc.removeSkillEffect(L1SkillId.AGLV85_1X);
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2)) {
                pc.removeSkillEffect(L1SkillId.ADLV80_2);
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_1)) {
                return;
            }
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7681));

            // SKILL移转
            final SkillMode mode = L1SkillMode.get().getSkill(
                    L1SkillId.ADLV80_1);
            if (mode != null) {
                try {
                    mode.start(pc, null, null, 2400);

                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

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
 * 莎尔<BR>
 * 70689<BR>
 * HP+80 MP+10 体力恢复量+3 魔力恢复量+3 水属性魔防+30 防御力-8 ER+15
 * 
 * @author dexc
 * 
 */
public class Npc_Saell extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Saell.class);

    private Npc_Saell() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Saell();
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
            if (pc.hasSkillEffect(L1SkillId.ADLV80_1)) {
                pc.removeSkillEffect(L1SkillId.ADLV80_1);
            }
            if (pc.hasSkillEffect(L1SkillId.ADLV80_2)) {
                return;
            }
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7680));

            // SKILL移转
            final SkillMode mode = L1SkillMode.get().getSkill(
                    L1SkillId.ADLV80_2);
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

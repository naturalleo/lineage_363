package com.lineage.data.cmd;

import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 技能学习成功与否的判断
 * 
 * @author dexc
 * 
 */
public class Skill_Check {

    /**
     * 技能学习成功与否的判断
     * 
     * @param pc
     * @param item
     * @param skillid
     * @param magicLv
     * @param attribute
     */
    public static void check(final L1PcInstance pc, final L1ItemInstance item,
            final int skillid, final int magicLv, final int attribute) {
        // 检查是否已学习该法术
        if (CharSkillReading.get().spellCheck(pc.getId(), skillid)) {
            // 79 没有任何事情发生
            final S_ServerMessage msg = new S_ServerMessage(79);
            pc.sendPackets(msg);

        } else {
            if (skillid != 0) {
                final Skill_StudyingExecutor addSkill = new Skill_Studying();
                // 执行技能学习结果判断
                addSkill.magic(pc, skillid, magicLv, attribute, item.getId());
            }
        }
    }
}

package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 古代亡灵<BR>
 * 45300<BR>
 * 
 * @author dexc
 * 
 */
public class E45_AncientApparition extends NpcExecutor {

    private static final Log _log = LogFactory
            .getLog(E45_AncientApparition.class);

    private E45_AncientApparition() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new E45_AncientApparition();
    }

    @Override
    public int type() {
        return 8;
    }

    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        try {
            // 判断主要攻击者
            final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

            if (pc != null) {
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                    return pc;
                }
                if (pc.getInventory().checkItem(40548)) { // 已经具有物品
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 40548, 1);// 古代亡灵之袋
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

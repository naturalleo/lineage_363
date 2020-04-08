package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 妖魔密使首领<BR>
 * 84005<BR>
 * 
 * @author dexc
 * 
 */
public class DK30_OrcEmissaryB extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DK30_OrcEmissaryB.class);

    private DK30_OrcEmissaryB() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DK30_OrcEmissaryB();
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
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49221)) { // 已经具有物品
                        return pc;
                    }
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 49221, 1);// 妖魔密使首领间谍书
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

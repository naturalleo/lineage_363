package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 路西尔斯 <BR>
 * 80139<BR>
 * 
 * @author dexc
 * 
 */
public class DK50_Luxiersi extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DK50_Luxiersi.class);

    private DK50_Luxiersi() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DK50_Luxiersi();
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
                // 任务已经开始
                if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49231)) { // 已经具有物品
                        return pc;
                    }
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            DragonKnightLv50_1.QUEST.get_id())) {
                        case 3:
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 49231, 1);// 路西尔斯邪念碎片

                            // 提升任务进度
                            pc.getQuest().set_step(
                                    DragonKnightLv50_1.QUEST.get_id(), 4);
                            break;
                    }
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

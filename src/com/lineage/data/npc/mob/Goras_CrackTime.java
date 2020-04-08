package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 时空裂痕 <BR>
 * 80140<BR>
 * 
 * @author dexc
 * 
 */
public class Goras_CrackTime extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Goras_CrackTime.class);

    private Goras_CrackTime() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Goras_CrackTime();
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
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            DragonKnightLv50_1.QUEST.get_id())) {
                        case 2:
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 49229, 1);// 异界邪念粉末
                            break;
                    }
                }
                // 任务已经开始
                if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv45_1.QUEST.get_id())) {
                        case 2:
                            // 将任务进度提升为3
                            pc.getQuest().set_step(
                                    IllusionistLv45_1.QUEST.get_id(), 3);

                            if (!pc.getInventory().checkItem(49202)) { // 不具有物品
                                // 取得任务道具
                                CreateNewItem.getQuestItem(pc, npc, 49202, 1);// 时空裂痕邪念碎片
                            }
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

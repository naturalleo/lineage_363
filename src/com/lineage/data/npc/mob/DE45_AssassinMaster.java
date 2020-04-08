package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 刺客首领<BR>
 * 45504
 * 
 * @author dexc
 * 
 */
public class DE45_AssassinMaster extends NpcExecutor {

    private static final Log _log = LogFactory
            .getLog(DE45_AssassinMaster.class);

    private DE45_AssassinMaster() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DE45_AssassinMaster();
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
                // 任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(DarkElfLv45_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(40571)) { // 已经具有物品
                                                              // (刺客首领的箱子)
                        return pc;
                    }
                    if (pc.getInventory().checkItem(40595)) { // 已经具有物品 (死亡之证)
                        return pc;
                    }
                    if (pc.getInventory().checkItem(40648)) { // 已经具有物品
                                                              // (生锈的刺客之剑)
                        return pc;
                    }

                    if (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id()) == 5) { // 任务进度5
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 40571, 1);// 刺客首领的箱子
                                                                      // x 1
                        return pc;
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

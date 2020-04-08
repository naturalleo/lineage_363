package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv15_2;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 妖魔长老<BR>
 * 45119<BR>
 * 
 * @author dexc
 * 
 */
public class DE15_OrcElder extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DE15_OrcElder.class);

    private DE15_OrcElder() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DE15_OrcElder();
    }

    @Override
    public int type() {
        return 8;
    }

    private static Random _random = new Random();

    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        try {
            // 判断主要攻击者
            final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

            if (pc != null) {
                // 任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv15_2.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(DarkElfLv15_2.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(40585)) { // 已经具有物品
                        return pc;
                    }
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 40585, 1);// 妖魔长老首级
                                                                      // x 1
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

package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 死亡的司祭<BR>
 * 45507
 * 
 * @author dexc
 * 
 */
public class DE50_DiscipleDeath extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DE50_DiscipleDeath.class);

    private DE50_DiscipleDeath() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DE50_DiscipleDeath();
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
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(DarkElfLv50_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(40541)) { // 已经具有物品
                        return pc;
                    }
                    // 提升任务进度
                    pc.getQuest().set_step(DarkElfLv50_1.QUEST.get_id(), 5);
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 40541, 1);// 黑暗之星
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

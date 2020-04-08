package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 艾尔摩大将军僵尸<BR>
 * 45017<BR>
 * 
 * @author dexc
 * 
 */
public class I30_ElmoreGeneralA extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(I30_ElmoreGeneralA.class);

    private I30_ElmoreGeneralA() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new I30_ElmoreGeneralA();
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
                if (pc.getQuest().isEnd(IllusionistLv30_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 49187, 1);// 艾尔摩将军之心
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

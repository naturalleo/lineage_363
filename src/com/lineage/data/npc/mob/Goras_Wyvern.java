package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 翼龙<BR>
 * 80141<BR>
 * 
 * @author dexc
 * 
 */
public class Goras_Wyvern extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Goras_Wyvern.class);

    private Goras_Wyvern() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Goras_Wyvern();
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
                if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv50_1.QUEST.get_id())) {
                        case 2:
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 49204, 1);// 翼龙之血
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

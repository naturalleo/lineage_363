package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 雪怪<BR>
 * 45294<BR>
 * 
 * @author dexc
 * 
 */
public class DK45_Yeti extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DK45_Yeti.class);

    private DK45_Yeti() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DK45_Yeti();
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
                // 任务已经开始
                if (pc.getQuest().isStart(DragonKnightLv45_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            DragonKnightLv45_1.QUEST.get_id())) {
                        case 3:
                            if (_random.nextInt(100) < 40) {
                                // 取得任务道具
                                CreateNewItem.getQuestItem(pc, npc, 49225, 1);// 雪怪之心
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

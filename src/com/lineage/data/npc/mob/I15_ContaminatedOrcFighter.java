package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv15_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 污浊 妖魔战士<BR>
 * 45128<BR>
 * 
 * @author dexc
 * 
 */
public class I15_ContaminatedOrcFighter extends NpcExecutor {

    private static final Log _log = LogFactory
            .getLog(I15_ContaminatedOrcFighter.class);

    private I15_ContaminatedOrcFighter() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new I15_ContaminatedOrcFighter();
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
                // LV15任务已经完成
                if (pc.getQuest().isEnd(IllusionistLv15_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(IllusionistLv15_1.QUEST.get_id())) {
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 49169, 1);// 污浊妖魔之心
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

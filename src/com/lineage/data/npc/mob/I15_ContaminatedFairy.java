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
 * 污浊精灵<BR>
 * 45153<BR>
 * 
 * @author dexc
 * 
 */
public class I15_ContaminatedFairy extends NpcExecutor {

    private static final Log _log = LogFactory
            .getLog(I15_ContaminatedFairy.class);

    private I15_ContaminatedFairy() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new I15_ContaminatedFairy();
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
                    if (pc.getInventory().checkItem(49170)) { // 已经具有物品
                        return pc;
                    }
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 49170, 1);// 污浊精灵核晶
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

package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 背叛的骷髅警卫兵<BR>
 * 45100<BR>
 * 
 * @author dexc
 * 
 */
public class W30_BetrayerSkeletonGuardsman extends NpcExecutor {

    private static final Log _log = LogFactory
            .getLog(W30_BetrayerSkeletonGuardsman.class);

    private W30_BetrayerSkeletonGuardsman() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new W30_BetrayerSkeletonGuardsman();
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
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(WizardLv30_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(40581)) { // 已经具有物品
                        return pc;
                    }
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 40581, 1);// 不死族的钥匙
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

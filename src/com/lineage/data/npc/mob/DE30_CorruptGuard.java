package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 叛逃的刺客警卫<BR>
 * 45292<BR>
 * 
 * @author dexc
 * 
 */
public class DE30_CorruptGuard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DE30_CorruptGuard.class);

    private DE30_CorruptGuard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DE30_CorruptGuard();
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
                if (pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().get_step(DarkElfLv30_1.QUEST.get_id()) == 1) {
                    if (pc.getInventory().checkItem(40554)) { // 已经具有物品-秘密名单
                        return pc;
                    }
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 40554, 1);// 秘密名单 x
                                                                      // 1
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

package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 火之牙<BR>
 * 45424<BR>
 * 
 * @author dexc
 * 
 */
public class K50_FangFlame extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(K50_FangFlame.class);

    private K50_FangFlame() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new K50_FangFlame();
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
                // LV45任务已经完成
                if (pc.getQuest().isEnd(KnightLv50_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(KnightLv50_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv50_1.QUEST.get_id())) {
                        case 3:
                            if (_random.nextInt(100) < 40) {
                                // 取得任务道具
                                CreateNewItem.getQuestItem(pc, npc, 49161, 1);// 精灵的私语
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

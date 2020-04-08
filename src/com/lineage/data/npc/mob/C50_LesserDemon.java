package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv50_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 小恶魔<BR>
 * 45481<BR>
 * 
 * @author dexc
 * 
 */
public class C50_LesserDemon extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(C50_LesserDemon.class);

    private C50_LesserDemon() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new C50_LesserDemon();
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
                // LV50任务已经完成
                if (pc.getQuest().isEnd(CrownLv50_1.QUEST.get_id())) {
                    return pc;
                }

                if (pc.getInventory().checkItem(49159)) { // 已经具有物品
                    return pc;
                }
                // 任务进度
                switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                    case 1:
                        if (_random.nextInt(100) < 40) {
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 49159, 1);// 调职命令书
                        }
                        break;
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

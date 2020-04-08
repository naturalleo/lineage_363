package com.lineage.data.npc.mob;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv15_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 妖魔密使(古鲁丁)<BR>
 * 84001<BR>
 * 
 * @author dexc
 * 
 */
public class DK15_OrcEmissaryA extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DK15_OrcEmissaryA.class);

    private DK15_OrcEmissaryA() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DK15_OrcEmissaryA();
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
                if (pc.getQuest().isEnd(DragonKnightLv15_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(DragonKnightLv15_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49218)) { // 已经具有物品
                        return pc;
                    }
                    if (_random.nextInt(100) < 40) {
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 49218, 1);// 妖魔搜索文件(古鲁丁)
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

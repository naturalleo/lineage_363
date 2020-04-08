package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 变形怪<BR>
 * 81069<BR>
 * 
 * @author dexc
 * 
 */
public class W45_Doppelganger extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(W45_Doppelganger.class);

    private W45_Doppelganger() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new W45_Doppelganger();
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
                // LV45任务已经完成
                if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                    return pc;
                }
                // 任务已经开始
                if (pc.getQuest().isStart(WizardLv45_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(40536)) { // 已经具有物品
                                                              // (古代恶魔的记载)
                        return pc;
                    }
                    if (pc.getInventory().checkItem(40542)) { // 已经具有物品 (变形怪的血)
                        return pc;
                    }
                    // 需要的物件确认(伊娃的祝福)
                    final L1ItemInstance item = npc.getInventory().checkItemX(
                            40032, 1);
                    if (item != null) {
                        if (npc.getInventory().removeItem(item, 1) == 1) {// 删除道具
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 40542, 1);// 变形怪的血
                                                                          // x 1
                        }
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

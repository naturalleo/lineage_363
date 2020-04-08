package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 妖魔密使(海音地监)<BR>
 * 84004<BR>
 * 
 * @author dexc
 * 
 */
public class DK30_OrcEmissaryA extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(DK30_OrcEmissaryA.class);

    private DK30_OrcEmissaryA() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new DK30_OrcEmissaryA();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            boolean isTak = false;
            if (pc.getTempCharGfx() == 6984) {// 妖魔密使
                isTak = true;
            }
            if (!isTak) {
                return;
            }

            if (pc.isCrown()) {// 王族

            } else if (pc.isKnight()) {// 骑士

            } else if (pc.isElf()) {// 精灵

            } else if (pc.isWizard()) {// 法师

            } else if (pc.isDarkelf()) {// 黑暗精灵

            } else if (pc.isDragonKnight()) {// 龙骑士
                if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                    // 对话动作
                    npc.onTalkAction(pc);
                    // 调查结束了吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "spy_orc1"));
                }

            } else if (pc.isIllusionist()) {// 幻术师

            } else {
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDragonKnight()) {// 龙骑士
            if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("request flute of spy")) {// 交出妖魔密使的徽印
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49223, 1);// 妖魔密使的徽印
                    if (item != null) {
                        pc.getInventory().removeItem(item, 1);// 删除道具
                        // 给予任务道具(妖魔密使之笛子)
                        CreateNewItem.getQuestItem(pc, npc, 49222, 1);
                    }
                }
            }
            isCloseList = true;

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 白蚁的尸体(C)<BR>
 * 80145<BR>
 * 说明:白蚁出现的理由 (幻术士45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_WcorpseC extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_WcorpseC.class);

    private Npc_WcorpseC() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_WcorpseC();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isKnight()) {// 骑士
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isElf()) {// 精灵
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isWizard()) {// 法师
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 任务开始状态
                if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                    if (!pc.getInventory().checkItem(49196)) { // 不具有物品(第三次记忆碎片
                                                               // 49196)
                        // 使用时空裂痕水晶(绿色)
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wcorpse8"));

                    } else {
                        // ...................
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wcorpse7"));
                    }

                } else {
                    // ...................
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));
                }

            } else {
                // ...................
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wcorpse7"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("a")) {// 使用时空裂痕水晶(绿色)
            // 任务开始状态
            if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                if (!pc.getInventory().checkItem(49196)) { // 不具有物品(第三次记忆碎片
                                                           // 49196)
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49192, 1);// 时空裂痕水晶(绿色)-3 49192
                    if (item != null) {
                        pc.getInventory().removeItem(item, 1);
                        // 使用时空裂痕水晶(绿色) ，在第三个白蚁的尸体中完成取得之前所留下的记忆碎片。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wcorpse9"));
                        // 给予任务道具(第三次记忆碎片 49196)
                        CreateNewItem.createNewItem(pc, 49196, 1);
                    }

                } else {
                    // 没有任何事情发生
                    pc.sendPackets(new S_ServerMessage(79));
                    isCloseList = true;
                }

            } else {
                isCloseList = true;
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

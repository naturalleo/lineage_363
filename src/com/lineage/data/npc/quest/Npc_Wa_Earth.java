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
 * 白蚁的痕迹(土壤)<BR>
 * 80146<BR>
 * 说明:白蚁出现的理由 (幻术士45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Wa_Earth extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Wa_Earth.class);

    private Npc_Wa_Earth() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Wa_Earth();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isKnight()) {// 骑士
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isElf()) {// 精灵
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isWizard()) {// 法师
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 任务开始状态
                if (pc.getQuest().isStart(IllusionistLv45_1.QUEST.get_id())) {
                    if (!pc.getInventory().checkItem(49197)) { // 不具有物品(第一次邪念碎片
                                                               // 49197)
                        // 使用时空裂痕水晶(蓝色)
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wa_earth2"));

                    } else {
                        // 白蚁所居住的痕迹留在土壤中...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wa_earth1"));
                    }

                } else {
                    // 白蚁所居住的痕迹留在土壤中...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));
                }

            } else {
                // 白蚁所居住的痕迹留在土壤中...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "wa_earth1"));
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
                if (!pc.getInventory().checkItem(49197)) { // 不具有物品(第一次邪念碎片
                                                           // 49197)
                    final L1ItemInstance item = pc.getInventory().checkItemX(
                            49193, 1);// 时空裂痕水晶(蓝色)-3 49193
                    if (item != null) {
                        pc.getInventory().removeItem(item, 1);
                        // 透过时空裂痕水晶(蓝色)，可在所留下的白蚁的土壤中完成取得邪念碎片。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "wa_earth3"));
                        // 给予任务道具(第一次邪念碎片 49197)
                        CreateNewItem.createNewItem(pc, 49197, 1);
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

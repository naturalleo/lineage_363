package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 红色灵魂之火<BR>
 * 80138<BR>
 * 说明:时空彼端的线索 (龙骑士50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_RedSoul extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_RedSoul.class);

    private Npc_RedSoul() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_RedSoul();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族

            } else if (pc.isKnight()) {// 骑士
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));

            } else if (pc.isElf()) {// 精灵
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));

            } else if (pc.isWizard()) {// 法师
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // LV50任务已经开始
                if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            DragonKnightLv50_1.QUEST.get_id())) {
                        case 2:
                            // 哇呜呜呜呜呜...是谁叫醒我的？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "redsoul_f1"));
                            break;

                        case 3:
                            // 你想知道哪些事？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "redsoul_f2"));
                            break;

                        default:
                            // 哇呜呜呜呜呜...是谁叫醒我的？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "redsoul_f4"));
                            break;
                    }

                } else {
                    // 哇呜呜呜呜呜...是谁叫醒我的？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "redsoul_f4"));
                }

            } else if (pc.isIllusionist()) {// 幻术师
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));

            } else {
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "redsoul_f4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (pc.isDragonKnight()) {// 龙骑士
            // LV50任务已经开始
            if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                // 任务进度
                switch (pc.getQuest().get_step(
                        DragonKnightLv50_1.QUEST.get_id())) {
                    case 2:
                    case 3:
                        if (cmd.equalsIgnoreCase("a")) {// 奉献异界邪念粉末
                            final L1ItemInstance item = pc.getInventory()
                                    .checkItemX(49229, 10);// 异界邪念粉末 49229
                            if (item != null) {
                                pc.getInventory().removeItem(item, 10);// 删除道具

                                // 你想知道些什么？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "redsoul_f2"));

                                // 提升任务进度
                                pc.getQuest().set_step(
                                        DragonKnightLv50_1.QUEST.get_id(), 3);

                                // 给予任务道具(灵魂之火灰烬)
                                CreateNewItem.getQuestItem(pc, npc, 49207, 1);
                                // 给予任务道具(红色之火碎片)
                                CreateNewItem.getQuestItem(pc, npc, 49227, 1);

                            } else {
                                // 可用<font fg=ffffaf>异界邪念粉末</font>10个进行仪式
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "redsoul_f3"));
                            }
                        }
                        break;

                    default:
                        // 关闭对话窗
                        pc.sendPackets(new S_CloseList(pc.getId()));
                        break;
                }

            } else {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        }
    }
}

package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 蓝色灵魂之火<BR>
 * 70669<BR>
 * 说明:时空彼端的线索 (幻术士50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_BlueSoul extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_BlueSoul.class);

    private Npc_BlueSoul() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_BlueSoul();
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
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));

            } else if (pc.isElf()) {// 精灵
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));

            } else if (pc.isWizard()) {// 法师
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // LV50任务已经开始
                if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                    // 任务进度
                    switch (pc.getQuest().get_step(
                            IllusionistLv50_1.QUEST.get_id())) {
                        case 2:
                            // 哇呜呜呜呜呜...是谁叫醒我的？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluesoul_f1"));
                            break;

                        case 3:
                            // 你想知道哪些事？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluesoul_f2"));
                            break;

                        default:
                            // 哇呜呜呜呜呜...是谁叫醒我的？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluesoul_f4"));
                            break;
                    }

                } else {
                    // 哇呜呜呜呜呜...是谁叫醒我的？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "bluesoul_f4"));
                }

            } else {
                // 哇呜呜呜呜呜...是谁叫醒我的？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluesoul_f4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (pc.isIllusionist()) {// 幻术师
            // LV50任务已经开始
            if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
                // 任务进度
                switch (pc.getQuest()
                        .get_step(IllusionistLv50_1.QUEST.get_id())) {
                    case 2:
                        if (cmd.equalsIgnoreCase("a")) {// 供奉魔族之血
                            // 需要物件不足
                            if (CreateNewItem.checkNewItem(pc, new int[] {
                                    49203,// 食腐兽之血 49203
                                    49204,// 翼龙之血 49204
                            }, new int[] { 5, 5, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                                // 请去帮我取来<font fg=ffffaf>食腐兽之血</font>5个、<font
                                // fg=ffffaf>翼龙之血</font>5个。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluesoul_f3"));

                            } else {// 需要物件充足
                                // 收回任务需要物件 给予任务完成物件
                                CreateNewItem.createNewItem(pc, new int[] {
                                        49203,// 食腐兽之血 49203
                                        49204,// 翼龙之血 49204
                                }, new int[] { 5, 5, }, new int[] { 49207,// 灵魂之火灰烬
                                                                          // 49207
                                        49208,// 蓝色之火碎片 49208
                                }, 1, new int[] { 1, 1, });// 给予

                                // 提升任务进度
                                pc.getQuest().set_step(
                                        IllusionistLv50_1.QUEST.get_id(), 3);
                                // 你想知道些什么？
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluesoul_f2"));
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

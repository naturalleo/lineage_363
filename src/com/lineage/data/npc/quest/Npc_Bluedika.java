package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 布鲁迪卡<BR>
 * 70895<BR>
 * 说明:纠正错误的观念 (黑暗妖精45级以上官方任务)<BR>
 * 说明:寻找黑暗之星 (黑暗妖精50级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Bluedika extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Bluedika.class);

    private Npc_Bluedika() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Bluedika();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else if (pc.isKnight()) {// 骑士
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else if (pc.isElf()) {// 精灵
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else if (pc.isWizard()) {// 法师
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV30任务未完成
                if (!pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                    // 总之请你好好保重身体。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "bluedikaq3"));
                    return;
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    // LV50任务已经完成
                    if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                        // 多亏你及时完成任务，才能阻止黑暗之星变质。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "bluedikaq8"));
                        return;
                    }
                    // 等级达成要求(LV50-1)
                    if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                DarkElfLv50_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 我希望你能找回黑暗之星
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluedikaq6"));
                                break;

                            default:// 达到1(任务开始)
                                // 你可以去找奇马，他会帮助你的。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluedikaq7"));
                                break;
                        }

                    } else {
                        // 今天也很悠闲
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "bluedikaq5"));
                    }
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 关于纠正错误观念
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluedikaq1"));
                            break;

                        default:// 达到1(任务开始)
                            // 给予死亡之证及刺客之证
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluedikaq2"));
                            break;
                    }

                } else {
                    // 你的实力还不足以让我放心的将事情托付给你，多去锻练锻练吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));

            } else {
                // 真是好久没有见到外人啊。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "bluedikaq4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isDarkelf()) {// 黑暗精灵
            // LV45-1任务已经完成
            if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                isCloseList = true;
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    isCloseList = true;

                } else {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                            if (cmd.equalsIgnoreCase("quest 24 bluedikaq7")) {// 执行任务
                                // 将任务设置为执行中
                                QuestClass.get().startQuest(pc,
                                        DarkElfLv50_1.QUEST.get_id());
                                // 你可以去找奇马，他会帮助你的。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluedikaq7"));
                                isCloseList = false;
                            }
                            break;

                        default:
                            if (cmd.equalsIgnoreCase("request finger of death")) {// 递给黑暗之星
                                // 需要物件不足
                                if (CreateNewItem.checkNewItem(pc,
                                        // 真实的面具、蘑菇毒液、黑暗之星
                                        new int[] { 20037, 40603, 40541 },
                                        new int[] { 1, 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                                    isCloseList = true;

                                } else {// 需要物件充足
                                    // 收回任务需要物件 给予任务完成物件
                                    CreateNewItem.createNewItem(
                                            pc,
                                            // 真实的面具、蘑菇毒液、黑暗之星
                                            new int[] { 20037, 40603, 40541 },
                                            new int[] { 1, 1, 1 },
                                            new int[] { 13 }, // 布鲁迪卡之袋 x 1
                                            1, new int[] { 1 });// 给予
                                                                // 将任务设置为结束
                                    QuestClass.get().endQuest(pc,
                                            DarkElfLv50_1.QUEST.get_id());
                                    // 多亏你及时完成任务，才能阻止黑暗之星变质。
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "bluedikaq8"));
                                    isCloseList = false;
                                }
                            }
                            break;
                    }
                }

            } else {
                // LV45-1任务进度
                switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                    case 0:// 达到0
                        if (cmd.equalsIgnoreCase("quest 17 bluedikaq2")) {// 关于纠正错误观念
                            // 将任务设置为执行中
                            QuestClass.get().startQuest(pc,
                                    DarkElfLv45_1.QUEST.get_id());
                            // 给予死亡之证及刺客之证
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "bluedikaq2"));
                        }
                        break;

                    default:
                        if (cmd.equalsIgnoreCase("request bluedikabag")) {// 给予死亡之证及刺客之证
                            // 需要物件不足
                            if (CreateNewItem.checkNewItem(pc,
                            // 刺客之证,死亡之证
                                    new int[] { 40572, 40595 }, new int[] { 1,
                                            1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                                isCloseList = true;

                            } else {// 需要物件充足
                                // 收回任务需要物件 给予任务完成物件
                                CreateNewItem.createNewItem(pc,
                                // 刺客之证,死亡之证
                                        new int[] { 40572, 40595 }, new int[] {
                                                1, 1 }, new int[] { 40553 }, // 布鲁迪卡之袋
                                                                             // x
                                                                             // 1
                                        1, new int[] { 1 });// 给予
                                                            // 将任务设置为结束
                                QuestClass.get().endQuest(pc,
                                        DarkElfLv45_1.QUEST.get_id());
                                // 总之请你好好保重身体。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "bluedikaq3"));
                            }
                        }
                        break;
                }
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

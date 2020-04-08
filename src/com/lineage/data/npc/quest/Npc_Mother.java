package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_2;
import com.lineage.data.quest.ElfLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 迷幻森林之母<BR>
 * 70844<BR>
 * 说明:达克马勒的威胁 (妖精30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Mother extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Mother.class);

    private Npc_Mother() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Mother();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else if (pc.isKnight()) {// 骑士
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else if (pc.isElf()) {// 精灵
                // LV15任务未完成
                if (!pc.getQuest().isEnd(ElfLv15_2.QUEST.get_id())) {
                    // 欢迎 世界树的小叶子
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mothere1"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                    // 世界树的幼小叶子啊，你承受了这个试练。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv30_1.QUEST.get_id())) {
                        case 0:// 达到0(任务未开始)
                               // 你已经能够接受成人仪式了，真高兴啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "motherEE1"));
                            break;

                        default:// 其他
                            // 递给受诅咒的精灵书
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "motherEE2"));
                            break;
                    }
                } else {
                    // 欢迎 世界树的小叶子
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "mothere1"));
                }

            } else if (pc.isWizard()) {// 法师
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));

            } else {
                // 在这里看见人类是一件罕见的事情
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherm1"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isElf()) {// 精灵
            // 任务已经完成
            if (pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 12 motherEE2")) {// 进行成人仪式
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, ElfLv30_1.QUEST.get_id());
                // 递给受诅咒的精灵书
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE2"));

            } else if (cmd.equalsIgnoreCase("request questitem2")) {// 递给受诅咒的精灵书
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40592 },// 受诅咒的精灵书
                                                                       // x 1
                        new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40592 },// 受诅咒的精灵书
                                                                        // x 1
                            new int[] { 1 }, new int[] { 40588 }, // 妖精族宝物 x 1
                            1, new int[] { 1 });// 给予

                    // 将任务设置为结束
                    QuestClass.get().endQuest(pc, ElfLv30_1.QUEST.get_id());
                    // 世界树的幼小叶子啊，你承受了这个试练。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "motherEE3"));
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

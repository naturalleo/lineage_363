package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 皮尔斯<BR>
 * 70908<BR>
 * 说明:皮尔斯的忧郁 (黑暗妖精15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Pears extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Pears.class);

    private Npc_Pears() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Pears();
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

            } else if (pc.isElf()) {// 精灵

            } else if (pc.isWizard()) {// 法师

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV15任务已经完成
//                if (pc.getQuest().isEnd(DarkElfLv15_1.QUEST.get_id())) {
//                    return;
//                } //修改黑妖换石头任务 hjx1000
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv15_1.QUEST.get_questlevel()) {
                    // 啊......为何服侍神的女性都如此高贵呢！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pears1"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士

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

        if (pc.isDarkelf()) {// 黑暗精灵
            // 等级达成要求
            if (pc.getLevel() >= DarkElfLv15_1.QUEST.get_questlevel()) {
                // LV15任务已经完成
//                if (pc.getQuest().isEnd(DarkElfLv15_1.QUEST.get_id())) {
//                    return;
//                } //修改黑妖换石头任务 hjx1000
                if (cmd.equalsIgnoreCase("request silver sting knife")) {// 给予二级黑魔石
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40321, // 任务完成需要物件(二级黑魔石)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40321, 1, // 需要二级黑魔石 x 1
                                40738, 1000);// 给予银飞刀 x 1000
                        // 将任务设置为执行中
//                        QuestClass.get().startQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id());
//                        // 将任务设置为结束
//                        QuestClass.get().endQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id()); //修改黑妖换石头任务 hjx1000
                        // 二级黑魔石 → 银飞刀(1,000)
                        isCloseList = true;
                    }

                } else if (cmd.equalsIgnoreCase("request heavy sting knife")) {// 给予三级黑魔石
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40322, // 任务完成需要物件(三级黑魔石)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40322, 1, // 需要三级黑魔石 x 1
                                40740, 2000);// 给予重飞刀 x 2000
                        // 将任务设置为执行中
//                        QuestClass.get().startQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id());
//                        // 将任务设置为结束
//                        QuestClass.get().endQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id()); //修改黑妖换石头任务 hjx1000
                        // 三级黑魔石 → 重飞刀(2,000
                        isCloseList = true;
                    }

                } else if (cmd.equalsIgnoreCase("request pears itembag")) {// 给予四级黑魔石
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40323, // 任务完成需要物件(四级黑魔石)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40323, 1, // 需要四级黑魔石 x 1
                                40715, 1);// 给予皮尔斯的礼物 x 1
                        // 将任务设置为执行中
//                        QuestClass.get().startQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id());
//                        // 将任务设置为结束
//                        QuestClass.get().endQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id()); //修改黑妖换石头任务 hjx1000
                        // 四级黑魔石 → 皮尔斯的礼物
                        isCloseList = true;
                    }

                } else if (cmd.equalsIgnoreCase("request jin gauntlet")) {// 给予五级黑魔石
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40324, // 任务完成需要物件(五级黑魔石)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40324, 1, // 需要五级黑魔石 x 1
                                194, 1);// 真铁手甲 x 1
                        // 将任务设置为执行中
//                        QuestClass.get().startQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id());
//                        // 将任务设置为结束
//                        QuestClass.get().endQuest(pc,
//                                DarkElfLv15_1.QUEST.get_id()); //修改黑妖换石头任务 hjx1000
                        // 五级黑魔石 → 真铁手甲
                        isCloseList = true;
                    }
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

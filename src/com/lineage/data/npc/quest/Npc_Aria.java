package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.data.quest.CrownLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 艾莉亚<BR>
 * 70783<BR>
 * 说明:艾莉亚的请求 (王族30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Aria extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Aria.class);

    private Npc_Aria() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Aria();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // LV15任务未完成
                if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                    // 唉～虽然无法拯救被蚂蚁抓走的村民
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv30_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(CrownLv30_1.QUEST.get_id())) {
                        // 愿意协助村民
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria1"));

                    } else {// 任务已经开始
                        // 交出村庄居民的遗物
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria2"));
                    }

                } else {
                    // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));
                }

            } else if (pc.isKnight()) {// 骑士
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else if (pc.isElf()) {// 精灵
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else if (pc.isWizard()) {// 法师
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));

            } else {
                // 冒险者啊。 如果要经过沙漠时请小心巨蚁。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isCrown()) {// 王族
            // LV15任务未完成
            if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                return;
            }
            // LV30任务已经完成
            if (pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(CrownLv30_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("quest 13 aria2")) {// 愿意协助村民
                    // 将任务设置为执行中
                    QuestClass.get().startQuest(pc, CrownLv30_1.QUEST.get_id());
                    // 交出村庄居民的遗物
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria2"));
                }

            } else {// 任务已经开始
                if (cmd.equalsIgnoreCase("request questitem")) {// 交出村庄居民的遗物
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, 40547, // 任务完成需要物件(村民的遗物
                                                              // x 1)
                            1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, 40547, 1, // 需要 x
                                                                  // 1(村民的遗物)
                                40570, 1);// 给予 x 1(艾莉亚的回报)

                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                CrownLv30_1.QUEST.get_id());
                        // 唉～虽然无法拯救被蚂蚁抓走的村民
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aria3"));
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

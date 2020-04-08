package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.IllusionistLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 祭坛<BR>
 * 80094<BR>
 * 说明:艾尔摩战场的轨迹 (幻术士30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Altar extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Altar.class);

    private Npc_Altar() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Altar();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isKnight()) {// 骑士
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isElf()) {// 精灵
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isWizard()) {// 法师
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));

            } else if (pc.isIllusionist()) {// 幻术师
                if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
                    // 用虔诚的心祭拜死去的怨灵
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar1"));

                } else {
                    // 证明你的勇气吧，把该携带的物品带来吧。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));
                }

            } else {
                // 证明你的勇气吧，把该携带的物品带来吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar2"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        int[] items = null;
        int[] counts = null;
        int[] gitems = null;
        int[] gcounts = null;

        if (cmd.equalsIgnoreCase("A")) {// 菊花花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41319 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("B")) {// 黛西花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41320 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("C")) {// 玫瑰花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41321 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("D")) {// 卡拉花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41322 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("E")) {// 太阳花花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41323 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("F")) {// 小苍兰花束(领取证明击败幽灵的勇士之证)
            items = new int[] { 41327, 41324 };
            counts = new int[] { 20, 1 };
            gitems = new int[] { 41325 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("G")) {// 菊花花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41319 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("H")) {// 黛西花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41320 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("I")) {// 玫瑰花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41321 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("J")) {// 卡拉花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41322 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("K")) {// 太阳花花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41323 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("L")) {// 小苍兰花束(领取证明击败哈蒙将军怨灵的勇士之证)
            items = new int[] { 41328, 41324 };
            counts = new int[] { 1, 1 };
            gitems = new int[] { 41326 };
            gcounts = new int[] { 1 };

        } else if (cmd.equalsIgnoreCase("M")) {// 祭拜艾尔摩将军
            if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
                // 需要物件不足(艾尔摩将军之心以及一束菊花花束)
                if (CreateNewItem.checkNewItem(pc, new int[] { 49187,// 艾尔摩将军之心
                        41319,// 菊花花束
                }, new int[] { 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 粹取幽灵之魂的材料不足！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar8"));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 49187,// 艾尔摩将军之心
                            41319,// 菊花花束
                    }, new int[] { 1, 1, }, new int[] { 49188,// 49188 索夏依卡灵魂之心
                            // 274,// 274 反王肯恩的权杖
                            }, 1, new int[] { 1,
                            // 1,
                            });// 给予
                    if (!pc.getInventory().checkItem(274)) { // 不具有物品
                        // 取得任务道具
                        CreateNewItem.getQuestItem(pc, npc, 274, 1);// 反王肯恩的权杖
                    }
                    // 将任务进度提升为2
                    pc.getQuest().set_step(IllusionistLv30_1.QUEST.get_id(), 2);
                    // 已粹取艾尔摩大将军的灵魂了
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar9"));
                }

            } else {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("N")) {// 解开将军遗物的封印
            if (pc.getQuest().isStart(IllusionistLv30_1.QUEST.get_id())) {
                // 需要物件不足(封印的索夏依卡遗物的封印/反王肯恩的权杖/卡拉花束)
                if (CreateNewItem.checkNewItem(pc, new int[] { 49190,// 49190
                                                                     // 封印的索夏依卡遗物
                        274,// 274 反王肯恩的权杖
                        41322,// 41322 卡拉花束
                }, new int[] { 1, 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 粹取幽灵之魂的材料不足！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar8"));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 49190,// 49190
                                                                      // 封印的索夏依卡遗物
                            274,// 274 反王肯恩的权杖
                            41322,// 41322 卡拉花束
                    }, new int[] { 1, 1, 1, }, new int[] { 49191,// 49191
                                                                 // 艾尔摩部队日记
                            }, 1, new int[] { 1, });// 给予
                                                    // 将任务进度提升为3
                    pc.getQuest().set_step(IllusionistLv30_1.QUEST.get_id(), 3);
                    // 已经解开了被封印的索夏依卡遗物
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar10"));
                }

            } else {
                isCloseList = true;
            }

        } else {
            isCloseList = true;
        }

        if (items != null) {
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 粹取幽灵之魂的材料不足！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar8"));

            } else {// 需要物件充足
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                // 你确实有勇士的资格。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "altar3"));
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

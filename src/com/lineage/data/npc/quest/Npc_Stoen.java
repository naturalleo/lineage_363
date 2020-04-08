package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 神秘的岩石<BR>
 * 81105<BR>
 * 说明:法师的考验 (法师45级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Stoen extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Stoen.class);

    private Npc_Stoen() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Stoen();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else if (pc.isKnight()) {// 骑士
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else if (pc.isElf()) {// 精灵
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else if (pc.isWizard()) {// 法师
                // LV45任务已经完成
                if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                    // 喀喀喀，人类果然比较愚蠢啊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm3"));
                    return;
                }

                // 等级达成要求
                if (pc.getLevel() >= WizardLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(WizardLv45_1.QUEST.get_id())) {
                        case 1:// 任务开始
                               // 咦？原来不是我要找的魔法师。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "stoenm1"));
                            break;

                        case 2:// 任务进度2
                               // 递给所要求的物品
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "stoenm2"));
                            break;

                        case 3:// 任务进度3
                               // 喀喀喀，人类果然比较愚蠢啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "stoenm3"));
                            break;

                        default:
                            // 咦？原来不是我要找的魔法师。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "stoenm4"));
                            break;
                    }

                } else {
                    // 咦？原来不是我要找的魔法师。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));

            } else {
                // 咦？原来不是我要找的魔法师。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isWizard()) {// 法师
            // LV45任务已经完成
            if (pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 19 stoenm2")) {// 神秘岩石的要求
                // 将任务进度提升为2
                pc.getQuest().set_step(WizardLv45_1.QUEST.get_id(), 2);
                // 递给所要求的物品
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm2"));

            } else if (cmd
                    .equalsIgnoreCase("request scroll about ancient evil")) {// 递给所要求的物品
                int[] items = new int[] { 40542, 40189 }; // 变形怪的血 x 1 魔法书
                                                          // (魔法相消术) x 1
                int[] counts = new int[] { 1, 1 };
                int[] gitems = new int[] { 40536 };// 古代恶魔的记载 x 1
                int[] gcounts = new int[] { 1 };

                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, items, // 变形怪的血 x 1 魔法书
                                                           // (魔法相消术) x 1
                            counts, gitems, // 古代恶魔的记载 x 1
                            1, gcounts);// 给予
                                        // 将任务进度提升为3
                    pc.getQuest().set_step(WizardLv45_1.QUEST.get_id(), 3);
                    // 喀喀喀，人类果然比较愚蠢啊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "stoenm3"));
                }
            }
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

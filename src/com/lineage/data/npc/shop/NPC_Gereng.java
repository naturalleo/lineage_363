package com.lineage.data.npc.shop;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv15_1;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 吉伦<BR>
 * 70009<BR>
 * 说明:不死族的叛徒 (法师30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class NPC_Gereng extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(NPC_Gereng.class);

    private static Random _random = new Random();

    /**
	 *
	 */
    private NPC_Gereng() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new NPC_Gereng();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你是拥有杜克血脉的后裔？哈哈哈...嗯~好吧，你说的也许是事实。
                //pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengp1"));
            	//hjx1000 更改其它职业可以从吉伦学魔法
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv1"));

            } else if (pc.isKnight()) {// 骑士
                // 啊！甘特的学生，为什么你会来到这里？
                //pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengk1"));
            	//hjx1000 更改其它职业可以从吉伦学魔法
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv1"));

            } else if (pc.isElf()) {// 精灵
                // 嗯~我从没想到我会在一个如此荒凉的地方遇见一个妖精。
                //pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerenge1"));
            	//hjx1000 更改其它职业可以从吉伦学魔法
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv1"));

            } else if (pc.isWizard()) {// 法师
                // LV15任务未完成
                if (!pc.getQuest().isEnd(WizardLv15_1.QUEST.get_id())) {
                    int htmlid = _random.nextInt(6) + 1;
                    // 很高兴看到你。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengTe"
                            + htmlid));
                    return;
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    // 欢迎啊。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengw5"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= WizardLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(WizardLv30_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 接受吉伦的建议
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengT1"));
                            break;

                        case 1:// 达到1(交出不死族的骨头)
                               // 交出不死族的骨头
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengT2"));
                            break;

                        case 2:// 达到2(被夺的灵魂)
                               // 取得魔法师必须的物品
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengT3"));
                            break;

                        case 3:// 达到3(请接受这个)
                               // 交给神秘水晶球
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengT4"));
                            break;

                        default:
                            // 欢迎啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerengw5"));
                            break;
                    }
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你是黑暗的一族，但你在追求光明
                //pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengde1"));
            	//hjx1000 更改其它职业可以从吉伦学魔法
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengEv1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 呵呵～这不是贝希摩斯的龙术士吗？真心欢迎你～
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengdk1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 哇～哇～哇～这不是席琳的子孙吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengi1"));
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
            // LV15任务未完成
            if (!pc.getQuest().isEnd(WizardLv15_1.QUEST.get_id())) {
                return;
            }
            // LV30任务已经完成
            if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 12 gerengT2")) {// 接受吉伦的建议
                // 将任务设置为执行中
                QuestClass.get().startQuest(pc, WizardLv30_1.QUEST.get_id());
                // 交出不死族的骨头
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengT2"));

            } else if (cmd.equalsIgnoreCase("request bone piece of undead")) {// 交出不死族的骨头
                // 任务完成需要物件(不死族骨头x 1)
                final L1ItemInstance tgitem = pc.getInventory().checkItemX(
                        40579, 1);
                if (tgitem != null) {// 需要物件充足
                    if (pc.getInventory().removeItem(tgitem, 1) == 1) {// 删除道具
                        // 将任务进度提升为2
                        pc.getQuest().set_step(WizardLv30_1.QUEST.get_id(), 2);
                        // 取得魔法师必须的物品
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "gerengT3"));
                    }

                } else {
                    // 337：\f1%0不足%s。 不死族的骨头
                    pc.sendPackets(new S_ServerMessage(337, "$2033 (1)"));
                    isCloseList = true;
                }

            } else if (cmd.equalsIgnoreCase("quest 14 gerengT4")) {// 取得魔法师必须的物品
                // 将任务进度提升为3
                pc.getQuest().set_step(WizardLv30_1.QUEST.get_id(), 3);
                // 取得魔法师必须的物品
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerengT4"));

            } else if (cmd.equalsIgnoreCase("request mystery staff")) {// 交给神秘水晶球
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, 40567, // 任务完成需要物件(神秘水晶球x 1)
                        1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40567 },// 神秘水晶球x
                                                                        // 1
                            new int[] { 1 }, new int[] { 40580, 40569 }, // 不死族的骨头碎片
                                                                         // x 1
                                                                         // 神秘魔杖
                                                                         // x 1
                            1, new int[] { 1, 1 });// 给予

                    // 将任务进度提升为4
                    pc.getQuest().set_step(WizardLv30_1.QUEST.get_id(), 4);
                    // 关闭对话窗
                    isCloseList = true;
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

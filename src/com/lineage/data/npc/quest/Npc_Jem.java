package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv15_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 詹姆<BR>
 * 70531<BR>
 * 说明:詹姆的请求 (法师15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Jem extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Jem.class);

    private Npc_Jem() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Jem();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else if (pc.isKnight()) {// 骑士
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else if (pc.isElf()) {// 精灵
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else if (pc.isWizard()) {// 法师
                // 任务已经完成
                if (pc.getQuest().isEnd(WizardLv15_1.QUEST.get_id())) {
                    // 如果想要了解更多魔法的事情，请去找吉伦吧。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem6"));

                } else {
                    // 等级达成要求
                    if (pc.getLevel() >= WizardLv15_1.QUEST.get_questlevel()) {
                        // 任务进度
                        switch (pc.getQuest().get_step(
                                WizardLv15_1.QUEST.get_id())) {
                            case 0:// 任务尚未开始
                                   // 关于死亡的灵魂
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "jem1"));

                                // 将任务设置为启动
                                QuestClass.get().startQuest(pc,
                                        WizardLv15_1.QUEST.get_id());
                                break;

                            case 1:// 达到1(任务开始)
                                   // 关于死亡的灵魂
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "jem1"));
                                break;

                            case 2:// 达到2(任务开始)
                                   // 交给骷髅头及受诅咒的魔法书
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "jem4"));
                                break;
                        }

                    } else {
                        // 所有生物在死亡时，会变成什么样子呢？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem2"));
                    }
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));

            } else {
                // 所有生物在死亡时，会变成什么样子呢？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem7"));
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
            // 任务已经完成
            if (pc.getQuest().isEnd(WizardLv15_1.QUEST.get_id())) {
                return;
            }

            if (cmd.equalsIgnoreCase("request cursed spellbook")) {// 交给食尸鬼的牙齿与指甲。
                // 任务已经开始
                if (pc.getQuest().isStart(WizardLv15_1.QUEST.get_id())) {
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, new int[] { 40539,// 食尸鬼的牙齿
                            40538,// 食尸鬼的指甲
                    }, new int[] { 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        // 关闭对话窗
                        pc.sendPackets(new S_CloseList(pc.getId()));

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, new int[] { 40539,// 食尸鬼的牙齿
                                40538,// 食尸鬼的指甲
                        }, new int[] { 1, 1, }, new int[] { 40591,// 受诅咒的魔法书 x 1
                                }, 1, new int[] { 1, });// 给予

                        // 提升任务进度
                        pc.getQuest().set_step(WizardLv15_1.QUEST.get_id(), 2);
                        // 交给骷髅头及受诅咒的魔法书
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem4"));
                    }
                }

            } else if (cmd.equalsIgnoreCase("request book of magical powers")) {// 交给骷髅头及受诅咒的魔法书。
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, new int[] { 40605,// 骷髅头
                        40591,// 受诅咒的魔法书 x 1
                }, new int[] { 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    pc.sendPackets(new S_CloseList(pc.getId()));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, new int[] { 40605,// 骷髅头
                            40591,// 受诅咒的魔法书 x 1
                    }, new int[] { 1, 1, }, new int[] { 20226,// 魔法能量之书 x 1
                            }, 1, new int[] { 1, });// 给予

                    // 将任务设置为结束
                    QuestClass.get().endQuest(pc, WizardLv15_1.QUEST.get_id());
                    // 如果想要了解更多魔法的事情，请去找吉伦吧。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jem6"));
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

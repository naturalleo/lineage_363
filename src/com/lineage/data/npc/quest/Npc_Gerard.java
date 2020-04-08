package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 杰瑞德<BR>
 * 70794<BR>
 * 说明:拯救被幽禁的吉姆 (骑士30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Gerard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Gerard.class);

    private Npc_Gerard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Gerard();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你就是躲避反王的欺压而逃离的王族吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardp1"));

            } else if (pc.isKnight()) {// 骑士
                // LV30任务已经完成
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    // 我们认定你是一个真正的红骑士。。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                            "gerardkEcg"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                        case 1:// 达到1(任务开始)
                        case 2:// 达到2(交谈吉姆)
                        case 3:// 达到3(接受试练)
                               // 欢迎光临，像你一样想要成为<a
                               // link="gerardk2">骑士</a>的许多年轻人会在这聚集。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardk7"));
                            break;

                        case 4:// 达到4
                               // 接受杰瑞德的试炼
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE1"));
                            break;

                        case 5:// 达到5
                               // 交给蛇女之鳞
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE2"));
                            break;

                        case 6:// 达到6
                               // 返生药水的真实
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE3"));
                            break;

                        case 7:// 达到7
                               // 交出得到的感谢信
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE4"));
                            break;

                        default:// 其他
                            // 恭喜你～你终于成为有资格带着武器保卫世界并负起责任的人啊。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE3"));
                            break;
                    }

                } else {
                    // 欢迎光临，像你一样想要成为<a link="gerardk2">骑士</a>的许多年轻人会在这聚集。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk7"));
                }

            } else if (pc.isElf()) {// 精灵
                // 唷~原来你就是妖精族的朋友
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerarde1"));

            } else if (pc.isWizard()) {// 法师
                // 魔法师，欢迎欢迎。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardw1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 嗯...你是堕落妖精的后代黑暗妖精
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardde1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 当亚丁唯一的国王杜克过世后
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 当亚丁唯一的国王杜克过世后
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));

            } else {
                // 当亚丁唯一的国王杜克过世后
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "gerardk4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isKnight()) {// 骑士
            // LV30任务已经完成
            if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                // 任务进度
                switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                    case 4:// 达到4
                        if (cmd.equalsIgnoreCase("quest 16 gerardkE2")) {// 接受杰瑞德的试炼
                            // 提升任务进度
                            pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(),
                                    5);
                            // 交给蛇女之鳞
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE2"));
                        }
                        break;

                    case 5:// 达到5
                        if (cmd.equalsIgnoreCase("request potion of rebirth")) {// 交给蛇女之鳞
                            // 需要物件不足
                            if (CreateNewItem.checkNewItem(pc, 40544, // 任务完成需要物件(蛇女之鳞
                                                                      // x 1)
                                    1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                                isCloseList = true;

                            } else {// 需要物件充足
                                // 收回任务需要物件 给予任务完成物件
                                CreateNewItem.createNewItem(pc, 40544, 1, // 蛇女之鳞
                                                                          // x 1
                                        40607, 1);// 返生药水 x 1
                                // 提升任务进度
                                pc.getQuest().set_step(
                                        KnightLv30_1.QUEST.get_id(), 6);
                                // 返生药水的真实
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "gerardkE3"));
                            }
                        }
                        break;

                    case 6:// 达到6
                        if (cmd.equalsIgnoreCase("quest 18 gerardkE4")) {// 返生药水的真实
                            // 提升任务进度
                            pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(),
                                    7);
                            // 交出得到的感谢信
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "gerardkE4"));
                        }
                        break;

                    case 7:// 达到7
                        if (cmd.equalsIgnoreCase("request shield of red knights")) {// 交出得到的感谢信
                            // 需要物件不足
                            if (CreateNewItem.checkNewItem(pc, 40529, // 任务完成需要物件(感谢信
                                                                      // x 1)
                                    1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                                isCloseList = true;

                            } else {// 需要物件充足
                                // 收回任务需要物件 给予任务完成物件
                                CreateNewItem.createNewItem(pc, 40529, 1, // 感谢信
                                                                          // x 1
                                        20230, 1);// 红骑士盾牌 x 1
                                // 将任务设置为结束
                                QuestClass.get().endQuest(pc,
                                        KnightLv30_1.QUEST.get_id());
                                // 恭喜你。 你终于成为一位有资格使用你身上武器的人类啊。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "gerardkE5"));
                            }
                        }
                        break;

                    default:// 其他
                        isCloseList = true;
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

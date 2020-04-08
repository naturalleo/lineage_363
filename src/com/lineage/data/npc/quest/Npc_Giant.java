package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 巨人长老<BR>
 * 70711<BR>
 * 骑士的证明 (骑士45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Giant extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Giant.class);

    private Npc_Giant() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Giant();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else if (pc.isKnight()) {// 骑士
                // 任务已经完成
                if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                    // 巨人守护神的阴谋啊...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv45_1.QUEST.get_id())) {
                        case 0:// 达到0
                        case 1:// 达到1
                               // 喂！你是不是来抢夺古代的遗物！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "giantk4"));
                            break;

                        case 2:// 达到2
                               // 寻找调查员
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "giantk1"));
                            break;

                        case 3:// 达到3
                               // 递给古代的遗物
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "giantk2"));
                            break;

                        default:// 其他
                            // 巨人守护神的阴谋啊...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "giantk3"));
                            break;
                    }
                } else {
                    // 喂！你是不是来抢夺古代的遗物！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
                }

            } else if (pc.isElf()) {// 精灵
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else if (pc.isWizard()) {// 法师
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));

            } else {
                // 喂！你是不是来抢夺古代的遗物！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk4"));
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
            // 任务已经完成
            if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 23 giantk2")) {// 寻找调查员
                // 递给古代的遗物
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk2"));
                // 提升任务进度
                pc.getQuest().set_step(KnightLv45_1.QUEST.get_id(), 3);

            } else if (cmd.equalsIgnoreCase("request head part of ancient key")) {// 递给古代的遗物
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc, 40537, // 任务完成需要物件(古代的遗物 x 1)
                        1) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    isCloseList = true;

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc, 40537, 1, // 古代的遗物 x 1
                            40534, 1);// 古代钥匙 x 1
                    // 提升任务进度
                    pc.getQuest().set_step(KnightLv45_1.QUEST.get_id(), 4);
                    // 巨人守护神的阴谋啊...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "giantk3"));
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

package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 奇马<BR>
 * 70906<BR>
 * 说明:寻找黑暗之星 (黑暗妖精50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Kima extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Kima.class);

    private Npc_Kima() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Kima();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else if (pc.isKnight()) {// 骑士
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else if (pc.isElf()) {// 精灵
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else if (pc.isWizard()) {// 法师
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    // 你活着的原因是什么？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
                    return;
                }
                // 等级达成要求(LV50-1)
                if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 你活着的原因是什么？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kima1"));
                            break;

                        case 1:
                        case 2:
                            // 嗯...你就是布鲁迪卡新收养的家伙吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kimaq1"));
                            break;

                        case 3:
                            // 关于灵魂枯竭的土地
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kimaq3"));
                            break;

                        default:
                            // 灵魂枯竭的土地上是不会有生命体的存在。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kimaq4"));
                            break;
                    }

                } else {
                    // 你活着的原因是什么？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));

            } else {
                // 你活着的原因是什么？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kima1"));
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
            // LV50任务已经完成
            if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                isCloseList = true;

            } else {
                // 任务进度
                switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                    case 1:
                    case 2:
                        if (cmd.equalsIgnoreCase("request mask of true")) {// 递给调查结果物
                            final L1ItemInstance item = pc.getInventory()
                                    .checkItemX(40583, 1);// 安迪亚之信
                            if (item == null) {
                                // 337 \f1%0不足%s。
                                pc.sendPackets(new S_ServerMessage(337,
                                        "$2654 (1)"));
                                isCloseList = true;

                            } else {
                                pc.getInventory().removeItem(item, 1);// 删除道具
                                // 提升任务进度
                                pc.getQuest().set_step(
                                        DarkElfLv50_1.QUEST.get_id(), 3);
                                // 关于灵魂枯竭的土地
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "kimaq3"));
                            }
                        }
                        break;

                    case 3:
                        if (cmd.equalsIgnoreCase("quest 26 kimaq4")) {// 关于灵魂枯竭的土地
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 20037, 1);// 真实的面具
                            // 提升任务进度
                            pc.getQuest().set_step(
                                    DarkElfLv50_1.QUEST.get_id(), 4);
                            // 灵魂枯竭的土地上是不会有生命体的存在。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kimaq4"));
                        }
                        break;

                    default:
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

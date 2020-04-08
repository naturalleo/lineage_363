package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 罗吉<BR>
 * 70744<BR>
 * 说明:纠正错误的观念 (黑暗妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Roje extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Roje.class);

    private Npc_Roje() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Roje();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else if (pc.isKnight()) {// 骑士
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else if (pc.isElf()) {// 精灵
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else if (pc.isWizard()) {// 法师
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    // 又是你这个臭东西出现在我眼前，我不会给你任何的帮忙。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje15"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 2:// 任务尚未开始
                               // 我要证明你是错的
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "roje11"));
                            break;

                        case 3:
                            // 给予雪怪首级
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "roje12"));
                            break;

                        case 4:
                            // 关于生锈的刺客之剑
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "roje13"));
                            break;

                        default:// 达到1(任务开始)
                            // 又是你这个臭东西出现在我眼前，我不会给你任何的帮忙。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "roje15"));
                            break;
                    }

                } else {
                    // 我在反省过去的罪孽并且迎接崭新的生活。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));

            } else {
                // 我在反省过去的罪孽并且迎接崭新的生活。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roje16"));
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
            // LV45-1任务已经完成
            if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                return;
            }
            // LV45-1任务进度
            switch (pc.getQuest().get_step(DarkElfLv45_1.QUEST.get_id())) {
                case 2:// 达到1
                    if (cmd.equalsIgnoreCase("quest 19 roje12")) {// 我要证明你是错的
                        // 提升任务进度
                        pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 3);
                        // 给予雪怪首级
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "roje12"));
                    }
                    break;

                case 3:// 达到2
                    if (cmd.equalsIgnoreCase("request mark of assassin")) {// 给予雪怪首级
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(40584, 1);// 雪怪首级 x 1

                        if (item != null) {
                            // 删除道具(雪怪首级 x 1)
                            pc.getInventory().removeItem(item, 1);
                            // 取得任务道具
                            CreateNewItem.getQuestItem(pc, npc, 40572, 1);// 刺客之证
                                                                          // x 1
                            // 提升任务进度
                            pc.getQuest().set_step(
                                    DarkElfLv45_1.QUEST.get_id(), 4);
                            // 关于生锈的刺客之剑
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "roje13"));

                        } else {
                            // 需要物件不足
                            isCloseList = true;
                            // 337 \f1%0不足%s。
                            pc.sendPackets(new S_ServerMessage(337, "$2424 (1)"));
                        }
                    }
                    break;

                case 4:// 达到3
                    if (cmd.equalsIgnoreCase("quest 21 roje14")) {// 关于生锈的刺客之剑
                        // 提升任务进度
                        pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 5);
                        // 你想知道关于生锈的刺客之剑的事！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "roje14"));
                    }
                    break;

                default:
                    isCloseList = true;
                    break;
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

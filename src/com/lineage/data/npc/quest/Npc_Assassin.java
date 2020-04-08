package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 刺客首领护卫<BR>
 * 70824<BR>
 * 说明:纠正错误的观念 (黑暗妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Assassin extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Assassin.class);

    private Npc_Assassin() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Assassin();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            boolean isTak = false;
            if (pc.getTempCharGfx() == 3634) {// 刺客
                isTak = true;
            }
            if (!isTak) {
                // 刺客首领会解决掉你这家伙！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin4"));
                return;
            }

            if (pc.isCrown()) {// 王族
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else if (pc.isKnight()) {// 骑士
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else if (pc.isElf()) {// 精灵
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else if (pc.isWizard()) {// 法师
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv45_1.QUEST.get_id())) {
                    // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= DarkElfLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv45_1.QUEST.get_id())) {
                        case 1:// 达到1(任务开始)
                               // 如何才能见到刺客首领
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "assassin1"));
                            break;

                        default:
                            // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "assassin3"));
                            break;
                    }

                } else {
                    // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));

            } else {
                // 传说中的”刺客首领”正在与黑暗妖精研讨改变世界的计划。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "assassin3"));
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
                case 1:// 达到1
                    if (cmd.equalsIgnoreCase("quest 18 assassin2")) {// 如何才能见到刺客首领
                        // 提升任务进度
                        pc.getQuest().set_step(DarkElfLv45_1.QUEST.get_id(), 2);
                        // 每个人都想见到他，但必须要有罗吉手上的刺客之证才能见到他。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "assassin2"));
                    }
                    break;

                default:// 达到1
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

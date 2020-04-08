package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 迪嘉勒廷的男间谍<BR>
 * 80012<BR>
 * <BR>
 * 说明:取回间谍的报告书 (法师50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Dspym extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Dspym.class);

    private Npc_Dspym() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Dspym();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 对话动作
            npc.onTalkAction(pc);

            if (pc.isCrown()) {// 王族
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else if (pc.isKnight()) {// 骑士
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else if (pc.isElf()) {// 精灵
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else if (pc.isWizard()) {// 法师
                // 任务已经完成
                if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                    // 啊～我从公爵听到已收到报告书了
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym4"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= WizardLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                        case 0:// 达到0
                               // 虽然我不认识你，但是请当做路过没看到。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dspym5"));
                            break;

                        case 1:// 达到1
                               // 你是...收到迪嘉勒廷公爵命令而来的那位？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dspym1"));
                            break;

                        default:// 其他
                            // 怎么样了，有找到报告书了吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dspym3"));
                            break;
                    }

                } else {
                    // 虽然我不认识你，但是请当做路过没看到。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));

            } else {
                // 虽然我不认识你，但是请当做路过没看到。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym5"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (npc.getMaster() != null) {
            return;
        }
        if (pc.isWizard()) {// 法师
            // 任务已经完成
            if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 27 dspym2")) {// 询问可以帮忙什么？
                // 提升任务进度
                pc.getQuest().set_step(WizardLv50_1.QUEST.get_id(), 2);
                // 我依照迪嘉勒廷公爵的指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspym2"));
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

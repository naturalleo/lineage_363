package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 志武<BR>
 * 70715<BR>
 * 骑士的证明 (骑士45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Jimu extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Jimu.class);

    private Npc_Jimu() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Jimu();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else if (pc.isKnight()) {// 骑士
                // 任务已经完成
                if (pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                    // 你也是为了巨人的宝物而来的吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= KnightLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv45_1.QUEST.get_id())) {
                        case 1:// 达到1(任务开始)
                               // 看到巨人守护神的方法
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jimuk1"));
                            break;

                        default:// 其他
                            // 你也是为了巨人的宝物而来的吧！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "jimuk4"));
                            break;
                    }
                } else {
                    // 你也是为了巨人的宝物而来的吧！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));
                }

            } else if (pc.isElf()) {// 精灵
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else if (pc.isWizard()) {// 法师
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));

            } else {
                // 你也是为了巨人的宝物而来的吧！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimuk4"));
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
            if (cmd.equalsIgnoreCase("quest 21 jimuk2")) {// 看到巨人守护神的方法
                // 任务进度
                switch (pc.getQuest().get_step(KnightLv45_1.QUEST.get_id())) {
                    case 1:// 达到1(任务开始)
                           // 想要看到巨人守护神必须要有强盗头目所拥有的夜之视野...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "jimuk2"));
                        // 提升任务进度
                        pc.getQuest().set_step(KnightLv45_1.QUEST.get_id(), 2);
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

package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.data.quest.CrownLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 行政官奇浩<BR>
 * 80132<BR>
 * 说明:小恶魔的可疑行动 (王族50级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Kiholl extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Kiholl.class);// 32927:32830

    private Npc_Kiholl() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Kiholl();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (!pc.getInventory().checkItem(49159)) { // 没有调职命令书
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));
                return;
            }
            if (pc.isCrown()) {// 王族
                if (pc.getQuest().isEnd(CrownLv50_1.QUEST.get_id())) {
                    if (pc.getTempCharGfx() == 4261) {
                        // 嗯嗯... 你果然没有让我失望，那意志力非常优秀坚强。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kiholl2"));

                    } else {
                        // 是谁！！！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kiholl0"));
                    }
                    return;
                }

                // 等级达成要求(LV50)
                if (pc.getLevel() >= CrownLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                            // 是谁！！！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "kiholl0"));
                            break;

                        case 2:
                            if (pc.getTempCharGfx() == 4261) {
                                // 询问自己能做些什么
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "kiholl1"));

                            } else {
                                // 是谁！！！
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "kiholl0"));
                            }
                            break;

                        default:
                            break;
                    }

                } else {
                    // 是谁！！！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));
                }

            } else if (pc.isKnight()) {// 骑士
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else if (pc.isElf()) {// 精灵
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else if (pc.isWizard()) {// 法师
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));

            } else {
                // 是谁！！！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "kiholl0"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isCrown()) {// 王族
            // 任务进度
            switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                case 2:
                    if (cmd.equalsIgnoreCase("a")) {// 询问自己能做些什么
                        final L1ItemInstance item = pc.getInventory()
                                .checkItemX(49159, 1);// 调职命令书
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具
                        }
                        // 嗯嗯... 你果然没有让我失望，那意志力非常优秀坚强。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "kiholl2"));
                        // 将任务设置为结束
                        QuestClass.get().endQuest(pc,
                                CrownLv50_1.QUEST.get_id());

                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                CKEWLv50_1.QUEST.get_id());
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

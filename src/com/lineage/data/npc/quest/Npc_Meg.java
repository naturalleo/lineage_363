package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 麦<BR>
 * 70776<BR>
 * 说明:王族的信念 (王族45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Meg extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Meg.class);

    private Npc_Meg() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Meg();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // LV45任务已经完成
                if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                    // 我很想追随像您这样的君主。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "meg4"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 被夺的灵魂
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "meg1"));
                            break;

                        case 2:// 达到2(被夺的灵魂)
                        case 3:// 达到3(请接受这个)
                        case 4:// 达到4(再次需要神秘袋子)
                               // 递给灵魂之证
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "meg2"));
                            break;

                        default:
                            // 我很想追随像您这样的君主。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "meg3"));
                            break;
                    }

                } else {
                    // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));
                }

            } else if (pc.isKnight()) {// 骑士
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else if (pc.isElf()) {// 精灵
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else if (pc.isWizard()) {// 法师
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));

            } else {
                // 你是冒险者吗？你有看到沙漠上的那个大洞吗？那是巨大蚂蚁生存的地方。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg4"));
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
            // LV45任务已经完成
            if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("quest 17 meg2")) {// 被夺的灵魂
                // 将任务进度提升为2
                pc.getQuest().set_step(CrownLv45_1.QUEST.get_id(), 2);
                // 递给灵魂之证
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg2"));

            } else if (cmd.equalsIgnoreCase("request royal family piece b")) {// 递给灵魂之证
                // 需要物件不足
                if (CreateNewItem.checkNewItem(
                        pc,
                        // 40573 灵魂之证(白)妖精
                        // 40574 灵魂之证(黑)法师
                        // 40575 灵魂之证(红)骑士
                        new int[] { 40573, 40574, 40575 },
                        new int[] { 1, 1, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 关闭对话窗
                    pc.sendPackets(new S_CloseList(pc.getId()));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc,
                    // 40573 灵魂之证(白)妖精
                    // 40574 灵魂之证(黑)法师
                    // 40575 灵魂之证(红)骑士
                            new int[] { 40573, 40574, 40575 }, new int[] { 1,
                                    1, 1 }, new int[] { 40587 }, // 王族徽章的碎片B x 1
                            1, new int[] { 1 });// 给予

                    // 将任务进度提升为5
                    pc.getQuest().set_step(CrownLv45_1.QUEST.get_id(), 5);
                    // 我很想追随像您这样的君主。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "meg3"));
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

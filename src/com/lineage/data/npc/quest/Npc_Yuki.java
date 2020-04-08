package com.lineage.data.npc.quest;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.Chapter01;
import com.lineage.data.quest.Chapter01R;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 尤基<BR>
 * 91328<BR>
 * 说明:穿越时空的探险(秘谭)
 * 
 * @author dexc
 * 
 */
public class Npc_Yuki extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Yuki.class);

    public static final Random _random = new Random();

    private Npc_Yuki() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Yuki();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 收到尤丽娅的通知了~~
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "id0"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        try {
            boolean isCloseList = false;

            if (cmd.equalsIgnoreCase("enter")) {// “移动至过去”
                if (!pc.isInParty()) {
                    // 希望能再召集一些同行的同伴！！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "id0_1"));
                    return;
                }
                int count = 5;
                if (pc.isGm()) {
                    count = 2;
                }
                if (pc.getParty().getNumOfMembers() < count) {
                    // 希望能再召集一些同行的同伴！！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "id0_1"));
                    return;
                }
                if (!pc.getParty().isLeader(pc)) {
                    // 出发之前需要和你们的代表说说话
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "id0_2"));
                    return;
                }

                // 传送前往复本
                teltport_all(pc);
                isCloseList = true;
            }

            if (isCloseList) {
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void teltport_all(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = Chapter01.QUEST.get_id();

            // 任务地图编号
            final int mapid = Chapter01.MAPID;

            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();

            final L1Party party = pc.getParty();

            for (L1PcInstance otherPc : party.partyUsers().values()) {
                if (otherPc.getId() != party.getLeaderID()) {
                    // 解除变身
                    L1PolyMorph.undoPoly(otherPc);
                    // 加入副本执行成员
                    WorldQuest.get().put(showId, mapid, questid, otherPc);
                    // 解除魔法技能绝对屏障
                    L1BuffUtil.cancelAbsoluteBarrier(otherPc);
                    L1Teleport.teleport(otherPc,
                            32729 + (_random.nextInt(5) - 2),
                            32725 + (_random.nextInt(5) - 2), (short) mapid, 0,
                            true);

                    // 将任务设置为执行中
                    QuestClass.get().startQuest(otherPc,
                            Chapter01.QUEST.get_id());
                }
            }

            // 加入副本执行成员
            final L1QuestUser quest = WorldQuest.get().put(showId, mapid,
                    questid, pc);

            if (quest == null) {
                _log.error("副本设置过程发生异常!!");
                // 关闭对话窗
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            quest.set_info(false);// 不公告NPC死亡
            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }
            // 解除变身
            L1PolyMorph.undoPoly(pc);
            // 解除魔法技能绝对屏障
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            // 队长位置
            L1Teleport.teleport(pc, 32729, 32725, (short) mapid, 2, true);

            // 将任务设置为执行中
            QuestClass.get().startQuest(pc, Chapter01.QUEST.get_id());

            Chapter01R chapter01R = new Chapter01R(party, showId,
                    Chapter01.QUEST.get_id());
            chapter01R.startR();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

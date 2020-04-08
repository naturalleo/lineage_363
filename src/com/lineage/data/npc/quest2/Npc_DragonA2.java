package com.lineage.data.npc.quest2;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ADLv80_2;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 蓝色 龙之门扉<BR>
 * 法利昂栖息地 70937<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_DragonA2 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_DragonA2.class);

    /** 已经参加过的人员列表 */
    private static final Map<Integer, String> _playList = new HashMap<Integer, String>();

    private Npc_DragonA2() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_DragonA2();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (isError(pc, npc)) {
                return;
            }
            // 传送前往<font fg=66ff66><var src="#0"></font>执行副本条件限制
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon1",
                    new String[] { "法利昂栖息地" }));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("0")) {// 传送进入
            if (isError(pc, npc)) {
                return;
            }
            // 法利昂栖息地
            staraQuestA(pc);
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 进入副本执行任务(法利昂栖息地)
     * 
     * @param pc
     * @return
     */
    private void staraQuestA(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = ADLv80_2.QUEST.get_id();

            // 任务地图编号
            final int mapid = ADLv80_2.MAPID;

            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();

            // 进入人数限制
            int users = QuestMapTable.get().getTemplate(mapid);
            if (users == -1) {// 无限制
                users = Byte.MAX_VALUE;// 设置为127
            }

            final L1Party party = pc.getParty();

            if (party != null) {
                int i = 0;
                // 队伍成员
                for (L1PcInstance otherPc : party.partyUsers().values()) {
                    if (i <= (users - 1)) {
                        if (otherPc.getId() != party.getLeaderID()) {
                            // 加入副本执行成员
                            WorldQuest.get().put(showId, mapid, questid,
                                    otherPc);
                            _playList.put(new Integer(otherPc.getId()),
                                    otherPc.getName());
                            // 传送成员
                            L1Teleport.teleport(otherPc, 32957, 32743,
                                    (short) mapid, 1, true);

                            // 将任务设置为执行中
                            QuestClass.get().startQuest(otherPc,
                                    ADLv80_2.QUEST.get_id());
                            // 将任务设置为结束
                            QuestClass.get().endQuest(otherPc,
                                    ADLv80_2.QUEST.get_id());
                        }
                    }
                    i++;
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

            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }

            // 召唤门0:/ 1:\ ↓Y←X
            // L1SpawnUtil.spawnDoor(quest, 10008, 1796, 32680, 32745, (short)
            // mapid, 1);// T

            L1SpawnUtil.spawnDoor(quest, 10008, 7858, 32741, 32712,
                    (short) mapid, 0);// A 32739 32712 1011 A /
            L1SpawnUtil.spawnDoor(quest, 10009, 7859, 32779, 32681,
                    (short) mapid, 1);// B 32779 32681 1011 B \
            L1SpawnUtil.spawnDoor(quest, 10010, 7858, 32861, 32709,
                    (short) mapid, 0);// C 32861 32709 1011 C /

            final L1Location loc = new L1Location(32951, 32842, mapid);
            L1SpawnUtil.spawn(71026, loc, 5, showId);// 新法利昂(1阶段)

            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32957, 32743, (short) mapid, 1, true);

            // 将任务设置为执行中
            QuestClass.get().startQuest(pc, ADLv80_2.QUEST.get_id());
            // 将任务设置为结束
            QuestClass.get().endQuest(pc, ADLv80_2.QUEST.get_id());

            _playList.put(new Integer(pc.getId()), pc.getName());

            // 移除掉落物
            for (L1NpcInstance npc : quest.npcList()) {
                if (npc instanceof L1MonsterInstance) {
                    final L1MonsterInstance mob = (L1MonsterInstance) npc;
                    if (npc.getNpcId() == 71026) {// 新法利昂(1阶段)
                        continue;
                    }
                    if (npc.getNpcId() == 71027) {// 新法利昂(2阶段)
                        continue;
                    }
                    if (npc.getNpcId() == 71028) {// 新法利昂(3阶段)
                        continue;
                    }
                    mob.set_storeDroped(true);
                    mob.getInventory().clearItems();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 执行条件
     * 
     * @param pc
     * @param npc
     * @return
     */
    private boolean isError(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.isGm()) {
            return false;
        }
        final L1Party party = pc.getParty();
        if (party == null) {
            // 必须在<font fg=66ff66>队伍状态</font>。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon2"));
            return true;
        }
        if (!party.isLeader(pc)) {
            // 必须由队伍队长执行传送命令。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon6"));
            return true;
        }

        if (party.getNumOfMembers() < 3) {
            // 队伍成员必须超过<font fg=66ff66>5人</font>。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon3"));
            return true;
        }

        StringBuilder list80 = null;
        StringBuilder list = null;
        for (L1PcInstance tgpc : party.partyUsers().values()) {
            // 等级低于80
            if (tgpc.getLevel() < 80) {
                if (list80 == null) {
                    list80 = new StringBuilder();
                }
                list80.append(tgpc.getName() + " ");
            }
            // 参加过
            if (_playList.get(new Integer(tgpc.getId())) != null) {
                if (list == null) {
                    list = new StringBuilder();
                }
                list.append(tgpc.getName() + " ");
            }
        }

        if (list80 != null) {
            // 队伍成员必须<font fg=66ff66>80级</font>以上。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon4",
                    new String[] { list80.toString() }));
            return true;
        }

        if (list != null) {
            // 副本任务<font fg=66ff66>每次服务器重新启动</font>可执行一次。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_n_dragon5",
                    new String[] { list.toString() }));
            return true;
        }
        return false;
    }
}

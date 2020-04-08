package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.data.quest.CrownLv30_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldQuest;

/**
 * 看守蚂蚁<BR>
 * 70779<BR>
 * 说明:艾莉亚的请求 (王族30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Ants extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Ants.class);

    private Npc_Ants() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Ants();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            boolean isTak = false;
            if (pc.getTempCharGfx() == 1039) {// 巨大兵蚁
                isTak = true;
            }
            if (pc.getTempCharGfx() == 1037) {// 巨蚁
                // 虽然我有点笨，但我还是能够分辨你不是我们族人。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ants3"));
                return;
            }
            if (!isTak) {
                // #$@$%#$%．．．#$%@#
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ants2"));
                return;
            }
            if (pc.isCrown()) {// 王族
                // LV15任务未完成
                if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    // 糟糕了。战斗兵蚁必须赶快回来...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

                } else {
                    // LV30任务已经完成
                    if (pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                        // 糟糕了。战斗兵蚁必须赶快回来...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

                    } else {
                        // 等级达成要求
                        if (pc.getLevel() >= CrownLv30_1.QUEST.get_questlevel()) {
                            // 任务尚未开始
                            if (!pc.getQuest().isStart(
                                    CrownLv30_1.QUEST.get_id())) {
                                // 糟糕了。战斗兵蚁必须赶快回来...
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "antsn"));

                            } else {// 任务已经开始
                                if (pc.getInventory().checkItem(40547)) { // 已经具有物品
                                                                          // 村民的遗物
                                    // 糟糕了。战斗兵蚁必须赶快回来...
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "antsn"));

                                } else {
                                    // 终于来了啊！我正在等你。
                                    pc.sendPackets(new S_NPCTalkReturn(npc
                                            .getId(), "ants1"));
                                }
                            }

                        } else {
                            // 糟糕了。战斗兵蚁必须赶快回来...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "antsn"));
                        }
                    }
                }

            } else if (pc.isKnight()) {// 骑士
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else if (pc.isElf()) {// 精灵
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else if (pc.isWizard()) {// 法师
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));

            } else {
                // 糟糕了。战斗兵蚁必须赶快回来...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antsn"));
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
            // LV15任务未完成
            if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                return;
            }
            // LV30任务已经完成
            if (pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(CrownLv30_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                if (pc.getInventory().checkItem(40547)) { // 已经具有物品 村民的遗物
                    isCloseList = true;

                } else {
                    if (cmd.equalsIgnoreCase("teleportURL")) {// 前往变种蚂蚁地监
                        // 战斗兵蚁，请您消灭他们吧!
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "antss"));

                    } else if (cmd.equalsIgnoreCase("teleport mutant-dungen")) {// 前往变种蚂蚁地监
                        staraQuest(pc);
                        isCloseList = true;
                    }
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

    /**
     * 进入副本执行任务
     * 
     * @param pc
     * @return
     */
    private void staraQuest(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = CrownLv30_1.QUEST.get_id();

            // 任务地图编号
            final int mapid = CrownLv30_1.MAPID;

            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();

            // 进入人数限制
            int users = QuestMapTable.get().getTemplate(mapid);
            if (users == -1) {// 无限制
                users = Byte.MAX_VALUE;// 设置为127
            }

            int i = 0;
            // 3格以内血盟成员
            for (final L1PcInstance otherPc : World.get().getVisiblePlayer(pc,
                    3)) {
                if (otherPc.getClanid() == 0) {// 没有血盟
                    continue;
                }
                if ((otherPc.getClanid() == pc.getClanid())
                        && (otherPc.getId() != pc.getId())) {
                    if (i <= (users - 1)) {
                        // 加入副本执行成员
                        WorldQuest.get().put(showId, mapid, questid, otherPc);
                        // 设置副本参加编号(已经在WorldQuest加入编号)
                        // otherPc.set_showId(showId);
                        // 传送成员
                        L1Teleport.teleport(otherPc, 32662, 32786,
                                (short) mapid, 3, true);
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

            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32662, 32786, (short) mapid, 3, true);
            // 将任务进度提升为2
            pc.getQuest().set_step(CrownLv30_1.QUEST.get_id(), 2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

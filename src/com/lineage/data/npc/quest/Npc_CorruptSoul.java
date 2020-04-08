package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 堕落的灵魂<BR>
 * 71095<BR>
 * 说明:寻找黑暗之星 (黑暗妖精50级以上官方任务)<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_CorruptSoul extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_CorruptSoul.class);

    private Npc_CorruptSoul() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_CorruptSoul();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (!pc.getInventory().checkEquipped(20037)) { // 真实的面具
                return;
            }
            if (pc.isCrown()) {// 王族
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else if (pc.isKnight()) {// 骑士
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else if (pc.isElf()) {// 精灵
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else if (pc.isWizard()) {// 法师
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    // 拥有邪念并不是堕落，而是存在，愿荣耀与你同在...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq3"));
                    return;
                }
                // 任务进度
                switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        // 你的邪念还不够！
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "csoulqn"));
                        break;
                    case 4:
                        // 关于邪念地监
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "csoulq1"));
                        break;
                    default:
                        // 拥有邪念并不是堕落，而是存在，愿荣耀与你同在...
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "csoulq3"));
                        break;
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));

            } else {
                // ....
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "csoulq2"));
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
                if (pc.getInventory().checkItem(20037)) { // 真实的面具
                    if (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id()) == 4) {// 进度4
                        if (cmd.equalsIgnoreCase("teleportURL")) {// 关于邪念地监
                            // 在邪念地监里有像你一样被邪念缠绕而堕落的人
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "csoulqs"));

                        } else if (cmd
                                .equalsIgnoreCase("teleport evil-dungeon")) {// 往邪念地监
                            staraQuest(pc);
                            isCloseList = true;
                        }

                    } else {
                        isCloseList = true;
                    }

                } else {
                    isCloseList = true;
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
     */
    private void staraQuest(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = DarkElfLv50_1.QUEST.get_id();

            // 任务地图编号
            final int mapid = DarkElfLv50_1.MAPID;

            // 取回新的任务副本编号
            final int showId = WorldQuest.get().nextId();

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

            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32748, 32799, (short) mapid, 5, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

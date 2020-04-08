package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 守门人<BR>
 * 70795<BR>
 * 说明:拯救被幽禁的吉姆 (骑士30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Qguard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Qguard.class);

    private Npc_Qguard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Qguard();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else if (pc.isKnight()) {// 骑士
                // 等级达成要求
                if (pc.getLevel() >= KnightLv30_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                        case 5:// 达到5
                               // 试炼地监是一些想要成为真正红骑士的骑士们修炼的地方。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "qguard"));
                            break;

                        default:// 其他
                            // 只有杰瑞德允许的人才能进入。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "qguard1"));
                            break;
                    }

                } else {
                    // 只有杰瑞德允许的人才能进入。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));
                }

            } else if (pc.isElf()) {// 精灵
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else if (pc.isWizard()) {// 法师
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));

            } else {
                // 只有杰瑞德允许的人才能进入。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "qguard1"));
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
            // LV30任务已经完成
            if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                // 任务进度
                switch (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id())) {
                    case 5:// 达到5
                        if (cmd.equalsIgnoreCase("teleportURL")) {// 往杰瑞德的试炼地监
                            // 请记住，在地监里只能使用红骑士之剑与翡翠药水。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "qguards"));

                        } else if (cmd
                                .equalsIgnoreCase("teleport gerard-dungen")) {// 进入杰瑞德的试炼地监
                            staraQuest(pc);
                            isCloseList = true;
                        }
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

    /**
     * 进入副本执行任务
     * 
     * @param pc
     * @return
     */
    private void staraQuest(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = KnightLv30_1.QUEST.get_id();

            // 任务地图编号
            final int mapid = KnightLv30_1.MAPID;

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

            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }

            // 召唤门0:/ 1:\ ↓Y←X
            L1SpawnUtil.spawnDoor(quest, 10004, 89, 32769, 32778,
                    (short) mapid, 1);

            // 召唤主要蛇女
            final L1Location loc = new L1Location(32774, 32778, mapid);
            final L1NpcInstance mob = L1SpawnUtil.spawn(81107, loc, 5, showId);
            mob.getInventory().storeItem(40544, 1);// 蛇女之鳞 x 1
            quest.addNpc(mob);

            // 使用牛的代号脱除全部装备
            pc.getInventory().takeoffEquip(945);
            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32769, 32768, (short) mapid, 4, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

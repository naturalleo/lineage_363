package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 赛菲亚 <BR>
 * 50031<BR>
 * 说明:妖精的任务 (妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Sepia extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Sepia.class);

    private Npc_Sepia() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Sepia();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族

            } else if (pc.isKnight()) {// 骑士

            } else if (pc.isElf()) {// 精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                        case 1:// 达到1
                               // 你是妖精...过去我也曾经是妖精！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "sepia2"));
                            break;

                        case 2:// 达到2
                        case 3:// 达到3
                               // 承受赛菲亚的罪行
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "sepia1"));
                            break;

                        default:// 其他
                            break;
                    }

                } else {
                    // 你是妖精...过去我也曾经是妖精！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sepia2"));
                }

            } else if (pc.isWizard()) {// 法师

            } else if (pc.isDarkelf()) {// 黑暗精灵

            } else if (pc.isDragonKnight()) {// 龙骑士

            } else if (pc.isIllusionist()) {// 幻术师

            } else {
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isElf()) {// 精灵
            // LV45任务已经完成
            if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                // 任务进度
                switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                    case 2:// 达到2
                    case 3:// 达到3
                        if (cmd.equalsIgnoreCase("teleport sepia-dungen")) {// 承受赛菲亚的罪行
                            // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 3);
                            staraQuest(pc, ElfLv45_1.MAPID);
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
     * @param mapid
     *            任务地图编号
     * @return
     */
    public static void staraQuest(final L1PcInstance pc, final int mapid) {
        try {
            // 任务编号
            final int questid = ElfLv45_1.QUEST.get_id();

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

            // 传送任务执行者
            L1Teleport.teleport(pc, 32745, 32872, (short) mapid, 0, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

package com.lineage.data.npc.quest;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv30_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 精灵公主<BR>
 * 70853<BR>
 * <BR>
 * 说明:达克马勒的威胁 (妖精30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Fairyp extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Fairyp.class);

    private static Random _random = new Random();

    private Npc_Fairyp() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Fairyp();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else if (pc.isKnight()) {// 骑士
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else if (pc.isElf()) {// 精灵
                // LV30任务已经完成
                if (pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                    // 你好，我受到迷幻森林之母指示
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv30_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(ElfLv30_1.QUEST.get_id())) {
                        // 你好，我受到迷幻森林之母指示
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "fairyp3"));

                    } else {// 任务已经开始
                        if (_random.nextInt(100) < 40) {
                            // teleport darkmar-dungen
                            // 嗨，你也是来参加迷幻森林之母所主持的成人仪式吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "fairyp1"));

                        } else {
                            // teleport dark-elf-dungen
                            // 嗨，你也是来参加迷幻森林之母所主持的成人仪式吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "fairyp2"));
                        }
                    }

                } else {
                    // 你好，我受到迷幻森林之母指示
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));
                }

            } else if (pc.isWizard()) {// 法师
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));

            } else {
                // 你好，我受到迷幻森林之母指示
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "fairyp3"));
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
            // 任务已经完成
            if (pc.getQuest().isEnd(ElfLv30_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("teleport darkmar-dungen")) {// 前往达克马勒的隐身处
                staraQuest(pc, ElfLv30_1.MAPID_1);
                isCloseList = true;

            } else if (cmd.equalsIgnoreCase("teleport dark-elf-dungen")) {// 前往达克马勒的隐身处
                staraQuest(pc, ElfLv30_1.MAPID_2);
                isCloseList = true;
            }
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
            final int questid = ElfLv30_1.QUEST.get_id();

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
            L1Teleport.teleport(pc, 32744, 32794, (short) mapid, 5, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

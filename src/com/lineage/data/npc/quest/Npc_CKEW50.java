package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;

/**
 * 往圣殿的入口<BR>
 * 80135<BR>
 * <BR>
 * 80136<BR>
 * <BR>
 * 80134<BR>
 * <BR>
 * 80133<BR>
 * <BR>
 * 说明:不死魔族再生的秘密 (王族,骑士,妖精,法师50级以上官方任务-50级后半段)
 * 
 * @author dexc
 * 
 */
public class Npc_CKEW50 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_CKEW50.class);

    private Npc_CKEW50() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_CKEW50();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (!pc.isInParty()) {// 未加入队伍
                return;
            }

            int i = 0;
            // 队伍成员
            for (final L1PcInstance otherPc : pc.getParty().partyUsers()
                    .values()) {
                if (otherPc.isCrown()) {// 王族
                    i += 1;
                } else if (otherPc.isKnight()) {// 骑士
                    i += 2;
                } else if (otherPc.isElf()) {// 精灵
                    i += 4;
                } else if (otherPc.isWizard()) {// 法师
                    i += 8;
                }
            }
            if (i != CKEWLv50_1.USER) {// 人数异常
                return;
            }
            if (pc.isCrown()) {// 王族
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                        && npc.getNpcId() == 80135) {
                    // 周围绕着暗红不吉利的光
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "50quest_p"));

                } else {
                    // 将任务设置为启动
                    QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                }

            } else if (pc.isKnight()) {// 骑士
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                        && npc.getNpcId() == 80136) {
                    // 周围绕着暗红不吉利的光
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "50quest_k"));

                } else {
                    // 将任务设置为启动
                    QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                }

            } else if (pc.isElf()) {// 精灵
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                        && npc.getNpcId() == 80133) {
                    // 周围绕着暗红不吉利的光
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "50quest_e"));

                } else {
                    // 将任务设置为启动
                    QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                }

            } else if (pc.isWizard()) {// 法师
                // 任务开始
                if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                        && npc.getNpcId() == 80134) {
                    // 周围绕着暗红不吉利的光
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "50quest_w"));

                } else {
                    // 将任务设置为启动
                    QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                }

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
        if (pc.isCrown()) {// 王族
            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                    && npc.getNpcId() == 80135) {
                if (cmd.equalsIgnoreCase("ent")) {// 手去触摸再进场
                    staraQuest(pc);
                }
            }
            isCloseList = true;

        } else if (pc.isKnight()) {// 骑士
            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                    && npc.getNpcId() == 80136) {
                if (cmd.equalsIgnoreCase("ent")) {// 手去触摸再进场
                    staraQuest(pc);
                }
            }
            isCloseList = true;

        } else if (pc.isElf()) {// 精灵
            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                    && npc.getNpcId() == 80133) {
                if (cmd.equalsIgnoreCase("ent")) {// 手去触摸再进场
                    staraQuest(pc);
                }
            }
            isCloseList = true;

        } else if (pc.isWizard()) {// 法师
            // 任务开始
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())
                    && npc.getNpcId() == 80134) {
                if (cmd.equalsIgnoreCase("ent")) {// 手去触摸再进场
                    staraQuest(pc);
                }
            }
            isCloseList = true;

        } else if (pc.isDarkelf()) {// 黑暗精灵
            isCloseList = true;
        } else if (pc.isDragonKnight()) {// 龙骑士
            isCloseList = true;
        } else if (pc.isIllusionist()) {// 幻术师
            isCloseList = true;
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
            if (!pc.isInParty()) {// 未加入队伍
                return;
            }
            // 任务编号
            final int questid = CKEWLv50_1.QUEST.get_id();

            // 任务地图编号
            final int mapid = CKEWLv50_1.MAPID;

            // 任务副本编号
            int showId = -1;

            // 进入人数限制
            int users = QuestMapTable.get().getTemplate(mapid);
            if (users == -1) {// 无限制
                users = Byte.MAX_VALUE;// 设置为127
            }

            int i = 0;
            // 队伍成员
            for (final L1PcInstance otherPc : pc.getParty().partyUsers()
                    .values()) {
                if (otherPc.get_showId() != -1) {
                    showId = otherPc.get_showId();
                }
                if (otherPc.isCrown()) {// 王族
                    i += 1;
                } else if (otherPc.isKnight()) {// 骑士
                    i += 2;
                } else if (otherPc.isElf()) {// 精灵
                    i += 4;
                } else if (otherPc.isWizard()) {// 法师
                    i += 8;
                }
            }
            if (i != CKEWLv50_1.USER) {// 人数异常
                return;
            }

            // 取回新的任务副本编号
            if (showId == -1) {
                showId = WorldQuest.get().nextId();
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
            quest.set_outStop(true);// 该副本参加者其中之一离开 立即结束

            // 取回进入时间限制
            final Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }

            if (pc.isCrown()) {// 王族
                // 传送任务执行者
                L1Teleport.teleport(pc, 32720, 32900, (short) mapid, 2, true);

            } else if (pc.isKnight()) {// 骑士
                // 传送任务执行者
                L1Teleport.teleport(pc, 32721, 32853, (short) mapid, 3, true);

            } else if (pc.isElf()) {// 精灵
                // 传送任务执行者
                L1Teleport.teleport(pc, 32725, 32940, (short) mapid, 1, true);

            } else if (pc.isWizard()) {// 法师
                // 传送任务执行者
                L1Teleport.teleport(pc, 32810, 32941, (short) mapid, 7, true);
            }

            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);

            // 将任务进度提升为2
            pc.getQuest().set_step(CKEWLv50_1.QUEST.get_id(), 2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

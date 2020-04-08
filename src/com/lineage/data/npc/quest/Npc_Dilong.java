package com.lineage.data.npc.quest;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.WizardLv30_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 迪隆<BR>
 * 50014<BR>
 * 说明:不死族的叛徒 (法师30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Dilong extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Dilong.class);

    private Npc_Dilong() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Dilong();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        /*
         * if (true) { staraQuest(pc); return; }
         */
        try {
            if (pc.isCrown()) {// 王族
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else if (pc.isKnight()) {// 骑士
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else if (pc.isElf()) {// 精灵
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else if (pc.isWizard()) {// 法师
                // LV30任务已经完成
                if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                    // 请问...你认识塔拉斯吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= WizardLv30_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(WizardLv30_1.QUEST.get_id())) {
                        // 请问...你认识塔拉斯吗？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dilong3"));

                    } else {// 任务已经开始
                        // 请问...您是我的吉伦师父所提到的人吧？
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "dilong1"));
                    }

                } else {
                    // 请问...你是冒险家吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));
                }

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));

            } else {
                // 请问...你是冒险家吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilong2"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isWizard()) {// 法师
            // LV30任务已经完成
            if (pc.getQuest().isEnd(WizardLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(WizardLv30_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("teleportURL")) {// 往不死族的地监
                // 没有不死族的钥匙
                if (!pc.getInventory().checkItem(40581)) {
                    // 啊，想要通过魔力之门需要有魔法材料...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilongn"));
                    return;
                }
                // 已经携带 不死族的骨头(40579)
                if (pc.getInventory().checkItem(40579)) {
                    // 啊，想要通过魔力之门需要有魔法材料...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilongn"));

                } else {
                    // 那么准备好了吗？以后成功时，请告诉我地监内部的事情。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dilongs"));
                }

            } else if (cmd.equalsIgnoreCase("teleport mage-quest-dungen")) {// 通过魔力之门
                staraQuest(pc);
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
     * @return
     */
    public static void staraQuest(L1PcInstance pc) {
        try {
            // 任务编号
            final int questid = WizardLv30_1.QUEST.get_id();

            // 任务地图编号(法师试炼地监)
            final int mapid = WizardLv30_1.MAPID;

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

            final ArrayList<L1NpcInstance> npcs = quest.npcList(81109);// 骷髅
            if (npcs != null) {
                for (L1NpcInstance npc : npcs) {
                    // 改变骷髅外型为阿鲁巴(时效30分钟)
                    L1PolyMorph.doPoly(npc, 1128, 1800,
                            L1PolyMorph.MORPH_BY_ITEMMAGIC);
                }
            }

            // 召唤门0:/ 1:\ ↓Y←X
            L1SpawnUtil.spawnDoor(quest, 10000, 89, 32809, 32795,
                    (short) mapid, 1);
            L1SpawnUtil.spawnDoor(quest, 10001, 88, 32812, 32909,
                    (short) mapid, 0);// 88
            L1SpawnUtil.spawnDoor(quest, 10002, 89, 32825, 32920,
                    (short) mapid, 1);
            L1SpawnUtil.spawnDoor(quest, 10003, 90, 32868, 32919,
                    (short) mapid, 0);
            // 设置副本参加编号(已经在WorldQuest加入编号)
            // pc.set_showId(showId);

            // 使用牛的代号脱除全部装备
            pc.getInventory().takeoffEquip(945);
            // 传送任务执行者
            L1Teleport.teleport(pc, 32791, 32788, (short) mapid, 5, true);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

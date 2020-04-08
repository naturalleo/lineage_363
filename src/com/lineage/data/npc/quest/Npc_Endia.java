package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 安迪亚<BR>
 * 71094<BR>
 * <BR>
 * 说明:寻找黑暗之星 (黑暗妖精50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Endia extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Endia.class);

    private Npc_Endia() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Endia();
    }

    @Override
    public int type() {
        return 7;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (npc.getMaster() != null) {
                if (npc.getMaster().equals(pc)) {// 是主人
                    // 只要找到莱拉，我的任务就算完成了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq2"));

                } else {
                    // 在我完成任务后我的灵魂将会消失。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                }
                return;
            }

            // 对话动作
            npc.onTalkAction(pc);

            if (pc.isCrown()) {// 王族
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else if (pc.isKnight()) {// 骑士
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else if (pc.isElf()) {// 精灵
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else if (pc.isWizard()) {// 法师
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                    // 在我完成任务后我的灵魂将会消失。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                    return;
                }
                // 等级达成要求(LV50-1)
                if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 1:
                            // 接受安迪亚的要求
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "endiaq1"));
                            break;

                        default:
                            // 在我完成任务后我的灵魂将会消失。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "endiaq4"));
                            break;
                    }

                } else {
                    // 在我完成任务后我的灵魂将会消失。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                }

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));

            } else {
                // 在我完成任务后我的灵魂将会消失。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (npc.getMaster() != null) {
            return;
        }
        if (pc.isDarkelf()) {// 黑暗精灵
            // LV50任务已经完成
            if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                isCloseList = true;

            } else {
                // 等级达成要求(LV50-1)
                if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest()
                            .get_step(DarkElfLv50_1.QUEST.get_id())) {
                        case 1:
                            if (cmd.equalsIgnoreCase("start")) {// 接受安迪亚的要求71094
                                final L1Npc l1npc = NpcTable.get().getTemplate(
                                        DarkElfLv50_1._endiaId);
                                // 召唤跟随者
                                new L1FollowerInstance(l1npc, npc, pc);
                                // 只要找到莱拉，我的任务就算完成了。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "endiaq2"));
                            }
                            break;

                        default:
                            isCloseList = true;
                            break;
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

    @Override
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (npc.getNpcId() != DarkElfLv50_1._endiaId) {// 安迪亚
                return;
            }
            if (pc == null) {// 主人为空
                return;
            }
            // 取回范围物件
            for (final L1Object object : World.get().getVisibleObjects(npc)) {
                if (object instanceof L1NpcInstance) {
                    final L1NpcInstance tgnpc = (L1NpcInstance) object;
                    if (tgnpc.getNpcTemplate().get_npcId() == DarkElfLv50_1._tgid) { // 找到莱拉
                        // 自己与主人距离小于3
                        if (npc.getLocation().getTileLineDistance(
                                pc.getLocation()) < 3) {
                            // 主人与莱拉距离小于3
                            if (tgnpc.getLocation().getTileLineDistance(
                                    pc.getLocation()) < 3) {
                                // 提升任务进度
                                pc.getQuest().set_step(
                                        DarkElfLv50_1.QUEST.get_id(), 2);
                                // 取得任务道具
                                CreateNewItem.getQuestItem(pc, npc, 40582, 1);// 安迪亚之袋
                                npc.setParalyzed(true);
                                npc.deleteMe();
                            }
                        }
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

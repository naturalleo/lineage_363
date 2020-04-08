package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 爱尔菈丝<BR>
 * 85019<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Elas extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Elas.class);

    private Npc_Elas() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Elas();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 这位兄弟，你有什么事吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas2"));

            } else if (pc.isKnight()) {// 骑士
                // 这位兄弟，你有什么事吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas2"));

            } else if (pc.isElf()) {// 精灵
                // 这位兄弟，你有什么事吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas2"));

            } else if (pc.isWizard()) {// 法师
                // 这位兄弟，你有什么事吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 这位兄弟，你有什么事吗？
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                    // 接受妖魔密使变形卷轴
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas1"));

                } else {
                    // 亚丁 上头的黑乌云实在有点可怕。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas6"));
                }

            } else if (pc.isIllusionist()) {// 幻术师
                // 我是守护<font fg=ffff0>贝希摩斯</font>的龙骑士之一
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas3"));

            } else {
                // 我是守护<font fg=ffff0>贝希摩斯</font>的龙骑士之一
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas3"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDragonKnight()) {// 龙骑士
            if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("a")) {// 接受妖魔密使变形卷轴
                    if (pc.getInventory().checkItem(49220)) { // 已经具有物品
                        // 已经提供给你<font fg=ffffaf>妖魔密使变形卷轴</font>了喔。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas5"));

                    } else {
                        // 给予任务道具(妖魔密使变形卷轴)
                        CreateNewItem.getQuestItem(pc, npc, 49220, 1);
                        // <font fg=ffffaf>妖魔密使变形卷轴</font>在这里。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "elas4"));
                    }
                }

            } else {
                isCloseList = true;
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

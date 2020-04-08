package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 李察<BR>
 * 70545<BR>
 * 说明:王族的信念 (王族45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Richard extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Richard.class);

    private Npc_Richard() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Richard();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // LV45任务已经完成
                if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                    // 唉～越来越怀念亚丁王国以往的时光。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv45_1.QUEST.get_questlevel()) {
                    // 任务尚未开始
                    if (!pc.getQuest().isStart(CrownLv45_1.QUEST.get_id())) {
                        // 唉～越来越怀念亚丁王国以往的时光。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "richard3"));

                    } else {// 任务已经开始
                        if (pc.getInventory().checkItem(40586)) { // 已经具有物品
                                                                  // 王族徽章的碎片A(背叛的妖魔队长)
                            // 以前管理亚丁王国治安的负责人－麦
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "richard4"));

                        } else {
                            // 亚丁王国的荣耀
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "richard1"));
                        }
                    }

                } else {
                    // 唉～越来越怀念亚丁王国以往的时光。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));
                }

            } else if (pc.isKnight()) {// 骑士
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else if (pc.isElf()) {// 精灵
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else if (pc.isWizard()) {// 法师
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));

            } else {
                // 唉～越来越怀念亚丁王国以往的时光。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "richard3"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;

/**
 * 任务NPC控制项
 * 
 * @author daien
 * 
 */
public class L1QuestInstance extends L1NpcInstance {

    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1QuestInstance.class);

    /**
     * 任务NPC
     * 
     * @param template
     */
    public L1QuestInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onNpcAI() {
        if (this.isAiRunning()) {
            return;
        }
        final int npcId = this.getNpcTemplate().get_npcId();
        switch (npcId) {
        // 指定NPC停止移动AI
            case 71075:// 疲惫的蜥蜴人战士
            case 70957:// 罗伊
            case 81209:// 罗伊-人形僵尸
            case 80012:// 迪嘉勒廷的女间谍
                break;

            default:
                this.setActived(false);
                this.startAI();
                break;
        }
    }

    /**
     * 受到攻击
     */
    @Override
    public void onAction(final L1PcInstance pc) {
        try {
            final L1AttackMode attack = new L1AttackPc(pc, this);
            attack.action();
            attack.commit();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 对话
     */
    @Override
    public void onTalkAction(final L1PcInstance pc) {
        /*
         * final int pcX = pc.getX(); final int pcY = pc.getY(); final int npcX
         * = this.getX(); final int npcY = this.getY();
         */

        final int npcId = this.getNpcTemplate().get_npcId();

        // 改变面向
        this.setHeading(this.targetDirection(pc.getX(), pc.getY()));
        this.broadcastPacketAll(new S_ChangeHeading(this));

        if (npcId == 71062) { // 卡米特
            if (pc.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 2) {
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), "kamit1b"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), "kamit1"));
            }
        } else if (npcId == 71075) { // 疲惫的蜥蜴人战士
            if (pc.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 1) {
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), "llizard1b"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), "llizard1a"));
            }
        }

        // 动作暂停
        set_stop_time(REST_MILLISEC);
        this.setRest(true);
    }

    /**
     * NPC对话结果的处理
     */
    @Override
    public void onFinalAction(final L1PcInstance pc, final String action) {
        if (action.equalsIgnoreCase("start")) {
            final int npcId = this.getNpcTemplate().get_npcId();
            if ((npcId == 71062)// 卡米特
                    && (pc.getQuest().get_step(L1PcQuest.QUEST_CADMUS) == 2)) {
                final L1Npc l1npc = NpcTable.get().getTemplate(71062);
                final L1FollowerInstance follow = new L1FollowerInstance(l1npc,
                        this, pc);
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), ""));

            } else if ((npcId == 71075)// 疲惫的蜥蜴人战士
                    && (pc.getQuest().get_step(L1PcQuest.QUEST_LIZARD) == 1)) {
                final L1Npc l1npc = NpcTable.get().getTemplate(71075);
                final L1FollowerInstance follow = new L1FollowerInstance(l1npc,
                        this, pc);
                pc.sendPackets(new S_NPCTalkReturn(this.getId(), ""));
            }
        }
    }
}

package com.lineage.data.npc.quest;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv50_1;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 迪嘉勒廷的女间谍<BR>
 * 80012<BR>
 * <BR>
 * 说明:协助间谍大逃亡 (妖精50级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Dspy extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Dspy.class);

    private Npc_Dspy() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Dspy();
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
                    // 呼，托你的帮忙让我获救了，真的很谢谢你。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy3"));

                } else {
                    // 啊啊！！该怎么办才好呢...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));
                }
                return;
            }

            // 对话动作
            npc.onTalkAction(pc);

            if (pc.isCrown()) {// 王族
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else if (pc.isKnight()) {// 骑士
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else if (pc.isElf()) {// 精灵
                // 任务已经完成
                if (pc.getQuest().isEnd(ElfLv50_1.QUEST.get_id())) {
                    // 啊啊！！该怎么办才好呢...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv50_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                        case 3:// 达到3
                        case 4:// 达到4
                               // 我受到迪嘉勒廷公爵的指示来保护你，我会带你到安全的地方。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dspy2"));
                            break;

                        default:// 其他
                            // 啊啊！！该怎么办才好呢...
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "dspy1"));
                            break;
                    }

                } else {
                    // 啊啊！！该怎么办才好呢...
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));
                }

            } else if (pc.isWizard()) {// 法师
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));

            } else {
                // 啊啊！！该怎么办才好呢...
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy1"));
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
        if (pc.isElf()) {// 精灵
            // 任务已经完成
            if (pc.getQuest().isEnd(ElfLv50_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("start")) {// 我受到迪嘉勒廷公爵的指示来保护你，我会带你到安全的地方。
                final L1Npc l1npc = NpcTable.get()
                        .getTemplate(ElfLv50_1._npcId);
                // 召唤跟随者
                new L1FollowerInstance(l1npc, npc, pc);
                // 提升任务进度
                pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 4);
                // 呼，托你的帮忙让我获救了，真的很谢谢你。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dspy3"));
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
            if (npc.getNpcId() != ElfLv50_1._npcId) {// 迪嘉勒廷的女间谍
                return;
            }
            if (pc == null) {// 主人为空
                return;
            }

            if (((pc.getX() >= 32557) && (pc.getX() <= 32576)) // 抵抗军村庄
                    && ((pc.getY() >= 32656) && (pc.getY() <= 32687))
                    && (pc.getMapId() == 400)) {
                // 提升任务进度
                pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 5);
                // 取得任务道具
                if (!pc.getInventory().checkItem(49163)) {
                    CreateNewItem.getQuestItem(pc, npc, 49163, 1);// 密封的情报书
                }
                npc.setParalyzed(true);
                npc.deleteMe();

            } else {
                spawnAssassin(pc, npc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static Random _random = new Random();

    /**
     * 召唤魔族暗杀团
     * 
     * @param npc
     */
    private void spawnAssassin(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (_random.nextInt(100) <= 2) {
                final L1Location loc = npc.getLocation().randomLocation(4,
                        false);
                // 登场效果
                pc.sendPacketsX8(new S_EffectLocation(loc, 3992));
                // 魔族暗杀团
                final L1MonsterInstance mob = L1SpawnUtil.spawnX(80011, loc,
                        npc.get_showId());
                mob.setLink(npc);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

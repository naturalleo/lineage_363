package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv15_1;
import com.lineage.data.quest.CrownLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 搜查蚂蚁<BR>
 * 70782<BR>
 * 说明:艾莉亚的请求 (王族30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Ant extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Ant.class);

    private Npc_Ant() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Ant();
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            boolean isTak = false;
            if (pc.getTempCharGfx() == 1037) {// 巨蚁
                isTak = true;
            }
            if (pc.getTempCharGfx() == 1039) {// 巨大兵蚁
                isTak = true;
            }
            if (!isTak) {
                // #$@$%#$%．．．#$%@#
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant2"));
                return;
            }
            if (pc.isCrown()) {// 王族
                // LV15任务未完成
                if (!pc.getQuest().isEnd(CrownLv15_1.QUEST.get_id())) {
                    // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

                } else {
                    // LV30任务已经完成
                    if (pc.getQuest().isEnd(CrownLv30_1.QUEST.get_id())) {
                        // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

                    } else {
                        // 等级达成要求
                        if (pc.getLevel() >= CrownLv30_1.QUEST.get_questlevel()) {
                            // 任务尚未开始
                            if (!pc.getQuest().isStart(
                                    CrownLv30_1.QUEST.get_id())) {
                                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "ant3"));

                            } else {// 任务已经开始
                                // 去西边有一群守门蚂蚁正在观察着变种蚂蚁的出没
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "ant1"));
                            }

                        } else {
                            // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "ant3"));
                        }
                    }
                }

            } else if (pc.isKnight()) {// 骑士
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else if (pc.isElf()) {// 精灵
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else if (pc.isWizard()) {// 法师
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));

            } else {
                // 你应该听说过吧？某一天这里忽然出现新的变种蚂蚁开始威胁到我们的生存。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ant3"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

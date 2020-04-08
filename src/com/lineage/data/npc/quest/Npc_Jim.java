package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.data.quest.KnightLv30_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 吉姆<BR>
 * 70555<BR>
 * 说明:拯救被幽禁的吉姆 (骑士30级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Jim extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Jim.class);

    private Npc_Jim() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Jim();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.getTempCharGfx() != 2374) {// 骷髅
                // #$@$%#$%．．．#$%@#．．．(吉姆说着你听不懂的骷髅语言)
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim1"));
                return;
            }

            if (pc.isCrown()) {// 王族
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else if (pc.isKnight()) {// 骑士
                // LV30任务已经完成
                if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                    // 你能够忍受这样长时间的所有试练，我感到很敬佩，愿殷海萨祝福你。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimcg"));
                    return;
                }
                if (pc.getInventory().checkItem(40529)) { // 已经具有物品 (感谢信)
                    // 你能够忍受这样长时间的所有试练，我感到很敬佩，愿殷海萨祝福你。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimcg"));
                    return;
                }
                // 任务尚未开始
                if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                    // 长久的等待...等待有人来帮我解除诅咒
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

                } else {// 任务已经开始
                    if (pc.getQuest().get_step(KnightLv30_1.QUEST.get_id()) < 2) {
                        // 提升任务进度
                        pc.getQuest().set_step(KnightLv30_1.QUEST.get_id(), 2);
                    }
                    // 有关吉姆的诅咒
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim2"));
                }

            } else if (pc.isElf()) {// 精灵
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else if (pc.isWizard()) {// 法师
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));

            } else {
                // 长久的等待...等待有人来帮我解除诅咒
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jim4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isKnight()) {// 骑士
            // LV15任务未完成
            if (!pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                return;
            }
            // LV30任务已经完成
            if (pc.getQuest().isEnd(KnightLv30_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(KnightLv30_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                if (cmd.equalsIgnoreCase("request letter of gratitude")) {// 递给返生药水
                    // 需要物件不足
                    if (CreateNewItem.checkNewItem(pc, new int[] { 40607,// 返生药水
                                                                         // x 1
                            }, new int[] { 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                        // 关闭对话窗
                        isCloseList = true;

                    } else {// 需要物件充足
                        // 收回任务需要物件 给予任务完成物件
                        CreateNewItem.createNewItem(pc, new int[] { 40607,// 返生药水
                                }, new int[] { 1, }, new int[] { 40529,// 感谢信 x
                                                                       // 1
                                }, 1, new int[] { 1, });// 给予

                        // 你能够忍受这样长时间的所有试练，我感到很敬佩，愿殷海萨祝福你。
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jimcg"));
                    }
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
}

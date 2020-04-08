package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 白魔法师皮尔塔 <BR>
 * 71200<BR>
 * 说明:王族的信念 (王族45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Pieta extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Pieta.class);

    private Npc_Pieta() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Pieta();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // LV45任务已经完成
                if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                    // 我知道你是为了那些没被救援到的百姓
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta9"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= CrownLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(CrownLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "pieta8"));
                            break;

                        case 1:// 达到1(任务开始)
                        case 2:// 达到2(被夺的灵魂)
                               // 当然任何事我都已有心理准备了。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "pieta2"));
                            // 将任务进度提升为3
                            pc.getQuest().set_step(CrownLv45_1.QUEST.get_id(),
                                    3);
                            break;

                        case 3:// 达到3(请接受这个)
                               // 请接受这个。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "pieta4"));
                            break;

                        case 4:// 达到4(再次需要神秘袋子)
                               // 再次需要神秘袋子
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "pieta6"));
                            break;

                        default:
                            // 我知道你是为了那些没被救援到的百姓
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "pieta9"));
                            break;
                    }

                } else {
                    // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta8"));
                }

            } else if (pc.isKnight()) {// 骑士
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else if (pc.isElf()) {// 精灵
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else if (pc.isWizard()) {// 法师
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));

            } else {
                // 我在研究人类灵魂的复活，人类灵魂一旦在肉体时间限制一到
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta1"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        if (pc.isCrown()) {// 王族
            // LV45任务已经完成
            if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                return;
            }
            if (cmd.equalsIgnoreCase("a")) {// 请接受这个。
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc,
                // 失去光明的灵魂
                        new int[] { 41422 }, new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 好像还没找到失去光明的灵魂阿。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta10"));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc,
                            // 失去光明的灵魂
                            new int[] { 41422 }, new int[] { 1 },
                            new int[] { 40568 }, // 神秘的袋子 x 1
                            1, new int[] { 1 });// 给予

                    // 将任务进度提升为5
                    pc.getQuest().set_step(CrownLv45_1.QUEST.get_id(), 4);
                    // 这就是.. 谢谢
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta5"));
                }

            } else if (cmd.equalsIgnoreCase("b")) {// 取来了
                // 需要物件不足
                if (CreateNewItem.checkNewItem(pc,
                // 失去光明的灵魂
                        new int[] { 41422 }, new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                    // 好像还没找到失去光明的灵魂阿。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta10"));

                } else {// 需要物件充足
                    // 收回任务需要物件 给予任务完成物件
                    CreateNewItem.createNewItem(pc,
                            // 失去光明的灵魂
                            new int[] { 41422 }, new int[] { 1 },
                            new int[] { 40568 }, // 神秘的袋子 x 1
                            1, new int[] { 1 });// 给予

                    // 这就是.. 谢谢
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pieta5"));
                }
            }
        }
    }
}

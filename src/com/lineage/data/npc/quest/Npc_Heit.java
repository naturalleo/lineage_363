package com.lineage.data.npc.quest;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;

/**
 * 希托 <BR>
 * 70724<BR>
 * 说明:妖精的任务 (妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Heit extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Heit.class);

    private Npc_Heit() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Heit();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.getLawful() < 0) {// 邪恶
                final Random random = new Random();
                if (random.nextInt(100) < 20) {
                    npc.broadcastPacketX8(new S_NpcChat(npc, "$4991"));
                }
                return;
            }
            if (pc.isCrown()) {// 王族
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else if (pc.isKnight()) {// 骑士
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else if (pc.isElf()) {// 精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                    // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit4"));
                            break;

                        case 1:// 达到1(任务开始)
                               // 询问需要帮助的事情
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit1"));
                            break;

                        case 2:// 达到2
                        case 3:// 达到3
                        case 4:// 达到4
                               // 递给蓝色长笛
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit2"));
                            break;

                        case 5:// 达到5
                               // 关于水之竖琴
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit3"));
                            break;

                        default:// 其他
                            // 外婆以前曾告诉过我水之竖琴的事情。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit5"));
                            break;
                    }

                } else {
                    // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
                }

            } else if (pc.isWizard()) {// 法师
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));

            } else {
                // 虽然说吉普赛人受到一般人的轻视与虐待...但他们也是一个珍贵的生命体！
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "heit4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        if (pc.isElf()) {// 精灵
            // LV45任务已经完成
            if (pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                return;
            }
            // 任务尚未开始
            if (!pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                isCloseList = true;

            } else {// 任务已经开始
                // 任务进度
                switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                    case 1:// 达到1
                        if (cmd.equalsIgnoreCase("quest 15 heit2")) {// 询问需要帮助的事情
                            // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 2);
                            // 递给蓝色长笛
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "heit2"));
                        }
                        break;

                    case 2:// 达到2
                    case 3:// 达到3
                    case 4:// 达到4
                        if (cmd.equalsIgnoreCase("request mystery shell")) {// 递给蓝色长笛
                            final L1ItemInstance item = pc.getInventory()
                                    .checkItemX(40602, 1);// 蓝色长笛 x 1

                            if (item != null) {
                                // 删除道具(蓝色长笛 x 1)
                                pc.getInventory().removeItem(item, 1);
                                // 提升任务进度
                                pc.getQuest().set_step(
                                        ElfLv45_1.QUEST.get_id(), 5);
                                // 谢谢你！帮我们找回蓝色长笛。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "heit3"));

                            } else {
                                // 需要物件不足
                                isCloseList = true;
                            }
                        }
                        break;

                    case 5:// 达到5
                        if (cmd.equalsIgnoreCase("quest 17 heit5")) {// 关于水之竖琴
                            if (pc.getInventory().checkItem(40566)) { // 已经具有物品
                                isCloseList = true;

                            } else {
                                // 提升任务进度
                                pc.getQuest().set_step(
                                        ElfLv45_1.QUEST.get_id(), 6);
                                // 取得任务道具 (神秘贝壳)
                                CreateNewItem.getQuestItem(pc, npc, 40566, 1);
                                // 外婆以前曾告诉过我水之竖琴的事情。
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "heit5"));
                            }
                        }
                        break;

                    default:// 其他
                        isCloseList = true;
                        break;
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

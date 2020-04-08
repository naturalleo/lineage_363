package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_2;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 神官知布烈<BR>
 * 71257<BR>
 * 说明:妖精族传说中的弓 (妖精45级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Zybril extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Zybril.class);

    private Npc_Zybril() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Zybril();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else if (pc.isKnight()) {// 骑士
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else if (pc.isElf()) {// 精灵
                // LV45任务已经完成
                if (pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                    // 你还有其他的事情要找我吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril19"));
                    return;
                }
                // 等级达成要求
                if (pc.getLevel() >= ElfLv45_2.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                        case 1:// 达到1(任务开始)
                               // 趁着莎尔的诅咒还没越来越深之前赶快救他吧。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril15"));
                            break;

                        case 2:// 达到2
                               // 有什么事吗
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril1"));
                            break;

                        case 3:// 达到3
                               // 我将你需要的材料带回了
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril7"));
                            break;

                        case 4:// 达到4
                               // 拿出精灵之泪10个。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril8"));
                            break;

                        case 5:// 达到5
                               // 莎尔之戒 41349
                            if (pc.getInventory().checkItem(41349, 1)) {
                                // 拿出莎尔的戒指
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "zybril18"));

                            } else {
                                // 好，祝你好运。愿伊娃的祝福陪伴着你...
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                        "zybril17"));
                            }
                            break;

                        default:// 其他
                            // 你还有其他的事情要找我吗？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril19"));
                            break;
                    }

                } else {
                    // 你有带精灵之泪？你是谁阿
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
                }

            } else if (pc.isWizard()) {// 法师
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));

            } else {
                // 你有带精灵之泪？你是谁阿
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "zybril16"));
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
            if (pc.getQuest().isEnd(ElfLv45_2.QUEST.get_id())) {
                return;
            }
            // 任务进度
            switch (pc.getQuest().get_step(ElfLv45_2.QUEST.get_id())) {
                case 0:// 达到0
                case 1:// 达到1
                    break;

                case 2:// 达到2
                    if (cmd.equals("A")) {// 我有带罗宾孙的信
                        // 罗宾孙的推荐书
                        final L1ItemInstance item = pc.getInventory()
                                .findItemId(41348);
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);// 删除道具(罗宾孙的推荐书)
                            // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 3);
                            // 罗宾孙？啊! 那个做弓的人阿？然后呢？
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril3"));

                        } else {
                            // 罗宾孙？那是谁啊...啊! 精灵森林的那个弓匠啊
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril11"));
                        }
                    }
                    break;

                case 3:// 达到3
                    if (cmd.equals("B")) {// 我将你需要的材料带回了
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc,
                                // 品质钻石,品质红宝石,品质蓝宝石,品质绿宝石
                                new int[] { 40048, 40049, 40050, 40051 },
                                new int[] { 10, 10, 10, 10 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            // 去收集
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril12"));

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(
                                    pc,
                                    // 品质钻石,品质红宝石,品质蓝宝石,品质绿宝石
                                    new int[] { 40048, 40049, 40050, 40051 },
                                    new int[] { 10, 10, 10, 10 },
                                    new int[] { 41353 }, // 伊娃短剑 x 1
                                    1, new int[] { 1 });// 给予
                                                        // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 4);
                            // 拿出精灵之泪10个。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril8"));
                        }
                    }
                    break;

                case 4:// 达到4
                    if (cmd.equals("C")) {// 拿出精灵之泪10个。
                        // 需要物件不足
                        if (CreateNewItem
                                .checkNewItem(pc,
                                // 精灵之泪,伊娃短剑
                                        new int[] { 40514, 41353 }, new int[] {
                                                10, 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            // 需要 精灵之泪10个
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril13"));

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc,
                            // 精灵之泪,伊娃短剑
                                    new int[] { 40514, 41353 }, new int[] { 10,
                                            1 }, new int[] { 41354 }, // 伊娃的圣水 x
                                                                      // 1
                                    1, new int[] { 1 });// 给予
                                                        // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 5);
                            // 谢谢你。我还想要再拜托你一件事情。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril9"));
                        }
                    }
                    break;

                case 5:// 达到5
                    if (cmd.equals("D")) {// 拿出莎尔的戒指
                        // 需要物件不足
                        if (CreateNewItem.checkNewItem(pc,
                        // 莎尔的戒指
                                new int[] { 41349 }, new int[] { 1 }) < 1) {// 传回可交换道具数小于1(需要物件不足)
                            // 你要解开莎尔的诅咒吗？我该怎么相信你呢?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril14"));

                        } else {// 需要物件充足
                            // 收回任务需要物件 给予任务完成物件
                            CreateNewItem.createNewItem(pc,
                                    // 精灵之泪,伊娃短剑
                                    new int[] { 41349 }, new int[] { 1 },
                                    new int[] { 41351 }, // 月光之气息 x 1
                                    1, new int[] { 1 });// 给予
                                                        // 提升任务进度
                            pc.getQuest().set_step(ElfLv45_2.QUEST.get_id(), 6);
                            // 请你接下 <font fg=ffffaf>月光之气息</font>吧。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "zybril10"));
                        }
                    }
                    break;

                default:// 其他
                    isCloseList = true;
                    break;
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

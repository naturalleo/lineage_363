package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv45_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 佣兵团长 多文<BR>
 * 71198<BR>
 * 说明:毒蛇之牙的名号(全职业45级任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Tion extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Tion.class);

    private Npc_Tion() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Tion();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 任务已经完成
            if (pc.getQuest().isEnd(ALv45_1.QUEST.get_id())) {
                // 哈哈哈。我早就知道你是个可造之才。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion8"));

            } else {
                // 等级达成要求
                if (pc.getLevel() >= ALv45_1.QUEST.get_questlevel()) {
                    // 任务进度
                    switch (pc.getQuest().get_step(ALv45_1.QUEST.get_id())) {
                        case 0:// 任务尚未开始
                               // 你来找我干么?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion1"));
                            // 将任务设置为执行中
                            QuestClass.get().startQuest(pc,
                                    ALv45_1.QUEST.get_id());
                            break;

                        case 1:// 达到1(任务开始)
                               // 你来找我干么?
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion1"));
                            break;

                        case 2:// 达到2
                        case 3:// 达到3
                               // 给他看帝伦的教本
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion5"));
                            break;

                        case 4:// 达到4
                               // 不错嘛，从现在起我要正式测你的实力
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion6"));
                            break;

                        case 5:// 达到5
                               // 来，这是最后一次了！
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion7"));
                            break;

                        default:
                            // 哈哈哈。我早就知道你是个可造之才。
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "tion8"));
                            break;
                    }

                } else {
                    // 你想要穿"训练骑士披肩"吗？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion20"));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;
        int[] items = null;
        int[] counts = null;
        int[] gitems = null;
        int[] gcounts = null;

        if (cmd.equalsIgnoreCase("A")) {// 拿出亡者的信件5个
            items = new int[] { 41339 };
            counts = new int[] { 5 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 你竟然有勇气加入毒蛇，我想你应该不会害怕海音恶灵才对?
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion9"));
                return;
            }

            gitems = new int[] { 41340 };
            gcounts = new int[] { 1 };

            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予
            // 还有两下子嘛。
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion4"));
            // 将任务进度提升为2
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 2);

        } else if (cmd.equalsIgnoreCase("B")) {// 给他看帝伦的教本
            items = new int[] { 41341 };
            counts = new int[] { 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 帝伦的教本在哪里呢？如果你还没有，就到旅馆附近去找找吧。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion10"));
                return;
            }
            // gitems = new int[]{41325};
            // gcounts = new int[]{1};
            // 听说帝伦最近不知道怎么了
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion5"));

        } else if (cmd.equalsIgnoreCase("C")) {// 拿出法利昂的血痕
            items = new int[] { 41343, 41341 };// 法利昂的血痕 x 1 41343
            counts = new int[] { 1, 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 还没找到原因吗?
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion12"));
                return;
            }
            gitems = new int[] { 21057 };// 训练骑士披肩 (1) x 1 21057
            gcounts = new int[] { 1 };

            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予

            // 不错嘛，从现在起我要正式测你的实力
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion6"));
            // 将任务进度提升为4
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 4);

        } else if (cmd.equalsIgnoreCase("D")) {// 交付水中的水
            items = new int[] { 41344, 21057 };// 水中的水 x 1 41344
            counts = new int[] { 1, 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 不错嘛，从现在起我要正式测你的实力
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion17"));
                return;
            }

            gitems = new int[] { 21058 };// 训练骑士披肩 (2) x 1 21058
            gcounts = new int[] { 1 };

            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予

            // 来，这是最后一次了！
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion7"));
            // 将任务进度提升为5
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 5);

        } else if (cmd.equalsIgnoreCase("E")) {// 交付酸性液体
            items = new int[] { 41345, 21058 };// 酸性液体 x 1 41345
            counts = new int[] { 1, 1 };
            // 需要物件不足
            if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                    counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
                // 不错嘛，从现在起我要正式测你的实力
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion18"));
                return;
            }

            gitems = new int[] { 21059 };// 毒蛇之牙披肩 x 1 21059
            gcounts = new int[] { 1 };

            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予

            // 来，这是最后一次了！
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tion8"));
            // 结束任务
            pc.getQuest().set_end(ALv45_1.QUEST.get_id());

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

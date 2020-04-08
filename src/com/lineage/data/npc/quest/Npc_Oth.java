package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_2;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 欧斯<BR>
 * 70826<BR>
 * 说明:欧斯的先见之明 (妖精15级以上官方任务)
 * 
 * @author dexc
 * 
 */
public class Npc_Oth extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Oth.class);

    private Npc_Oth() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Oth();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else if (pc.isKnight()) {// 骑士
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else if (pc.isElf()) {// 精灵
                // 任务已经完成
                if (pc.getQuest().isEnd(ElfLv15_2.QUEST.get_id())) {
                    // 关于哈汀
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth5"));

                } else {
                    // 等级达成要求
                    if (pc.getLevel() >= ElfLv15_2.QUEST.get_questlevel()) {
                        // 关于妖魔族的魔法
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth1"));
                        // 将任务设置为启动
                        QuestClass.get().startQuest(pc,
                                ElfLv15_2.QUEST.get_id());

                    } else {
                        // 你好啊。好久没看过自己的同族，
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth6"));
                    }
                }

            } else if (pc.isWizard()) {// 法师
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));

            } else {
                // 燃柳村里常常会聚集拥有坏思想的人类。
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "oth2"));
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
            // 任务已经完成
            if (pc.getQuest().isEnd(ElfLv15_2.QUEST.get_id())) {
                return;
            }

            if (cmd.equalsIgnoreCase("request dex helmet of elven")) {// 选择精灵敏捷头盔
                getItem(pc, 20021);

            } else if (cmd.equalsIgnoreCase("request con helmet of elven")) {// 选择精灵体质头盔
                getItem(pc, 20039);
            }

        } else {
            isCloseList = true;
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 交换物品
     * 
     * @param pc
     * @param npc
     * @param srcid
     * @param getid
     */
    private void getItem(final L1PcInstance pc, final int getid) {
        // 需要物件不足
        if (CreateNewItem.checkNewItem(pc, new int[] { 40609,// 甘地妖魔魔法书
                40610,// 那鲁加妖魔魔法书
                40611,// 都达玛拉妖魔魔法书
                40612,// 阿吐巴妖魔魔法书
        }, new int[] { 1, 1, 1, 1, }) < 1) {// 传回可交换道具数小于1(需要物件不足)
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));

        } else {// 需要物件充足
            // 收回任务需要物件 给予任务完成物件
            CreateNewItem.createNewItem(pc, new int[] { 40609,// 甘地妖魔魔法书
                    40610,// 那鲁加妖魔魔法书
                    40611,// 都达玛拉妖魔魔法书
                    40612,// 阿吐巴妖魔魔法书
            }, new int[] { 1, 1, 1, 1, }, new int[] { getid,// GET
                    }, 1, new int[] { 1, });// 给予

            // 将任务设置为结束
            QuestClass.get().endQuest(pc, ElfLv15_2.QUEST.get_id());

            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}

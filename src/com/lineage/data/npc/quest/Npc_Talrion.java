package com.lineage.data.npc.quest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.data.quest.DragonKnightLv45_1;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 塔尔立昂<BR>
 * 85020<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Talrion extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Talrion.class);

    private Npc_Talrion() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Talrion();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {// 王族
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isKnight()) {// 骑士
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isElf()) {// 精灵
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isWizard()) {// 法师
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isDarkelf()) {// 黑暗精灵
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isDragonKnight()) {// 龙骑士
                // LV50任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv50_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49228)) {// 发光的银块
                        if (!pc.getInventory().checkItem(49230)) {// 没有清单
                            // 交出发光的银块
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                    "talrion10"));
                            return;
                        }
                    }
                }
                // LV45任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv45_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49214)) {// 普洛凯尔的第二次信件
                        // 普洛凯尔的第二次信件
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "talrion9"));
                        return;
                    }
                }
                // LV30任务已经完成
                if (pc.getQuest().isEnd(DragonKnightLv30_1.QUEST.get_id())) {
                    if (pc.getInventory().checkItem(49213)) {// 普洛凯尔的第一次信件
                        // 交出普洛凯尔的第一次信件
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(),
                                "talrion1"));
                        return;
                    }
                }
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else if (pc.isIllusionist()) {// 幻术师
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));

            } else {
                // 我是护卫 <font fg=ffff0>贝希摩斯</font>的龙骑士
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion4"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (pc.isDragonKnight()) {// 龙骑士
            if (cmd.equalsIgnoreCase("a")) {// 交出普洛凯尔的第一次信件
                final L1ItemInstance item = pc.getInventory().checkItemX(49213,
                        1);// 普洛凯尔的第一次信件
                if (item != null) {
                    pc.getInventory().removeItem(item, 1);// 删除道具
                    // 给予任务道具(龙鳞臂甲)
                    CreateNewItem.getQuestItem(pc, npc, 21103, 1);
                    // 恭喜～<font fg=fff00>普洛凯尔</font>下了赐予你<font
                    // fg=ffffaf>龙鳞臂甲</font>的命令。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion2"));

                } else {
                    // <font fg=ffffaf>普洛凯尔的第一次信件</font>在哪呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion3"));
                }

            } else if (cmd.equalsIgnoreCase("b")) {// 普洛凯尔的第二次信件
                final L1ItemInstance item = pc.getInventory().checkItemX(49214,
                        1);// 普洛凯尔的第二次信件
                if (item != null) {
                    pc.getInventory().removeItem(item, 1);// 删除道具
                    // 给予任务道具(龙骑士斗篷)
                    CreateNewItem.getQuestItem(pc, npc, 21102, 1);
                    // 恭喜～<font fg=fff00>普洛凯尔</font>赐予你 <font
                    // fg=ffffaf>龙骑士之斗篷</font>了。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion7"));

                } else {
                    // <font fg=ffffaf>普洛凯尔的第二次信件</font>在哪呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion8"));
                }
            } else if (cmd.equalsIgnoreCase("c")) {// 交出发光的银块
                final L1ItemInstance item = pc.getInventory().checkItemX(49228,
                        1);// 发光的银块
                if (item != null) {
                    // 给予任务道具(塔尔立昂的武器材料清单)
                    CreateNewItem.getQuestItem(pc, npc, 49230, 1);
                    // 恭喜～长老 <font fg=fff00>普洛凯尔</font>称赞你的实力。
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion5"));

                } else {
                    // <font fg=ffffaf>发光的银块</font>在哪呢？
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talrion6"));
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

    @Override
    public void attack(final L1PcInstance pc, final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }

    @Override
    public void work(final L1NpcInstance npc) {
        // TODO Auto-generated method stub
    }
}

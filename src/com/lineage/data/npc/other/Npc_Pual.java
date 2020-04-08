package com.lineage.data.npc.other;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 铁匠^皮尔<BR>
 * 85028<BR>
 * 
 * @author dexc
 * 
 */
public class Npc_Pual extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(Npc_Pual.class);

    private Npc_Pual() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new Npc_Pual();
    }

    @Override
    public int type() {
        return 3;
    }

    @Override
    public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
        try {
            // 请问有什么事吗？
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pual1"));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void action(final L1PcInstance pc, final L1NpcInstance npc,
            final String cmd, final long amount) {
        boolean isCloseList = false;

        if (cmd.equalsIgnoreCase("request halpas symbol")) {// 交出普洛凯尔的矿物袋
            if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                final L1ItemInstance item = pc.getInventory().checkItemX(49215,
                        1);// 普洛凯尔的矿物袋
                if (item != null) {
                    pc.getInventory().removeItem(item, 1);// 删除道具
                    // 给予任务道具(妖魔密使的徽印)
                    CreateNewItem.getQuestItem(pc, npc, 49223, 1);

                } else {
                    // 337 \f1%0不足%s。
                    pc.sendPackets(new S_ServerMessage(337, "$5731(1)"));
                }
            }

            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("request chainsword extinctioner")) {// 制作破灭者锁链剑

            final L1ItemInstance item1 = pc.getInventory().checkItemX(49230, 1);// 塔尔立昂的武器材料清单
            if (item1 != null) {
                pc.getInventory().removeItem(item1);// 删除道具
            }
            /*
             * 发光的银块1个 米索莉100个 钢铁块10个 高品质钻石3个
             */
            int[] items = new int[] { 49228, 40494, 40779, 40052 };
            int[] counts = new int[] { 1, 100, 10, 3 };
            int[] gitems = new int[] { 273 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

        } else if (cmd.equalsIgnoreCase("request lump of steel")) {// 制作钢铁块
            /*
             * 钢铁原石 5个 金属块5个 500 个金币
             */
            int[] items = new int[] { 40899, 40408, 40308 };
            int[] counts = new int[] { 5, 5, 500 };
            int[] gitems = new int[] { 40779 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, 1, gcounts);// 给予
                isCloseList = true;

            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));

            } else if (xcount < 1) {
                isCloseList = true;
            }

        } else if (cmd.equalsIgnoreCase("a1")) {// 制作钢铁块
            /*
             * 钢铁原石 5个 金属块5个 500 个金币
             */
            int[] items = new int[] { 40899, 40408, 40308 };
            int[] counts = new int[] { 5, 5, 500 };
            int[] gitems = new int[] { 40779 };
            int[] gcounts = new int[] { 1 };
            // 可制作数量
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount >= amount) {
                // 收回需要物件 给予完成物件
                CreateNewItem.createNewItem(pc, items, counts, // 需要
                        gitems, amount, gcounts);// 给予
            }
            isCloseList = true;

        } else if (cmd.equalsIgnoreCase("request chainsword destroyer")) {// 制作破灭者锁链剑
            /*
             * 受封印被遗忘的巨剑1个 芮克妮的网20个 高级皮革20个 火龙鳞1个 1000000个金币
             */
            int[] items = new int[] { 17, 40406, 40503, 40393, 40308 };
            int[] counts = new int[] { 1, 20, 20, 1, 1000000 };
            int[] gitems = new int[] { 273 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

        } else if (cmd.equalsIgnoreCase("request guarder of ancient archer")) {// 制作古代神射臂甲
            /*
             * 受封印被遗忘的皮盔甲1个 安特之树皮50个 米索莉线50个 芮克妮的蜕皮20个 精灵羽翼20个 黑色米索莉金属板3个
             * 1000000个金币
             */
            int[] items = new int[] { 20140, 40445, 40504, 40505, 40521, 40495,
                    40308 };
            int[] counts = new int[] { 1, 3, 20, 50, 20, 50, 1000000 };
            int[] gitems = new int[] { 21105 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);

        } else if (cmd.equalsIgnoreCase("request guarder of ancient champion")) {// 制作古代斗士臂甲
            /*
             * 受封印被遗忘的金属盔甲1个 黑色米索莉金属板5个 1000000个金币
             */
            int[] items = new int[] { 20143, 40445, 40308 };
            int[] counts = new int[] { 1, 5, 1000000 };
            int[] gitems = new int[] { 21106 };
            int[] gcounts = new int[] { 1 };
            isCloseList = getItem(pc, items, counts, gitems, gcounts);
        }

        if (isCloseList) {
            // 关闭对话窗
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    /**
     * 交换道具
     * 
     * @param pc
     * @param items
     * @param counts
     * 
     * @return 是否关闭现有视窗
     */
    private boolean getItem(final L1PcInstance pc, int[] items, int[] counts,
            int[] gitems, int[] gcounts) {
        // 需要物件不足
        if (CreateNewItem.checkNewItem(pc, items, // 需要物件
                counts) < 1) {// 传回可交换道具数小于1(需要物件不足)
            return true;

        } else {// 需要物件充足
            // 收回需要物件 给予完成物件
            CreateNewItem.createNewItem(pc, items, counts, // 需要
                    gitems, 1, gcounts);// 给予
            return true;
        }
    }
}

package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>灵魂水晶40576</font><BR>
 * <font color=#00800>灵魂水晶40577</font><BR>
 * <font color=#00800>灵魂水晶40578</font><BR>
 * Crystal Piece of Soul
 * 
 * @see 使用者死亡 并产生任务道具
 * @author dexc
 * 
 */
public class Crystal_PieceSoul extends ItemExecutor {

    /**
	 *
	 */
    private Crystal_PieceSoul() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Crystal_PieceSoul();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        final int itemId = item.getItemId();
        // 可使用判断
        boolean notUse = false;
        // 物件编号
        switch (itemId) {

            case 40576:// 灵魂水晶(白)妖精
                if (!pc.isElf()) {
                    notUse = true;
                }
                break;

            case 40577:// 灵魂水晶(黑)法师
                if (!pc.isWizard()) {
                    notUse = true;
                }
                break;

            case 40578:// 灵魂水晶(红)骑士
                if (!pc.isKnight()) {
                    notUse = true;
                }
                break;
        }

        if (notUse) {
            // 264 \f1你的职业无法使用此装备。
            pc.sendPackets(new S_ServerMessage(264));

        } else {

            final String itenName = item.getLogName();

            if (pc.castleWarResult() == true) { // 战争中
                // 330 \f1无法使用 %0%o。
                pc.sendPackets(new S_ServerMessage(403, itenName));

            } else if (pc.getMapId() == 303) { // 梦幻之岛
                // 330 \f1无法使用 %0%o。
                pc.sendPackets(new S_ServerMessage(403, itenName));

            } else {
                // 使用者死亡
                pc.death(null);
                // 删除道具
                pc.getInventory().removeItem(item, 1);
                final int newItemId = item.getItemId() - 3;
                // 取得任务道具
                CreateNewItem.createNewItem(pc, newItemId, 1);
            }
        }
    }
}

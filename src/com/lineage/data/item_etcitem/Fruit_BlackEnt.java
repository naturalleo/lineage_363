package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>黑暗安特的水果40411</font><BR>
 * Fruit of Black Ent
 * 
 * @author dexc
 * 
 */
public class Fruit_BlackEnt extends ItemExecutor {

    /**
	 *
	 */
    private Fruit_BlackEnt() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Fruit_BlackEnt();
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

        final String itenName = item.getLogName();

        if (pc.castleWarResult() == true) { // 战争中
            // 330 \f1无法使用 %0%o。
            pc.sendPackets(new S_ServerMessage(403, itenName));

        } else if (pc.getMapId() == 303) { // 梦幻之岛
            // 330 \f1无法使用 %0%o。
            pc.sendPackets(new S_ServerMessage(403, itenName));

        } else {
            // 删除道具
            pc.getInventory().removeItem(item, 1);

            // 使用者死亡
            pc.death(null);

            final int newItemId = 20299;// 死亡的誓约
            // 取得任务道具
            CreateNewItem.createNewItem(pc, newItemId, 1);
        }
    }
}

package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>封印的傲慢之塔传送符(11F)40280</font><BR>
 * Sealed TOI Teleport Charm, 11F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(21F)40281</font><BR>
 * Sealed TOI Teleport Charm, 21F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(31F)40282</font><BR>
 * Sealed TOI Teleport Charm, 31F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(41F)40283</font><BR>
 * Sealed TOI Teleport Charm, 41F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(51F)40284</font><BR>
 * Sealed TOI Teleport Charm, 51F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(61F)40285</font><BR>
 * Sealed TOI Teleport Charm, 61F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(71F)40286</font><BR>
 * Sealed TOI Teleport Charm, 71F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(81F)40287</font><BR>
 * Sealed TOI Teleport Charm, 81F<BR>
 * <font color=#00800>封印的傲慢之塔传送符(91F)40288</font><BR>
 * Sealed TOI Teleport Charm, 91F<BR>
 * 
 * @see 取得传送前往指令楼层的传送符
 * @author dexc
 * 
 */
public class Transmission_Reel_Seal extends ItemExecutor {

    /**
	 *
	 */
    private Transmission_Reel_Seal() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Transmission_Reel_Seal();
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
        pc.getInventory().removeItem(item, 1);
        final L1ItemInstance item1 = pc.getInventory().storeItem(itemId + 9, 1);
        if (item1 != null) {// 获得%0
            pc.sendPackets(new S_ServerMessage(403, item1.getLogName()));
        }
    }
}

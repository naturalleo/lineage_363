package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/***
 * 磨刀石40317
 */
public class Hone extends ItemExecutor {

    /**
	 *
	 */
    private Hone() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Hone();
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
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }
        // 对象为武器或者防具
        if ((item1.getItem().getType2() != 0) && (item1.get_durability() > 0)) {
            String msg0;
            pc.getInventory().recoveryDamage(item1);
            msg0 = item1.getLogName();
            if (item1.get_durability() == 0) {
                pc.sendPackets(new S_ServerMessage(464, msg0)); // %0
                // 现在变成像个新的一样。
            } else {
                pc.sendPackets(new S_ServerMessage(463, msg0)); // %0 变好多了。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
        pc.getInventory().removeItem(item, 1);
    }

}

package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_IdentifyDesc;

/**
 * 鉴定卷轴40126 象牙塔鉴定卷轴40098
 */
public class Appraisal_Reel extends ItemExecutor {

    /**
	 *
	 */
    private Appraisal_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Appraisal_Reel();
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
        if (!item1.isIdentified()) {
            item1.setIdentified(true);
            pc.getInventory().updateItem(item1, L1PcInventory.COL_IS_ID);
        }
        pc.sendPackets(new S_IdentifyDesc(item1));
        pc.getInventory().removeItem(item, 1);
    }
}

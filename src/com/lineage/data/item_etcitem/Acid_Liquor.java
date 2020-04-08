package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.poison.L1DamagePoison;

/**
 * 酸性的液体41345
 */
public class Acid_Liquor extends ItemExecutor {

    /**
	 *
	 */
    private Acid_Liquor() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Acid_Liquor();
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
        L1DamagePoison.doInfection(pc, pc, 3000, 5);
        pc.getInventory().removeItem(item, 1);
    }
}

package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 妖魔密使变形卷轴 49220
 */
public class OrcEmissaryPoly extends ItemExecutor {

    /**
	 *
	 */
    private OrcEmissaryPoly() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new OrcEmissaryPoly();
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
        pc.getInventory().removeItem(item);// 移除道具
        // 妖魔密使
        L1PolyMorph.doPoly(pc, 6984, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }
}

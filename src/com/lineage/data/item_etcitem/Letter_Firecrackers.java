package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * <font color=#00800>字母鞭炮A~Z</font><BR>
 * 
 * @author dexc
 */
public class Letter_Firecrackers extends ItemExecutor {

    /**
	 *
	 */
    private Letter_Firecrackers() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Letter_Firecrackers();
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
        final int soundid = itemId - 34946;
        pc.sendPacketsAll(new S_SkillSound(pc.getId(), soundid));
        pc.getInventory().removeItem(item, 1);
    }
}

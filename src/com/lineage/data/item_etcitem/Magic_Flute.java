package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1GuardianInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Sound;

/**
 * 魔法笛子40493
 */
public class Magic_Flute extends ItemExecutor {

    /**
	 *
	 */
    private Magic_Flute() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Magic_Flute();
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
        pc.sendPacketsX8(new S_Sound(165));

        for (final L1Object visible : pc.getKnownObjects()) {
            if (visible instanceof L1GuardianInstance) {
                final L1GuardianInstance guardian = (L1GuardianInstance) visible;
                if (guardian.getNpcTemplate().get_npcId() == 70850) {
                    if (CreateNewItem.createNewItem(pc, 88, 1)) {
                        pc.getInventory().removeItem(item, 1);
                    }
                }
            }
        }

    }
}

package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_Sound;

/**
 * 哨子40315
 */
public class Whistle extends ItemExecutor {

    /**
	 *
	 */
    private Whistle() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Whistle();
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
        pc.sendPacketsX8(new S_Sound(437));

        final Object[] petList = pc.getPetList().values().toArray();
        for (final Object petObject : petList) {
            if (petObject instanceof L1PetInstance) {
                final L1PetInstance pet = (L1PetInstance) petObject;
                pet.call();
            }
        }
    }
}

package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1DragonSlayer;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DragonGate;

/**
 * 龙之钥匙 - 47010
 */
public class DragonKey extends ItemExecutor {

    /**
	 *
	 */
    private DragonKey() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new DragonKey();
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
        if (!L1CastleLocation.checkInAllWarArea(pc.getLocation())) { // 检查是否在城堡区域内
            pc.sendPackets(new S_DragonGate(pc, L1DragonSlayer.getInstance()
                    .checkDragonPortal()));
        }
    }
}

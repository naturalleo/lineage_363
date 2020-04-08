package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Letter;

/**
 * 各种信纸(开封的) 49017 490189 49021 49023 49025
 */
public class Letter_open extends ItemExecutor {

    /**
	 *
	 */
    private Letter_open() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Letter_open();
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
        pc.sendPackets(new S_Letter(item));
    }
}

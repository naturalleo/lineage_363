package com.lineage.data.item_etcitem.brave;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 强化勇气的药水41415<br>
 */
public class Potion_2 extends ItemExecutor {

    /**
	 *
	 */
    private Potion_2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Potion_2();
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
        // 例外状况:物件为空
        if (item == null) {
            return;
        }
        // 例外状况:人物为空
        if (pc == null) {
            return;
        }
        // 1,447：目前暂不开放。
        pc.sendPackets(new S_ServerMessage(1447));
    }
}

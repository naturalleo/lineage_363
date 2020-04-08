package com.lineage.data.item_etcitem.mp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 精神药水40042
 */
public class Mpr4 extends ItemExecutor {

    /**
	 *
	 */
    private Mpr4() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Mpr4();
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
        if (L1BuffUtil.stopPotion(pc)) {
            // 你的 %0%s 渐渐恢复。
            pc.sendPackets(new S_ServerMessage(338, "$1084"));
            pc.setCurrentMp(pc.getCurrentMp() + 50);
            pc.getInventory().removeItem(item, 1);
        }
    }
}

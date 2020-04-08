package com.lineage.data.item_etcitem.mp;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 艾草年糕40067<br>
 * 福饼41414
 */
public class Mpr2 extends ItemExecutor {

    /**
	 *
	 */
    private Mpr2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Mpr2();
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
            final Random _random = new Random();
            // 你的 %0%s 渐渐恢复。
            pc.sendPackets(new S_ServerMessage(338, "$1084"));
            pc.setCurrentMp(pc.getCurrentMp() + (15 + _random.nextInt(16))); // 15~30
            pc.getInventory().removeItem(item, 1);
        }
    }
}

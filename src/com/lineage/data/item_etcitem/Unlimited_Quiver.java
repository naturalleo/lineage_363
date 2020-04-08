package com.lineage.data.item_etcitem;

import java.sql.Timestamp;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>无限箭筒40330</font><BR>
 * Unlimited Quiver
 * 
 * @see 24小时后，方可再度使用
 * @author dexc
 * 
 */
public class Unlimited_Quiver extends ItemExecutor {

    /**
	 *
	 */
    private Unlimited_Quiver() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Unlimited_Quiver();
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

        int item_id = 0;

        int count = 1000;

        final int k = (int) (Math.random() * 100);// 随机数字范围0~99

        switch (k) {
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
                item_id = 40746;// 米索莉箭
                break;

            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
                item_id = 40747;// 黑色米索莉箭
                break;

            case 96:
            case 97:
            case 98:
            case 99:
            case 0:
                item_id = 40748;// 奥里哈鲁根箭
                break;

            default:
                item_id = 40744;// 银箭
                count = 4000;
                break;
        }

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
        // 设置延迟使用机制
        final Timestamp ts = new Timestamp(System.currentTimeMillis());
        item.setLastUsed(ts);
        pc.getInventory().updateItem(item, L1PcInventory.COL_DELAY_EFFECT);
        pc.getInventory().saveItem(item, L1PcInventory.COL_DELAY_EFFECT);
    }
}

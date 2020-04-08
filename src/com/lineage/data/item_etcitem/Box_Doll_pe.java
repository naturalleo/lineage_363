package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法娃娃的袋子58000</font><BR>
 * 
 * @author dexc
 * 
 */
public class Box_Doll_pe extends ItemExecutor {

    /**
	 *
	 */
    private Box_Doll_pe() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Box_Doll_pe();
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
        // 删除道具
        pc.getInventory().removeItem(item, 1);

        final int k = (int) (Math.random() * 8);// 随机数字范围0~7 hjx1000

        int item_id = 0;

        switch (k) {
            case 0:
                item_id = 58011;// 魔法娃娃：肥肥
                break;

            case 1:
                item_id = 58012;// 魔法娃娃：小思克巴
                break;

            case 2:
                item_id = 58013;// 魔法娃娃：野狼宝宝
                break;

            case 3:
                item_id = 58014;// 魔法娃娃：长者
                break;

            case 4:
                item_id = 58015;// 魔法娃娃：奎斯坦修
                break;

            case 5:
                item_id = 58016;// 魔法娃娃：石头高仑
                break;

            case 6:
                item_id = 58017;// 魔法娃娃：雪怪
                break;

            case 7:
                item_id = 58018;// 魔法娃娃：亚利安
                break;

        }

        if (item_id != 0) {
            CreateNewItem.createNewItem(pc, item_id, 1);
        }
    }
}

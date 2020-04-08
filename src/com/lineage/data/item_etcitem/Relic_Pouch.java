package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>遗物袋子40415</font><BR>
 * Relic Pouch
 * 
 * @author dexc
 * 
 */
public class Relic_Pouch extends ItemExecutor {

    /**
	 *
	 */
    private Relic_Pouch() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Relic_Pouch();
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

        int count = 1;// 预设给予数量1

        final int k = (int) (Math.random() * 14);// 随机数字范围0~13

        switch (k) {
            case 1:// 烟熏的面包屑
                item_id = 40058;
                count = 4;
                break;

            case 2:// 烤焦的面包屑
                item_id = 40071;
                count = 3;
                break;

            case 3:// 红酒
                item_id = 40039;
                count = 4;
                break;

            case 4:// 威士忌
                item_id = 40040;
                count = 3;
                break;

            case 5:// 肯特战士斧头
                item_id = 40335;
                break;

            case 6:// 肯特射手之弓
                item_id = 40332;
                break;

            case 7:// 肯特勇士之剑
                item_id = 40331;
                break;

            case 8:// 肯特徽章长靴
                item_id = 40336;
                break;

            case 9:// 肯特徽章手套
                item_id = 40338;
                break;

            case 10:// 肯特刺客双刀
                item_id = 40334;
                break;

            case 11:// 肯特徽章盾牌
                item_id = 40339;
                break;

            case 12:// 肯特徽章头盔
                item_id = 40337;
                break;

            case 13:// 肯特法师魔杖
                item_id = 40333;
                break;

            default:// 肯特徽章盔甲
                item_id = 40337;
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

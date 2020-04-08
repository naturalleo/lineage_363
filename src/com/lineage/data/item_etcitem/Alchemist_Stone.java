package com.lineage.data.item_etcitem;

import java.sql.Timestamp;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>炼金术之石40414</font><BR>
 * Alchemist's Stone
 * 
 * @see 24小时后，方可再度使用
 * @author dexc
 * 
 */
public class Alchemist_Stone extends ItemExecutor {

    /**
	 *
	 */
    private Alchemist_Stone() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Alchemist_Stone();
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

        int count = 2;// 预设给予数量2

        final int k = (int) (Math.random() * 7);// 随机数字范围0~6

        switch (k) {
            case 1:// 古代终极体力恢复剂 2 个(40024)
                item_id = 40024;
                break;

            case 2:// 古代强力体力恢复剂 2 个(40023)
                item_id = 40023;
                break;

            case 3:// 古代体力恢复剂 3 个(40022)
                item_id = 40022;
                count = 3;
                break;

            case 4:// 加速魔力恢复药水 2 个(40015)
                item_id = 40015;
                break;

            case 5:// 慎重药水 2 个(40016)
                item_id = 40016;
                break;

            case 6:// 精神药水 1 个(40042)
                item_id = 40042;
                count = 1;
                break;

            default:// 精灵饼干 2 个(40068)
                item_id = 40068;
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

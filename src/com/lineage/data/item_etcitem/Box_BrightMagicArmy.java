package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>冥法军王印记盒40640</font><BR>
 * Evidence Box of Bright Magic Army
 * 
 * @author dexc
 * 
 */
public class Box_BrightMagicArmy extends ItemExecutor {

    /**
	 *
	 */
    private Box_BrightMagicArmy() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Box_BrightMagicArmy();
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

        final int k = (int) (Math.random() * 100);// 随机数字范围0~99

        switch (k) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
                item_id = 40642;// 冥法军团印记
                count = 3;
                break;

            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
                item_id = 40675;// 黑暗矿石
                count = 3;
                break;

            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
                item_id = 40703;// 心灵支配石
                break;

            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
                item_id = 20030;// 神官头饰
                break;

            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
                item_id = 20129;// 神官法袍
                break;

            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
                item_id = 20176;// 神官手套
                break;

            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
                item_id = 20208;// 神官长靴
                break;

            case 98:
                item_id = 20057;// 冥法军王斗篷
                break;

            case 99:
                item_id = 20255;// 冥法军王之戒
                break;
        }

        if (item_id != 0) {
            // 删除道具
            pc.getInventory().removeItem(item, 1);

            // 取得道具
            CreateNewItem.createNewItem(pc, item_id, count);
        }
    }
}

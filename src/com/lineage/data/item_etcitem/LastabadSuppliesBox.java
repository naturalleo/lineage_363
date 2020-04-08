package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>拉斯塔巴德补给箱41244</font><BR>
 * Lastabad Supplies Bag
 * 
 * @author dexc
 * 
 */
public class LastabadSuppliesBox extends ItemExecutor {

    /**
	 *
	 */
    private LastabadSuppliesBox() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new LastabadSuppliesBox();
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
            case 1:
                item_id = 6;// 拉斯塔巴德短剑
                break;

            case 2:
                item_id = 10;// 小武士刀
                break;

            case 3:
                item_id = 38;// 拉斯塔巴德长剑
                break;

            case 4:
                item_id = 82;// 拉斯塔巴德双刀
                break;

            case 5:
                item_id = 101;// 拉斯塔巴德矛
                break;

            case 6:
                item_id = 122;// 拉斯塔巴德魔杖
                break;

            case 7:
                item_id = 176;// 拉斯塔巴德弓
                break;

            case 8:
                item_id = 187;// 拉斯塔巴德十字弓
                break;

            case 9:
                item_id = 188;// 拉斯塔巴德重十字弓
                break;

            case 10:
                item_id = 20032;// 黑暗 头饰
                break;

            case 11:
                item_id = 20102;// 拉斯塔巴德皮盔甲
                break;

            case 12:
                item_id = 20103;// 拉斯塔巴德长袍
                break;

            case 13:
                item_id = 20104;// 拉斯塔巴德银钉皮盔甲
                break;

            case 14:
                item_id = 20105;// 拉斯塔巴德链甲
                break;

            case 15:
                item_id = 20132;// 黑暗 披肩
                break;

            case 16:
                item_id = 20199;// 拉斯塔巴德长靴
                break;

            case 17:
                item_id = 20224;// 拉斯塔巴德圆盾
                break;

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
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
                item_id = 40675;// 黑暗矿石
                break;

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
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                item_id = 40675;// 黑暗矿石
                count = 2;
                break;

            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                item_id = 40675;// 黑暗矿石
                count = 3;
                break;

            case 78:
            case 79:
            case 80:
            case 81:
                item_id = 40746;// 米索莉箭
                count = 10;
                break;

            case 82:
            case 83:
            case 84:
            case 85:
                item_id = 40746;// 米索莉箭
                count = 15;
                break;

            case 86:
            case 87:
            case 88:
            case 89:
                item_id = 40746;// 米索莉箭
                count = 20;
                break;

            case 90:
            case 91:
            case 92:
                item_id = 40746;// 米索莉箭
                count = 25;
                break;

            case 93:
            case 94:
            case 95:
                item_id = 40746;// 米索莉箭
                count = 30;
                break;

            case 96:
            case 97:
                item_id = 40746;// 米索莉箭
                count = 35;
                break;

            case 98:
            case 99:
                item_id = 40746;// 米索莉箭
                count = 40;
                break;

            default:
                item_id = 40746;// 米索莉箭
                count = 50;
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

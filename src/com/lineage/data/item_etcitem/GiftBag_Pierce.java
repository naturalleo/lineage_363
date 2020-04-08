package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>皮尔斯的礼物40715</font><BR>
 * Gift Bag from Pierce<BR>
 * 
 * @author dexc
 * 
 */
public class GiftBag_Pierce extends ItemExecutor {

    /**
	 *
	 */
    private GiftBag_Pierce() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new GiftBag_Pierce();
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
            case 5:// 青铜 钢爪 1 个
                item_id = 152;
                break;

            case 6:
            case 7:
            case 8:
            case 9:
            case 10:// 青铜 双刀 1 个
            case 93:
                item_id = 69;
                break;

            case 11:
            case 12:
            case 13:
            case 14:
            case 15:// 钢铁 钢爪 1 个
                item_id = 153;
                break;

            case 16:
            case 17:
            case 18:
            case 19:// 钢铁 双刀 1 个
                item_id = 71;
                break;

            case 21:
            case 22:
            case 23:
            case 24:// 暗影 钢爪 1 个
                item_id = 154;
                break;

            case 26:
            case 27:
            case 28:
            case 29:// 暗影 双刀 1 个
                item_id = 72;
                break;

            case 31:
            case 32:
            case 33:
            case 34:// 短 钢爪 1 个
                item_id = 159;
                break;

            case 92:
            case 36:
            case 37:
            case 38:
            case 39:// 短 双刀 1 个
                item_id = 77;
                break;

            case 67:
            case 41:
            case 42:
            case 43:
            case 44:// 大马士革 钢爪 1 个
                item_id = 161;
                break;

            case 87:
            case 46:
            case 47:
            case 48:
            case 49:// 大马士革 双刀 1 个
                item_id = 80;
                break;

            case 91:
            case 51:
            case 52:
            case 53:
            case 54:// 黑暗 钢爪 1 个
            case 88:
                item_id = 158;
                break;

            case 66:
            case 56:
            case 57:
            case 58:
            case 59:// 黑暗 双刀 1 个
                item_id = 75;
                break;

            case 86:
            case 61:
            case 62:
            case 63:
            case 64:// 黑暗 十字弓 1 个
            case 68:
                item_id = 168;
                break;

            
            
            
            case 89:// 幽暗 钢爪 1 个
                item_id = 162;
                break;

            
            
            
            case 69:// 幽暗 双刀 1 个
                item_id = 81;
                break;

            
            
            
            case 94:// 幽暗 十字弓 1 个
                item_id = 177;
                break;

            case 71:
            case 72:
            case 73:
            case 74:// 飞刀 1000 个
                item_id = 40739;
                count = 1000;
                break;

            case 76:
            case 77:
            case 78:
            case 79:// 银飞刀 1000 个
                item_id = 40738;
                count = 1000;
                break;

            case 81:
            case 82:
            case 83:
            case 84:// 重飞刀 500 个
                item_id = 40740;
                count = 500;
                break;

            case 96:
            case 97:
            case 98:
            case 99:// 黑暗 头饰 1 个
                item_id = 20032;
                break;

            case 85:
            case 80:
            case 75:
            case 95:// 黑暗 斗篷 1 个
                item_id = 20070;
                break;

            case 70:
            case 90:
            case 65:
            case 60:// 黑暗 手套 1 个
                item_id = 20180;
                break;

            case 55:
            case 50:
            case 45:
            case 40:// 黑暗 披肩 1 个
                item_id = 20132;
                break;

            default:// 黑暗长靴 1 个 20210
                item_id = 20210;
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

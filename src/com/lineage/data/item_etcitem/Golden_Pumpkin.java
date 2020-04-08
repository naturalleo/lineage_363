package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>金南瓜40722</font><BR>
 * Golden Pumpkin<BR>
 * 
 * @author dexc
 * 
 */
public class Golden_Pumpkin extends ItemExecutor {

    /**
	 *
	 */
    private Golden_Pumpkin() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Golden_Pumpkin();
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
            case 12:// 黑色血痕
                item_id = 40524;
                count = 10;
                break;

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
            case 27:// 兔子的肝
                item_id = 40043;
                count = 10;
                break;

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
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:// 精灵饼干
                item_id = 40068;
                count = 30;
                break;

            case 43:
            case 44:
            case 45:
            case 46:
            case 47:// 红酒
                item_id = 40039;
                count = 10;
                break;

            case 48:
            case 49:
            case 50:
            case 51:
            case 52:// 威士忌
                item_id = 40040;
                count = 10;
                break;

            case 53:
            case 54:
            case 55:
            case 56:
            case 57:// 恶魔之血
                item_id = 40031;
                count = 10;
                break;

            case 58:
            case 59:
            case 60:
            case 61:
            case 62:// 死神披肩
                item_id = 20342;
                break;

            case 63:
            case 64:
            case 65:
            case 66:
            case 67:// 吸血鬼斗篷
                item_id = 20079;
                break;

            case 68:
            case 69:
            case 70:
            case 71:
            case 72:// 南瓜头套
                item_id = 20047;
                break;

            case 73:
            case 74:
            case 75:
            case 76:
            case 77:// 强化 INT T恤
                item_id = 20086;
                break;

            case 78:
            case 79:
            case 80:
            case 81:
            case 82: // 强化 DEX T恤
                item_id = 20087;
                break;

            case 83:
            case 84:
            case 85:
            case 86:
            case 87:// 强化 STR T恤
                item_id = 20088;
                break;

            case 88:// 银长剑(祝福)
                item_id = 100029;
                break;

            case 89:// 大马士革刀(祝福)
                item_id = 100037;
                break;

            case 90:// 细剑(祝福)
                item_id = 100042;
                break;

            case 91:// 武官之刃(祝福)
                item_id = 100049;
                break;

            case 92:// 银光双刀(祝福)
                item_id = 100074;
                break;

            case 93:// 灭魔戒指(祝福)
                item_id = 120280;
                break;

            case 94:// 光明身体腰带(祝福)
                item_id = 120309;
                break;

            case 95:// 光明灵魂腰带(祝福)
                item_id = 120310;
                break;

            case 96:// 光明精神腰带(祝福)
                item_id = 120311;
                break;

            case 97:// 欧吉皮带(祝福)
                item_id = 120317;
                break;

            case 98:// 泰坦皮带(祝福)
                item_id = 120320;
                break;

            default:// 多罗皮带(祝福)
                item_id = 120321;
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

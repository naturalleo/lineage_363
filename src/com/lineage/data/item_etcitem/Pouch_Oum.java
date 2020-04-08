package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>欧姆的袋子40687</font><BR>
 * Pouch of Oum
 * 
 * @author dexc
 * 
 */
public class Pouch_Oum extends ItemExecutor {

    /**
	 *
	 */
    private Pouch_Oum() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Pouch_Oum();
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

        final int k = (int) (Math.random() * 100);// 随机数字范围0~99

        int item_id = 0;
        final int count = 1;

        switch (k) {
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
                item_id = 40691;// 半成品的雕像
                break;

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
                item_id = 40689;// 未精雕的雕像
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
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
                item_id = 40690;// 未修补的雕像
                break;

            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
                item_id = 40685;// 未磨光的雕像
                break;

            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
                item_id = 40688;// 未上漆的雕像
                break;

            default:
                item_id = 40686;// 完成品的雕像
                break;
        }

        if (item_id != 0) {
            // 取得道具
            CreateNewItem.createNewItem(pc, item_id, count);
        }
    }
}

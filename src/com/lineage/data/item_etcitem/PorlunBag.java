package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>波伦的袋子41355</font><BR>
 * 
 * @author dexc
 * 
 */
public class PorlunBag extends ItemExecutor {

    /**
	 *
	 */
    private PorlunBag() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new PorlunBag();
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

        final int count = 1;// 预设给予数量1

        final int k = (int) (Math.random() * 100);// 随机数字范围0~99

        switch (k) {
            case 1:
                item_id = 140074;// 对盔甲施法的卷轴(祝福)
                break;

            case 2:
                item_id = 140087;// 对武器施法的卷轴(祝福)
                break;

            case 3:
            case 93:
            case 94:
            case 95:
            case 98:
                item_id = 40074;// 对盔甲施法的卷轴
                break;

            case 4:
            case 88:
            case 89:
            case 96:
            case 97:
                item_id = 40087;// 对武器施法的卷轴
                break;

            case 5:
            case 17:
                item_id = 40441;// 白金原石
                break;

            case 6:
            case 90:
            case 9:
                item_id = 40458;// 光明的鳞片
                break;

            case 7:
            case 91:
                item_id = 40444;// 黑色米索莉原石
                break;

            case 8:
            case 92:
                item_id = 40508;// 奥里哈鲁根
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
                item_id = 40410;// 黑暗安特的树皮
                break;

            case 10:
                item_id = 40412;// 黑暗安特的树枝
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
                item_id = 40024;// 古代终极体力恢复剂
                break;

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
                item_id = 40023;// 古代强力体力恢复剂
                break;

            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
                item_id = 40022;// 古代体力恢复剂
                break;

            case 14:
            case 11:
                item_id = 40304;// 马普勒之石
                break;

            case 15:
            case 12:
                item_id = 40305;// 帕格里奥之石
                break;

            case 16:
            case 13:
                item_id = 40306;// 伊娃之石
                break;

            default:
                item_id = 40307;// 沙哈之石
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

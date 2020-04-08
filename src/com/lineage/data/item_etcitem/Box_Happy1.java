package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>惊喜箱</font><BR>
 * 最后修正 hjx1000
 * 
 * @author dexc
 * 
 */
public class Box_Happy1 extends ItemExecutor {

    /**
	 *
	 */
    private Box_Happy1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Box_Happy1();
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
        // 删除道具
        pc.getInventory().removeItem(item, 1);
        final int k = (int) (Math.random() * 100);// 随机数字范围0~99

        switch (k) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                item_id = 40014;// 勇敢药水
                count = 10;
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                item_id = 40031;// 恶魔之血
                count = 10;
                break;
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                item_id = 40068;// 精灵饼干
                count = 10;
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
                item_id = 40861;// 魔法卷轴(保护罩)
                count = 10;
            	break;
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                item_id = 40867;// 魔法卷轴(解毒术)
                count = 10;
            	break;
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
                item_id = 40872;// 魔法卷轴(负重强化)
                count = 10;
            	break;
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
                item_id = 40879;// 魔法卷轴(铠甲护持)
                count = 10;
            	break;
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                item_id = 40884;// 魔法卷轴(通畅气脉术)
                count = 10;
            	break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
                item_id = 59001;// 魔法卷轴(体魄强健术)
                count = 10;
            	break;
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                item_id = 59002;// 魔法卷轴(祝福魔法武器)
                count = 10;
            	break;
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
                item_id = 59003;// 魔法卷轴(体力回复术)
                count = 10;
            	break;
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                item_id = 59004;// 魔法卷轴(神圣疾走)
                count = 10;
            	break;
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
                item_id = 59005;// 魔法卷轴(强力加速术)
                count = 10;
            	break;
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
                item_id = 59006;// 魔法卷轴(全部治愈术)
                count = 10;
            	break;
            case 88:
            case 89:
            case 90:
            	item_id = 41251;// 骷髅圣杯
            	break;
            case 91:
            case 92:
            case 93:
            	item_id = 41252;// 珍奇的乌龟
            	break;
            case 94:
            case 95:
            case 96:
            	item_id = 41253;// 王宫料理师的调味料
            	break;
            case 97:
            case 98:
            case 99:
            	item_id = 41254;// 胜利的徽章
            	break;
        }

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

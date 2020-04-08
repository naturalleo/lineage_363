package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>卡立普的最高级袋子(高品蓝)54004</font><BR>
 * High Quality Pouch of Karif (HQS)
 * 
 * @author dexc
 * 
 */
public class Caliph_Pouch_SHQS extends ItemExecutor {

    /**
	 *
	 */
    private Caliph_Pouch_SHQS() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Caliph_Pouch_SHQS();
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
    	case 2:
    	case 3:
    	case 4:
    	case 5:
    	case 6:
    	case 7:
    	case 8:
    		item_id = 40019;//	浓缩体力恢复剂
    		count = 5;
    		break;
    	case 9:
    	case 10:
    	case 11:
    	case 12:
    	case 13:
    	case 14:
    	case 15:
    	case 16:
    		item_id = 40020;//	浓缩强力体力恢复剂
    		count = 5;
    		break;
    	case 17:
    	case 18:
    	case 19:
    	case 20:
    	case 21:
    	case 22:
    	case 23:
    	case 24:
    		item_id = 40021;//	浓缩终极体力恢复剂
    		count = 5;
    		break;
    	case 25:
    	case 26:
    	case 27:
    	case 28:
    	case 29:
    	case 30:
    	case 31:
    	case 32:
    		item_id = 40458;//	光明的鳞片
        	break;
    	case 33:
    	case 34:
    	case 35:
    	case 36:
    	case 37:
    	case 38:
    	case 39:
    	case 40:
    		item_id = 40320;//	一级黑魔石
    		break;
    	case 41:
    	case 42:
    	case 43:
    	case 44:
    	case 45:
    	case 46:
    	case 47:
    	case 48:
    		item_id = 40468;//	银原石
    		break;
    	case 49:
    	case 50:
    	case 51:
    	case 52:
    	case 53:
    	case 54:
    	case 55:
    	case 56:
    		item_id = 40489;//	黄金原石
    		break;
    	case 57:
    	case 58:
    	case 59:
    	case 60:
    	case 61:
    	case 62:
    	case 63:
    	case 64:
    		item_id = 40441;//	白金原石
    		break;
    	case 65:
    	case 66:
    	case 67:
    	case 68:
    	case 69:
    	case 70:
    	case 71:
    	case 72:
    		item_id = 40444;//	黑色米索莉原石
    		break;
    	case 73:
    	case 74:
    	case 75:
    	case 76:
    	case 77:
    	case 78:
    	case 79:
    	case 80:
    		item_id = 40307;//	沙哈之石
    		break;
    	case 81:
    	case 82:
    	case 83:
    	case 84:
    	case 85:
    	case 86:
    	case 87:
    	case 88:
    		item_id = 40460;//阿西塔基奥的灰烬
    		break;
    	case 89:
    	case 90:
    	case 91:
    	case 92:
    	case 93:
    	case 94:
    		item_id = 40513;// 食人巨魔的血
    		break;
        default:
            item_id = 40394;//	风龙鳞
            break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>德鲁嘉袋子</font><BR>
 * 最后修正 hjx1000
 * 
 * @author dexc
 * 
 */
public class Box_Delujia extends ItemExecutor {

    /**
	 *
	 */
    private Box_Delujia() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Box_Delujia();
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
                item_id = 40020;// 浓缩强力体力恢复剂
                count = 30;
                break;
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
                item_id = 40021;// 浓缩终极体力恢复剂
                count = 20;
                break;
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
                item_id = 40023;// 古代强力体力恢复剂
                count = 30;
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
                item_id = 40024;// 古代终极体力恢复剂
                count = 20;
            	break;
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
                item_id = 40015;// 加速魔力回复药水
                count = 10;
            	break;
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                item_id = 40499;// 蘑菇汁
                count = 20;
            	break;
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
                item_id = 41263;// 太阳花籽
                count = 20;
            	break;
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
                item_id = 40506;// 安特的水果
                count = 20;
            	break;
            case 67:
            case 68:
            case 69:            	
            case 70:
            case 71:
                item_id = 40524;// 黑色血痕
                count = 2;
            	break;
            case 72:
            case 73:
            case 74:
                item_id = 47010;// 龙之钥匙
            	break;
            case 75:
            case 76:
                item_id = 26;// 小侏儒短剑
            	break;
            case 77:
            case 78:
                item_id = 144;// 侏儒铁斧
            	break;
            case 79:
            case 80:
                item_id = 20007; //侏儒铁盔
            	break;
            case 81:
            case 82:
            	item_id = 20052;//	侏儒斗篷
            	break;
            case 83:
            case 84:
            	item_id = 20223;//	侏儒圆盾
            	break;
            case 85:
            case 86:
            	item_id = 40393;//	火龙鳞
            	break;
            case 87:
            case 88:
            	item_id = 40394;//	风龙鳞
            	break;
            case 89:
            case 90:
            	item_id = 40395;//	水龙鳞
            	break;
            case 91:
            case 92:
            	item_id = 40396;//	地龙鳞
            	break;
            case 93:
            case 94:
            	item_id = 40513;//	食人巨魔的血
            	break;
            case 95:
            	item_id = 41352;//	神圣独角兽之角
            	break;
            case 96:
            	item_id = 47029;//	地龙之渴望的眼泪
            	break;
            case 97:
            	item_id = 47030;//	火龙之渴望的眼泪
            	break;
            case 98:
            	item_id = 47031;//	水龙之渴望的眼泪
            	break;
            case 99:
            	item_id = 47032;//	风龙之渴望的眼泪
            	break;
        }

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>铜南瓜40724</font><BR>
 * Bronze Pumpkin
 * 
 * @author dexc
 * 
 */
public class Bronze_Pumpkin extends ItemExecutor {

    /**
	 *
	 */
    private Bronze_Pumpkin() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Bronze_Pumpkin();
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

        final short k = (short) (Math.random() * 100);// 随机数字范围0~99

        switch (k) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:// 苹果汁
                item_id = 40028;
                count = 60;
                break;

            case 5:
            case 6:
            case 7:
            case 8:
            case 9:// 烟熏的面包屑
                item_id = 40058;
                count = 3;
                break;

            case 10:
            case 11:
            case 12:
            case 13:
            case 14:// 烤焦的面包屑
                item_id = 40071;
                break;

            case 15:
            case 16:
            case 17:
            case 18:
            case 19:// 强化自我加速药水
                item_id = 40018;
                break;

            case 20:
            case 21:
            case 22:
            case 23:
            case 24:// 变形卷轴
                item_id = 40088;
                count = 3;
                break;

            case 25:
            case 26:
            case 27:
            case 28:
            case 29:// 加速魔力恢复药水
                item_id = 40015;
                count = 5;
                break;

            case 30:
            case 31:
            case 32:
            case 33:
            case 34:// 魔法宝石
                item_id = 40318;
                count = 20;
                break;

            case 35:
            case 36:
            case 37:
            case 38:
            case 39:// 勇敢药水
                item_id = 40014;
                count = 10;
                break;

            case 40:
            case 41:
            case 42:
            case 43:
            case 44:// 精灵饼干
                item_id = 40068;
                break;

            case 45:
            case 46:
            case 47:
            case 48:
            case 49:// 复活卷轴
                item_id = 40089;
                count = 10;
                break;

            case 50:
            case 51:
            case 52:
            case 53:
            case 54:// 兔子的肝
                item_id = 40043;
                break;

            case 55:
            case 56:
            case 57:
            case 58:
            case 59:// 柯利的项链
                item_id = 20345;
                break;

            case 60:
            case 61:
            case 62:
            case 63:
            case 64:// 浣熊的项链
                item_id = 20346;
                break;

            case 65:
            case 66:
            case 67:
            case 68:
            case 69:// 猎犬项链
                item_id = 20361;
                break;

            case 70:
            case 71:
            case 72:
            case 73:
            case 74:// 柠檬/祝福
                item_id = 140061;
                break;

            case 75:
            case 76:
            case 77:
            case 78:
            case 79:// 香蕉/祝福
                item_id = 140062;
                break;

            case 80:
            case 81:
            case 82:
            case 83:
            case 84:// 糖果/祝福
                item_id = 140065;
                break;

            case 85:
            case 86:
            case 87:
            case 88:
            case 89:// 橘子/祝福
                item_id = 140069;
                break;

            case 90:
            case 91:
            case 92:
            case 93:
            case 94:// 烤薄饼/祝福
                item_id = 140072;
                break;

            default:// 安特的水果/祝福
                item_id = 140506;
                break;
        }

        // 删除道具
        pc.getInventory().removeItem(item, 1);

        // 取得道具
        CreateNewItem.createNewItem(pc, item_id, count);
    }
}

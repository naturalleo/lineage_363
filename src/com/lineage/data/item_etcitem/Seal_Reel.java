package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/***
 * 封印卷轴41426
 */
public class Seal_Reel extends ItemExecutor {

    /**
	 *
	 */
    private Seal_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Seal_Reel();
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
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }
        final int lockItemId = item1.getItem().getItemId();
        if (((item1 != null) && (item1.getItem().getType2() == 1))
                || (item1.getItem().getType2() == 2)
                || ((item1.getItem().getType2() == 0) && ((lockItemId == 40314)
                        || (lockItemId == 40316) || (lockItemId == 41248)
                        || (lockItemId == 41249) || (lockItemId == 41250)
                        || (lockItemId == 49037) || (lockItemId == 49038) || (lockItemId == 49039)))) {

            if ((item1.getBless() == 0) || (item1.getBless() == 1)
                    || (item1.getBless() == 2) || (item1.getBless() == 3)) {
                int bless = 1;
                switch (item1.getBless()) {
                    case 0:// 祝福
                        bless = 128;
                        break;
                    case 1:// 一般
                        bless = 129;
                        break;
                    case 2:// 诅咒
                        bless = 130;
                        break;
                    case 3:
                        bless = 131;
                        break;
                }
                item1.setBless(bless);
                pc.getInventory().updateItem(item1, L1PcInventory.COL_BLESS);
                pc.getInventory().saveItem(item1, L1PcInventory.COL_BLESS);
                pc.getInventory().removeItem(item, 1);

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。

        }
    }

}

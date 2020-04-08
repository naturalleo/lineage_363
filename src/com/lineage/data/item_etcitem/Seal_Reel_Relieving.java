package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/***
 * 封印解除卷轴41427
 */
public class Seal_Reel_Relieving extends ItemExecutor {

    /**
	 *
	 */
    private Seal_Reel_Relieving() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Seal_Reel_Relieving();
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
            if ((item1.getBless() == 128) || (item1.getBless() == 129)
                    || (item1.getBless() == 130) || (item1.getBless() == 131)) {
                int bless = 1;
                switch (item1.getBless()) {
                    case 128:
                        bless = 0;
                        break;
                    case 129:
                        bless = 1;
                        break;
                    case 130:
                        bless = 2;
                        break;
                    case 131:
                        bless = 3;
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

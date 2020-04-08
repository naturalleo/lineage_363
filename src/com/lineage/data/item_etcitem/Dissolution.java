package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ResolventTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 溶解剂41245
 */
public class Dissolution extends ItemExecutor {

    /**
	 *
	 */
    private Dissolution() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Dissolution();
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
        this.useResolvent(pc, item1, item);
    }

    private void useResolvent(final L1PcInstance pc, final L1ItemInstance item,
            final L1ItemInstance resolvent) {
        final Random _random = new Random();
        if ((item == null) || (resolvent == null)) {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            return;
        }
        if ((item.getItem().getType2() == 1)
                || (item.getItem().getType2() == 2)) { // 武器?防具
            if (item.getEnchantLevel() != 0) { // 强化完毕
                pc.sendPackets(new S_ServerMessage(1161)); // 无法溶解。
                return;
            }
            if (item.isEquipped()) { // 装备中
                pc.sendPackets(new S_ServerMessage(1161)); // 无法溶解。
                return;
            }
        }
        long crystalCount = ResolventTable.get().getCrystalCount(
                item.getItem().getItemId());
        if (crystalCount == 0) {
            pc.sendPackets(new S_ServerMessage(1161)); // 无法溶解。
            return;
        }
        final int rnd = _random.nextInt(100) + 1;
        if ((rnd >= 1) && (rnd <= 20)) { //修改失败机率为 20% hjx1000
            crystalCount = 0;
            pc.sendPackets(new S_ServerMessage(158, item.getName())); // \f1%0%s
            // 消失。
        } else if ((rnd >= 26) && (rnd <= 81)) {//修改失败机率为 30% hjx1000
            crystalCount *= 1;
        } else if ((rnd >= 91) && (rnd <= 100)) {
            crystalCount *= 1.5;
            //pc.getInventory().storeItem(41246, (long) (crystalCount * 1.5));
        }
        if (crystalCount != 0) {
            CreateNewItem.createNewItem(pc, 41246, crystalCount);
        }
        pc.getInventory().removeItem(item, 1);
        pc.getInventory().removeItem(resolvent, 1);
    }
}

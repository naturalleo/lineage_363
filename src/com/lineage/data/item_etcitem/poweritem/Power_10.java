package com.lineage.data.item_etcitem.poweritem;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.PowerItemSet;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ItemPower_name;

/**
 * 56066 防御(防御-2)
 * 
 * @author dexc
 * 
 */
public class Power_10 extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Power_10.class);

    private static final Random _random = new Random();

    /**
	 *
	 */
    private Power_10() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Power_10();
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
        try {
            // 对象OBJID
            final int targObjId = data[0];

            // 目标物品
            final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

            if (tgItem == null) {
                return;
            }
            if (tgItem.get_power_name() == null) {
                pc.sendPackets(new S_ServerMessage("\\fT这个物品没有凹槽!"));
                return;
            }
            if (tgItem.getItem().getType2() != 2) {// 修正 YiWei thatmystyle (UID:
                                                   // 3602)
                pc.sendPackets(new S_ServerMessage("\\fT这只能使用在防具上"));
                return;
            }
            if (tgItem.isEquipped()) {
                pc.sendPackets(new S_ServerMessage("\\fR你必须先解除物品装备!"));
                return;
            }
            int random = PowerItemSet.HOLER;
            if (pc.isGm()) {
                random = 1000;
            }
            final L1ItemPower_name power = tgItem.get_power_name();
            if (power.get_hole_1() == 0 && power.get_hole_count() >= 1) {
                pc.getInventory().removeItem(item, 1);
                if (_random.nextInt(1000) > random) {
                    pc.sendPackets(new S_ServerMessage("\\fT凹槽置入魔法物品失败!"));
                    return;
                }
                power.set_hole_1(10);
                pc.sendPackets(new S_ItemStatus(tgItem, null));
                pc.sendPackets(new S_ItemName(tgItem));
                CharItemPowerReading.get().updateItem(tgItem.getId(),
                        tgItem.get_power_name());

            } else if (power.get_hole_2() == 0 && power.get_hole_count() >= 2) {
                pc.getInventory().removeItem(item, 1);
                if (_random.nextInt(1000) > random) {
                    pc.sendPackets(new S_ServerMessage("\\fT凹槽置入魔法物品失败!"));
                    return;
                }
                power.set_hole_2(10);
                pc.sendPackets(new S_ItemStatus(tgItem, null));
                pc.sendPackets(new S_ItemName(tgItem));
                CharItemPowerReading.get().updateItem(tgItem.getId(),
                        tgItem.get_power_name());

            } else if (power.get_hole_3() == 0 && power.get_hole_count() >= 3) {
                pc.getInventory().removeItem(item, 1);
                if (_random.nextInt(1000) > random) {
                    pc.sendPackets(new S_ServerMessage("\\fT凹槽置入魔法物品失败!"));
                    return;
                }
                power.set_hole_3(10);
                pc.sendPackets(new S_ItemStatus(tgItem, null));
                pc.sendPackets(new S_ItemName(tgItem));
                CharItemPowerReading.get().updateItem(tgItem.getId(),
                        tgItem.get_power_name());

            } else if (power.get_hole_4() == 0 && power.get_hole_count() >= 4) {
                pc.getInventory().removeItem(item, 1);
                if (_random.nextInt(1000) > random) {
                    pc.sendPackets(new S_ServerMessage("\\fT凹槽置入魔法物品失败!"));
                    return;
                }
                power.set_hole_4(10);
                pc.sendPackets(new S_ItemStatus(tgItem, null));
                pc.sendPackets(new S_ItemName(tgItem));
                CharItemPowerReading.get().updateItem(tgItem.getId(),
                        tgItem.get_power_name());

            } else if (power.get_hole_5() == 0 && power.get_hole_count() >= 5) {
                pc.getInventory().removeItem(item, 1);
                if (_random.nextInt(1000) > random) {
                    pc.sendPackets(new S_ServerMessage("\\fT凹槽置入魔法物品失败!"));
                    return;
                }
                power.set_hole_5(10);
                pc.sendPackets(new S_ItemStatus(tgItem, null));
                pc.sendPackets(new S_ItemName(tgItem));
                CharItemPowerReading.get().updateItem(tgItem.getId(),
                        tgItem.get_power_name());

            } else {
                pc.sendPackets(new S_ServerMessage("\\fT这个物品没有足够凹槽!"));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

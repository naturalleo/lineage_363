package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1ItemPower_name;
/**
 * 武器致命魔法卷轴lv5 55115<br>
 */
public class Deadly_Lv5 extends ItemExecutor {

    /**
	 *
	 */
    private Deadly_Lv5() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Deadly_Lv5();
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
        // 对象OBJID
        final int targObjId = data[0];

        final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

        if (tgItem == null) {
            return;
        }
        
		if (tgItem.isEquipped()) {//如果装备中不能使用星防卷冲
			pc.sendPackets(new S_SystemMessage("你必须先解除该武器!"));
			return;
		}

        final int safe_enchant = tgItem.getItem().get_safeenchant();
        final L1ItemPower_name power_name = tgItem.get_power_name();
        // 物品已追加值
        final int enchant_level = tgItem.getEnchantLevel();
       
        boolean isErr = false;
        if (power_name == null) {
        	pc.sendPackets(new S_SystemMessage("这个卷轴只可以强化Lv4的武器。"));
        	return;
        }
        if (power_name.get_xing_count() != 4) {
        	pc.sendPackets(new S_SystemMessage("这个卷轴只可以强化Lv4的武器。"));
        	return;
        }
        // 取得物件触发事件
        final int use_type = tgItem.getItem().getUseType();
        switch (use_type) {
            case 1:// 武器
                if (safe_enchant < 0) { // 物品不可强化
                    isErr = true;
                }
                break;

            default:
                isErr = true;
                break;
        }
        final int weaponId = tgItem.getItem().getItemId();
        if ((weaponId >= 246) && (weaponId <= 255)) { // 物品不可强化
            isErr = true;
        }

        if (tgItem.getBless() >= 128) {// 封印的装备
            isErr = true;
        }

        if (isErr) {
            pc.sendPackets(new S_ServerMessage(79));// 没有任何事发生
            return;
        }
        if (safe_enchant == 0 && enchant_level < 8) {
        	pc.sendPackets(new S_SystemMessage("你的武器必须要+8以上才可以使用这个卷轴。"));
        	return;
        }
        if (safe_enchant == 6 && enchant_level < 10) {
        	pc.sendPackets(new S_SystemMessage("你的武器必须要+10以上才可以使用这个卷轴。"));
        	return;
        }
        final Random random = new Random();
        pc.getInventory().removeItem(item, 1);                 					
        tgItem.get_power_name().set_hole_3(random.nextInt(6) + 1);
		tgItem.get_power_name().set_xing_count(5);
		CharItemPowerReading.get().updateItem(tgItem.getId(), tgItem.get_power_name());
		pc.getInventory().updateItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
		pc.getInventory().saveItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
    }
}

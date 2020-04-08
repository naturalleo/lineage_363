package com.lineage.data.item_etcitem.reel;

import java.util.Random;

import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.cmd.EnchantWeapon;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ItemPower_name;

/**
 * <font color=#00800>强烈的武卷</font><BR>
 * 41219
 */
public class ScrollEnchantWeaponA extends ItemExecutor {

    /**
	 *
	 */
    private ScrollEnchantWeaponA() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ScrollEnchantWeaponA();
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

        final int safe_enchant = tgItem.getItem().get_safeenchant();
        final L1ItemPower_name power_name = tgItem.get_power_name();
        boolean isErr = false;

        // 取得物件触发事件
        final int use_type = tgItem.getItem().getUseType();
        switch (use_type) {
            case 1:// 武器
                if (safe_enchant < 0) { // 物品不可强化
                    isErr = true;
                }
//    			if (power_name != null && tgItem.get_power_name().get_xing_count() > 0) { //不可强化星武器hjx1000
//    				isErr = true;
//    			}
                break;

            default:
                isErr = true;
                break;
        }

        final int weaponId = tgItem.getItem().getItemId();
        if ((weaponId >= 246) && (weaponId <= 255)
        		|| (weaponId >= 312) && (weaponId <= 318)) { // 物品不可强化
            isErr = true;
        }

        if (tgItem.getBless() >= 128) {// 封印的装备
            isErr = true;
        }

        if (isErr) {
            pc.sendPackets(new S_ServerMessage(79));// 没有任何事发生
            return;
        }

        // 物品已追加值
        final int enchant_level = tgItem.getEnchantLevel();

        // 限定 最高加到20
        if (enchant_level >= 20) {
            pc.sendPackets(new S_ServerMessage(79));// 没有任何事发生
            return;
        }

        final EnchantExecutor enchantExecutor = new EnchantWeapon();
        pc.getInventory().removeItem(item, 1);

        boolean isEnchant = false;
        final Random random = new Random();
        int rand = 1000;
        if (enchant_level > -6 && enchant_level <= 5) {
            rand = 1000;

        } else if (enchant_level >= 6 && enchant_level <= 9) {
            rand = 250;

        } else if (enchant_level >= 10 && enchant_level <= 11) {
            rand = 50;

        } else if (enchant_level >= 12 && enchant_level <= 13) {
            rand = 10;

        } else if (enchant_level >= 14 && enchant_level <= 15) {
            rand = 5;

        } else if (enchant_level == 16) {
            rand = 2;

        } else if (enchant_level == 17) {
            rand = 2;

        } else if (enchant_level >= 18) {
            rand = 1;
        }

        if (random.nextInt(1050) < rand) {
            isEnchant = true;
        }

        if (isEnchant) {// 成功
            enchantExecutor.successEnchant(pc, tgItem, 1);

        } else {// 失败
            enchantExecutor.successEnchant(pc, tgItem, 0);
        }
    }
}

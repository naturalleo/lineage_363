package com.lineage.data.item_etcitem.reel;

import java.util.Random;

import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.cmd.EnchantWeapon;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1ItemUpdata;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 对武器施法的幻象卷轴40127<br>
 */
public class ScrollEnchantWeaponIllusion extends ItemExecutor {

    /**
	 *
	 */
    private ScrollEnchantWeaponIllusion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ScrollEnchantWeaponIllusion();
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

        // 安定值
        final int safe_enchant = tgItem.getItem().get_safeenchant();

        boolean isErr = false;

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
        final EnchantExecutor enchantExecutor = new EnchantWeapon();
        int randomELevel = enchantExecutor
                .randomELevel(tgItem, item.getBless());
        pc.getInventory().removeItem(item, 1);

        boolean isEnchant = true;
        if (enchant_level < -6) {// 武器将会消失,最大可追加到-7
            isEnchant = false;

        } else if (enchant_level < safe_enchant) {// 安定值内
            isEnchant = true;

        } else {

            final Random random = new Random();
            final int rnd = random.nextInt(100) + 1;
            int enchant_chance_wepon;
            int enchant_level_tmp;

            if (safe_enchant == 0) { // 对武器安定直为0初始计算+6
                enchant_level_tmp = enchant_level + 6;

            } else {
                enchant_level_tmp = enchant_level;
            }

            if (enchant_level >= 9) {
                enchant_chance_wepon = (int) L1ItemUpdata
                        .enchant_wepon_up9(enchant_level_tmp);

            } else {
                enchant_chance_wepon = (int) L1ItemUpdata
                        .enchant_wepon_dn9(enchant_level_tmp);
            }

            if (rnd < enchant_chance_wepon) {
                isEnchant = true;

            } else {
                if ((enchant_level >= 9) && (rnd < (enchant_chance_wepon * 2))) {
                    randomELevel = 0;

                } else {
                    isEnchant = false;
                }
            }
        }
        if ((randomELevel <= 0) && (enchant_level > -6)) {
            isEnchant = true;
        }

        if (isEnchant) {// 成功
            enchantExecutor.successEnchant(pc, tgItem, randomELevel);

        } else {// 失败
            enchantExecutor.failureEnchant(pc, tgItem);
        }
    }
}

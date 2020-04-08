package com.lineage.data.item_etcitem.reel;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 44118 暗之武器强化卷轴
 * 
 * @author loli
 * 
 */
public class ScrollEnchantS2Weapon extends ItemExecutor {

    /**
	 *
	 */
    private ScrollEnchantS2Weapon() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ScrollEnchantS2Weapon();
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

        // 目标物品
        final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

        if (tgItem == null) {
            return;
        }
        // 0:无属性 1:地 2:火 4:水 8:风 16:光 32:暗 64:圣 128:邪
        int oldAttrEnchantKind = tgItem.getAttrEnchantKind();
        int oldAttrEnchantLevel = tgItem.getAttrEnchantLevel();
        final int newAttrEnchantKind = 32;// 32:暗
        boolean isErr = false;

        // 取得物件触发事件
        final int use_type = tgItem.getItem().getUseType();
        switch (use_type) {
            case 1:// 武器
                   // 相同属性强化直大于3
                if (oldAttrEnchantKind == newAttrEnchantKind) {
                    if (oldAttrEnchantLevel >= 3) {
                        isErr = true;
                    }
                }
                break;

            default:
                isErr = true;
                break;
        }

        if (tgItem.getBless() >= 128) {// 封印的装备
            isErr = true;
        }

        if (isErr) {
            pc.sendPackets(new S_ServerMessage(79)); // 没有发生任何事情。
            return;
        }

        pc.getInventory().removeItem(item, 1);

        final Random random = new Random();
        final int rnd = random.nextInt(100) + 1;

        if (ConfigRate.ATTR_ENCHANT_CHANCE >= rnd) {
            // 1410 对\f1%0附加强大的魔法力量成功。
            pc.sendPackets(new S_ServerMessage(1410, tgItem.getLogName()));

            int newAttrEnchantLevel = oldAttrEnchantLevel + 1;

            if (oldAttrEnchantKind != newAttrEnchantKind) {
                newAttrEnchantLevel = 1;
            }

            tgItem.setAttrEnchantKind(newAttrEnchantKind);
            pc.getInventory().updateItem(tgItem,
                    L1PcInventory.COL_ATTR_ENCHANT_KIND);
            pc.getInventory().saveItem(tgItem,
                    L1PcInventory.COL_ATTR_ENCHANT_KIND);

            tgItem.setAttrEnchantLevel(newAttrEnchantLevel);
            pc.getInventory().updateItem(tgItem,
                    L1PcInventory.COL_ATTR_ENCHANT_LEVEL);
            pc.getInventory().saveItem(tgItem,
                    L1PcInventory.COL_ATTR_ENCHANT_LEVEL);

        } else {
            // 1411 对\f1%0附加魔法失败。
            pc.sendPackets(new S_ServerMessage(1411, tgItem.getLogName()));
        }
    }
}

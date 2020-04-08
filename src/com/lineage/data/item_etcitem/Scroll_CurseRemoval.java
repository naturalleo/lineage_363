package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;

/**
 * 象牙塔解咒卷轴40097<br>
 * 解除咀咒的卷轴40119<br>
 * 解除咀咒的卷轴(祝福)140119<br>
 * 原住民图腾 (祝福)140329<br>
 */
public class Scroll_CurseRemoval extends ItemExecutor {

    /**
	 *
	 */
    private Scroll_CurseRemoval() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Scroll_CurseRemoval();
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
    	//增加武器修理卷轴 add hjx1000
    	if (item.getItemId() == 58007) {
    		final L1ItemInstance weapon = pc.getWeapon();
    		if (weapon != null && weapon.get_durability() > 0) {
    			String msg0;
    			weapon.set_durability(0);
    			msg0 = weapon.getLogName();
    			pc.sendPackets(new S_ServerMessage(464, msg0));
                pc.getInventory().updateItem(weapon);
    	        pc.getInventory().removeItem(item, 1);
    		}
    		return;
    	}
    	//增加武器修理卷轴end
        for (final L1ItemInstance tgItem : pc.getInventory().getItems()) {
            // 非诅咒物品
            if (tgItem.getBless() != 2) {
                continue;
            }

            // 非封印装备
            if (tgItem.getBless() >= 128) {
                continue;
            }

            // 对盔甲施法的卷轴
            if (tgItem.getItemId() == 240074) {
                if (item.getBless() != 0) {
                    continue;
                }
            }

            // 对武器施法的卷轴
            if (tgItem.getItemId() == 240087) {
                if (item.getBless() != 0) {
                    continue;
                }
            }

            // 受诅咒的 幻化石
            if (tgItem.getItemId() == 41216) {
                continue;
            }

            final int id_normal = tgItem.getItemId() - 200000;
            final L1Item template = ItemTable.get().getTemplate(id_normal);

            if (template == null) {
                continue;
            }

            boolean isEun = false;
            // 身上具有该解除诅咒后物品
            if (pc.getInventory().checkItem(id_normal)) {
                // 物品可以堆叠
                if (template.isStackable()) {
                    // 删除身上原有物件
                    pc.getInventory().removeItem(tgItem, tgItem.getCount());
                    // 给予新物件
                    pc.getInventory().storeItem(id_normal, tgItem.getCount());

                } else {
                    isEun = true;
                }

                // 身上不具备该物品
            } else {
                isEun = true;
            }

            if (isEun) {
                // System.out.println("isEun");
                tgItem.setBless(1);
                tgItem.setItem(template);
                pc.getInventory().updateItem(tgItem,
                        L1PcInventory.COL_ITEMID + L1PcInventory.COL_BLESS);
                pc.getInventory().saveItem(tgItem,
                        L1PcInventory.COL_ITEMID + L1PcInventory.COL_BLESS);
            }
        }
        pc.getInventory().removeItem(item, 1);
        pc.sendPackets(new S_ServerMessage(155)); // \f1你感觉到似乎有人正在帮助你。
    }
}

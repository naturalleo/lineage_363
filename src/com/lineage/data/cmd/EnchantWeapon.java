package com.lineage.data.cmd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRecord;
import com.lineage.server.datatables.lock.LogEnchantReading;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class EnchantWeapon extends EnchantExecutor {

    private static final Log _log = LogFactory.getLog(EnchantWeapon.class);

    /**
     * 强化纪录(失败)
     * 
     * @param pc
     *            执行者
     * @param item
     *            对象物件
     */
    @Override
    public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item) {
        final StringBuilder s = new StringBuilder();

        if (ConfigRecord.LOGGING_BAN_ENCHANT) {
            LogEnchantReading.get().failureEnchant(pc, item);
        }

        // 未鉴定
        if (!item.isIdentified()) {
            s.append(item.getName());

        } else {
            s.append(item.getLogName());
        }
        // 164 \f1%0%s 产生激烈的 %1 光芒，一会儿后就消失了。
        pc.sendPackets(new S_ServerMessage(164, s.toString(), "$252"));
        pc.getInventory().removeItem(item, item.getCount());
        _log.info("人物:" + pc.getName() + "点爆物品(武器)" + item.getNumberedName_to_String()
                + " 物品OBJID:" + item.getId());
    }

    /**
     * 强化成功
     * 
     * @param pc
     *            执行者
     * @param item
     *            对象物件
     * @param i
     *            强化质
     */
    @Override
    public void successEnchant(final L1PcInstance pc,
            final L1ItemInstance item, final int i) {
        final StringBuilder s = new StringBuilder();
        final StringBuilder sa = new StringBuilder();
        final StringBuilder sb = new StringBuilder();

        // 未鉴定
        if (!item.isIdentified()) {
            s.append(item.getName());

        } else {
            s.append(item.getLogName());
        }

        switch (i) {
            case 0:
                // \f1%0%s %2 产生激烈的 %1 光芒，但是没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(160, s.toString(), "$252",
                        "$248"));
                return;

            case -1:
                sa.append("$246");// 黑色的
                sb.append("$247");// 一瞬间发出
                break;

            case 1: // '\001'
                sa.append("$245");// 蓝色的
                sb.append("$247");// 一瞬间发出
                break;

            case 2: // '\002'
            case 3: // '\003'
                sa.append("$245");// 蓝色的
                sb.append("$248");// 持续发出
                break;
    		case 4: //上星 hjx1000
    			return;
        }

        // 161 \f1%0%s %2 %1 光芒。
        pc.sendPackets(new S_ServerMessage(161, s.toString(), sa.toString(), sb
                .toString()));

        final int oldEnchantLvl = item.getEnchantLevel();
        final int newEnchantLvl = oldEnchantLvl + i;
        if (oldEnchantLvl != newEnchantLvl) {
            if (newEnchantLvl >= 9) {// 强化值等于或超过9
                // 1,652：强化
                // 产生讯息封包 (强化成功)
                World.get()
                        .broadcastPacketToAll(
                                new S_HelpMessage(pc.getName(), s.toString()
                                        + " " + sb.toString() + " "
                                        + sa.toString() + " $251"));
            }
            item.setEnchantLevel(newEnchantLvl);
            pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
            pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
        }
    }
}

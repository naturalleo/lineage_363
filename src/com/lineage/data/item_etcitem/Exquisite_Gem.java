package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 精工宝石 49031~49042
 */
public class Exquisite_Gem extends ItemExecutor {

    /**
	 *
	 */
    private Exquisite_Gem() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Exquisite_Gem();
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
        final int itemId = item.getItemId();
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }
        final Random _random = new Random();

        // 对各种耳环进行加工41161~41172
        final int earingId = item1.getItem().getItemId();
        int earinglevel = 0;
        if ((earingId >= 41161) && (41172 >= earingId)) {
            if (earingId == (itemId + 230)) {// 宝石与耳环一一对应
                if ((_random.nextInt(99) + 1) < ConfigRate.CREATE_CHANCE_PROCESSING) {
                    if (earingId == 41161) {
                        earinglevel = 21014;
                    } else if (earingId == 41162) {
                        earinglevel = 21006;
                    } else if (earingId == 41163) {
                        earinglevel = 21007;
                    } else if (earingId == 41164) {
                        earinglevel = 21015;
                    } else if (earingId == 41165) {
                        earinglevel = 21009;
                    } else if (earingId == 41166) {
                        earinglevel = 21008;
                    } else if (earingId == 41167) {
                        earinglevel = 21016;
                    } else if (earingId == 41168) {
                        earinglevel = 21012;
                    } else if (earingId == 41169) {
                        earinglevel = 21010;
                    } else if (earingId == 41170) {
                        earinglevel = 21017;
                    } else if (earingId == 41171) {
                        earinglevel = 21013;
                    } else if (earingId == 41172) {
                        earinglevel = 21011;
                    }
                    CreateNewItem.createNewItem(pc, earinglevel, 1);

                } else {
                    pc.sendPackets(new S_ServerMessage(158, item1.getName()));
                    // \f1%0%s 消失。。
                }
                pc.getInventory().removeItem(item1, 1);
                pc.getInventory().removeItem(item, 1);
            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
        }
    }
}

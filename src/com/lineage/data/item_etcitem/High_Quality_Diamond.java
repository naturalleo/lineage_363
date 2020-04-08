package com.lineage.data.item_etcitem;

import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 精工的土之钻40943<br>
 * 精工的品质土之钻40944<br>
 * 精工的高品质土之钻40945<br>
 * 精工的极品土之钻40946<br>
 * 精工的水之钻40947<br>
 * 精工的品质水之钻40948<br>
 * 精工的高品质水之钻40949<br>
 * 精工的极品水之钻40950<br>
 * 精工的火之钻40951<br>
 * 精工的品质火之钻40952<br>
 * 精工的高品质火之钻40953<br>
 * 精工的极品火之钻40954<br>
 * 精工的风之钻40955<br>
 * 精工的品质风之钻40956<br>
 * 精工的高品质风之钻40957<br>
 * 精工的极品风之钻40958<br>
 */
public class High_Quality_Diamond extends ItemExecutor {

    /**
	 *
	 */
    private High_Quality_Diamond() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new High_Quality_Diamond();
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
        if ((itemId >= 40943) && (40958 >= itemId)) {
            // 与各种戒指相对应
            final int ringId = item1.getItem().getItemId();
            int ringlevel = 0;
            int gmas = 0;
            int gmam = 0;
            if ((ringId >= 41185) && (41200 >= ringId)) {
                if ((itemId == 40943) || (itemId == 40947) || (itemId == 40951)
                        || (itemId == 40955)) {
                    gmas = 443;
                    gmam = 447;
                } else if ((itemId == 40944) || (itemId == 40948)
                        || (itemId == 40952) || (itemId == 40956)) {
                    gmas = 442;
                    gmam = 446;
                } else if ((itemId == 40945) || (itemId == 40949)
                        || (itemId == 40953) || (itemId == 40957)) {
                    gmas = 441;
                    gmam = 445;
                } else if ((itemId == 40946) || (itemId == 40950)
                        || (itemId == 40954) || (itemId == 40958)) {
                    gmas = 444;
                    gmam = 448;
                }
                if (ringId == (itemId + 242)) {
                    if ((_random.nextInt(99) + 1) < ConfigRate.CREATE_CHANCE_PROCESSING_DIAMOND) {
                        if (ringId == 41185) {
                            ringlevel = 20435;
                        } else if (ringId == 41186) {
                            ringlevel = 20436;
                        } else if (ringId == 41187) {
                            ringlevel = 20437;
                        } else if (ringId == 41188) {
                            ringlevel = 20438;
                        } else if (ringId == 41189) {
                            ringlevel = 20439;
                        } else if (ringId == 41190) {
                            ringlevel = 20440;
                        } else if (ringId == 41191) {
                            ringlevel = 20441;
                        } else if (ringId == 41192) {
                            ringlevel = 20442;
                        } else if (ringId == 41193) {
                            ringlevel = 20443;
                        } else if (ringId == 41194) {
                            ringlevel = 20444;
                        } else if (ringId == 41195) {
                            ringlevel = 20445;
                        } else if (ringId == 41196) {
                            ringlevel = 20446;
                        } else if (ringId == 41197) {
                            ringlevel = 20447;
                        } else if (ringId == 41198) {
                            ringlevel = 20448;
                        } else if (ringId == 41199) {
                            ringlevel = 20449;
                        } else if (ringId == 41200) {
                            ringlevel = 20450;
                        }
                        pc.sendPackets(new S_ServerMessage(gmas, item1
                                .getName()));
                        CreateNewItem.createNewItem(pc, ringlevel, 1);
                        pc.getInventory().removeItem(item1, 1);
                        pc.getInventory().removeItem(item, 1);
                    } else {
                        pc.sendPackets(new S_ServerMessage(gmam, item.getName()));
                        pc.getInventory().removeItem(item, 1);
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                }
            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }
        }
    }
}

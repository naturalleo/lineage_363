package com.lineage.data.item_etcitem.reel;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 毁灭盔甲的卷轴40075
 */
public class Destroys_Helmet_Reel extends ItemExecutor {

    private static final Log _log = LogFactory
            .getLog(Destroys_Helmet_Reel.class);

    private static final Random _random = new Random();

    /**
	 *
	 */
    private Destroys_Helmet_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Destroys_Helmet_Reel();
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
            final int itemobj = data[0];
            final L1ItemInstance tgItem = pc.getInventory().getItem(itemobj);

            if (tgItem.getBless() >= 128) {// 封印的装备
                // 79：没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            int getItemId_1 = 0;
            int getItemId_2 = 0;
            // 毁灭盔甲的卷轴40075
            if (tgItem.getItem().getType2() == 2) {
                switch (tgItem.getItem().getItemId()) {
                    case 20137:// 精灵链甲
                    case 120137:// 精灵链甲
                        getItemId_1 = 26001;// 精灵链甲上半部
                        getItemId_2 = 26003;// 护法链甲护裙
                        break;

                    case 26000:// 精灵银盔甲
                        getItemId_1 = 26004;// 精灵银盔甲上半部
                        getItemId_2 = 26006;// 将军银盔甲护裙
                        break;

                    case 20138:// 精灵金属盔甲
                        getItemId_1 = 26007;// 精灵金属盔甲上半部
                        getItemId_2 = 26009;// 元帅金属盔甲护裙
                        break;
                }

                // 移除毁灭盔甲的卷轴
                pc.getInventory().removeItem(item, 1);

                if (getItemId_1 == 0) {
                    // 154：\f1这个卷轴散开了。
                    pc.sendPackets(new S_ServerMessage(154));

                } else {
                    int random = _random.nextInt(1000);
                    if (random >= 980 && random < 1000) {
                        // 3101：\f1%0 受到了毁灭盔甲的卷轴的冲击，连续发出灿烂的光芒之后裂开了。
                        pc.sendPackets(new S_ServerMessage(3101, tgItem
                                .getLogName()));

                        // 移除强化道具
                        pc.getInventory().removeItem(tgItem, tgItem.getCount());

                        // 取得道具
                        CreateNewItem.createNewItem(pc, getItemId_1, 1);
                        CreateNewItem.createNewItem(pc, getItemId_2, 1);

                    } else if (random > 750 && random < 980) {
                        // 169：\f1你的盔甲变成尘埃落地。
                        pc.sendPackets(new S_ServerMessage(169));

                        // 移除强化道具
                        pc.getInventory().removeItem(tgItem, tgItem.getCount());

                    } else {
                        // 3100：\f1%0 受到了毁灭盔甲的卷轴的冲击，一阵强烈的震动后没有任何事情发生。
                        pc.sendPackets(new S_ServerMessage(3100, tgItem
                                .getLogName()));
                    }
                }

            } else {
                // 79：没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
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
    public void execute2(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        final int itemobj = data[0];
        final L1ItemInstance item1 = pc.getInventory().getItem(itemobj);
        if (item1 == null) {
            return;
        }

        if (item1.getBless() >= 128) {// 封印的装备
            // 79：没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }

        // 毁灭盔甲的卷轴40075
        if (item1.getItem().getType2() == 2) {
            int msg = 0;
            switch (item1.getItem().getType()) {
                case 1: // helm
                    msg = 171; // \f1你的钢盔如爆炸般地破碎了。
                    break;
                case 2: // armor
                    msg = 169; // \f1你的盔甲变成尘埃落地。
                    break;
                case 3: // T
                    msg = 170; // \f1你的T恤破碎成线四散。
                    break;
                case 4: // cloak
                    msg = 168; // \f1你的斗蓬破碎化为尘埃。
                    break;
                case 5: // glove
                    msg = 172; // \f1你的手套消失。
                    break;
                case 6: // boots
                    msg = 173; // \f1你的长靴分解。
                    break;
                case 7: // shield
                    msg = 174; // \f1你的盾崩溃分散。
                    break;
                default:
                    msg = 167; // \f1你的皮肤痒。
                    break;
            }
            pc.sendPackets(new S_ServerMessage(msg));
            pc.getInventory().removeItem(item1, 1);

        } else {
            pc.sendPackets(new S_ServerMessage(154)); // \f1这个卷轴散开了。
        }

        pc.getInventory().removeItem(item, 1);

    }

}

package com.lineage.data.item_etcitem.quest;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 49337 最高级魔法气息
 */
public class MagicReel_M03 extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(MagicReel_M03.class);

    /**
	 *
	 */
    private MagicReel_M03() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_M03();
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
            int itemobj = data[0];
            final L1ItemInstance tgitem = pc.getInventory().getItem(itemobj);
            // 对象为道具
            if (tgitem.getItem().getType2() == 0) {
                int itemid = -1;
                switch (tgitem.getItemId()) {
                    case 49317:// 封印的近距离攻击符石
                        itemid = 30421;
                        break;
                    case 49318:// 封印的远距离攻击符石
                        itemid = 30423;
                        break;
                    case 49319:// 封印的魔法攻击符石
                        itemid = 30425;
                        break;
                    case 49320:// 封印的近距离命中率符石
                        itemid = 30422;
                        break;
                    case 49321:// 封印的远距离命中率符石
                        itemid = 30424;
                        break;
                    case 49322:// 封印的魔法命中率符石
                        itemid = 30426;
                        break;
                    case 49323:// 封印的火之防御符石
                        itemid = 30429;
                        break;
                    case 49324:// 封印的风之防御符石
                        itemid = 30430;
                        break;
                    case 49325:// 封印的水之防御符石
                        itemid = 30428;
                        break;
                    case 49326:// 封印的土之防御符石
                        itemid = 30427;
                        break;
                    case 49327:// 封印的剑(追加打击)
                        itemid = 30471;
                        break;
                    case 49328:// 封印的剑(攻击成功)
                        itemid = 30472;
                        break;
                    case 49329:// 封印的弓(追加打击)
                        itemid = 30473;
                        break;
                    case 49330:// 封印的弓(命中)
                        itemid = 30474;
                        break;
                    case 49331:// 封印的魔杖(魔攻)
                        itemid = 30475;
                        break;
                    case 49332:// 封印的魔杖(魔法命中)
                        itemid = 30476;
                        break;
                    case 49333:// 封印的哥布林盔甲
                        itemid = 30477;
                        break;
                    case 49334:// 封印的一只鞋
                        itemid = 30478;
                        break;

                    default:
                        pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                        break;
                }
                if (itemid != -1) {
                    pc.getInventory().removeItem(item, 1);// 删除道具
                    pc.getInventory().removeItem(tgitem, 1);// 删除道具

                    L1ItemInstance newitem = ItemTable.get().createItem(itemid);
                    long time = System.currentTimeMillis();// 目前时间豪秒
                    long x1 = 10800;// 指定耗用秒数(3H)
                    long x2 = x1 * 1000;// 转为豪秒
                    long upTime = x2 + time;// 目前时间 加上指定耗用秒数

                    // 时间数据
                    final Timestamp ts = new Timestamp(upTime);
                    newitem.set_time(ts);

                    // 人物背包物品使用期限资料
                    CharItemsTimeReading.get().addTime(newitem.getId(), ts);

                    CreateNewItem.createNewItem(pc, newitem, 1);
                }

            } else {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

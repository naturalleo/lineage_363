package com.lineage.data.item_etcitem.shop;

import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemPowerUpdateTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ItemPowerUpdate;

/**
 * 自订物品进化卷轴[需实装至DB内]
 * 
 * # 范例: UPDATE `etcitem` SET `classname`='shop.Power_Up_01',`use_type`='choice'
 * WHERE `item_id`='60175';#定置物品进化卷轴
 */
public class Power_Up_01 extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Power_Up_01.class);

    private static final Random _random = new Random();

    /**
	 *
	 */
    private Power_Up_01() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Power_Up_01();
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
            // 对象OBJID
            final int targObjId = data[0];

            final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

            if (tgItem == null) {
                return;
            }

            // 取回物件属性
            final L1ItemPowerUpdate info = ItemPowerUpdateTable.get().get(
                    tgItem.getItemId());
            if (info == null) {
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            if (info.get_mode() == 4) {// 不能再强化
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            if (info.get_nedid() != item.getItemId()) {
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            // 同组物品清单
            final Map<Integer, L1ItemPowerUpdate> tmplist = ItemPowerUpdateTable
                    .get().get_type_id(tgItem.getItemId());
            if (tmplist.isEmpty()) {
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            final int order_id = info.get_order_id();// 排序
            final L1ItemPowerUpdate tginfo = tmplist.get(order_id + 1);// 取回下一个排序资料
            if (tginfo == null) {
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }

            // 删除卷轴
            pc.getInventory().removeItem(item, 1);

            if (_random.nextInt(1000) <= tginfo.get_random()) {
                // 强化成功
                pc.getInventory().removeItem(tgItem, 1);

                // CreateNewItem.createNewItem(pc, tginfo.get_itemid(), 1);

                // 产生新物件
                final L1ItemInstance tginfo_item = ItemTable.get().createItem(
                        tginfo.get_itemid());
                if (tginfo_item != null) {
                    tginfo_item.setCount(1);
                    pc.getInventory().storeItem(tginfo_item);
                    pc.sendPackets(new S_ServerMessage("\\fT"
                            + tgItem.getName() + " 崩裂后获得展新的 "
                            + tginfo_item.getName()));
                    return;

                } else {
                    _log.error("给予物件失败 原因: 指定编号物品不存在(" + tginfo.get_itemid()
                            + ")");
                    return;
                }
            } else {
                // 强化失败
                switch (info.get_mode()) {// 目前物品失败时处理类型
                    case 0:
                        // \f1%0%s %2 产生激烈的 %1 光芒，但是没有任何事情发生。
                        pc.sendPackets(new S_ServerMessage(160, tgItem
                                .getLogName(), "$252", "$248"));
                        break;

                    case 1:// 1:强化失败会倒退
                        final L1ItemPowerUpdate ole1 = tmplist
                                .get(order_id - 1);// 取回上一个排序资料
                        pc.sendPackets(new S_ServerMessage("\\fR"
                                + tgItem.getName() + "升级失败!"));
                        pc.getInventory().removeItem(tgItem, 1);
                        CreateNewItem.createNewItem(pc, ole1.get_itemid(), 1);
                        break;

                    case 2:// 2:强化失败会消失
                           // 164 \f1%0%s 产生激烈的 %1 光芒，一会儿后就消失了。银色的
                        pc.sendPackets(new S_ServerMessage(164, tgItem
                                .getLogName(), "$252"));
                        pc.getInventory().removeItem(tgItem, 1);
                        break;

                    case 3:// 强化失败会倒退 或 强化失败会消失
                        if (_random.nextBoolean()) {// 强化失败会倒退
                            final L1ItemPowerUpdate ole2 = tmplist
                                    .get(order_id - 1);// 取回上一个排序资料
                            pc.getInventory().removeItem(tgItem, 1);
                            CreateNewItem.createNewItem(pc, ole2.get_itemid(),
                                    1);

                        } else {// 强化失败会消失
                            // 164 \f1%0%s 产生激烈的 %1 光芒，一会儿后就消失了。银色的
                            pc.sendPackets(new S_ServerMessage(164, tgItem
                                    .getLogName(), "$252"));
                            pc.getInventory().removeItem(tgItem, 1);
                        }
                        break;
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

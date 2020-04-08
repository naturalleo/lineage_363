package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 保箱钥匙
 * 
 * 
 * DELETE FROM `etcitem` WHERE `item_id`>='56069' AND `item_id`<='56071'; INSERT
 * INTO `etcitem` VALUES ('56069', '保箱钥匙', 'shop.Power_Key', '保箱钥匙', 'other',
 * 'choice', 'mineral', '0', '1021', '3963', '0', '1', '0', '0', '0', '0', '0',
 * '1', '0', '0', '0', '0', '0', '0', '0'); INSERT INTO `etcitem` VALUES
 * ('56070', '保箱钥匙', 'shop.Power_Key', '保箱钥匙', 'other', 'choice', 'mineral',
 * '0', '1023', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0', '0', '0',
 * '0', '0', '0', '0');
 * 
 * INSERT INTO `etcitem` VALUES ('56071', '测试之袋', '0', '测试之袋', 'treasure_box',
 * 'normal', 'none', '10000', '957', '3963', '0', '1', '0', '0', '0', '0', '0',
 * '1', '1', '0', '0', '0', '0', '0', '0');
 * 
 * 
 * SET FOREIGN_KEY_CHECKS=0; -- ---------------------------- -- 建立资料表
 * `etcitem_box_key` -- ---------------------------- DROP TABLE IF EXISTS
 * `etcitem_box_key`; CREATE TABLE `etcitem_box_key` ( `id` int(11) NOT NULL
 * AUTO_INCREMENT, `key_itemid` int(10) NOT NULL DEFAULT '0' COMMENT
 * '需要使用的开启物件编号', `box_item_id` int(10) NOT NULL DEFAULT '0' COMMENT '物品编号',
 * `get_item_id` int(10) NOT NULL, `name` varchar(100) DEFAULT NULL COMMENT
 * '物品名称', `randomint` int(10) NOT NULL DEFAULT '1000000' COMMENT '比对用机率',
 * `random` int(10) NOT NULL DEFAULT '1000' COMMENT '机率', `min_count` int(10)
 * NOT NULL DEFAULT '1' COMMENT '给予数量(最少)', `max_count` int(10) NOT NULL DEFAULT
 * '1' COMMENT '给予数量(最多)', `out` tinyint(1) NOT NULL DEFAULT '0' COMMENT '公告',
 * PRIMARY KEY (`id`) ) ENGINE=MyISAM AUTO_INCREMENT=1694 DEFAULT CHARSET=utf8;
 * 
 * -- ---------------------------- -- 建立资料表 etcitem_box_key 范例内容 --
 * ---------------------------- INSERT INTO `etcitem_box_key` VALUES ('385',
 * '56070', '56071', '40053', '高品质红宝石', '1000000', '60000', '1', '1', '0');
 * INSERT INTO `etcitem_box_key` VALUES ('386', '56070', '56071', '40054',
 * '高品质蓝宝石', '1000000', '60000', '1', '1', '0'); INSERT INTO `etcitem_box_key`
 * VALUES ('387', '56070', '56071', '40055', '高品质绿宝石', '1000000', '60000', '1',
 * '1', '0'); INSERT INTO `etcitem_box_key` VALUES ('407', '56069', '56071',
 * '40044', '钻石', '1000000', '130000', '3', '3', '0'); INSERT INTO
 * `etcitem_box_key` VALUES ('408', '56069', '56071', '40045', '红宝石', '1000000',
 * '130000', '3', '3', '0'); INSERT INTO `etcitem_box_key` VALUES ('409',
 * '56069', '56071', '40046', '蓝宝石', '1000000', '130000', '3', '3', '0'); INSERT
 * INTO `etcitem_box_key` VALUES ('410', '56069', '56071', '40047', '绿宝石',
 * '1000000', '130000', '3', '3', '0'); INSERT INTO `etcitem_box_key` VALUES
 * ('411', '56069', '56071', '40048', '品质钻石', '1000000', '80000', '1', '1',
 * '0'); INSERT INTO `etcitem_box_key` VALUES ('412', '56069', '56071', '40049',
 * '品质红宝石', '1000000', '80000', '1', '1', '0'); INSERT INTO `etcitem_box_key`
 * VALUES ('413', '56069', '56071', '40050', '品质蓝宝石', '1000000', '80000', '1',
 * '1', '0'); INSERT INTO `etcitem_box_key` VALUES ('414', '56069', '56071',
 * '40051', '品质绿宝石', '1000000', '80000', '1', '1', '0');
 * 
 * 
 */
public class Power_Key extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Power_Key.class);

    /**
	 *
	 */
    private Power_Key() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Power_Key();
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
            final L1ItemInstance tgitem = pc.getInventory().getItem(itemobj);
            if (tgitem == null) {
                return;
            }

            final int itemid = data[0];
            if (tgitem.getItem().getType() == 16) { // treasure_box
                if (ItemBoxTable.get().is_key(tgitem.getItemId(), itemid)) {
                    if (pc.getInventory().removeItem(item, 1) != 1) {
                        return;
                    }
                    ItemBoxTable.get().get_key(pc, tgitem, itemid);
                } else {
                    // 79 没有任何事情发生
                    pc.sendPackets(new S_ServerMessage(79));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

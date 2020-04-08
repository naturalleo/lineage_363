package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 恶汉药水 49450
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='49449'; DELETE FROM `etcitem` WHERE
 * `item_id`='49450'; INSERT INTO `etcitem` VALUES ('49449', '英雄药水',
 * 'shop.UserColorU', '英雄药水', 'questitem', 'normal', 'none', '0', '3743',
 * '3963', '0', '1', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0',
 * '0'); INSERT INTO `etcitem` VALUES ('49450', '恶汉药水', 'shop.UserColorD',
 * '恶汉药水', 'questitem', 'normal', 'none', '0', '3744', '3963', '0', '1', '0',
 * '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0');
 * 
 * @author dexc
 * 
 */
public class UserColorD extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(UserColorD.class);

    /**
	 *
	 */
    private UserColorD() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new UserColorD();
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
        int lawful = pc.getLawful();
        if (lawful >= 0) {// 修正sorrowrose (UID: 3204)
            // 移除道具
            pc.getInventory().removeItem(item, 1);
            // 更新正义质
            pc.setLawful(-32768);

            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));

            try {
                pc.save();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

        } else {
            // 没有任何事情发生。
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}

package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 经验兑换卷44156 $1499：经验值 $1487：魔法卷轴 DELETE FROM `etcitem` WHERE
 * `item_id`='44156'; INSERT INTO `etcitem` VALUES (44156, '经验值 魔法卷轴',
 * 'shop.Clan_Honor_Reel', '$1499 $1487', 'scroll', 'normal', 'paper', 0, 3069,
 * 3963, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1);
 */
public class Clan_Honor_Reel extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Clan_Honor_Reel.class);

    /**
	 *
	 */
    private Clan_Honor_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Clan_Honor_Reel();
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
        // 例外状况:物件为空
        if (item == null) {
            return;
        }
        // 例外状况:人物为空
        if (pc == null) {
            return;
        }

        pc.addExp(578754802);//65级 hjx1000
        //pc.setExp(1209893906);//82级。。
        pc.getInventory().removeItem(item, 1);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 9714));

        try {
            pc.save();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

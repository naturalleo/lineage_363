package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 49537 封号卡
 * 
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='49537'; INSERT INTO `etcitem` VALUES
 * ('49537', '封号卡', 'shop.UserTitle', '封号卡', 'other', 'normal', 'paper', '0',
 * '1863', '3963', '0', '1', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0',
 * '0', '0', '0');
 * 
 * @author daien
 * 
 */
public class UserTitle extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(UserTitle.class);

    /**
	 *
	 */
    private UserTitle() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new UserTitle();
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
        if (item == null) { // 例外状况:物件为空
            return;
        }

        if (pc == null) { // 例外状况:人物为空
            return;
        }

        if (pc.isGhost()) { // 鬼魂模式
            return;
        }

        if (pc.isDead()) { // 死亡
            return;
        }

        if (pc.isTeleport()) { // 传送中
            return;
        }

        if (!ConfigOther.CLANTITLE) {
            pc.sendPackets(new S_ServerMessage("\\fT尚未开放"));
            return;
        }

        if (pc.isPrivateShop()) {// 商店村模式
            pc.sendPackets(new S_ServerMessage("\\fT请先结束商店村模式!"));
            return;
        }

        try {
            pc.retitle(true);
            pc.sendPackets(new S_ServerMessage("\\fR输入封号后直接按下ENTER"));

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

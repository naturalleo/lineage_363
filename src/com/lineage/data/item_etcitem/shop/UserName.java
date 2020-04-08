package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 变名药水 41227
 * 
 * DELETE FROM `etcitem` WHERE `item_id`='41227'; INSERT INTO `etcitem` VALUES
 * ('41227', '名称药水', 'shop.UserName', '名称药水', 'questitem', 'normal', 'none',
 * '0', '3597', '3963', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0',
 * '0', '0', '0', '1');
 * 
 * @author dexc
 * 
 */
public class UserName extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(UserName.class);

    /**
	 *
	 */
    private UserName() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new UserName();
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

        if (pc.isPrivateShop()) {// 商店村模式
            pc.sendPackets(new S_ServerMessage("\\fT请先结束商店村模式!"));
            return;
        }

        final Object[] petList = pc.getPetList().values().toArray();
        if (petList.length > 0) {
            pc.sendPackets(new S_ServerMessage("\\fT请先回收宠物!"));
            return;
        }

        if (!pc.getDolls().isEmpty()) {
            pc.sendPackets(new S_ServerMessage("\\fT请先回收魔法娃娃!"));
            return;
        }

        if (pc.getParty() != null) {
            pc.sendPackets(new S_ServerMessage("\\fT请先退出队伍!"));
            return;
        }

        if (pc.getClanid() != 0) {
            pc.sendPackets(new S_ServerMessage("\\fT请先退出血盟!"));
            return;
        }
        try {
            // 名称
            pc.sendPackets(new S_Message_YN(325));
            pc.rename(true);

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

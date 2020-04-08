package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 经验卡59008 $1499：经验值 $1487：魔法卷轴 DELETE FROM `etcitem` WHERE
 * `item_id`='59008'; INSERT INTO `etcitem` VALUES (44156, '经验卡',
 * 'shop.Clan_Honor_Reel', '$1499 $1487', 'scroll', 'normal', 'paper', 0, 3069,
 * 3963, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1);
 */
public class AddExp_Car extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(AddExp_Car.class);

    /**
	 *
	 */
    private AddExp_Car() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new AddExp_Car();
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
            // 例外状况:物件为空
            if (item == null) {
                return;
            }
            // 例外状况:人物为空
            if (pc == null) {
                return;
            }     
            
            if (pc.getLevel() >= ExpTable.MAX_LEVEL) {// 已达最大等级终止计算
                return;
            }
            
            if (pc.getInventory().consumeItem(40308, 7500)) {
                double addExp = 1500; //经验值增加1500

                // 目前等级可获取的经验值
                final double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
                // 目前等级可获取的经验值
                if (exppenalty < 1D) {
                    addExp *= exppenalty;
                }

                // 服务器经验加倍
                if (ConfigRate.RATE_XP > 1.0) {
                    addExp *= ConfigRate.RATE_XP;
                }
                
                pc.addExp((long) addExp);
                pc.getInventory().removeItem(item, 1);
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 9714));
            } else {
                // 189 \f1金币不足。
                pc.sendPackets(new S_ServerMessage(189));
            }
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

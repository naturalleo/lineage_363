package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;

/**
 * 44004 金色能量石 HP提高500点 MP提高50点
 */
public class Up_hm1 extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Up_hm1.class);

    /**
	 *
	 */
    private Up_hm1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Up_hm1();
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
        final short addhp = 500;
        final short addmp = 50;

        if ((addhp != 0) && (addmp != 0)) {
            pc.getInventory().removeItem(item, 1);

            pc.get_other().set_addmp(pc.get_other().get_addmp() + addmp);
            pc.get_other().set_addhp(pc.get_other().get_addhp() + addhp);

            pc.addMaxHp(addhp);
            pc.setCurrentHpDirect(pc.getMaxHp());

            pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
            if (pc.isInParty()) { // 队伍中
                pc.getParty().updateMiniHP(pc);
            }

            pc.addMaxMp(addmp);
            pc.setCurrentHpDirect(pc.getMaxMp());

            pc.sendPackets(new S_MPUpdate(pc));

            try {
                pc.save();

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}

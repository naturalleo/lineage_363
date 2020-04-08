package com.lineage.data.item_etcitem.power;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 万能药(精神)40037<br>
 * 
 * @author dexc
 * 
 */
public class PanaceaWis extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(PanaceaWis.class);

    /**
	 *
	 */
    private PanaceaWis() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new PanaceaWis();
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
        if (pc.getBaseWis() < ConfigAlt.POWERMEDICINE) {
            if (pc.getElixirStats() < ConfigAlt.MEDICINE) {
                pc.addBaseWis((byte) 1); // WIS+1
                pc.resetBaseMr();
                pc.setElixirStats(pc.getElixirStats() + 1);
                pc.getInventory().removeItem(item, 1);
                pc.sendPackets(new S_OwnCharStatus2(pc));
                try {
                    pc.save();
                } catch (final Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
            } else {
                // 79：\f1没有任何事情发生。
                pc.sendPackets(new S_ServerMessage(79));
            }

        } else {
            // \f1属性最大值只能到35。
            pc.sendPackets(new S_ServerMessage(481));
        }
    }
}

package com.lineage.data.item_armor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;

/**
 * 增加经验的装备 add hjx1000
 */
public class Increase_Exp extends ItemExecutor {

    private static final Log _log = LogFactory
            .getLog(Increase_Exp.class);

    /**
	 *
	 */
    private Increase_Exp() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Increase_Exp();
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

            switch (data[0]) {
                case 0:// 解除装备
                    pc.set_expadd(-_exp);
                    pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 0, false));
                    break;

                case 1:// 装备
                	pc.set_expadd(_exp);
                	pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 0, true));
                    break;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
    private int _exp = 0;
    
    @Override
    public void set_set(String[] set) {
        try {
            _exp = Integer.parseInt(set[1]);
        } catch (Exception e) {
        }
    }
}

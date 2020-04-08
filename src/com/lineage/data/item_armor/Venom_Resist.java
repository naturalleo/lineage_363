package com.lineage.data.item_armor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 20117 巴风特盔甲 20298 洁尼斯戒指
 */
public class Venom_Resist extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(Venom_Resist.class);

    /**
	 *
	 */
    private Venom_Resist() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Venom_Resist();
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
                    pc.set_venom_resist(-1);
                    break;

                case 1:// 装备
                    pc.set_venom_resist(1);
                    break;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}

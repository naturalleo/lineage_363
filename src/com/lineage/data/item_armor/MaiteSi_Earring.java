package com.lineage.data.item_armor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * MaiteSi_Earring 麦特斯的蓝光耳环 参数: 药水恢复增加比率(1/100)
 */
public class MaiteSi_Earring extends ItemExecutor {

    private static final Log _log = LogFactory.getLog(MaiteSi_Earring.class);

    /**
	 *
	 */
    private MaiteSi_Earring() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MaiteSi_Earring();
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
                    pc.set_up_hp_potion(pc.get_up_hp_potion() - _up_hp_potion);
                    break;

                case 1:// 装备
                    pc.set_up_hp_potion(pc.get_up_hp_potion() + _up_hp_potion);
                    break;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _up_hp_potion = 0;

    @Override
    public void set_set(String[] set) {
        try {
            _up_hp_potion = Integer.parseInt(set[1]);

        } catch (Exception e) {
        }
    }
}

package com.lineage.data.item_armor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 30324 法利昂的力量 30325 法利昂的魅惑 30326 法利昂的泉源 30327 法利昂的霸气
 */
public class ElitePlateMail_Fafurion extends ItemExecutor {

    private static final Log _log = LogFactory
            .getLog(ElitePlateMail_Fafurion.class);

    /**
	 *
	 */
    private ElitePlateMail_Fafurion() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ElitePlateMail_Fafurion();
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
                    pc.set_elitePlateMail_Fafurion(0, 0, 0);
                    break;

                case 1:// 装备
                    pc.set_elitePlateMail_Fafurion(_r, _hp_min, _hp_max);
                    break;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private int _r = 0;
    private int _hp_min = 0;
    private int _hp_max = 0;

    @Override
    public void set_set(String[] set) {
        try {
            _r = Integer.parseInt(set[1]);
        } catch (Exception e) {
        }
        try {
            _hp_min = Integer.parseInt(set[2]);
        } catch (Exception e) {
        }
        try {
            _hp_max = Integer.parseInt(set[3]);
        } catch (Exception e) {
        }
    }
}

package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:给予物品<BR>
 * 给予物品 参数：TYPE1(时间:秒) TYPE2(给予物品编号) TYPE3(给予的数量)
 * 
 * @author dexc
 * 
 */
public class Doll_GetItem extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_GetItem.class);

    private int _int1;// 值1

    private int _int2;// 值2

    private int _int3;// 值3

    /**
     * 娃娃效果:给予物品
     */
    public Doll_GetItem() {
    }

    public static L1DollExecutor get() {
        return new Doll_GetItem();
    }

    @Override
    public void set_power(int int1, int int2, int int3) {
        try {
            _int1 = int1;
            _int2 = int2;
            _int3 = int3;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setDoll(L1PcInstance pc) {
        try {
            pc.set_doll_get(_int2, _int3);
            pc.set_doll_get_time_src(_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.set_doll_get(0, 0);
            pc.set_doll_get_time_src(0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

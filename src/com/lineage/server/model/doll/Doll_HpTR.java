package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:HP回复相关(指定时间)<BR>
 * HP回复提高 参数：TYPE1(提高质) TYPE2(时间:秒)
 * 
 * @author dexc
 * 
 */
public class Doll_HpTR extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_HpTR.class);

    private int _int1;// 值1

    private int _int2;// 值2

    /**
     * 娃娃效果:HP增加(指定时间)
     */
    public Doll_HpTR() {
    }

    public static L1DollExecutor get() {
        return new Doll_HpTR();
    }

    @Override
    public void set_power(int int1, int int2, int int3) {
        try {
            _int1 = int1;
            _int2 = int2;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setDoll(L1PcInstance pc) {
        try {
            pc.set_doll_hpr(_int1);
            pc.set_doll_hpr_time_src(_int2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.set_doll_hpr(0);
            pc.set_doll_hpr_time_src(0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

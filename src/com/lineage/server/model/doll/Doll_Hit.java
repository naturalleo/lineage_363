package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:攻击成功相关<BR>
 * 攻击成功提高 参数：TYPE1(攻击成功/命中)
 * 
 * @author dexc
 * 
 */
public class Doll_Hit extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Hit.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:攻击成功增加
     */
    public Doll_Hit() {
    }

    public static L1DollExecutor get() {
        return new Doll_Hit();
    }

    @Override
    public void set_power(int int1, int int2, int int3) {
        try {
            _int1 = int1;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setDoll(L1PcInstance pc) {
        try {
            pc.addHitup(_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addHitup(-_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

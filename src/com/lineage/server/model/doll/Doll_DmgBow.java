package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:远距离攻击相关<BR>
 * 攻击力提高 参数：TYPE1(远距离攻击+)
 * 
 * @author dexc
 * 
 */
public class Doll_DmgBow extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_DmgBow.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:远距离攻击增加
     */
    public Doll_DmgBow() {
    }

    public static L1DollExecutor get() {
        return new Doll_DmgBow();
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
            pc.addBowDmgup(_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addBowDmgup(-_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

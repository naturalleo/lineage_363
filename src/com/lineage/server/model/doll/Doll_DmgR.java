package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:攻击力相关<BR>
 * 攻击力提高 参数：TYPE1(攻击力) TYPE2(机率1/100)
 * 
 * @author dexc
 * 
 */
public class Doll_DmgR extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_DmgR.class);

    private int _int1;// 值1

    private int _int2;// 值2

    /**
     * 娃娃效果:攻击力增加
     */
    public Doll_DmgR() {
    }

    public static L1DollExecutor get() {
        return new Doll_DmgR();
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
            pc.set_dmgAdd(_int1, _int2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.set_dmgAdd(-_int1, -_int2);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

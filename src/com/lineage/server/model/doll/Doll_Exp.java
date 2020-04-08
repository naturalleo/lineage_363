package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃能力 模组:经验质相关<BR>
 * 经验质提高 参数：TYPE1(增加质 1/100)
 * 
 * @author dexc
 * 
 */
public class Doll_Exp extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Exp.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:经验质增加
     */
    public Doll_Exp() {
    }

    public static L1DollExecutor get() {
        return new Doll_Exp();
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
            pc.set_expadd(_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.set_expadd(-_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

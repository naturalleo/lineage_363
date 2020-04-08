package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套装效果:暗闇耐性增加 暗闇耐性提高 参数：TYPE1(增加质)
 * 
 * @author daien
 * 
 */
public class Doll_Regist_Blind extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Regist_Blind.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:暗闇耐性增加
     */
    public Doll_Regist_Blind() {
    }

    public static L1DollExecutor get() {
        return new Doll_Regist_Blind();
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
            pc.addRegistBlind(_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addRegistBlind(-_int1);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

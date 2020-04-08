package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;

/**
 * 娃娃效果:精神 精神提高 参数：TYPE1(增加质)
 * 
 * @author daien
 * 
 */
public class Doll_Stat_Wis extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Stat_Wis.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:精神
     */
    public Doll_Stat_Wis() {
    }

    public static L1DollExecutor get() {
        return new Doll_Stat_Wis();
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
            pc.addWis(_int1);
            pc.sendPackets(new S_OwnCharStatus2(pc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addWis(-_int1);
            pc.sendPackets(new S_OwnCharStatus2(pc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;

/**
 * 娃娃效果:地属性增加 地属性增加 参数：TYPE1
 * 
 * @author daien
 * 
 */
public class Doll_DefenseEarth extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_DefenseEarth.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:地属性增加
     */
    public Doll_DefenseEarth() {
    }

    public static L1DollExecutor get() {
        return new Doll_DefenseEarth();
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
            pc.addEarth(_int1);
            pc.sendPackets(new S_OwnCharAttrDef(pc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addEarth(-_int1);
            pc.sendPackets(new S_OwnCharAttrDef(pc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

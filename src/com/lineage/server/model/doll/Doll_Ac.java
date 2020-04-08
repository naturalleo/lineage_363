package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;

/**
 * 娃娃能力 模组:AC相关<BR>
 * 防御力提高 参数：TYPE1
 * 
 * @author dexc
 * 
 */
public class Doll_Ac extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Ac.class);

    private int _int1;// 值1

    /**
     * 娃娃效果:AC增加
     */
    public Doll_Ac() {
    }

    public static L1DollExecutor get() {
        return new Doll_Ac();
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
            pc.addAc(_int1);
            pc.sendPackets(new S_OwnCharAttrDef(pc));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addAc(-_int1);
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

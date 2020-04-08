package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;

/**
 * 娃娃能力 模组:负重相关<BR>
 * 防御力提高 参数：TYPE1(/100) TYPE2(%)
 * 
 * @author dexc
 * 
 */
public class Doll_Weight extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Weight.class);

    private int _int1;// 值1(现有负重 /值1)

    private int _int2;// 值2(负重能力+值2%)

    /**
     * 娃娃效果:负重增加
     */
    public Doll_Weight() {
    }

    public static L1DollExecutor get() {
        return new Doll_Weight();
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
            if (_int1 != 0) {
                pc.addWeightReduction(_int1);
            }
            if (_int2 != 0) {
                pc.add_weightUP(_int2);
            }
            pc.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, pc
                    .getInventory().getWeight240()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            if (_int1 != 0) {
                pc.addWeightReduction(-_int1);
            }
            if (_int2 != 0) {
                pc.add_weightUP(-_int2);
            }
            pc.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, pc
                    .getInventory().getWeight240()));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

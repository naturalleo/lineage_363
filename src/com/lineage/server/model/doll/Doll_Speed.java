package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillHaste;

/**
 * 娃娃能力 模组:人物速度 x1.33(1段加速)<BR>
 * 人物速度 x1.33(1段加速) 参数：无
 * 
 * @author dexc
 * 
 */
public class Doll_Speed extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Speed.class);

    /**
     * 娃娃效果:人物速度 x1.33(1段加速)
     */
    public Doll_Speed() {
    }

    public static L1DollExecutor get() {
        return new Doll_Speed();
    }

    @Override
    public void set_power(int int1, int int2, int int3) {
        try {
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void setDoll(L1PcInstance pc) {
        try {
            pc.addHasteItemEquipped(1);
            pc.removeHasteSkillEffect();
            pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1));

            if (pc.getMoveSpeed() != 1) {
                pc.setMoveSpeed(1);
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.addHasteItemEquipped(-1);
            if (pc.getHasteItemEquipped() == 0) {
                pc.setMoveSpeed(0);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

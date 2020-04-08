package com.lineage.server.model.doll;

import static com.lineage.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;

/**
 * 娃娃能力 模组:能在水底呼吸<BR>
 * 能在水底呼吸 参数：无
 * 
 * @author dexc
 * 
 */
public class Doll_Water extends L1DollExecutor {

    private static final Log _log = LogFactory.getLog(Doll_Water.class);

    /**
     * 娃娃效果:能在水底呼吸
     */
    public Doll_Water() {
    }

    public static L1DollExecutor get() {
        return new Doll_Water();
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
            if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
                pc.killSkillEffectTimer(STATUS_UNDERWATER_BREATH);
            }
            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), -1));
            pc.setSkillEffect(STATUS_UNDERWATER_BREATH, 0);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.killSkillEffectTimer(STATUS_UNDERWATER_BREATH);
            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public boolean is_reset() {
        return false;
    }
}

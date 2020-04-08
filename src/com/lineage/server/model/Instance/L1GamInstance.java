package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.templates.L1Npc;

/**
 * 赌场NPC(参赛者)
 * 
 * @author dexc
 * 
 */
public class L1GamInstance extends L1NpcInstance {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Log _log = LogFactory.getLog(L1GamInstance.class);

    /**
     * @param template
     */
    public L1GamInstance(final L1Npc template) {
        super(template);
    }

    @Override
    public void onAction(final L1PcInstance pc) {
        try {
            final L1AttackMode attack = new L1AttackPc(pc, this);
            // attack.calcHit();
            attack.action();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void onNpcAI() {
    }

    @Override
    public void onTalkAction(final L1PcInstance player) {
    }

    @Override
    public void onFinalAction(final L1PcInstance player, final String action) {
    }

    public void doFinalAction(final L1PcInstance player) {
    }

}

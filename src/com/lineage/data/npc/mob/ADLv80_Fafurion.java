package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 法利昂(3阶段)<BR>
 * 71028<BR>
 * 
 * @author dexc
 * 
 */
public class ADLv80_Fafurion extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(ADLv80_Fafurion.class);

    private ADLv80_Fafurion() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new ADLv80_Fafurion();
    }

    @Override
    public int type() {
        return 8;
    }

    @Override
    public L1PcInstance death(final L1Character lastAttacker,
            final L1NpcInstance npc) {
        try {
            // 判断主要攻击者
            final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

            if (pc != null) {

                final L1Party party = pc.getParty();

                if (party != null) {

                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 魔法师．哈汀(故事)<BR>
 * 12关卡怪物通用
 * 
 * @author dexc
 * 
 */
public class CH_Monster extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(CH_Monster.class);

    private CH_Monster() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new CH_Monster();
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
                if (pc.get_hardinR() != null) {
                    pc.get_hardinR().down_count(npc);
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

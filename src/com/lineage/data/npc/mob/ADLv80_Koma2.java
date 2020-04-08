package com.lineage.data.npc.mob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ADLv80_1;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 喀玛王(B)<BR>
 * 71012<BR>
 * 
 * @author dexc
 * 
 */
public class ADLv80_Koma2 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(ADLv80_Koma2.class);

    private ADLv80_Koma2() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new ADLv80_Koma2();
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
                // 任务已经开始
                if (pc.getMapId() == ADLv80_1.MAPID) {
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 42511, 1);// 喀玛王之心 (B)
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

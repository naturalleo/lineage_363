package com.lineage.data.npc.mob;

import static com.lineage.server.model.skill.L1SkillId.CKEW_LV50;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 神官奇诺 (妖精) 87015<BR>
 * 神官奇诺 (法师) 87016<BR>
 * 神官奇诺 (王族) 87017<BR>
 * 神官奇诺 (骑士) 87018<BR>
 * 
 * @author dexc
 * 
 */
public class CKEW50_Chino extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(CKEW50_Chino.class);

    private CKEW50_Chino() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new CKEW50_Chino();
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
                if (pc.hasSkillEffect(CKEW_LV50)) {
                    return pc;
                }
                if (pc.getInventory().checkItem(49168)) { // 已经具有物品
                    return pc;
                }
                // 取得任务道具
                CreateNewItem.getQuestItem(pc, npc, 49168, 1);// 破坏之秘药
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

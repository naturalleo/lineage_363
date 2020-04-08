package com.lineage.data.npc.mob;

import static com.lineage.server.model.skill.L1SkillId.CKEW_LV50;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

/**
 * 高洁意志的守护者<BR>
 * 87001<BR>
 * 
 * @author dexc
 * 
 */
public class CKEW50_Guardian1 extends NpcExecutor {

    private static final Log _log = LogFactory.getLog(CKEW50_Guardian1.class);

    private CKEW50_Guardian1() {
        // TODO Auto-generated constructor stub
    }

    public static NpcExecutor get() {
        return new CKEW50_Guardian1();
    }

    @Override
    public int type() {
        return 8;
    }

    private static Random _random = new Random();

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
                if (pc.getInventory().checkItem(49165)) { // 已经具有物品
                    return pc;
                }
                if (_random.nextInt(100) < 40) {
                    // 取得任务道具
                    CreateNewItem.getQuestItem(pc, npc, 49165, 1);// 圣殿 2楼钥匙
                }
            }
            return pc;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}

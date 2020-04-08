package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;

/**
 * 魔法卷轴 (圣结界)
 */
public class ImmuntToHarm extends ItemExecutor {

    /**
	 *
	 */
    private ImmuntToHarm() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new ImmuntToHarm();
    }

    /**
     * 道具物件执行
     * 
     * @param data
     *            参数
     * @param pc
     *            执行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc,
            final L1ItemInstance item) {
        if (pc == null) {
            return;
        }
        if (item == null) {
            return;
        }

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final int skillid = L1SkillId.IMMUNE_TO_HARM;

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, pc.getId(), 0, 0, 0,
                    L1SkillUse.TYPE_SPELLSC);
        }
    }
}

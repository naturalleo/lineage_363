package com.lineage.data.item_etcitem.event;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 第二段1.3倍经验加倍 41238
 */
public class SExp13 extends ItemExecutor {

    /**
	 *
	 */
    private SExp13() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new SExp13();
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
        // 例外状况:物件为空
        if (item == null) {
            return;
        }
        // 例外状况:人物为空
        if (pc == null) {
            return;
        }
        // 判断经验加倍技能
        if (L1BuffUtil.cancelExpSkill_2(pc)) {
            final int time = 600;
            // 删除物品
            if (pc.getInventory().removeItem(item, 1) == 1) {
                pc.setSkillEffect(L1SkillId.SEXP13, time * 1000);
                // 3076 第二段经验1.3倍计时开始，效果时间600秒。
                pc.sendPackets(new S_ServerMessage("第二段 经验质提升130%(600秒)"));
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));
            }
        }
    }
}

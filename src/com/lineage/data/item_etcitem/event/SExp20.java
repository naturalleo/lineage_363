package com.lineage.data.item_etcitem.event;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 第二段2.0倍经验加倍 41241
 */
public class SExp20 extends ItemExecutor {

    /**
	 *
	 */
    private SExp20() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new SExp20();
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
                pc.setSkillEffect(L1SkillId.SEXP20, time * 1000);
                pc.sendPackets(new S_ServerMessage("第二段 经验质提升200%(600秒)"));
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));
            }
        }
    }
}

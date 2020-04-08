package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 40868 魔法卷轴 (寒冷战栗) 40886 魔法卷轴 (吸血鬼之吻) 40897 魔法卷轴 (魔力夺取)
 * 
 */
public class MagicReel_Short extends ItemExecutor {

    /**
	 *
	 */
    private MagicReel_Short() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_Short();
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

        // 隐身状态可用魔法限制
        if (pc.isInvisble() || pc.isInvisDelay()) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final int targetID = data[0];
        final int spellsc_x = data[1];
        final int spellsc_y = data[2];

        if ((targetID == pc.getId()) || (targetID == 0)) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final int itemId = item.getItemId();
            final int skillid = itemId - 40858;

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, targetID, spellsc_x,
                    spellsc_y, 0, L1SkillUse.TYPE_SPELLSC);
        }
    }
}

package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 40859 魔法卷轴 (初级治愈术) 40866 魔法卷轴 (神圣武器) 40867 魔法卷轴 (解毒术) 40877 魔法卷轴 (中级治愈术)
 * 40884 魔法卷轴 (通畅气脉术) 40893 魔法卷轴 (高级治愈术) 40895 魔法卷轴 (圣洁之光)
 */
public class MagicReel_Buff extends ItemExecutor {

    /**
	 *
	 */
    private MagicReel_Buff() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_Buff();
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
        final int targetID = data[0];

        if (targetID == 0) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final L1Object target = World.get().findObject(targetID);
        if (target == null) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final int spellsc_x = target.getX();
        final int spellsc_y = target.getY();

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final int itemId = item.getItemId();
            int skillid = itemId - 40858;
            switch (itemId) {//添加缺少的高级空的魔法卷轴 hjx1000
            case 59001: // 魔法卷轴 (体魄强健术)
                skillid = 42;
                break;

            case 59002: // 魔法卷轴 (祝福魔法武器)
                skillid = 48;
                break;

            case 59006: // 魔法卷轴 (全部治愈术)
                skillid = 57;
                break;
            case 59007: // 魔法卷轴 (圣结界)
                skillid = 68;
                break;
            }

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, targetID, spellsc_x,
                    spellsc_y, 0, L1SkillUse.TYPE_SPELLSC);
        }
    }
}

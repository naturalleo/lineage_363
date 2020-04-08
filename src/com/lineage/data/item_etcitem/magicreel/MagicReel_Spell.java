package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;

/**
 * 40860 魔法卷轴 (日光术) 40861 魔法卷轴 (保护罩) 40871 魔法卷轴 (无所遁形术) 40872 魔法卷轴 (负重强化) 40889
 * 魔法卷轴 (魔法屏障) 40890 魔法卷轴 (冥想术)
 */
public class MagicReel_Spell extends ItemExecutor {

    /**
	 *
	 */
    private MagicReel_Spell() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_Spell();
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

            final int itemId = item.getItemId();
            int skillid = itemId - 40858;
            switch (itemId) {//添加缺少的高级空的魔法卷轴 hjx1000
            case 59003: // 魔法卷轴 (体力回复术)
                skillid = 49;
                break;

            case 59004: // 魔法卷轴 (神圣疾走)
                skillid = 52;
                break;

            case 59005: // 魔法卷轴 (强力加速术)
                skillid = 54;
                break;
            }

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, pc.getId(), 0, 0, 0,
                    L1SkillUse.TYPE_SPELLSC);
        }
    }
}

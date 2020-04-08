package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级6)</font><BR>
 * Spell Book Lv6
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv6 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv6() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv6();
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
        // 取得名称
        final String nameId = item.getItem().getNameId();
        // 技能编号
        int skillid = 0;
        // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
        // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
        int attribute = 0;
        // 分组
        final int magicLv = 6;

        if (nameId.equalsIgnoreCase("$542")) {// 魔法书(造尸术)
            // 技能编号
            skillid = 41;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$543")) {// 魔法书(体魄强健术)
            // 技能编号
            skillid = 42;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$544")) {// 魔法书(加速术)
            // 技能编号
            skillid = 43;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$545")) {// 魔法书(魔法相消术)
            // 技能编号
            skillid = 44;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$546")) {// 魔法书(地裂术)
            // 技能编号
            skillid = 45;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1588")) {// 魔法书(烈炎术)
            // 技能编号
            skillid = 46;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1864")) {// 魔法书(弱化术)
            // 技能编号
            skillid = 47;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$1865")) {// 魔法书(祝福魔法武器)
            // 技能编号
            skillid = 48;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级5)</font><BR>
 * Spell Book Lv5
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv5 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv5() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv5();
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
        final int magicLv = 5;

        if (nameId.equalsIgnoreCase("$537")) {// 魔法书(木乃伊的诅咒)
            // 技能编号
            skillid = 33;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$538")) {// 魔法书(极道落雷)
            // 技能编号
            skillid = 34;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$539")) {// 魔法书(高级治愈术)
            // 技能编号
            skillid = 35;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$540")) {// 魔法书(迷魅术)
            // 技能编号
            skillid = 36;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$541")) {// 魔法书(圣洁之光)
            // 技能编号
            skillid = 37;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$1587")) {// 魔法书(冰锥)
            // 技能编号
            skillid = 38;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1862")) {// 魔法书(魔力夺取)
            // 技能编号
            skillid = 39;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1863")) {// 魔法书(黑闇之影)
            // 技能编号
            skillid = 40;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

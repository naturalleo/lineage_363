package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级1)</font><BR>
 * Spell Book Lv1
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv1 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv1() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv1();
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
        final int magicLv = 1;

        if (nameId.equalsIgnoreCase("$517")) {// 魔法书(初级治愈术)
            // 技能编号
            skillid = 1;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$518")) {// 魔法书(日光术)
            // 技能编号
            skillid = 2;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$519")) {// 魔法书(保护罩)
            // 技能编号
            skillid = 3;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$520")) {// 魔法书(光箭)
            // 技能编号
            skillid = 4;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$521")) {// 魔法书(指定传送)
            // 技能编号
            skillid = 5;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1581")) {// 魔法书(冰箭)
            // 技能编号
            skillid = 6;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1582")) {// 魔法书(风刃)
            // 技能编号
            skillid = 7;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1857")) {// 魔法书(神圣武器)
            // 技能编号
            skillid = 8;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

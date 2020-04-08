package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级2)</font><BR>
 * Spell Book Lv2
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv2 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv2();
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
        final int magicLv = 2;

        if (nameId.equalsIgnoreCase("$522")) {// 魔法书(解毒术)
            // 技能编号
            skillid = 9;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$523")) {// 魔法书(寒冷战栗)
            // 技能编号
            skillid = 10;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$524")) {// 魔法书(毒咒)
            // 技能编号
            skillid = 11;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$525")) {// 魔法书(拟似魔法武器)
            // 技能编号
            skillid = 12;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$526")) {// 魔法书(无所遁形术)
            // 技能编号
            skillid = 13;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1858")) {// 魔法书(负重强化)
            // 技能编号
            skillid = 14;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1583")) {// 魔法书 (火箭)
            // 技能编号
            skillid = 15;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1584")) {// 魔法书 (地狱之牙)
            // 技能编号
            skillid = 16;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

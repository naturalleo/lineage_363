package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级3)</font><BR>
 * Spell Book Lv3
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv3 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv3() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv3();
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
        final int magicLv = 3;

        if (nameId.equalsIgnoreCase("$527")) {// 魔法书(极光雷电)
            // 技能编号
            skillid = 17;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$528")) {// 魔法书(起死回生术)
            // 技能编号
            skillid = 18;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$529")) {// 魔法书(中级治愈术)
            // 技能编号
            skillid = 19;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$530")) {// 魔法书(闇盲咒术)
            // 技能编号
            skillid = 20;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$531")) {// 魔法书(铠甲护持)
            // 技能编号
            skillid = 21;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1585")) {// 魔法书(寒冰气息)
            // 技能编号
            skillid = 22;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1859")) {// 魔法书(能量感测)
            // 技能编号
            skillid = 23;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * <font color=#00800>魔法书(等级4)</font><BR>
 * Spell Book Lv4
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv4 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv4() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv4();
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
        final int magicLv = 4;

        if (nameId.equalsIgnoreCase("$532")) {// 魔法书(燃烧的火球)
            // 技能编号
            skillid = 25;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$533")) {// 魔法书(通畅气脉术)
            // 技能编号
            skillid = 26;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$534")) {// 魔法书(坏物术)
            // 技能编号
            skillid = 27;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$535")) {// 魔法书(吸血鬼之吻)
            // 技能编号
            skillid = 28;
            // 技能属性
            attribute = 2;

        } else if (nameId.equalsIgnoreCase("$536")) {// 魔法书(缓速术)
            // 技能编号
            skillid = 29;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1586")) {// 魔法书(岩牢)
            // 技能编号
            skillid = 30;
            // 技能属性
            attribute = 0;

        } else if (nameId.equalsIgnoreCase("$1860")) {// 魔法书(魔法屏障)
            // 技能编号
            skillid = 31;
            // 技能属性
            attribute = 1;

        } else if (nameId.equalsIgnoreCase("$1861")) {// 魔法书(冥想术)
            // 技能编号
            skillid = 32;
            // 技能属性
            attribute = 0;

        }

        // 检查学习该法术是否成立
        Skill_Check.check(pc, item, skillid, magicLv, attribute);
    }
}

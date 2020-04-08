package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>魔法书(等级7)</font><BR>
 * Spell Book Lv7
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv7 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv7() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv7();
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
        // 不是法师
        if (!pc.isWizard()) {
            // 79 没有任何事情发生
            final S_ServerMessage msg = new S_ServerMessage(79);
            pc.sendPackets(msg);

        } else {
            // 取得名称
            final String nameId = item.getItem().getNameId();
            // 技能编号
            int skillid = 0;
            // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
            // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
            int attribute = 0;
            // 分组
            final int magicLv = 7;

            if (nameId.equalsIgnoreCase("$547")) {// 魔法书(体力回复术)
                // 技能编号
                skillid = 49;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$548")) {// 魔法书(冰矛围篱)
                // 技能编号
                skillid = 50;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$549")) {// 魔法书(召唤术)
                // 技能编号
                skillid = 51;
                // 技能属性
                attribute = 2;

            } else if (nameId.equalsIgnoreCase("$550")) {// 魔法书(神圣疾走)
                // 技能编号
                skillid = 52;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$551")) {// 魔法书(龙卷风)
                // 技能编号
                skillid = 53;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1651")) {// 魔法书(强力加速术)
                // 技能编号
                skillid = 54;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1866")) {// 魔法书(狂暴术)
                // 技能编号
                skillid = 55;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1867")) {// 魔法书(疾病术)
                // 技能编号
                skillid = 56;
                // 技能属性
                attribute = 2;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>魔法书(等级8)</font><BR>
 * Spell Book Lv8
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv8 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv8() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv8();
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
            final int magicLv = 8;

            if (nameId.equalsIgnoreCase("$552")) {// 魔法书(全部治愈术)
                // 技能编号
                skillid = 57;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$553")) {// 魔法书(火牢)
                // 技能编号
                skillid = 58;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$554")) {// 魔法书(冰雪暴)
                // 技能编号
                skillid = 59;
                // 技能属性
                attribute = 2;

            } else if (nameId.equalsIgnoreCase("$555")) {// 魔法书(隐身术)
                // 技能编号
                skillid = 60;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$556")) {// 魔法书(返生术)
                // 技能编号
                skillid = 61;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$1589")) {// 魔法书(震裂术)
                // 技能编号
                skillid = 62;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1868")) {// 魔法书(治愈能量风暴)
                // 技能编号
                skillid = 63;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1869")) {// 魔法书(魔法封印)
                // 技能编号
                skillid = 64;
                // 技能属性
                attribute = 0;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

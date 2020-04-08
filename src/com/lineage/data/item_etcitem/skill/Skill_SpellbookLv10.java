package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>魔法书(等级10)</font><BR>
 * Spell Book Lv10
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv10 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv10() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv10();
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
            final int magicLv = 10;

            if (nameId.equalsIgnoreCase("$562")) {// 魔法书(创造魔法武器)
                // 技能编号
                skillid = 73;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$563")) {// 魔法书(流星雨)
                // 技能编号
                skillid = 74;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$564")) {// 魔法书(终极返生术)
                // 技能编号
                skillid = 75;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$565")) {// 魔法书(集体缓速术)
                // 技能编号
                skillid = 76;
                // 技能属性
                attribute = 2;

            } else if (nameId.equalsIgnoreCase("$566")) {// 魔法书(究极光裂术)
                // 技能编号
                skillid = 77;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$1872")) {// 魔法书(绝对屏障)
                // 技能编号
                skillid = 78;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1873")) {// 魔法书(灵魂升华)
                // 技能编号
                skillid = 79;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1874")) {// 魔法书(冰雪飓风)
                // 技能编号
                skillid = 80;
                // 技能属性
                attribute = 2;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

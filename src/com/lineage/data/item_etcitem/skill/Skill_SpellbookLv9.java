package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>魔法书(等级9)</font><BR>
 * Spell Book Lv9
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookLv9 extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookLv9() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookLv9();
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
            final int magicLv = 9;

            if (nameId.equalsIgnoreCase("$557")) {// 魔法书(雷霆风暴)
                // 技能编号
                skillid = 65;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$558")) {// 魔法书(沉睡之雾)
                // 技能编号
                skillid = 66;
                // 技能属性
                attribute = 2;

            } else if (nameId.equalsIgnoreCase("$559")) {// 魔法书(变形术)
                // 技能编号
                skillid = 67;
                // 技能属性
                attribute = 2;

            } else if (nameId.equalsIgnoreCase("$560")) {// 魔法书(圣结界)
                // 技能编号
                skillid = 68;
                // 技能属性
                attribute = 1;

            } else if (nameId.equalsIgnoreCase("$561")) {// 魔法书(集体传送术)
                // 技能编号
                skillid = 69;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1590")) {// 魔法书(火风暴)
                // 技能编号
                skillid = 70;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1870")) {// 魔法书(药水霜化术)
                // 技能编号
                skillid = 71;
                // 技能属性
                attribute = 0;

            } else if (nameId.equalsIgnoreCase("$1871")) {// 魔法书(强力无所遁形术)
                // 技能编号
                skillid = 72;
                // 技能属性
                attribute = 0;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

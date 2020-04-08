package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>精灵水晶(精灵魔法-无属性)</font><BR>
 * Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_SpiritCrystal extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpiritCrystal() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal();
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
        // 不是精灵
        if (!pc.isElf()) {
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
            final int attribute = 3;
            // 分组
            int magicLv = 0;

            if (nameId.equalsIgnoreCase("$1829")) {// 精灵水晶(魔法防御)
                // 技能编号
                skillid = RESIST_MAGIC;
                // 分组
                magicLv = 11;

            } else if (nameId.equalsIgnoreCase("$1830")) {// 精灵水晶(心灵转换)
                // 技能编号
                skillid = BODY_TO_MIND;
                // 分组
                magicLv = 11;

            } else if (nameId.equalsIgnoreCase("$1831")) {// 精灵水晶(世界树的呼唤)
                // 技能编号
                skillid = TELEPORT_TO_MATHER;
                // 分组
                magicLv = 11;

            } else if (nameId.equalsIgnoreCase("$1832")) {// 精灵水晶(净化精神)
                // 技能编号
                skillid = CLEAR_MIND;
                // 分组
                magicLv = 12;

            } else if (nameId.equalsIgnoreCase("$1833")) {// 精灵水晶(属性防御)
                // 技能编号
                skillid = RESIST_ELEMENTAL;
                // 分组
                magicLv = 12;

            } else if (nameId.equalsIgnoreCase("$3261")) {// 精灵水晶(三重矢)
                // 技能编号
                skillid = TRIPLE_ARROW;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$1834")) {// 精灵水晶(释放元素)
                // 技能编号
                skillid = RETURN_TO_NATURE;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$1835")) {// 精灵水晶(魂体转换)
                // 技能编号
                skillid = BLOODY_SOUL;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$1836")) {// 精灵水晶(单属性防御)
                // 技能编号
                skillid = ELEMENTAL_PROTECTION;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$3262")) {// 精灵水晶(弱化属性)
                // 技能编号
                skillid = ELEMENTAL_FALL_DOWN;
                // 分组
                magicLv = 14;

            } else if (nameId.equalsIgnoreCase("$1842")) {// 精灵水晶(魔法消除)
                // 技能编号
                skillid = ERASE_MAGIC;
                // 分组
                magicLv = 14;

            } else if (nameId.equalsIgnoreCase("$1843")) {// 精灵水晶(召唤属性精灵)
                // 技能编号
                skillid = LESSER_ELEMENTAL;
                // 分组
                magicLv = 14;

            } else if (nameId.equalsIgnoreCase("$3263")) {// 精灵水晶(镜反射)
                // 技能编号
                skillid = COUNTER_MIRROR;
                // 分组
                magicLv = 15;

            } else if (nameId.equalsIgnoreCase("$1849")) {// 精灵水晶(封印禁地)
                // 技能编号
                skillid = AREA_OF_SILENCE;
                // 分组
                magicLv = 15;

            } else if (nameId.equalsIgnoreCase("$1850")) {// 精灵水晶(召唤强力属性精灵)
                // 技能编号
                skillid = GREATER_ELEMENTAL;
                // 分组
                magicLv = 15;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>黑暗精灵水晶(黑暗精灵魔法)</font><BR>
 * Dark Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_DarkSpiritCrystal extends ItemExecutor {

    /**
	 *
	 */
    private Skill_DarkSpiritCrystal() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_DarkSpiritCrystal();
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
        // 不是黑暗精灵
        if (!pc.isDarkelf()) {
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
            final int attribute = 6;
            // 分组
            int magicLv = 0;

            if (nameId.equalsIgnoreCase("$2518")) {// 黑暗精灵水晶(暗隐术)
                // 技能编号
                skillid = BLIND_HIDING;
                // 分组
                magicLv = 41;

            } else if (nameId.equalsIgnoreCase("$2519")) {// 黑暗精灵水晶(附加剧毒)
                // 技能编号
                skillid = ENCHANT_VENOM;
                // 分组
                magicLv = 41;

            } else if (nameId.equalsIgnoreCase("$2520")) {// 黑暗精灵水晶(影之防护)
                // 技能编号
                skillid = SHADOW_ARMOR;
                // 分组
                magicLv = 41;

            } else if (nameId.equalsIgnoreCase("$2521")) {// 黑暗精灵水晶(提炼魔石)
                // 技能编号
                skillid = BRING_STONE;
                // 分组
                magicLv = 41;

            } else if (nameId.equalsIgnoreCase("$3172")) {// 黑暗精灵水晶(力量提升)
                // 技能编号
                skillid = DRESS_MIGHTY;
                // 分组
                magicLv = 41;

            } else if (nameId.equalsIgnoreCase("$2522")) {// 黑暗精灵水晶(行走加速)
                // 技能编号
                skillid = MOVING_ACCELERATION;
                // 分组
                magicLv = 42;

            } else if (nameId.equalsIgnoreCase("$2523")) {// 黑暗精灵水晶(燃烧斗志)
                // 技能编号
                skillid = BURNING_SPIRIT;
                // 分组
                magicLv = 42;

            } else if (nameId.equalsIgnoreCase("$2524")) {// 黑暗精灵水晶(暗黑盲咒)
                // 技能编号
                skillid = DARK_BLIND;
                // 分组
                magicLv = 42;

            } else if (nameId.equalsIgnoreCase("$2525")) {// 黑暗精灵水晶(毒性抵抗)
                // 技能编号
                skillid = VENOM_RESIST;
                // 分组
                magicLv = 42;

            } else if (nameId.equalsIgnoreCase("$3173")) {// 黑暗精灵水晶(敏捷提升)
                // 技能编号
                skillid = DRESS_DEXTERITY;
                // 分组
                magicLv = 42;

            } else if (nameId.equalsIgnoreCase("$2526")) {// 黑暗精灵水晶(双重破坏)
                // 技能编号
                skillid = DOUBLE_BRAKE;
                // 分组
                magicLv = 43;

            } else if (nameId.equalsIgnoreCase("$2527")) {// 黑暗精灵水晶(暗影闪避)
                // 技能编号
                skillid = UNCANNY_DODGE;
                // 分组
                magicLv = 43;

            } else if (nameId.equalsIgnoreCase("$2528")) {// 黑暗精灵水晶(暗影之牙)
                // 技能编号
                skillid = SHADOW_FANG;
                // 分组
                magicLv = 43;

            } else if (nameId.equalsIgnoreCase("$2529")) {// 黑暗精灵水晶(会心一击)
                // 技能编号
                skillid = FINAL_BURN;
                // 分组
                magicLv = 43;

            } else if (nameId.equalsIgnoreCase("$3174")) {// 黑暗精灵水晶(闪避提升)
                // 技能编号
                skillid = DRESS_EVASION;
                // 分组
                magicLv = 43;

            } else if (nameId.equalsIgnoreCase("黑暗精灵水晶(黑暗笼罩)")) {// 黑暗精靈水晶(閃避提升)
                // 技能編號
                skillid = DRESS_HALZ;
                // 分組
                magicLv = 43;
            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

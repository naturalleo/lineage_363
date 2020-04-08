package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>记忆水晶(幻术师魔法)</font><BR>
 * Dark Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_Illusionist extends ItemExecutor {

    /**
	 *
	 */
    private Skill_Illusionist() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_Illusionist();
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
        // 不是幻术师
        if (!pc.isIllusionist()) {
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
            // 技能属性 7:龙骑士专属魔法 8:幻术师专属魔法
            final int attribute = 6;
            // 分组
            int magicLv = 0;
            /*
             * 记忆水晶(镜像) $5681 记忆水晶(混乱) $5682 记忆水晶(暴击) $5683 记忆水晶(幻觉：欧吉) $5684
             * 记忆水晶(立方：燃烧) $5685 记忆水晶(专注) $5686 记忆水晶(心灵破坏 ) $5687 记忆水晶(骷髅毁坏)
             * $5688
             * 
             * 记忆水晶(幻觉：巫妖) $5689 记忆水晶(立方：地裂) $5690 记忆水晶(耐力) $5691 记忆水晶(幻想) $5692
             * 
             * 记忆水晶(武器破坏者) $5693 记忆水晶(幻觉：钻石高仑) $5694 记忆水晶(立方：冲击) $5695 记忆水晶(洞察)
             * $5696
             * 
             * 记忆水晶(恐慌) $5697 记忆水晶(疼痛的欢愉) $5698 记忆水晶(幻觉：化身) $5699 记忆水晶(立方：和谐)
             * $5700
             */
            // TODO 1
            if (nameId.equalsIgnoreCase("$5681")) {// 记忆水晶(镜像)
                // 技能编号
                skillid = MIRROR_IMAGE;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5682")) {// 记忆水晶(混乱)
                // 技能编号
                skillid = CONFUSION;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5683")) {// 记忆水晶(暴击)
                // 技能编号
                skillid = SMASH;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5684")) {// 记忆水晶(幻觉：欧吉)
                // 技能编号
                skillid = ILLUSION_OGRE;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5685")) {// 记忆水晶(立方：燃烧)
                // 技能编号
                skillid = CUBE_IGNITION;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5686")) {// 记忆水晶(专注)
                // 技能编号
                skillid = CONCENTRATION;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5687")) {// 记忆水晶(心灵破坏 )
                // 技能编号
                skillid = MIND_BREAK;
                // 分组
                magicLv = 61;

            } else if (nameId.equalsIgnoreCase("$5688")) {// 记忆水晶(骷髅毁坏)
                // 技能编号
                skillid = BONE_BREAK;
                // 分组
                magicLv = 61;

                // TODO 2
            } else if (nameId.equalsIgnoreCase("$5689")) {// 记忆水晶(幻觉：巫妖)
                // 技能编号
                skillid = ILLUSION_LICH;
                // 分组
                magicLv = 62;

            } else if (nameId.equalsIgnoreCase("$5690")) {// 记忆水晶(立方：地裂)
                // 技能编号
                skillid = CUBE_QUAKE;
                // 分组
                magicLv = 62;

            } else if (nameId.equalsIgnoreCase("$5691")) {// 记忆水晶(耐力)
                // 技能编号
                skillid = PATIENCE;
                // 分组
                magicLv = 62;

            } else if (nameId.equalsIgnoreCase("$5692")) {// 记忆水晶(幻想)
                // 技能编号
                skillid = PHANTASM;
                // 分组
                magicLv = 62;

                // TODO 3
            } else if (nameId.equalsIgnoreCase("$5693")) {// 记忆水晶(武器破坏者)
                // 技能编号
                skillid = ARM_BREAKER;
                // 分组
                magicLv = 63;

            } else if (nameId.equalsIgnoreCase("$5694")) {// 记忆水晶(幻觉：钻石高仑)
                // 技能编号
                skillid = ILLUSION_DIA_GOLEM;
                // 分组
                magicLv = 63;

            } else if (nameId.equalsIgnoreCase("$5695")) {// 记忆水晶(立方：冲击)
                // 技能编号
                skillid = CUBE_SHOCK;
                // 分组
                magicLv = 63;

            } else if (nameId.equalsIgnoreCase("$5696")) {// 记忆水晶(洞察)
                // 技能编号
                skillid = INSIGHT;
                // 分组
                magicLv = 63;

                // TODO 4
            } else if (nameId.equalsIgnoreCase("$5697")) {// 记忆水晶(恐慌)
                // 技能编号
                skillid = PANIC;
                // 分组
                magicLv = 64;

            } else if (nameId.equalsIgnoreCase("$5698")) {// 记忆水晶(疼痛的欢愉)
                // 技能编号
                skillid = JOY_OF_PAIN;
                // 分组
                magicLv = 64;

            } else if (nameId.equalsIgnoreCase("$5699")) {// 记忆水晶(幻觉：化身)
                // 技能编号
                skillid = ILLUSION_AVATAR;
                // 分组
                magicLv = 64;

            } else if (nameId.equalsIgnoreCase("$5700")) {// 记忆水晶(立方：和谐)
                // 技能编号
                skillid = CUBE_BALANCE;
                // 分组
                magicLv = 64;
            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

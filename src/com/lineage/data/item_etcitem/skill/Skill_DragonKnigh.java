package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>龙骑士书板(龙骑士魔法)</font><BR>
 * Dark Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_DragonKnigh extends ItemExecutor {

    /**
	 *
	 */
    private Skill_DragonKnigh() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_DragonKnigh();
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
        // 不是龙骑士
        if (!pc.isDragonKnight()) {
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
             * 龙骑士书板(龙之护铠) $5778 龙骑士书板(燃烧击砍) $5701 龙骑士书板(护卫毁灭) $5702 龙骑士书板(岩浆喷吐)
             * $5703
             * 
             * 龙骑士书板(觉醒：安塔瑞斯) $5704 龙骑士书板(血之渴望) $5705 龙骑士书板(屠宰者) $5706
             * 龙骑士书板(恐惧无助) $5707 龙骑士书板(冲击之肤) $5708 龙骑士书板(觉醒：法利昂) $5709
             * 龙骑士书板(致命身躯) $5710 龙骑士书板(夺命之雷) $5711
             * 
             * 龙骑士书板(惊悚死神) $5712 龙骑士书板(寒冰喷吐) $5713 龙骑士书板(觉醒：巴拉卡斯) $5714
             */
            // TODO 1
            if (nameId.equalsIgnoreCase("$5778")) {// 龙骑士书板(龙之护铠)
                // 技能编号
                skillid = DRAGON_SKIN;
                // 分组
                magicLv = 51;

            } else if (nameId.equalsIgnoreCase("$5701")) {// 龙骑士书板(燃烧击砍)
                // 技能编号
                skillid = BURNING_SLASH;
                // 分组
                magicLv = 51;

            } else if (nameId.equalsIgnoreCase("$5702")) {// 龙骑士书板(护卫毁灭)
                // 技能编号
                skillid = GUARD_BRAKE;
                // 分组
                magicLv = 51;

            } else if (nameId.equalsIgnoreCase("$5703")) {// 龙骑士书板(岩浆喷吐)
                // 技能编号
                skillid = MAGMA_BREATH;
                // 分组
                magicLv = 51;

                // TODO 2
            } else if (nameId.equalsIgnoreCase("$5704")) {// 龙骑士书板(觉醒：安塔瑞斯)
                // 技能编号
                skillid = AWAKEN_ANTHARAS;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5705")) {// 龙骑士书板(血之渴望)
                // 技能编号
                skillid = BLOODLUST;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5706")) {// 龙骑士书板(屠宰者)
                // 技能编号
                skillid = FOE_SLAYER;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5707")) {// 龙骑士书板(恐惧无助)
                // 技能编号
                skillid = RESIST_FEAR;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5708")) {// 龙骑士书板(冲击之肤)
                // 技能编号
                skillid = SHOCK_SKIN;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5709")) {// 龙骑士书板(觉醒：法利昂)
                // 技能编号
                skillid = AWAKEN_FAFURION;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5710")) {// 龙骑士书板(致命身躯)
                // 技能编号
                skillid = MORTAL_BODY;
                // 分组
                magicLv = 52;

            } else if (nameId.equalsIgnoreCase("$5711")) {// 龙骑士书板(夺命之雷)
                // 技能编号
                skillid = THUNDER_GRAB;
                // 分组
                magicLv = 52;

                // TODO 3
            } else if (nameId.equalsIgnoreCase("$5712")) {// 龙骑士书板(惊悚死神)
                // 技能编号
                skillid = HORROR_OF_DEATH;
                // 分组
                magicLv = 53;

            } else if (nameId.equalsIgnoreCase("$5713")) {// 龙骑士书板(寒冰喷吐)
                // 技能编号
                skillid = FREEZING_BREATH;
                // 分组
                magicLv = 53;

            } else if (nameId.equalsIgnoreCase("$5714")) {// 龙骑士书板(觉醒：巴拉卡斯)
                // 技能编号
                skillid = AWAKEN_VALAKAS;
                // 分组
                magicLv = 53;
            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

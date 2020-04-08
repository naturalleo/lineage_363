package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>技术书(骑士技能)</font><BR>
 * Technical Document
 * 
 * @author dexc
 * 
 */
public class Skill_TechnicalDocument extends ItemExecutor {

    /**
	 *
	 */
    private Skill_TechnicalDocument() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_TechnicalDocument();
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
        // 不是骑士
        if (!pc.isKnight()) {
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
            int magicLv = 0;

            if (nameId.equalsIgnoreCase("$3259")) {// 技术书(冲击之晕)
                // 技能编号
                skillid = SHOCK_STUN;
                // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
                // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
                attribute = 5;
                // 分组
                magicLv = 31;

            } else if (nameId.equalsIgnoreCase("$4007")) {// 技术书(增幅防御)
                // 技能编号
                skillid = REDUCTION_ARMOR;
                // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
                // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
                attribute = 5;
                // 分组
                magicLv = 31;

            } else if (nameId.equalsIgnoreCase("$4008")) {// 技术书(尖刺盔甲)
                // 技能编号
                skillid = BOUNCE_ATTACK;
                // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
                // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
                attribute = 5;
                // 分组
                magicLv = 32;

            } else if (nameId.equalsIgnoreCase("$4712")) {// 技术书(坚固防护)
                // 技能编号
                skillid = SOLID_CARRIAGE;
                // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
                // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
                attribute = 5;
                // 分组
                magicLv = 31;

            } else if (nameId.equalsIgnoreCase("$4713")) {// 技术书(反击屏障)
                // 技能编号
                skillid = COUNTER_BARRIER;
                // 技能属性 0:中立属性魔法 1:正义属性魔法 2:邪恶属性魔法
                // 技能属性 3:精灵专属魔法 4:王族专属魔法 5:骑士专属技能 6:黑暗精灵专属魔法
                attribute = 5;
                // 分组
                magicLv = 31;
            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

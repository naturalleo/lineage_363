package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>魔法书(王族专属魔法)</font><BR>
 * Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_SpellbookCrown extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpellbookCrown() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpellbookCrown();
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
        // 不是王族
        if (!pc.isCrown()) {
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
            final int attribute = 4;
            // 分组
            int magicLv = 0;

            if (nameId.equalsIgnoreCase("$1959")) {// 魔法书 (精准目标)
                // 技能编号
                skillid = TRUE_TARGET;
                // 分组
                magicLv = 21;

            } else if (nameId.equalsIgnoreCase("$2089")) {// 魔法书 (呼唤盟友)
                // 技能编号
                skillid = CALL_CLAN;
                // 分组
                magicLv = 22;

            } else if (nameId.equalsIgnoreCase("$1960")) {// 魔法书(激励士气)
                // 技能编号
                skillid = GLOWING_AURA;
                // 分组
                magicLv = 23;

            } else if (nameId.equalsIgnoreCase("$3260")) {// 魔法书(援护盟友)
                // 技能编号
                skillid = RUN_CLAN;
                // 分组
                magicLv = 24;

            } else if (nameId.equalsIgnoreCase("$3176")) {// 魔法书(冲击士气)
                // 技能编号
                skillid = BRAVE_AURA;
                // 分组
                magicLv = 25;

            } else if (nameId.equalsIgnoreCase("$3175")) {// 魔法书(钢铁士气)
                // 技能编号
                skillid = SHINING_AURA;
                // 分组
                magicLv = 26;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

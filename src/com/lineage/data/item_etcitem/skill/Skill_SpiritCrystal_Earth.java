package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * <font color=#00800>精灵水晶(精灵魔法-地属性)</font><BR>
 * Spirit Crystal
 * 
 * @author dexc
 * 
 */
public class Skill_SpiritCrystal_Earth extends ItemExecutor {

    /**
	 *
	 */
    private Skill_SpiritCrystal_Earth() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skill_SpiritCrystal_Earth();
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

            // 属性系不同
        } else if (pc.getElfAttr() != 1) {
            // 684 属性系列不同无法学习。
            final S_ServerMessage msg = new S_ServerMessage(684);
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

            if (nameId.equalsIgnoreCase("$1840")) {// 精灵水晶(大地防护)
                // 技能编号
                skillid = EARTH_SKIN;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$1841")) {// 精灵水晶(地面障碍)
                // 技能编号
                skillid = ENTANGLE;
                // 分组
                magicLv = 13;

            } else if (nameId.equalsIgnoreCase("$1846")) {// 精灵水晶(大地屏障)
                // 技能编号
                skillid = EARTH_BIND;
                // 分组
                magicLv = 14;

            } else if (nameId.equalsIgnoreCase("$1848")) {// 精灵水晶(大地的祝福)
                // 技能编号
                skillid = EARTH_BLESS;
                // 分组
                magicLv = 14;

            } else if (nameId.equalsIgnoreCase("$1856")) {// 精灵水晶(钢铁防护)
                // 技能编号
                skillid = IRON_SKIN;
                // 分组
                magicLv = 15;

            } else if (nameId.equalsIgnoreCase("$3265")) {// 精灵水晶(体能激发)
                // 技能编号
                skillid = EXOTIC_VITALIZE;
                // 分组
                magicLv = 15;

            }

            // 检查学习该法术是否成立
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}

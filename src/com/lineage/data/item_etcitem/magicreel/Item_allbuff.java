package com.lineage.data.item_etcitem.magicreel;

import static com.lineage.server.model.skill.L1SkillId.SHIELD;
import static com.lineage.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static com.lineage.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static com.lineage.server.model.skill.L1SkillId.BLESS_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.ADVANCE_SPIRIT;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.templates.L1Skills;

/**
 * 状态魔法卷轴 -add hjx1000
 */
public class Item_allbuff extends ItemExecutor {
	
    private static final int[] allBuffSkill = { 
    	SHIELD,
    	PHYSICAL_ENCHANT_DEX,
        PHYSICAL_ENCHANT_STR,
        BLESS_WEAPON,
        /*BERSERKERS,*/
        ADVANCE_SPIRIT, 

    };

    /**
	 *
	 */
    private Item_allbuff() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Item_allbuff();
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
        if (pc == null) {
            return;
        }
        if (item == null) {
            return;
        }

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

//            final int skillid = L1SkillId.IMMUNE_TO_HARM;
//
//            final L1SkillUse l1skilluse = new L1SkillUse();
//            l1skilluse.handleCommands(pc, skillid, pc.getId(), 0, 0, 0,
//                    L1SkillUse.TYPE_SPELLSC);
            for (int i = 0; i < allBuffSkill.length; i++) {
                final L1Skills skill = SkillsTable.get().getTemplate(
                        allBuffSkill[i]);
                final L1SkillUse skillUse = new L1SkillUse();
                skillUse.handleCommands(pc, allBuffSkill[i], pc.getId(),
                		pc.getX(), pc.getY(), skill.getBuffDuration(),
                        L1SkillUse.TYPE_SPELLSC);// */
            }
        }
    }
}

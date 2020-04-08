package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 40862 魔法卷轴 (光箭) 40864 魔法卷轴 (冰箭) 40865 魔法卷轴 (风刃) 40869 魔法卷轴 (毒咒) 40873 魔法卷轴
 * (火箭) 40874 魔法卷轴 (地狱之牙) 40875 魔法卷轴 (极光雷电) 40876 魔法卷轴 (起死回生术) 40878 魔法卷轴 (闇盲咒术)
 * 40880 魔法卷轴 (寒冰气息) 40881 魔法卷轴 (能量感测) 40883 魔法卷轴 (燃烧的火球) 40885 魔法卷轴 (坏物术) 40887
 * 魔法卷轴 (缓速术) 40888 魔法卷轴 (岩牢) 40891 魔法卷轴 (木乃伊的诅咒) 40892 魔法卷轴 (极道落雷) 40894 魔法卷轴
 * (迷魅术) 40896 魔法卷轴 (冰锥) 40898 魔法卷轴 (黑闇之影)
 * 
 */
public class MagicReel_Long2 extends ItemExecutor {

    /**
	 *
	 */
    private MagicReel_Long2() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new MagicReel_Long2();
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

        // 隐身状态可用魔法限制
        if (pc.isInvisble() || pc.isInvisDelay()) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final int targetID = data[0];
        final int spellsc_x = data[1];
        final int spellsc_y = data[2];

        if ((targetID == pc.getId()) || (targetID == 0)) {
            // 281 \f1施咒取消。
            pc.sendPackets(new S_ServerMessage(281));
            return;
        }

        final int useCount = 1;
        if (pc.getInventory().removeItem(item, useCount) >= useCount) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);

            final int itemId = item.getItemId();
            final int skillid = itemId - 40858;

            final L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(pc, skillid, targetID, spellsc_x,
                    spellsc_y, 0, L1SkillUse.TYPE_SPELLSC);
        }
    }
}

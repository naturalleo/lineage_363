package com.lineage.data.item_etcitem.brave;

import static com.lineage.server.model.skill.L1SkillId.BLOODLUST;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 名誉货币<br>
 * 名誉货币 - 40733<br>
 * 名誉货币(新手礼包) - 49299<br>
 */
public class Potion_6 extends ItemExecutor {

    /**
	 *
	 */
    private Potion_6() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Potion_6();
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

        // 血之渴望
        if (pc.hasSkillEffect(BLOODLUST)) {
            // 1,413：目前情况是无法使用。
            pc.sendPackets(new S_ServerMessage(1413));
            return;
        }

        // 具有生命之树果实效果
        if (pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE)) {
            // 1,413：目前情况是无法使用。
            pc.sendPackets(new S_ServerMessage(1413));
            return;
        }
        
        if (pc.isDragonKnight() || pc.isKnight() || pc.isCrown()
        		|| pc.isElf()) {
        	//
        } else {//hjx1000 限制名誉货币使用
        	return;
        }

        if (L1BuffUtil.stopPotion(pc)) {
            this.useBravePotion(pc);
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void useBravePotion(final L1PcInstance pc) {
        // // 解除魔法技能绝对屏障
        L1BuffUtil.cancelAbsoluteBarrier(pc);

        final int time = 600;

        // 勇敢效果 抵销对应技能
        L1BuffUtil.braveStart(pc);

        int mode = 1;
        int skillMode = L1SkillId.STATUS_BRAVE;
        /*
         * if (pc.isDarkelf()) { mode = 3; skillMode =
         * L1SkillId.STATUS_ELFBRAVE;
         * 
         * } else if (pc.isDragonKnight()) { mode = 3; skillMode =
         * L1SkillId.STATUS_ELFBRAVE; }
         */

        pc.sendPackets(new S_SkillBrave(pc.getId(), mode, time));
        pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), mode, 0));

        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 751));
        pc.setSkillEffect(skillMode, time * 1000);

        pc.setBraveSpeed(1);
    }
}

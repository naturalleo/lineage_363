package com.lineage.data.item_etcitem.quest;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static com.lineage.server.model.skill.L1SkillId.DE_LV30;
import static com.lineage.server.model.skill.L1SkillId.CKEW_LV50;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 破坏之秘药 49168
 */
public class CKEWLv50Medicine extends ItemExecutor {

    /**
	 *
	 */
    private CKEWLv50Medicine() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new CKEWLv50Medicine();
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
        if (pc.getMapId() != (short) CKEWLv50_1.MAPID) {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        if (pc.hasSkillEffect(DE_LV30)) {
            pc.removeSkillEffect(DE_LV30);
        }
        if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {
            pc.removeSkillEffect(STATUS_CURSE_YAHEE);
        }
        if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {
            pc.removeSkillEffect(STATUS_CURSE_BARLOG);
        }
        if (pc.hasSkillEffect(CKEW_LV50)) {
            pc.removeSkillEffect(CKEW_LV50);
        }

        if (pc.getWeapon() != null && !pc.isCrown()) {
            // 解除装备
            pc.getInventory().setEquipped(pc.getWeapon(), false, false, false);
            // 1,027：装备的武器被强制解除。
            pc.sendPackets(new S_ServerMessage(1027));
        }

        pc.setSkillEffect(CKEW_LV50, 1500 * 1000);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7248));
        // 1,300：神秘力量的影响正在发生。
        pc.sendPackets(new S_ServerMessage(1300));
        pc.getInventory().removeItem(item, 1);
    }
}

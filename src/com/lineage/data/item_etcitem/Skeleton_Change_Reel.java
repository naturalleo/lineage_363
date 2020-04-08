package com.lineage.data.item_etcitem;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 海贼骷髅首领变身药水41143 海贼骷髅士兵变身药水41144 海贼骷髅刀手变身药水41145
 */
public class Skeleton_Change_Reel extends ItemExecutor {

    /**
	 *
	 */
    private Skeleton_Change_Reel() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Skeleton_Change_Reel();
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
        final int itemId = item.getItemId();
        this.usePolyPotion(pc, itemId);
        pc.getInventory().removeItem(item, 1);
    }

    private void usePolyPotion(final L1PcInstance pc, final int itemId) {
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身。
            return;
        }
        int polyId = 0;
        if (itemId == 41143) { // 海贼骷髅首领变身药水
            polyId = 6086;
        } else if (itemId == 41144) { // 海贼骷髅士兵变身药水
            polyId = 6087;
        } else if (itemId == 41145) { // 海贼骷髅刀手变身药水
            polyId = 6088;
        }
        L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }
}

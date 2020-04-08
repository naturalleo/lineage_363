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
 * 暗之鳞41154<br>
 * 火之鳞41155<br>
 * 叛之鳞41156<br>
 * 恨之鳞41157<br>
 * 
 * @author dexc
 */
public class Scale extends ItemExecutor {

    /**
	 *
	 */
    private Scale() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Scale();
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
        this.usePolyScale(pc, itemId);
        pc.getInventory().removeItem(item, 1);
    }

    private void usePolyScale(final L1PcInstance pc, final int itemId) {
        final int awakeSkillId = pc.getAwakeSkillId();
        if ((awakeSkillId == AWAKEN_ANTHARAS)
                || (awakeSkillId == AWAKEN_FAFURION)
                || (awakeSkillId == AWAKEN_VALAKAS)) {
            pc.sendPackets(new S_ServerMessage(1384)); // 目前状态中无法变身.
            return;
        }

        int polyId = 0;
        if (itemId == 41154) { // 暗之鳞
            polyId = 3101;

        } else if (itemId == 41155) { // 火之鳞
            polyId = 3126;

        } else if (itemId == 41156) { // 叛之鳞
            polyId = 3888;

        } else if (itemId == 41157) { // 恨之鳞
            polyId = 3784;
        }
        L1PolyMorph.doPoly(pc, polyId, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC);
    }
}

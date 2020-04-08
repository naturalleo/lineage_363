package com.lineage.data.item_etcitem.brave;

import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 巧克力蛋糕 49138<br>
 */
public class CakeChocolate extends ItemExecutor {

    /**
	 *
	 */
    private CakeChocolate() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new CakeChocolate();
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

        if (pc.hasSkillEffect(STATUS_BRAVE3)) {
            pc.killSkillEffectTimer(STATUS_BRAVE3);
        }

        if (pc.getInventory().removeItem(item, 1) == 1) {
            // 巧克力蛋糕效果(速度增加1.15)
            pc.sendPacketsAll(new S_Liquor(pc.getId(), 0x08));

            // 1065:将发生神秘的奇迹力量。
            pc.sendPackets(new S_ServerMessage(1065));

            pc.sendPacketsX10(new S_SkillSound(pc.getId(), 8031));

            pc.setSkillEffect(STATUS_BRAVE3, 600 * 1000);
        }
    }
}

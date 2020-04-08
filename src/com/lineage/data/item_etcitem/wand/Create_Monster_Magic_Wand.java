package com.lineage.data.item_etcitem.wand;

import java.util.Random;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 创造怪物的魔杖40006 创造怪物的魔杖（祝福）140006
 */
public class Create_Monster_Magic_Wand extends ItemExecutor {

    /**
	 *
	 */
    private Create_Monster_Magic_Wand() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Create_Monster_Magic_Wand();
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
        final Random random = new Random();

        boolean isUse = true;

        // 送出封包(动作)
        pc.broadcastPacketX8(new S_DoActionGFX(pc.getId(),
                ActionCodes.ACTION_Wand));

        if (pc.getMap().isUsePainwand()) {
            final int chargeCount = item.getChargeCount();
            if (chargeCount <= 0) {
                // 无可用次数
                isUse = false;
            }

        } else {
            // System.out.println("禁止使用");
            isUse = false;
        }

        if (isUse) {
            final int[] mobArray = { 45008, 45140, 45016, 45021, 45025, 45033,
                    45099, 45147, 45123, 45130, 45046, 45092, 45138, 45098,
                    45127, 45143, 45149, 45171, 45040, 45155, 45192, 45173,
                    45213, 45079, 45144 };

            final int rnd = random.nextInt(mobArray.length);
            L1SpawnUtil.spawn(pc, mobArray[rnd], 0, 300);

            int count = item.getChargeCount() - 1;
            if (count <= 0) {
                count = 0;
            }
            item.setChargeCount(count);
            pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);

        } else {
            // 没有任何事情发生
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}

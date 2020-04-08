package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;

/**
 * 太古的玉玺41428 增加可登入人数1
 */
public class Ancient_Jade extends ItemExecutor {

    /**
	 *
	 */
    private Ancient_Jade() {
        // TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
        return new Ancient_Jade();
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
        if ((pc != null) && (item != null)) {
            L1Account account = pc.getNetConnection().getAccount();

            if (account == null) {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                return;
            }
            int characterSlot = account.get_character_slot();
            final int maxAmount = ConfigAlt.DEFAULT_CHARACTER_SLOT
                    + characterSlot;

            if (maxAmount >= 8) {
                pc.sendPackets(new S_ServerMessage(79)); // 没有任何事情发生。
                return;
            }
            // 移除道具
            pc.getInventory().removeItem(item, 1);

            if (characterSlot < 0) {
                characterSlot = 0;

            } else {
                characterSlot += 1;
            }

            account.set_character_slot(characterSlot);
            AccountReading.get().updateCharacterSlot(pc.getAccountName(),
                    characterSlot);

        } else {
            pc.sendPackets(new S_ServerMessage(79));
        }
    }

}

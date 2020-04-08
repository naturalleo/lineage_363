package com.lineage.server.serverpackets;

import java.util.HashMap;
import java.util.Map;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 卖(回收商)
 * 
 * @author dexc
 * 
 */
public class S_ShopBuyListAll extends ServerBasePacket {

    private byte[] _byte = null;

    public S_ShopBuyListAll(final L1PcInstance pc, final L1NpcInstance npc) {
        final Map<L1ItemInstance, Integer> assessedItems = this.assessItems(pc
                .getInventory());

        if (assessedItems.isEmpty()) {
            // 你并没有我需要的东西
            pc.sendPackets(new S_NoSell(npc));
            return;
        }

        if (assessedItems.size() <= 0) {
            // 你并没有我需要的东西
            pc.sendPackets(new S_NoSell(npc));
            return;
        }

        this.writeC(S_OPCODE_SHOWSHOPSELLLIST);
        this.writeD(npc.getId());

        this.writeH(assessedItems.size());

        for (final L1ItemInstance key : assessedItems.keySet()) {
            this.writeD(key.getId());
            this.writeD(assessedItems.get(key));
        }
    }

    /**
     * 传回物品价格
     * 
     * @param inv
     * @return
     */
    private Map<L1ItemInstance, Integer> assessItems(final L1PcInventory inv) {
        final Map<L1ItemInstance, Integer> result = new HashMap<L1ItemInstance, Integer>();
        for (final L1ItemInstance item : inv.getItems()) {
            switch (item.getItem().getItemId()) {
                case 40308: // 金币
                case 44070: // 天宝
                case 40314: // 项圈
                case 40316: // 高等宠物项圈
                    continue;
            }

            if (item.isEquipped()) {// 使用中
                continue;
            }
            final int key = item.getItemId();
            final int price = ShopTable.get().getPrice(key);
            if (price > 0) {
                result.put(item, price);
            }

        }
        return result;
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}

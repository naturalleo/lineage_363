package com.lineage.server.serverpackets;

import java.util.HashMap;
import java.util.Map;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1ShopS;

/**
 * 出售管理员商店(天宝)
 * 
 * @author dexc
 * 
 */
public class S_CnSShopSellList extends ServerBasePacket {

    private byte[] _byte = null;

    public S_CnSShopSellList(final L1PcInstance pc, final L1NpcInstance npc) {
        this.buildPacket(pc, npc.getId());
    }

    private void buildPacket(final L1PcInstance pc, final int tgObjid) {

        final Map<L1ShopS, L1ItemInstance> shopItems = new HashMap<L1ShopS, L1ItemInstance>();

        final Map<Integer, L1ItemInstance> srcMap = DwarfShopReading.get()
                .allItems();

        for (final Integer key : srcMap.keySet()) {
            L1ShopS info = DwarfShopReading.get().getShopS(key);
            if (info == null) {
                continue;
            }
            if (info.get_end() != 0) {// 物品非出售中
                continue;
            }
            if (info.get_item() == null) {// 物品设置为空
                continue;
            }

            L1ItemInstance item = srcMap.get(key);
            shopItems.put(info, item);
        }

        this.writeC(S_OPCODE_SHOWSHOPBUYLIST);

        this.writeD(tgObjid);

        if (shopItems.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(shopItems.size());

        int i = 0;
        for (final L1ShopS key : shopItems.keySet()) {
            i++;
            L1ItemInstance item = shopItems.get(key);
            pc.get_otherList().add_cnSList(item, i);

            this.writeD(i);// 排序编号
            this.writeH(item.getItem().getGfxId());
            this.writeD(key.get_adena());

            this.writeS(item.getLogName1());

            // 取回物品详细资讯
            final byte[] status = item.getStatusBytes(null);
            this.writeC(status.length);
            for (final byte b : status) {
                this.writeC(b);
            }
        }

        this.writeH(0x17d4); // 0x0000:无显示 0x0001:珍珠 0x0007:金币 0x17d4:天宝
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

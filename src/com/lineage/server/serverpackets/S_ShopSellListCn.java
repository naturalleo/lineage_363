package com.lineage.server.serverpackets;

import java.util.ArrayList;

import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.model.Instance.L1ItemStatus;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;

/**
 * 特殊商店(天宝)
 * 
 * @author dexc
 * 
 */
public class S_ShopSellListCn extends ServerBasePacket {

    private byte[] _byte = null;

    public S_ShopSellListCn(final L1PcInstance pc, final L1NpcInstance npc) {
        this.buildPacket(pc, npc.getId(), npc.getNpcId());
    }

    private void buildPacket(final L1PcInstance pc, final int tgObjid,
            final int npcid) {
        this.writeC(S_OPCODE_SHOWSHOPBUYLIST);

        this.writeD(tgObjid);

        final ArrayList<L1ShopItem> shopItems = ShopCnTable.get().get(npcid);

        if (shopItems.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(shopItems.size());

        int i = 0;
        for (final L1ShopItem shopItem : shopItems) {
            i++;
            pc.get_otherList().add_cnList(shopItem, i);

            final L1Item item = shopItem.getItem();

            this.writeD(i);
            this.writeH(item.getGfxId());
            this.writeD(shopItem.getPrice());
            if (shopItem.getPackCount() > 1) {
                this.writeS(item.getNameId() + " (" + shopItem.getPackCount()
                        + ")");

            } else {
                this.writeS(item.getNameId());
            }

            final L1ItemStatus itemInfo = new L1ItemStatus(item);
            // 取回物品资讯
            final byte[] status = itemInfo.getStatusBytes(null).getBytes();
            this.writeC(status.length);
            for (final byte b : status) {
                this.writeC(b);
            }
            // 降低封包量 不传送详细资讯
            // this.writeC(0x00);
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

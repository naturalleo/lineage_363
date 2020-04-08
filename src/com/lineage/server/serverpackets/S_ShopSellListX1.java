package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.world.World;

/**
 * 未知购物清单1
 * 
 * @author dexc
 * 
 */
public class S_ShopSellListX1 extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 未知购物清单1
     */
    public S_ShopSellListX1(final int objId) {
        // System.out.println("S_ShopSellList 1");
        this.writeC(S_OPCODE_SHOPX1);
        this.writeD(objId);

        final L1Object npcObj = World.get().findObject(objId);
        if (!(npcObj instanceof L1NpcInstance)) {
            this.writeH(0x0000);
            return;
        }
        final int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

        final L1TaxCalculator calc = new L1TaxCalculator(npcId);
        final L1Shop shop = ShopTable.get().get(npcId);
        final List<L1ShopItem> shopItems = shop.getSellingItems();

        if (shopItems.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(shopItems.size());

        for (int i = 0; i < shopItems.size(); i++) {
            final L1ShopItem shopItem = shopItems.get(i);
            final L1Item item = shopItem.getItem();
            final int price = calc
                    .layTax((int) (shopItem.getPrice() * ConfigRate.RATE_SHOP_SELLING_PRICE));
            this.writeH(i);// 排序

            this.writeH(shopItem.getItem().getGfxId());// 图形

            this.writeH(price);// 售价

            if (shopItem.getPackCount() > 1) {
                this.writeS(item.getNameId() + " (" + shopItem.getPackCount()
                        + ")");// 名称

            } else {
                this.writeS(item.getNameId());// 名称
            }

            // 降低封包量 不传送详细资讯
            this.writeC(0x00);
            /*
             * final L1Item template =
             * ItemTable.getInstance().getTemplate(item.getItemId()); if
             * (template == null) { this.writeC(0x00);
             * 
             * } else { final L1ItemStatus itemInfo = new
             * L1ItemStatus(template); // 取回物品资讯 final byte[] status =
             * itemInfo.getStatusBytes().getBytes(); this.writeC(status.length);
             * for (final byte b : status) { this.writeC(b); } }
             */
        }
        this.writeH(0x0000); // 0x0000:无显示 0x0001:珍珠 0x0007:金币 0x17d4:天宝
    }

    /**
     * 未知购物清单1
     */
    public S_ShopSellListX1(final L1NpcInstance npc) {
        this.writeC(S_OPCODE_SHOPX1);
        this.writeD(npc.getId());

        final int npcId = npc.getNpcTemplate().get_npcId();

        final L1Shop shop = ShopTable.get().get(npcId);
        final List<L1ShopItem> shopItems = shop.getSellingItems();

        if (shopItems.size() <= 0) {
            this.writeH(0x0000);
            return;
        }

        this.writeH(shopItems.size());

        for (int i = 0; i < shopItems.size(); i++) {
            final L1ShopItem shopItem = shopItems.get(i);
            final L1Item item = shopItem.getItem();
            final int price = shopItem.getPrice();
            this.writeD(i);// 排序
            this.writeH(shopItem.getItem().getGfxId());// 图形
            this.writeD(price);// 售价
            if (shopItem.getPackCount() > 1) {
                this.writeS(item.getNameId() + " (" + shopItem.getPackCount()
                        + ")");

            } else {
                this.writeS(item.getNameId());// 名称
            }

            // 降低封包量 不传送详细资讯
            this.writeC(0x00);
            /*
             * final L1Item template =
             * ItemTable.getInstance().getTemplate(item.getItemId()); if
             * (template == null) { this.writeC(0x00);
             * 
             * } else { final L1ItemStatus itemInfo = new
             * L1ItemStatus(template); // 取回物品资讯 final byte[] status =
             * itemInfo.getStatusBytes().getBytes(); this.writeC(status.length);
             * for (final byte b : status) { this.writeC(b); } }
             */
        }
        this.writeH(0x0000); // 0x0000:无显示 0x0001:珍珠 0x0007:金币 0x17d4:天宝
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

package com.lineage.server.model.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

/**
 * 购物相关
 * 
 * @author dexc
 * 
 */
public class L1Shop {

    private final int _npcId;

    private final List<L1ShopItem> _sellingItems;

    private final List<L1ShopItem> _purchasingItems;

    public L1Shop(final int npcId, final List<L1ShopItem> sellingItems,
            final List<L1ShopItem> purchasingItems) {
        if ((sellingItems == null) || (purchasingItems == null)) {
            throw new NullPointerException();
        }

        this._npcId = npcId;
        this._sellingItems = sellingItems;
        this._purchasingItems = purchasingItems;
    }

    public int getNpcId() {
        return this._npcId;
    }

    public List<L1ShopItem> getSellingItems() {
        return this._sellingItems;
    }

    /**
     * この商店で、指定されたアイテムが买取可能な状态であるかを返す。
     * 
     * @param item
     * @return アイテムが买取可能であればtrue
     */
    private boolean isPurchaseableItem(final L1ItemInstance item) {
        if (item == null) {
            return false;
        }
        if (item.isEquipped()) { // 装备中不可
            return false;
        }
        if (item.getEnchantLevel() != 0) { // 强化(or弱化)不可
            return false;
        }
        if (item.getBless() >= 128) { // 封印装备
            return false;
        }

        return true;
    }

    private L1ShopItem getPurchasingItem(final int itemId) {
        for (final L1ShopItem shopItem : this._purchasingItems) {
            if (shopItem.getItemId() == itemId) {
                return shopItem;
            }
        }
        return null;
    }

    public L1AssessedItem assessItem(final L1ItemInstance item) {
        final L1ShopItem shopItem = this.getPurchasingItem(item.getItemId());
        if (shopItem == null) {
            return null;
        }
        return new L1AssessedItem(item.getId(), this.getAssessedPrice(shopItem));
    }

    private int getAssessedPrice(final L1ShopItem item) {
        return (int) (item.getPrice() * ConfigRate.RATE_SHOP_PURCHASING_PRICE / item
                .getPackCount());
    }

    /**
     * インベントリ内の买取可能アイテムを查定する。
     * 
     * @param inv
     *            查定对象のインベントリ
     * @return 查定された买取可能アイテムのリスト
     */
    public List<L1AssessedItem> assessItems(final L1PcInventory inv) {
        final List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
        for (final L1ShopItem item : this._purchasingItems) {
            for (final L1ItemInstance targetItem : inv.findItemsId(item
                    .getItemId())) {
                if (!this.isPurchaseableItem(targetItem)) {
                    continue;
                }

                result.add(new L1AssessedItem(targetItem.getId(), this
                        .getAssessedPrice(item)));
            }
        }
        return result;
    }

    /**
     * プレイヤーへアイテムを贩卖できることを保证する。
     * 
     * @return 何らかの理由でアイテムを贩卖できない场合、false
     */
    private boolean ensureSell(final L1PcInstance pc,
            final L1ShopBuyOrderList orderList) {
        final int price = orderList.getTotalPriceTaxIncluded();
        // 检查贩卖物品总价
        if (!RangeInt.includes(price, 0, 2000000000)) {
            // 904 总共贩卖价格无法超过 %d金币。
            pc.sendPackets(new S_ServerMessage(904, "2000000000"));
            return false;
        }
        // 检查背包金币数量
        if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
            // System.out.println(price);
            // 189 \f1金币不足。
            pc.sendPackets(new S_ServerMessage(189));
            return false;
        }
        // 检查重量限制
        final int currentWeight = pc.getInventory().getWeight() * 1000;
        if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
            // 82 此物品太重了，所以你无法携带。
            pc.sendPackets(new S_ServerMessage(82));
            return false;
        }
        // 检查买入数量
        int totalCount = pc.getInventory().getSize();
        for (final L1ShopBuyOrder order : orderList.getList()) {
            final L1Item temp = order.getItem().getItem();
            if (temp.isStackable()) {
                if (!pc.getInventory().checkItem(temp.getItemId())) {
                    totalCount += 1;
                }
            } else {
                totalCount += 1;
            }
        }
        if (totalCount > 180) {
            // 263 \f1一个角色最多可携带180个道具。
            pc.sendPackets(new S_ServerMessage(263));
            return false;
        }
        
//        int Count = 0;
//        int new_price = 0;
//        int set_count = 0;
//        for (L1ShopBuyOrder order : orderList.getList())
//        {
//          L1Item temp = order.getItem().getItem();
//          Count++;
//          new_price += orderList.getTotalPrice();
//        }
//        set_count = 2000000000 / new_price;
//        System.out.println("new_price== " + new_price);
//        System.out.println("set_count== " + set_count);
//        System.out.println("Count== " + Count);
//        if (Count > set_count)
//        {
//          pc.sendPackets(new S_ServerMessage(936));
//          return false;
//        }
        
        return true;
    }

    /**
     * 地域税纳税处理 アデン城?ディアド要塞を除く城はアデン城へ国税として10%纳税する
     * 
     * @param orderList
     */
    private void payCastleTax(final L1ShopBuyOrderList orderList) {
        final L1TaxCalculator calc = orderList.getTaxCalculator();

        final int price = orderList.getTotalPrice();

        final int castleId = L1CastleLocation.getCastleIdByNpcid(this._npcId);
        int castleTax = calc.calcCastleTaxPrice(price);
        int nationalTax = calc.calcNationalTaxPrice(price);
        // アデン城?ディアド城の场合は国税なし
        if ((castleId == L1CastleLocation.ADEN_CASTLE_ID)
                || (castleId == L1CastleLocation.DIAD_CASTLE_ID)) {
            castleTax += nationalTax;
            nationalTax = 0;
        }

        if ((castleId != 0) && (castleTax > 0)) {
            final L1Castle castle = CastleReading.get()
                    .getCastleTable(castleId);

            synchronized (castle) {
                long money = castle.getPublicMoney();
                money = money + castleTax;
                castle.setPublicMoney(money);
                CastleReading.get().updateCastle(castle);
            }

            if (nationalTax > 0) {
                final L1Castle aden = CastleReading.get().getCastleTable(
                		//修改主城税收为奇岩 hjx1000
                        L1CastleLocation.GIRAN_CASTLE_ID);
                synchronized (aden) {
                    long money = aden.getPublicMoney();
                    money = money + nationalTax;
                    aden.setPublicMoney(money);
                    CastleReading.get().updateCastle(aden);
                }
            }
        }
    }

    /**
     * ディアド税纳税处理 战争税の10%がディアド要塞の公金となる。
     * 
     * @param orderList
     */
    private void payDiadTax(final L1ShopBuyOrderList orderList) {
        final L1TaxCalculator calc = orderList.getTaxCalculator();

        final int price = orderList.getTotalPrice();

        // ディアド税
        final int diadTax = calc.calcDiadTaxPrice(price);
        if (diadTax <= 0) {
            return;
        }

        final L1Castle castle = CastleReading.get().getCastleTable(
        		//修改主城税收为奇岩 hjx1000
                L1CastleLocation.GIRAN_CASTLE_ID);
        synchronized (castle) {
            long money = castle.getPublicMoney();
            money = money + diadTax;
            castle.setPublicMoney(money);
            CastleReading.get().updateCastle(castle);
        }
    }

    /**
     * 町税纳税处理
     * 
     * @param orderList
     */
    private void payTownTax(final L1ShopBuyOrderList orderList) {
        final int price = orderList.getTotalPrice();
        // 町の卖上
        if (!World.get().isProcessingContributionTotal()) {
            final int town_id = L1TownLocation.getTownIdByNpcid(this._npcId);
            if ((town_id >= 1) && (town_id <= 10)) {
                TownReading.get().addSalesMoney(town_id, price);
            }
        }
    }

    // XXX 纳税处理はこのクラスの责务では无い气がするが、とりあえず
    private void payTax(final L1ShopBuyOrderList orderList) {
        this.payCastleTax(orderList);
        this.payTownTax(orderList);
        this.payDiadTax(orderList);
    }

    /**
     * 取回买入物品
     */
    private void sellItems(final L1PcInventory inv,
            final L1ShopBuyOrderList orderList) {
        if (orderList == null) {
            return;
        }
        final int priceTax = orderList.getTotalPriceTaxIncluded();
        if (priceTax <= 0) {
            return;
        }
        if (!inv.consumeItem(L1ItemId.ADENA, priceTax)) {
            return;// 购买物品时金币不足
        }
        for (final L1ShopBuyOrder order : orderList.getList()) {
            final int itemId = order.getItem().getItemId();
            final long amount = order.getCount();

            if (amount <= 0) {
                continue;
            }
            final L1ItemInstance item = ItemTable.get().createItem(itemId);
            item.setCount(amount);
            item.setIdentified(true);
            inv.storeItem(item);
            if ((this._npcId == 70068) || (this._npcId == 70020)) {// 随机给予强化质的NPC
                item.setIdentified(false);
                final Random random = new Random();
                final int chance = random.nextInt(100) + 1;
                if (chance <= 15) {
                    item.setEnchantLevel(-2);

                } else if ((chance >= 16) && (chance <= 30)) {
                    item.setEnchantLevel(-1);

                } else if ((chance >= 31) && (chance <= 70)) {
                    item.setEnchantLevel(0);

                } else if ((chance >= 71) && (chance <= 87)) {
                    item.setEnchantLevel(random.nextInt(2) + 1);

                } else if ((chance >= 88) && (chance <= 97)) {
                    item.setEnchantLevel(random.nextInt(3) + 3);

                } else if ((chance >= 98) && (chance <= 99)) {
                    item.setEnchantLevel(6);

                } else if (chance == 100) {
                    item.setEnchantLevel(7);
                }
            }
        }
    }

    /**
     * プレイヤーに、L1ShopBuyOrderListに记载されたアイテムを贩卖する。
     * 
     * @param pc
     *            贩卖するプレイヤー
     * @param orderList
     *            贩卖すべきアイテムが记载されたL1ShopBuyOrderList
     */
    public void sellItems(final L1PcInstance pc,
            final L1ShopBuyOrderList orderList) {
        if (orderList.isEmpty()) {
            return;
        }
        if (!this.ensureSell(pc, orderList)) {
            return;
        }

        this.sellItems(pc.getInventory(), orderList);
        this.payTax(orderList);
    }

    /**
     * L1ShopSellOrderListに记载されたアイテムを买い取る。
     * 
     * @param orderList
     *            买い取るべきアイテムと価格が记载されたL1ShopSellOrderList
     */
    public void buyItems(final L1ShopSellOrderList orderList) {
        final L1PcInventory inv = orderList.getPc().getInventory();
        int totalPrice = 0;
        for (final L1ShopSellOrder order : orderList.getList()) {
            final long count = inv.removeItem(order.getItem().getTargetId(),
                    order.getCount());
            totalPrice += order.getItem().getAssessedPrice() * count;
        }

        totalPrice = RangeInt.ensure(totalPrice, 0, 2000000000);
        if (0 < totalPrice) {
            inv.storeItem(L1ItemId.ADENA, totalPrice);
        }
    }

    public L1ShopBuyOrderList newBuyOrderList() {
        return new L1ShopBuyOrderList(this);
    }

    public L1ShopSellOrderList newSellOrderList(final L1PcInstance pc) {
        return new L1ShopSellOrderList(this, pc);
    }
}

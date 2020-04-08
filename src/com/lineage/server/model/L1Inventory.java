package com.lineage.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.datatables.sql.LetterTable;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;

/**
 * 背包
 * 
 * @author dexc
 * 
 */
public class L1Inventory extends L1Object {

    private static final Log _log = LogFactory.getLog(L1Inventory.class);

    private static final long serialVersionUID = 1L;

    protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<L1ItemInstance>();

    public static final int MAX_WEIGHT = 1500;

    public L1Inventory() {
        //
    }

    /**
     * 背包内全部数量
     * 
     * @return
     */
    public int getSize() {
        if (this._items.isEmpty()) {
            return 0;
        }
        return this._items.size();
    }

    /**
     * 背包内全物件清单
     * 
     * @return
     */
    public List<L1ItemInstance> getItems() {
        return this._items;
    }

    /**
     * 背包内全部重量
     * 
     * @return
     */
    public int getWeight() {
        int weight = 0;

        for (final L1ItemInstance item : this._items) {
            weight += item.getWeight();
        }

        return weight;
    }

    public static final int OK = 0;// 成功

    public static final int SIZE_OVER = 1;// 超过数量

    public static final int WEIGHT_OVER = 2;// 超过可携带重量

    public static final int AMOUNT_OVER = 3;// 超过LONG最大质

    public int checkAddItem(final int item, final long count) {
        return -1;
    }

    /**
     * 增加物品是否成功(背包)
     * 
     * @param item
     *            物品
     * @param count
     *            数量
     * @return 0:成功 1:超过可携带数量 2:超过可携带重量 3:超过LONG最大质
     */
    public int checkAddItem(final L1ItemInstance item, final long count) {
        if (item == null) {
            return -1;
        }

        if ((item.getCount() <= 0) || (count <= 0)) {
            return -1;
        }

        if ((this.getSize() > ConfigAlt.MAX_NPC_ITEM)
                || ((this.getSize() == ConfigAlt.MAX_NPC_ITEM) && (!item
                        .isStackable() || !this.checkItem(item.getItem()
                        .getItemId())))) { // 容量确认
            return SIZE_OVER;
        }

        final long weight = this.getWeight() + item.getItem().getWeight()
                * count / 1000 + 1;
        if ((weight < 0) || ((item.getItem().getWeight() * count / 1000) < 0)) {
            return WEIGHT_OVER;
        }
        if (weight > (MAX_WEIGHT * ConfigRate.RATE_WEIGHT_LIMIT_PET)) { // 重量确认
            return WEIGHT_OVER;
        }

        final L1ItemInstance itemExist = this.findItemId(item.getItemId());
        if ((itemExist != null)
                && ((itemExist.getCount() + count) > Long.MAX_VALUE)) {
            return AMOUNT_OVER;
        }

        return OK;
    }

    public static final int WAREHOUSE_TYPE_PERSONAL = 0;// 个人/精灵仓库

    public static final int WAREHOUSE_TYPE_CLAN = 1;// 血盟仓库

    /**
     * 增加物品是否成功(仓库)
     * 
     * @param item
     *            物品
     * @param count
     *            数量
     * @param type
     *            模式 0:个人/精灵仓库 1:血盟仓库
     * @return 0:成功 1:超过数量
     */
    public int checkAddItemToWarehouse(final L1ItemInstance item,
            final long count, final int type) {
        if (item == null) {
            return -1;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return -1;
        }

        int maxSize = 100;
        if (type == WAREHOUSE_TYPE_PERSONAL) {
            maxSize = ConfigAlt.MAX_PERSONAL_WAREHOUSE_ITEM;

        } else if (type == WAREHOUSE_TYPE_CLAN) {
            maxSize = ConfigAlt.MAX_CLAN_WAREHOUSE_ITEM;
        }
        if ((this.getSize() > maxSize)
                || ((this.getSize() == maxSize) && (!item.isStackable() || !this
                        .checkItem(item.getItem().getItemId())))) { // 容量确认
            return SIZE_OVER;
        }

        return OK;
    }

    /**
     * 全新物件加入背包
     * 
     * @param id
     * @param count
     * @return
     */
    public synchronized L1ItemInstance storeItem(final int id, final long count) {
        try {
            if (count <= 0) {
                return null;
            }
            final L1Item temp = ItemTable.get().getTemplate(id);
            if (temp == null) {
                return null;
            }

            if (temp.isStackable()) {
                final L1ItemInstance item = new L1ItemInstance(temp, count);

                if (this.findItemId(id) == null) { // 新しく生成する必要がある场合のみIDの発行とL1Worldへの登录を行う
                    item.setId(IdFactory.get().nextId());
                    World.get().storeObject(item);
                }

                return this.storeItem(item);
            }

            // スタックできないアイテムの场合
            L1ItemInstance result = null;
            for (int i = 0; i < count; i++) {
                final L1ItemInstance item = new L1ItemInstance(temp, 1);
                item.setId(IdFactory.get().nextId());
                World.get().storeObject(item);
                this.storeItem(item);
                result = item;
            }
            // 最后に作ったアイテムを返す。配列を戻すようにメソッド定义を变更したほうが良いかもしれない。
            return result;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 背包中新物品的增加 (物品购买/道具交换)
     * 
     * @param item
     * @return
     */
    public synchronized L1ItemInstance storeItem(final L1ItemInstance item) {
        try {
            if (item == null) {
                return null;
            }
            if (item.getCount() <= 0) {
                return null;
            }

            if (item.isStackable()) {
                if (item.getItem().getUseType() == -5) {// 食人妖精竞赛票
                    final L1ItemInstance[] items = this.findItemsId(item
                            .getItemId());
                    // System.out.println(items);
                    for (final L1ItemInstance tgitem : items) {
                        final String gamNo = tgitem.getGamNo();
                        if (item.getGamNo().equals(gamNo)) {
                            tgitem.setCount(tgitem.getCount() + item.getCount());
                            this.updateItem(tgitem);
                            return tgitem;
                        }
                    }

                } else {
                    final L1ItemInstance findItem = this.findItemId(item
                            .getItem().getItemId());
                    if (findItem != null) {
                        findItem.setCount(findItem.getCount() + item.getCount());
                        this.updateItem(findItem);
                        return findItem;
                    }
                }
            }
            item.setX(this.getX());
            item.setY(this.getY());
            item.setMap(this.getMapId());

            // 资料库最大可用次数
            int chargeCount = item.getItem().getMaxChargeCount();

            // 魔杖类次数给予判断
            switch (item.getItem().getItemId()) {
                case 20383: // 军马头盔
                    chargeCount = 50;
                    break;

                case 40006: // 创造怪物魔杖
                case 140006: // 创造怪物魔杖

                case 40008: // 变形魔杖
                case 140008: // 变形魔杖

                case 40007: // 闪电魔杖
                case 40009: // 驱逐魔杖
                    final Random random1 = new Random();
                    chargeCount -= random1.nextInt(5);
                    break;

                default:
                    break;
            }

            item.setChargeCount(chargeCount);

            if ((item.getItem().getType2() == 0)
                    && (item.getItem().getType() == 2)) { // 照明道具时间设置
                item.setRemainingTime(item.getItem().getLightFuel());

            } else {
                item.setRemainingTime(item.getItem().getMaxUseTime());
            }

            item.setBless(item.getItem().getBless());

            this._items.add(item);
            this.insertItem(item);
            return item;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 背包中新物品的增加 (仓库取回/仓库存入/丢弃/捡取)
     * 
     * @param item
     * @return
     */
    public synchronized L1ItemInstance storeTradeItem(final L1ItemInstance item) {
        try {
            if (item == null) {
                return null;
            }
            if (item.getCount() <= 0) {
                return null;
            }

            if (item.isStackable()) {
                if (item.getItem().getUseType() == -5) {// 食人妖精竞赛票/死亡竞赛票/彩票
                    final L1ItemInstance[] items = this.findItemsId(item
                            .getItemId());
                    // System.out.println(items);
                    for (final L1ItemInstance tgitem : items) {
                        final String gamNo = tgitem.getGamNo();
                        if (item.getGamNo().equals(gamNo)) {
                            tgitem.setCount(tgitem.getCount() + item.getCount());
                            this.updateItem(tgitem);
                            return tgitem;
                        }
                    }

                } else {
                    final L1ItemInstance findItem = this.findItemId(item
                            .getItem().getItemId());
                    if (findItem != null) {
                        findItem.setCount(findItem.getCount() + item.getCount());
                        this.updateItem(findItem);
                        return findItem;
                    }

                }
            }
            item.setX(this.getX());
            item.setY(this.getY());
            item.setMap(this.getMapId());
            /*
             * if (!this._items.contains(item)) { this._items.add(item); }
             */
            this._items.add(item);
            this.insertItem(item);
            return item;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 删除指定编号物品及数量
     * 
     * @param itemid
     *            - 删除物品的编号
     * @param count
     *            - 删除的数量
     * @return true:删除完成 false:删除失败
     */
    public boolean consumeItem(final int itemid, final long count) {
        if (count <= 0) {
            return false;
        }
        // 物品可以堆叠
        if (ItemTable.get().getTemplate(itemid).isStackable()) {
            final L1ItemInstance item = this.findItemId(itemid);
            if ((item != null) && (item.getCount() >= count)) {
                this.removeItem(item, count);
                return true;
            }

        } else {
            final L1ItemInstance[] itemList = this.findItemsId(itemid);
            if (itemList.length == count) {
                for (int i = 0; i < count; i++) {
                    this.removeItem(itemList[i], 1);
                }
                return true;

            } else if (itemList.length > count) {
                // 指定物品具有多个
                final DataComparator dc = new DataComparator();
                Arrays.sort(itemList, dc); // 按照强化质 由低至高排列
                for (int i = 0; i < count; i++) {
                    // 先由强化质低的开始移除
                    this.removeItem(itemList[i], 1);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 按照强化质 由低至高排列物品
     * 
     * @author daien
     * 
     */
    public class DataComparator implements Comparator<Object> {
        @Override
        public int compare(final Object item1, final Object item2) {
            return ((L1ItemInstance) item1).getEnchantLevel()
                    - ((L1ItemInstance) item2).getEnchantLevel();
        }
    }

    /**
     * 移转物品
     * 
     * @param objectId
     * @param count
     * @return
     */
    public L1ItemInstance shiftingItem(final int objectId, final long count) {
        final L1ItemInstance item = this.getItem(objectId);
        if (item == null) {
            return null;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return null;
        }
        if (item.getCount() < count) {
            return null;
        }
        if (item.getCount() == count) {
            if (!item.isEquipped()) {
                this.deleteItem(item);
                return item;
            }

        }
        return null;
    }

    /**
     * 指定OBJID以及数量 删除物品
     * 
     * @param objectId
     * @param count
     * @return 实际删除数量
     */
    public long removeItem(final int objectId, final long count) {
        final L1ItemInstance item = this.getItem(objectId);
        return this.removeItem(item, count);
    }

    /**
     * 指定物品(全部数量) 删除物品
     * 
     * @param item
     * @return 实际删除数量
     */
    public long removeItem(final L1ItemInstance item) {
        return this.removeItem(item, item.getCount());
    }

    /**
     * 指定物品以及数量 删除物品
     * 
     * @param item
     * @param count
     * @return 实际删除数量
     */
    public long removeItem(final L1ItemInstance item, long count) {
        if (item == null) {
            return 0;
        }
        if (!_items.contains(item)) {
            return 0;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return 0;
        }
        if (item.getCount() < count) {
            count = item.getCount();
        }
        if (item.getCount() == count) {
            final int itemId = item.getItem().getItemId();
            if ((itemId >= 49016) && (itemId <= 49025)) { // 便笺
                final LetterTable lettertable = new LetterTable();
                lettertable.deleteLetter(item.getId());

            } else if ((itemId >= 41383) && (itemId <= 41400)) { // 家具
                for (final L1Object l1object : World.get().getObject()) {
                    if (l1object instanceof L1FurnitureInstance) {
                        final L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
                        if (furniture.getItemObjId() == item.getId()) { // 既に引き出している家具
                            FurnitureSpawnReading.get().deleteFurniture(
                                    furniture);
                        }
                    }
                }
            }
            this.deleteItem(item);
            World.get().removeObject(item);

        } else {
            item.setCount(item.getCount() - count);
            this.updateItem(item);
        }
        return count;
    }

    /**
     * 物品资料消除
     * 
     * @param item
     */
    public void deleteItem(final L1ItemInstance item) {
        this._items.remove(item);
    }

    // 引数のインベントリにアイテムを移让
    public synchronized L1ItemInstance tradeItem(final int objectId,
            final long count, final L1Inventory inventory) {
        final L1ItemInstance item = this.getItem(objectId);
        return this.tradeItem(item, count, inventory);
    }

    /**
     * 物品转移
     * 
     * @param item
     *            转移的物品
     * @param count
     *            移出的数量
     * @param showId
     *            副本编号
     * @param inventory
     *            移出对象的背包
     */
    public synchronized L1ItemInstance tradeItem(L1ItemInstance item,
            int count, int showId, L1GroundInventory inventory) {
        if (item == null) {
            return null;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return null;
        }
        if (item.isEquipped()) {
            return null;
        }
        if (item.getCount() < count) {
            return null;
        }
        /*
         * if (!this.checkItem(item.getItem().getItemId(), count)) { return
         * null; }
         */
        L1ItemInstance carryItem;
        if (item.getCount() == count) {
            this.deleteItem(item);
            carryItem = item;
            // 副本编号
            carryItem.set_showId(showId);

        } else {
            item.setCount(item.getCount() - count);
            this.updateItem(item);
            carryItem = ItemTable.get().createItem(item.getItem().getItemId());
            // 副本编号
            carryItem.set_showId(showId);
            carryItem.setCount(count);
            carryItem.setEnchantLevel(item.getEnchantLevel());
            carryItem.setIdentified(item.isIdentified());
            carryItem.set_durability(item.get_durability());
            carryItem.setChargeCount(item.getChargeCount());
            carryItem.setRemainingTime(item.getRemainingTime());
            carryItem.setLastUsed(item.getLastUsed());
            carryItem.setBless(item.getBless());
        }

        return inventory.storeTradeItem(carryItem);
    }

    /**
     * 物品转移
     * 
     * @param item
     *            转移的物品
     * @param count
     *            移出的数量
     * @param inventory
     *            移出对象的背包
     * @return
     */
    public synchronized L1ItemInstance tradeItem(final L1ItemInstance item,
            final long count, final L1Inventory inventory) {
        if (item == null) {
            return null;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return null;
        }
        if (item.isEquipped()) {
            return null;
        }
        if (item.getCount() < count) {
            return null;
        }
        /*
         * if (!this.checkItem(item.getItem().getItemId(), count)) { return
         * null; }
         */
        L1ItemInstance carryItem;
        if (item.getCount() == count) {
            this.deleteItem(item);
            carryItem = item;

        } else {
            item.setCount(item.getCount() - count);
            this.updateItem(item);
            carryItem = ItemTable.get().createItem(item.getItem().getItemId());
            carryItem.setCount(count);
            carryItem.setEnchantLevel(item.getEnchantLevel());
            carryItem.setIdentified(item.isIdentified());
            carryItem.set_durability(item.get_durability());
            carryItem.setChargeCount(item.getChargeCount());
            carryItem.setRemainingTime(item.getRemainingTime());
            carryItem.setLastUsed(item.getLastUsed());
            carryItem.setBless(item.getBless());
        }
        return inventory.storeTradeItem(carryItem);
    }
    
    /**
     * 物品转移
     * 
     * @param item
     *            转移的物品
     * @param count
     *            移出的数量
     * @param inventory
     *            移出对象的背包
     * @return
     */
    public synchronized L1ItemInstance tradeItembuypc(final L1ItemInstance item,
            final long count, final L1Inventory inventory) {
        if (item == null) {
            return null;
        }
        if ((item.getCount() <= 0) || (count <= 0)) {
            return null;
        }
        if (item.isEquipped()) {
            return null;
        }
        if (item.getCount() < count) {
            return null;
        }
        /*
         * if (!this.checkItem(item.getItem().getItemId(), count)) { return
         * null; }
         */
        L1ItemInstance carryItem;
        if (item.getCount() == count) {
            this.deleteItem(item);
            carryItem = item;

        } else {
            item.setCount(item.getCount() - count);
            this.updateItemNow(item);//修正离线商店出售物品可以刷物BUG hjx1000
            carryItem = ItemTable.get().createItem(item.getItem().getItemId());
            carryItem.setCount(count);
            carryItem.setEnchantLevel(item.getEnchantLevel());
            carryItem.setIdentified(item.isIdentified());
            carryItem.set_durability(item.get_durability());
            carryItem.setChargeCount(item.getChargeCount());
            carryItem.setRemainingTime(item.getRemainingTime());
            carryItem.setLastUsed(item.getLastUsed());
            carryItem.setBless(item.getBless());
        }
        return inventory.storeTradeItem(carryItem);
    }

    /**
     * アイテムを损伤?损耗させる（武器?防具も含む） アイテムの场合、损耗なのでマイナスするが 武器?防具は损伤度を表すのでプラスにする。
     */
    public L1ItemInstance receiveDamage(final int objectId) {
        final L1ItemInstance item = this.getItem(objectId);
        return this.receiveDamage(item);
    }

    public L1ItemInstance receiveDamage(final L1ItemInstance item) {
        return this.receiveDamage(item, 1);
    }

    public L1ItemInstance receiveDamage(final L1ItemInstance item,
            final int count) {
        if (item == null) {
            return null;
        }
        final int itemType = item.getItem().getType2();
        final int currentDurability = item.get_durability();

        if (((currentDurability == 0) && (itemType == 0))
                || (currentDurability < 0)) {
            item.set_durability(0);
            return null;
        }

        // 武器?防具のみ损伤度をプラス
        if (itemType == 0) {
            final int minDurability = (item.getEnchantLevel() + 5) * -1;
            int durability = currentDurability - count;
            if (durability < minDurability) {
                durability = minDurability;
            }
            if (currentDurability > durability) {
                item.set_durability(durability);
            }
        } else {
            final int maxDurability = item.getEnchantLevel() + 5;
            int durability = currentDurability + count;
            if (durability > maxDurability) {
                durability = maxDurability;
            }
            if (currentDurability < durability) {
                item.set_durability(durability);
            }
        }

        this.updateItem(item, L1PcInventory.COL_DURABILITY);
        return item;
    }

    public L1ItemInstance recoveryDamage(final L1ItemInstance item) {
        if (item == null) {
            return null;
        }
        final int itemType = item.getItem().getType2();
        final int durability = item.get_durability();

        if (((durability == 0) && (itemType != 0)) || (durability < 0)) {
            item.set_durability(0);
            return null;
        }

        if (itemType == 0) {
            // 耐久度をプラスしている。
            item.set_durability(durability + 1);
        } else {
            // 损伤度をマイナスしている。
            item.set_durability(durability - 1);
        }

        this.updateItem(item, L1PcInventory.COL_DURABILITY);
        return item;
    }

    /**
     * 找寻指定物品(未装备)
     * 
     * @param itemId
     * @return
     */
    public L1ItemInstance findItemIdNoEq(final int itemId) {
        for (final L1ItemInstance item : this._items) {
            if (item.getItem().getItemId() == itemId && !item.isEquipped()) {
                if (item.get_time() == null) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 找寻指定物品<BR>
     * 不检查装备状态
     * 
     * @param itemId
     * @return
     */
    public L1ItemInstance findItemId(final int itemId) {
        for (final L1ItemInstance item : this._items) {
            if (item.getItem().getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    /**
     * 找寻指定物品
     * 
     * @param nameid
     * @return
     */
    public L1ItemInstance findItemId(final String nameid) {
        for (final L1ItemInstance item : this._items) {
            if (item.getName().equals(nameid)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 传出是否有该编号物品(阵列)
     * 
     * @param itemId
     *            物品编号
     * @return
     */
    public L1ItemInstance[] findItemsId(final int itemId) {
        final ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
        for (final L1ItemInstance item : _items) {
            if (item.getItemId() == itemId) {// itemid相等
            	//hjx1000 物品ID 58030 点卡除外 
                if (item.get_time() == null || item.getItemId() == 58030) {// 不具备时间限制
                    itemList.add(item);
                }
            }
        }
        return itemList.toArray(new L1ItemInstance[] {});
    }

    /**
     * 未装备物品清单(阵列)
     * 
     * @param itemId
     * @return
     */
    public L1ItemInstance[] findItemsIdNotEquipped(final int itemId) {
        final ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
        for (final L1ItemInstance item : _items) {
            if (item.getItemId() == itemId) {
                if (!item.isEquipped()) {
                    itemList.add(item);
                }
            }
        }
        return itemList.toArray(new L1ItemInstance[] {});
    }

    /**
     * 未装备物品清单(阵列)
     * 
     * @param nameid
     * @return
     */
    public L1ItemInstance[] findItemsIdNotEquipped(final String nameid) {
        final ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
        for (final L1ItemInstance item : _items) {
            if (item.getName().equals(nameid)) {
                if (!item.isEquipped()) {
                    itemList.add(item);
                }
            }
        }
        return itemList.toArray(new L1ItemInstance[] {});
    }

    /**
     * 检查是否具有指定OBJID物品
     * 
     * @param objectId
     * @return
     */
    public L1ItemInstance getItem(final int objectId) {
        for (final Object itemObject : this._items) {
            final L1ItemInstance item = (L1ItemInstance) itemObject;
            if (item.getId() == objectId) {
                return item;
            }
        }
        return null;
    }

    /**
     * 检查指定物品是否足够数量1（矢 魔石的确认）
     * 
     * @param id
     * @return
     */
    public boolean checkItem(final int id) {
        return this.checkItem(id, 1);
    }

    /**
     * 检查指定物品是否足够数量
     * 
     * @param itemId
     *            物品编号
     * @param count
     *            需要数量
     * @return
     */
    public boolean checkItem(final int itemId, final long count) {
        if (count <= 0) {
            return true;
        }

        // 可堆叠
        if (ItemTable.get().getTemplate(itemId).isStackable()) {
            final L1ItemInstance item = this.findItemId(itemId);
            if ((item != null) && (item.getCount() >= count)) {
                return true;
            }

            // 不可堆叠
        } else {
            final Object[] itemList = this.findItemsId(itemId);
            if (itemList.length >= count) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查未装备物品是否足够数量
     * hjx1000
     * @param itemId
     *            物品编号
     * @param count
     *            需要数量
     * @return
     */
    public boolean checkItemXnoEq(final int itemId, final long count) {
        if (count <= 0) {
            return true;
        }

        // 可堆叠
        if (ItemTable.get().getTemplate(itemId).isStackable()) {
            final L1ItemInstance item = this.findItemId(itemId);
            if ((item != null) && (item.getCount() >= count)) {
                return true;
            }

            // 不可堆叠
        } else {
            final Object[] itemList = this.findItemsIdNotEquipped(itemId);
            if (itemList.length >= count) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查指定物品是否足够数量
     * 
     * @param item
     *            物品
     * @param count
     *            需要数量
     * @return
     */
    public boolean checkItem(final L1ItemInstance item, final long count) {
        if (count <= 0) {
            return true;
        }
        if (item.getCount() >= count) {
            return true;
        }
        return false;
    }

    /**
     * 指定物品编号以及数量<BR>
     * 该物件未在装备状态
     * 
     * @param itemid
     * @param count
     * @return 足够传回物品
     */
    public L1ItemInstance checkItemX(final int itemid, final long count) {
        if (count <= 0) {
            return null;
        }
        if (ItemTable.get().getTemplate(itemid) != null) {
            final L1ItemInstance item = this.findItemIdNoEq(itemid);
            if ((item != null) && (item.getCount() >= count)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 指定物品编号以及数量(未装备)
     * 
     * @param itemid
     * @param count
     * @return 足够传回物品
     */
    public L1ItemInstance checkItemXNoEq(final int itemid, final long count) {
        if (count <= 0) {
            return null;
        }
        if (ItemTable.get().getTemplate(itemid) != null) {
            final L1ItemInstance item = this.findItemIdNoEq(itemid);
            if ((item != null) && (item.getCount() >= count)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 具有未装备指定的物品包含强化质
     * 
     * @param id
     *            指定物件编号
     * @param enchant
     *            指定强化质
     * @param count
     *            数量
     * @return
     */
    public boolean checkEnchantItem(final int id, final int enchant,
            final long count) {
        int num = 0;
        for (final L1ItemInstance item : this._items) {
            if (item.isEquipped()) { // 物品装备状态
                continue;
            }
            if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)) {
                num++;
                if (num == count) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 具有未装备指定的物品包含强化质
     * 
     * @param id
     *            指定物件编号
     * @param enchant
     *            指定强化质
     * @param count
     *            数量
     * @param objid 
     *            指定的objid不算
     * @param xing
     * 			       武器星级      
     * @return
     */
    public boolean checkEnchantItem(final int id, final int enchant,
            final long count, final int objid, final int xing) {
        int num = 0;
    	int tmp = 0;
        for (final L1ItemInstance item : this._items) {
            if (item.isEquipped()) { // 物品装备状态
                continue;
            }
            if (item.getId() == objid) {
            	continue;
            }

            if (item.get_power_name() != null) {
            	tmp = item.get_power_name().get_xing_count();
            }
            if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)
            		&& (tmp == xing)) {
                num++;
                if (num == count) {
                    return true;
                }
            }
            tmp = 0;
        }
        return false;
    }

    /**
     * 删除未装备指定的物品包含强化质
     * 
     * @param id
     *            指定物件编号
     * @param enchant
     *            指定强化质
     * @param count
     *            数量
     * @return
     */
    public boolean consumeEnchantItem(final int id, final int enchant,
            final long count) {
        for (final L1ItemInstance item : this._items) {
            if (item.isEquipped()) { // 装备しているものは该当しない
                continue;
            }
            if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)) {
                this.removeItem(item);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 删除未装备指定的物品包含强化质
     * 
     * @param id
     *            指定物件编号
     * @param enchant
     *            指定强化质
     * @param count
     *            数量
     * @param objid 
     *            指定的objid不算
     * @param xing
     * 			       武器星级           
     * @return
     */
    public boolean consumeEnchantItem(final int id, final int enchant,
            final long count, final int objid, final int xing) {
    	int tmp = 0;
        for (final L1ItemInstance item : this._items) {
            if (item.isEquipped()) { // 装备しているものは该当しない
                continue;
            }
            if (item.getId() == objid) {
            	continue;
            }
            
            if (item.get_power_name() != null) {
            	tmp = item.get_power_name().get_xing_count();
            }
            if ((item.getItemId() == id) && (item.getEnchantLevel() == enchant)
            		&& (tmp == xing)) {
                this.removeItem(item);
                return true;
            }
            tmp = 0;
        }
        return false;
    }

    /**
     * 比较未装备物品数量
     * 
     * @param nameid
     * @param count
     * @return
     */
    public boolean checkItemNotEquipped(final String nameid, final long count) {
        if (count == 0) {
            return true;
        }
        return count <= this.countItems(nameid);
    }

    /**
     * 比较未装备物品数量
     * 
     * @param id
     * @param count
     * @return
     */
    public boolean checkItemNotEquipped(final int id, final long count) {
        if (count == 0) {
            return true;
        }
        return count <= this.countItems(id);
    }

    // 特定のアイテムを全て必要な个数所持しているか确认（イベントとかで复数のアイテムを所持しているか确认するため）
    public boolean checkItem(final int[] ids) {
        final int len = ids.length;
        final int[] counts = new int[len];
        for (int i = 0; i < len; i++) {
            counts[i] = 1;
        }
        return this.checkItem(ids, counts);
    }

    public boolean checkItem(final int[] ids, final int[] counts) {
        for (int i = 0; i < ids.length; i++) {
            if (!this.checkItem(ids[i], counts[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找未装备物品数量
     * 
     * @param itemId
     * @return
     */
    public long countItems(final int itemId) {
        // 可堆叠
        if (ItemTable.get().getTemplate(itemId).isStackable()) {
            final L1ItemInstance item = this.findItemId(itemId);
            if (item != null) {
                return item.getCount();
            }

            // 不可堆叠
        } else {
            final Object[] itemList = this.findItemsIdNotEquipped(itemId);
            return itemList.length;
        }
        return 0;
    }

    /**
     * 查找未装备物品数量
     * 
     * @param nameid
     * @return
     */
    public long countItems(final String nameid) {
        // 可堆叠
        if (ItemTable.get().getTemplate(nameid).isStackable()) {
            final L1ItemInstance item = this.findItemId(nameid);
            if (item != null) {
                return item.getCount();
            }

            // 不可堆叠
        } else {
            final Object[] itemList = this.findItemsIdNotEquipped(nameid);
            return itemList.length;
        }
        return 0;
    }

    public void shuffle() {
        Collections.shuffle(this._items);
    }

    /**
     * 背包内全部物件删除
     */
    public void clearItems() {
        for (final Object itemObject : this._items) {
            final L1ItemInstance item = (L1ItemInstance) itemObject;
            World.get().removeObject(item);
        }
    }

    // オーバーライド用
    public void loadItems() {
    }

    public void insertItem(final L1ItemInstance item) {
    }

    public void updateItem(final L1ItemInstance item) {
    }
    
    public void updateItemNow(final L1ItemInstance item) {//修正离线商店出售物品可以刷物BUG hjx1000
    }
    

    public void updateItem(final L1ItemInstance item, final int colmn) {
    }

}

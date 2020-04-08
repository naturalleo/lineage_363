package com.lineage.server.model;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRate;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_AddItem;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_DeleteInventoryItem;
import com.lineage.server.serverpackets.S_ItemColor;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;

/**
 * 人物背包数据
 * 
 * @author dexc
 * 
 */
public class L1PcInventory extends L1Inventory {

    private static final Log _log = LogFactory.getLog(L1PcInventory.class);

    private static final long serialVersionUID = 1L;

    private static final int MAX_SIZE = 180;// 最大容量

    private final L1PcInstance _owner; // 背包所有者

    private int _arrowId; // 优先使用的箭ItemID

    private int _stingId; // 优先使用的飞刀ItemID

    public L1PcInventory(final L1PcInstance owner) {
        this._owner = owner;
        this._arrowId = 0;
        this._stingId = 0;
    }

    public L1PcInstance getOwner() {
        return this._owner;
    }

    /**
     * 传回240阶段重量
     * 
     * @return
     */
    public int getWeight240() {
        return this.calcWeight240(this.getWeight());
    }

    /**
     * 240阶段重量计算
     * 
     * @param weight
     * @return
     */
    public int calcWeight240(final long weight) {
        int weight240 = 0;
        if (ConfigRate.RATE_WEIGHT_LIMIT != 0) {
            final double maxWeight = this._owner.getMaxWeight();
            if (weight > maxWeight) {
                weight240 = 240;

            } else {
                double wpTemp = (weight * 100 / maxWeight) * 240.00 / 100.00;
                final DecimalFormat df = new DecimalFormat("00.##");
                df.format(wpTemp);
                wpTemp = Math.round(wpTemp);
                weight240 = (int) (wpTemp);
            }

        } else { // ウェイトレートが０なら重量常に０
            weight240 = 0;
        }

        return weight240;
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
    @Override
    public int checkAddItem(final L1ItemInstance item, final long count) {
        return this.checkAddItem(item, count, true);
    }

    /**
     * 增加物品是否成功(背包)
     * 
     * @param item
     *            物品数据
     * @param count
     *            数量
     * @param message
     *            发送讯息
     * @return 0:成功 1:超过可携带数量 2:超过可携带重量 3:超过LONG最大质
     */
    public int checkAddItem(final L1Item item, final long count) {
        if (item == null) {
            return -1;
        }

        boolean isMaxSize = false;// 容量数据异常
        boolean isWeightOver = false;// 重量数据异常

        // 可以堆叠
        if (item.isStackable()) {
            // 身上不具备该物件
            if (!this.checkItem(item.getItemId())) {
                // 超过可携带数量
                if (this.getSize() + 1 >= MAX_SIZE) {
                    isMaxSize = true;
                }
            }

            // 不可以堆叠
        } else {
            // 超过可携带数量
            if (this.getSize() + 1 >= MAX_SIZE) {
                isMaxSize = true;
            }
        }

        if (isMaxSize) {
            // 263 \f1一个角色最多可携带180个道具。
            this.sendOverMessage(263);
            return SIZE_OVER;
        }

        // 现有重量 + (物品重量 * 数量 / 1000) + 1
        final long weight = this.getWeight() + item.getWeight() * count / 1000
                + 1;

        // 重量数据异常 (重量计算表示小于0)
        if ((weight < 0) || ((item.getWeight() * count / 1000) < 0)) {
            isWeightOver = true;
        }

        // 超过可携带重量
        if (this.calcWeight240(weight) >= 240 && !isWeightOver) {
            isWeightOver = true;
        }

        if (isWeightOver) {
            // 82 此物品太重了，所以你无法携带。
            this.sendOverMessage(82);
            return WEIGHT_OVER;
        }
        return OK;
    }

    /**
     * 增加物品是否成功(背包)
     * 
     * @param item
     *            物品(物品已加入世界)
     * @param count
     *            数量
     * @param message
     *            发送讯息
     * @return 0:成功 1:超过可携带数量 2:超过可携带重量 3:超过LONG最大质
     */
    public int checkAddItem(final L1ItemInstance item, final long count,
            final boolean message) {
        if (item == null) {
            return -1;
        }
        if (count <= 0) {
            return -1;
        }

        boolean isMaxSize = false;// 容量数据异常
        boolean isWeightOver = false;// 重量数据异常

        // 可以堆叠
        if (item.isStackable()) {
            // 身上不具备该物件
            if (!this.checkItem(item.getItem().getItemId())) {
                // 超过可携带数量
                if (this.getSize() + 1 >= MAX_SIZE) {
                    isMaxSize = true;
                }
            }

            // 不可以堆叠
        } else {
            // 超过可携带数量
            if (this.getSize() + 1 >= MAX_SIZE) {
                isMaxSize = true;
            }
        }

        if (isMaxSize) {
            if (message) {
                // 263 \f1一个角色最多可携带180个道具。
                this.sendOverMessage(263);
            }
            return SIZE_OVER;
        }

        // 现有重量 + (物品重量 * 数量 / 1000) + 1
        final long weight = this.getWeight() + item.getItem().getWeight()
                * count / 1000 + 1;

        // 重量数据异常 (重量计算表示小于0)
        if ((weight < 0) || ((item.getItem().getWeight() * count / 1000) < 0)) {
            isWeightOver = true;
        }

        // 超过可携带重量
        if (this.calcWeight240(weight) >= 240 && !isWeightOver) {
            isWeightOver = true;
        }

        if (isWeightOver) {
            if (message) {
                // 82 此物品太重了，所以你无法携带。
                this.sendOverMessage(82);
            }
            return WEIGHT_OVER;
        }
        return OK;
    }

    public void sendOverMessage(final int message_id) {
        this._owner.sendPackets(new S_ServerMessage(message_id));
    }

    /**
     * 初始化人物背包资料
     */
    @Override
    public void loadItems() {
        try {
            final CopyOnWriteArrayList<L1ItemInstance> items = CharItemsReading
                    .get().loadItems(this._owner.getId());

            if (items != null) {
                _items = items;

                List<L1ItemInstance> equipped = new CopyOnWriteArrayList<L1ItemInstance>();
                for (final L1ItemInstance item : items) {
                    if (item.isEquipped()) {
                        equipped.add(item);
                    }

                    item.setEquipped(false);

                    if ((item.getItem().getType2() == 0)
                            && (item.getItem().getType() == 2)) { // 照明道具
                        item.setRemainingTime(item.getItem().getLightFuel());
                    }
                }

                // 将已经设置为装备的防具 重新设置为装备状态
                for (final L1ItemInstance item : equipped) {
                    this.setEquipped(item, true, true, false);
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * LIST物品资料新增
     */
    @Override
    public void insertItem(final L1ItemInstance item) {
        if (item.getCount() <= 0) {
            return;
        }
        // 设置使用者OBJID
        item.set_char_objid(this._owner.getId());

        this._owner.sendPackets(new S_AddItem(item));
        if (item.getItem().getWeight() != 0) {
            // 重量
            this._owner.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, this
                    .getWeight240()));
        }

        try {
            CharItemsReading.get().storeItem(this._owner.getId(), item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static final int COL_ATTR_ENCHANT_LEVEL = 2048;

    public static final int COL_ATTR_ENCHANT_KIND = 1024;

    public static final int COL_BLESS = 512;

    public static final int COL_REMAINING_TIME = 256;

    public static final int COL_CHARGE_COUNT = 128;

    public static final int COL_ITEMID = 64;

    public static final int COL_DELAY_EFFECT = 32;

    public static final int COL_COUNT = 16;

    public static final int COL_EQUIPPED = 8;

    public static final int COL_ENCHANTLVL = 4;

    public static final int COL_IS_ID = 2;

    public static final int COL_DURABILITY = 1;

    @Override
    public void updateItem(final L1ItemInstance item) {
        this.updateItem(item, COL_COUNT);
        if (item.getItem().isToBeSavedAtOnce()) {
            this.saveItem(item, COL_COUNT);
        }
    }
    
    @Override
    public void updateItemNow(final L1ItemInstance item) {//修正离线商店出售物品可以刷物BUG hjx1000
        this.updateItem(item, COL_COUNT);
        this.saveItem(item, COL_COUNT);
    }

    /**
     * 背包内物件状态更新
     * 
     * @param item
     *            需要更新的物件
     * @param column
     *            更新种类
     */
    @Override
    public void updateItem(final L1ItemInstance item, int column) {
        if (column >= COL_ATTR_ENCHANT_LEVEL) { // 属性强化数
            this._owner.sendPackets(new S_ItemStatus(item, null));
            column -= COL_ATTR_ENCHANT_LEVEL;
        }

        if (column >= COL_ATTR_ENCHANT_KIND) { // 属性强化の种类
            this._owner.sendPackets(new S_ItemStatus(item, null));
            column -= COL_ATTR_ENCHANT_KIND;
        }

        if (column >= COL_BLESS) { // 祝福?封印
            this._owner.sendPackets(new S_ItemColor(item));
            column -= COL_BLESS;
        }

        if (column >= COL_REMAINING_TIME) { // 残余可用时间
            this._owner.sendPackets(new S_ItemName(item));
            column -= COL_REMAINING_TIME;
        }

        if (column >= COL_CHARGE_COUNT) { // 残余可用次数
            this._owner.sendPackets(new S_ItemName(item));
            column -= COL_CHARGE_COUNT;
        }

        if (column >= COL_ITEMID) { // 别のアイテムになる场合(便笺を开封したときなど)
            this._owner.sendPackets(new S_ItemStatus(item, null));
            this._owner.sendPackets(new S_ItemColor(item));
            this._owner.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, this
                    .getWeight240()));
            column -= COL_ITEMID;
        }

        if (column >= COL_DELAY_EFFECT) { // 效果ディレイ
            column -= COL_DELAY_EFFECT;
        }

        if (column >= COL_COUNT) { // カウント
            this._owner.sendPackets(new S_ItemStatus(item, null));

            final int weight = item.getWeight();
            if (weight != item.getLastWeight()) {
                item.setLastWeight(weight);
                this._owner.sendPackets(new S_ItemStatus(item, null));

            } else {
                this._owner.sendPackets(new S_ItemName(item));
            }
            if (item.getItem().getWeight() != 0) {
                // XXX 240段阶のウェイトが变化しない场合は送らなくてよい
                this._owner.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT,
                        this.getWeight240()));
            }
            column -= COL_COUNT;
        }

        if (column >= COL_EQUIPPED) { // 装备状态
            this._owner.sendPackets(new S_ItemName(item));
            column -= COL_EQUIPPED;
        }

        if (column >= COL_ENCHANTLVL) { // エンチャント
            this._owner.sendPackets(new S_ItemStatus(item, _owner));
            column -= COL_ENCHANTLVL;
        }

        if (column >= COL_IS_ID) { // 确认状态
            this._owner.sendPackets(new S_ItemStatus(item, null));
            this._owner.sendPackets(new S_ItemColor(item));
            column -= COL_IS_ID;
        }

        if (column >= COL_DURABILITY) { // 耐久性
            this._owner.sendPackets(new S_ItemStatus(item, null));
            column -= COL_DURABILITY;
        }
    }

    /**
     * 背包内资料更新(SQL)
     * 
     * @param item
     *            - 更新对象のアイテム
     * @param column
     *            - 更新するステータスの种类
     */
    public void saveItem(final L1ItemInstance item, int column) {
        if (column == 0) {
            return;
        }

        try {
            if (column >= COL_ATTR_ENCHANT_LEVEL) { // 属性强化数
                CharItemsReading.get().updateItemAttrEnchantLevel(item);
                column -= COL_ATTR_ENCHANT_LEVEL;
            }

            if (column >= COL_ATTR_ENCHANT_KIND) { // 属性强化の种类
                CharItemsReading.get().updateItemAttrEnchantKind(item);
                column -= COL_ATTR_ENCHANT_KIND;
            }

            if (column >= COL_BLESS) { // 祝福?封印
                CharItemsReading.get().updateItemBless(item);
                column -= COL_BLESS;
            }

            if (column >= COL_REMAINING_TIME) { // 使用可能な残り时间
                CharItemsReading.get().updateItemRemainingTime(item);
                column -= COL_REMAINING_TIME;
            }

            if (column >= COL_CHARGE_COUNT) { // チャージ数
                CharItemsReading.get().updateItemChargeCount(item);
                column -= COL_CHARGE_COUNT;
            }

            if (column >= COL_ITEMID) { // 别のアイテムになる场合(便笺を开封したときなど)
                CharItemsReading.get().updateItemId(item);
                column -= COL_ITEMID;
            }

            if (column >= COL_DELAY_EFFECT) { // 效果ディレイ
                CharItemsReading.get().updateItemDelayEffect(item);
                column -= COL_DELAY_EFFECT;
            }

            if (column >= COL_COUNT) { // カウント
                CharItemsReading.get().updateItemCount(item);
                column -= COL_COUNT;
            }

            if (column >= COL_EQUIPPED) { // 装备状态
                CharItemsReading.get().updateItemEquipped(item);
                column -= COL_EQUIPPED;
            }

            if (column >= COL_ENCHANTLVL) { // エンチャント
                CharItemsReading.get().updateItemEnchantLevel(item);
                column -= COL_ENCHANTLVL;
            }

            if (column >= COL_IS_ID) { // 确认状态
                CharItemsReading.get().updateItemIdentified(item);
                column -= COL_IS_ID;
            }

            if (column >= COL_DURABILITY) { // 耐久性
                CharItemsReading.get().updateItemDurability(item);
                column -= COL_DURABILITY;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * LIST物品资料移除
     */
    @Override
    public void deleteItem(final L1ItemInstance item) {
        try {
            CharItemsReading.get().deleteItem(this._owner.getId(), item);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }

        if (item.isEquipped()) {
            this.setEquipped(item, false);
        }

        if (item != null) {
            this._owner.sendPackets(new S_DeleteInventoryItem(item));
            this._items.remove(item);
            if (item.getItem().getWeight() != 0) {
                this._owner.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT,
                        this.getWeight240()));
            }
        }
    }

    /**
     * アイテムを装着脱着させる（L1ItemInstanceの变更、补正值の设定、character_itemsの更新、パケット送信まで管理）
     * 
     * @param item
     * @param equipped
     */
    public void setEquipped(final L1ItemInstance item, final boolean equipped) {
        this.setEquipped(item, equipped, false, false);
    }

    public void setEquipped(final L1ItemInstance item, final boolean equipped,
            final boolean loaded, final boolean changeWeapon) {
        if (item.isEquipped() != equipped) { // 设定值と违う场合だけ处理
            final L1Item temp = item.getItem();
            if (equipped) { // 装着
                item.setEquipped(true);
                // 装备穿着效果判断
                this._owner.getEquipSlot().set(item);

            } else { // 脱着
                if (!loaded) {
                    // インビジビリティクローク バルログブラッディクローク装备中でインビジ状态の场合はインビジ状态の解除
                    if ((temp.getItemId() == 20077)
                            || (temp.getItemId() == 20062)
                            || (temp.getItemId() == 120077)) {
                        if (this._owner.isInvisble()) {
                            this._owner.delInvis();
                            return;
                        }
                    }
                }
                item.setEquipped(false);
                // 装备脱除效果判断
                this._owner.getEquipSlot().remove(item);
            }

            if (!loaded) { // 最初の读迂时はＤＢパケット关连の处理はしない
                // System.out.println("物品装备状态");
                // XXX:意味のないセッター
                this._owner.setCurrentHp(this._owner.getCurrentHp());
                this._owner.setCurrentMp(this._owner.getCurrentMp());
                this.updateItem(item, COL_EQUIPPED);
                this._owner.sendPackets(new S_OwnCharStatus(this._owner));
                // 武器の场合はビジュアル更新。ただし、武器の持ち替えで武器を脱着する时は更新しない
                if ((temp.getType2() == 1) && (changeWeapon == false)) {
                    this._owner.sendPacketsAll(new S_CharVisualUpdate(
                            this._owner));
                }
            }
        }
    }

    /**
     * 装备具有指定编号道具
     * 
     * @param id
     *            物品编号
     * @return 传回该物品
     */
    public L1ItemInstance checkEquippedItem(final int id) {
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品编号相同 并且在使用中
                if ((item.getItem().getItemId() == id) && item.isEquipped()) {
                    return item;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    /**
     * 装备具有指定编号道具
     * 
     * @param id
     *            物品编号
     * @return true:使用中 false:非使用中
     */
    public boolean checkEquipped(final int id) {
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品编号相同 并且在使用中
                if ((item.getItem().getItemId() == id) && item.isEquipped()) {
                    return true;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    /**
     * 装备具有指定名称道具
     * 
     * @param nameid
     *            物品名称
     * @return true:使用中 false:非使用中
     */
    public boolean checkEquipped(final String nameid) {
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品名称相同 并且在使用中
                if ((item.getName().equals(nameid)) && item.isEquipped()) {
                    return true;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    /**
     * 装备具有指定编号道具群(套装)
     * 
     * @param ids
     * @return
     */
    public boolean checkEquipped(final int[] ids) {
        try {
            for (final int id : ids) {
                if (!this.checkEquipped(id)) {
                    return false;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return true;
    }

    /**
     * 装备具有指定名称道具群(套装)
     * 
     * @param names
     * @return
     */
    public boolean checkEquipped(final String[] names) {
        try {
            for (final String name : names) {
                if (!this.checkEquipped(name)) {
                    return false;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return true;
    }

    /**
     * 装备中指定类型物品数量
     * 
     * @param type2
     *            类型
     * @param type
     *            物品分类
     * 
     * @return 装备中指定类型物品数量
     */
    public int getTypeEquipped(final int type2, final int type) {
        int equipeCount = 0;// 装备中指定位置物品数量
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品类型相等 物品分类相等 并且在使用中
                if ((item.getItem().getType2() == type2)
                        && (item.getItem().getType() == type)
                        && item.isEquipped()) {
                    equipeCount++;// 使用数量+1
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeCount;
    }

    /**
     * 装备中指定类型物品
     * 
     * @param type2
     *            类型
     * @param type
     *            物品分类
     * 
     * @return 装备中指定类型物品
     */
    public L1ItemInstance getItemEquipped(final int type2, final int type) {
        L1ItemInstance equipeitem = null;
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品类型相等 物品分类相等 并且在使用中
                if ((item.getItem().getType2() == type2)
                        && (item.getItem().getType() == type)
                        && item.isEquipped()) {
                    equipeitem = item;
                    break;
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeitem;
    }

    /**
     * 设置 显示/消除 套装效果 XXX
     * 
     * @param armorSet
     *            套装
     * @param isMode
     *            是否显示 额外属性
     */
    public void setPartMode(final ArmorSet armorSet, final boolean isMode) {
        final int tgItemId = armorSet.get_ids()[0];// 取回套装第一样物品ID
        final L1ItemInstance[] tgItems = findItemsId(tgItemId);
        for (L1ItemInstance tgItem : tgItems) {
            tgItem.setIsMatch(isMode);
            this._owner.sendPackets(new S_ItemStatus(tgItem, null));
        }
    }

    /**
     * 装备中界指阵列
     * 
     * @return
     */
    public L1ItemInstance[] getRingEquipped() {
        final L1ItemInstance equipeItem[] = new L1ItemInstance[2];
        try {
            int equipeCount = 0;
            for (final L1ItemInstance item : this._items) {
                // 物品为戒指 并且在使用中
                if (item.getItem().getUseType() == 23 && // 戒指
                        item.isEquipped()) {
                    equipeItem[equipeCount] = item;
                    equipeCount++;
                    if (equipeCount == 2) {
                        break;
                    }
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeItem;
    }

    // 变身时に装备できない装备を外す
    public void takeoffEquip(final int polyid) {
        this.takeoffWeapon(polyid);
        this.takeoffArmor(polyid);
    }

    // 变身时に装备できない武器を外す
    private void takeoffWeapon(final int polyid) {
        if (this._owner.getWeapon() == null) { // 素手
            return;
        }

        boolean takeoff = false;
        final int weapon_type = this._owner.getWeapon().getItem().getType();
        // 装备出来ない武器を装备してるか？
        takeoff = !L1PolyMorph.isEquipableWeapon(polyid, weapon_type);

        if (takeoff) {
            this.setEquipped(this._owner.getWeapon(), false, false, false);
        }
    }

    // 变身时に装备できない防具を外す
    private void takeoffArmor(final int polyid) {
        L1ItemInstance armor = null;

        // ヘルムからガーダーまでチェックする
        for (int type = 0; type <= 13; type++) {
            // 装备していて、装备不可の场合は外す
            if ((this.getTypeEquipped(2, type) != 0)
                    && !L1PolyMorph.isEquipableArmor(polyid, type)) {
                if (type == 9) { // リングの场合は、两手分外す
                    armor = this.getItemEquipped(2, type);
                    if (armor != null) {
                        this.setEquipped(armor, false, false, false);
                    }

                    armor = this.getItemEquipped(2, type);
                    if (armor != null) {
                        this.setEquipped(armor, false, false, false);
                    }
                    
                    armor = this.getItemEquipped(2, type);
                    if (armor != null) {
                        this.setEquipped(armor, false, false, false);
                    }
                    
                    armor = this.getItemEquipped(2, type);
                    if (armor != null) {
                        this.setEquipped(armor, false, false, false);
                    }

                } else {
                    armor = this.getItemEquipped(2, type);
                    if (armor != null) {
                        this.setEquipped(armor, false, false, false);
                    }
                }
            }
        }
    }

    /**
     * 使用的箭
     * 
     * @return
     */
    public L1ItemInstance getArrow() {
        return this.getBullet(-2);
    }

    /**
     * 使用的飞刀
     * 
     * @return
     */
    public L1ItemInstance getSting() {
        return this.getBullet(-3);
    }

    /**
     * 
     * @param useType
     * @return
     */
    private L1ItemInstance getBullet(final int useType) {
        L1ItemInstance bullet;
        int priorityId = 0;
        if (useType == -2) {
            if (this._owner.getWeapon().getItemId() == 192) {// 水精灵之弓
                bullet = this.findItemId(40742);// 古代之箭
                if (bullet == null) {
                    // 329：\f1没有具有 %0%o。
                    this._owner.sendPackets(new S_ServerMessage(329, "$2377"));
                }
                return bullet;

            } else {
                priorityId = this._arrowId; // 箭
            }
        }

        if (useType == -3) {
            priorityId = this._stingId; // 飞刀
        }

        if (priorityId > 0) {// 优先する弹があるか
            bullet = this.findItemId(priorityId);
            if (bullet != null) {
                return bullet;

            } else {// なくなっていた场合は优先を消す
                if (useType == -2) {
                    this._arrowId = 0;
                }
                if (useType == -3) {
                    this._stingId = 0;
                }
            }
        }

        for (final Object itemObject : this._items) {// 弹を探す
            bullet = (L1ItemInstance) itemObject;
            if (bullet.getItem().getUseType() == useType) {
                if (useType == -2) {// 箭
                    this._arrowId = bullet.getItem().getItemId(); // 优先にしておく
                }

                if (useType == -3) {
                    this._stingId = bullet.getItem().getItemId(); // 优先にしておく
                }
                return bullet;
            }
        }
        return null;
    }

    // 优先するアローの设定
    public void setArrow(final int id) {
        this._arrowId = id;
    }

    // 优先するスティングの设定
    public void setSting(final int id) {
        this._stingId = id;
    }

    /**
     * 装备 hp自然回复补正
     * 
     * @return
     */
    public int hpRegenPerTick() {
        int hpr = 0;
        try {
            for (final Object itemObject : this._items) {
                final L1ItemInstance item = (L1ItemInstance) itemObject;
                if (item.isEquipped()) {
                    hpr += item.getItem().get_addhpr();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return hpr;
    }

    /**
     * 装备 mp自然回复补正
     * 
     * @return
     */
    public int mpRegenPerTick() {
        int mpr = 0;
        try {
            for (final Object itemObject : this._items) {
                final L1ItemInstance item = (L1ItemInstance) itemObject;
                if (item.isEquipped()) {
                    mpr += item.getItem().get_addmpr();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return mpr;
    }

    /**
     * 传回随机掉落物品
     * 
     * @return
     */
    public L1ItemInstance caoPenalty() {
        try {
            final Random random = new Random();
            final int rnd = random.nextInt(_items.size());
            final L1ItemInstance penaltyItem = _items.get(rnd);
            // 天宝
            if (penaltyItem.getItem().getItemId() == 44070) {
                return null;
            }

            // 金币
            if (penaltyItem.getItem().getItemId() == L1ItemId.ADENA) {
                return null;
            }

            // 不可删除物品
            if (penaltyItem.getItem().isCantDelete()) {
                return null;
            }

            // 不可转移物品
            if (!penaltyItem.getItem().isTradable()) {
                return null;
            }

            // 具有时间限制
            if (penaltyItem.get_time() != null) {
                return null;
            }

            // 宠物项圈
            final Object[] petlist = this._owner.getPetList().values()
                    .toArray();
            for (final Object petObject : petlist) {
                if (petObject instanceof L1PetInstance) {
                    final L1PetInstance pet = (L1PetInstance) petObject;
                    if (penaltyItem.getId() == pet.getItemObjId()) {
                        return null;
                    }
                }
            }

            // 取回娃娃
            if (_owner.getDoll(penaltyItem.getId()) != null) {
                return null;
            }

            // 解除使用状态
            this.setEquipped(penaltyItem, false);
            return penaltyItem;

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * 移除全部指定编号道具
     * 
     * @param itemId
     */
    public void delQuestItem(final int itemId) {
        try {
            final Random random = new Random();
            for (L1ItemInstance item : this._items) {
                if (item.getItemId() == itemId) {
                    removeItem(item);
                    // 445：\f1%0%s 渐渐变热之后燃烧成灰烬。
                    // 446：\f1%0%s 冻结之后破碎。
                    // 447：\f1%0%s 经过狂烈的震动之后变成土。
                    // 448：\f1%0%s 渐渐腐蚀之后被风吹散。
                    this._owner.sendPackets(new S_ServerMessage(random
                            .nextInt(4) + 445, item.getName()));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 戒指装备中指定位置物品数量
     * @param type2
     * @param type
     * @param itemId
     * @return
     */
	public int getTypeAndItemIdEquipped(final int type2, final int type,
			final String NameId) {
        int equipeCount = 0;// 装备中指定位置物品数量
        try {
            for (final L1ItemInstance item : this._items) {
                // 物品类型相等 物品分类相等 并且在使用中
        		if (item.getItem().getType2() == type2 
        			&& item.getItem().getType() == type 
        		    && (item.getItem().getNameId().equalsIgnoreCase(NameId)
        		    && item.isEquipped())) {
                    equipeCount++;// 使用数量+1
                }
            }

        } catch (final Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeCount;
    }
}

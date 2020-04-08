package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.storage.DwarfShopStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopS;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 托售物件清单
 * 
 * @author dexc
 * 
 */
public class DwarfShopTable implements DwarfShopStorage {

    private static final Log _log = LogFactory.getLog(DwarfShopTable.class);

    // 托售物件清单 (OBJID/物品)
    private static final Map<Integer, L1ItemInstance> _itemList = new ConcurrentHashMap<Integer, L1ItemInstance>();

    // 托售物件清单 (ID/托售资讯)
    private static final Map<Integer, L1ShopS> _shopSList = new ConcurrentHashMap<Integer, L1ShopS>();

    /**
     * 预先加载
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_shopinfo`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int objid = rs.getInt("id");
                final int item_id = rs.getInt("item_id");

                final L1Item itemTemplate = ItemTable.get()
                        .getTemplate(item_id);
                if (itemTemplate == null) {
                    // 无该物品资料 移除
                    errorItem(objid);
                    continue;
                }
                final long count = rs.getLong("count");
                final int enchantlvl = rs.getInt("enchantlvl");
                final int is_id = rs.getInt("is_id");
                final int durability = rs.getInt("durability");
                final int charge_count = rs.getInt("charge_count");
                final int remaining_time = rs.getInt("remaining_time");
                final Timestamp last_used = rs.getTimestamp("last_used");
                final int bless = rs.getInt("bless");
                final int attr_enchant_kind = rs.getInt("attr_enchant_kind");
                final int attr_enchant_level = rs.getInt("attr_enchant_level");
                final String gamno = rs.getString("gamno");

                final L1ItemInstance item = new L1ItemInstance();
                item.setId(objid);
                item.setItem(itemTemplate);
                item.setCount(count);
                item.setEquipped(false);
                item.setEnchantLevel(enchantlvl);
                item.setIdentified(is_id != 0 ? true : false);
                item.set_durability(durability);
                item.setChargeCount(charge_count);
                item.setRemainingTime(remaining_time);
                item.setLastUsed(last_used);
                item.setBless(bless);
                item.setAttrEnchantKind(attr_enchant_kind);
                item.setAttrEnchantLevel(attr_enchant_level);
                item.setGamNo(gamno);

                addItem(objid, item);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);

            // 托售资讯
            loadShopS();
        }
        _log.info("载入托售道具资料数量: " + _itemList.size() + "/" + _shopSList.size()
                + "(" + timer.get() + "ms)");
    }

    /**
     * 建立托售物件资料
     * 
     * @param key
     * @param value
     */
    private static void addItem(final int key, final L1ItemInstance value) {
        if (_itemList.get(key) == null) {
            _itemList.put(key, value);
        }
        // 将物品加入世界
        if (World.get().findObject(key) == null) {
            World.get().storeObject(value);
        }
    }

    private static int _id = 0;

    @Override
    public int get_id() {
        return _id;
    }

    @Override
    public void set_id(int id) {
        _id = id;
    }

    /**
     * 载入托售资讯
     */
    private static void loadShopS() {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_shop`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int id = rs.getInt("id");
                final int item_obj_id = rs.getInt("item_obj_id");
                final int user_obj_id = rs.getInt("user_obj_id");
                final int adena = rs.getInt("adena");
                final Timestamp overtime = rs.getTimestamp("overtime");
                // 结束模式 0:出售中 1:已售出 2:已售出领回天宝 3:未售出 4:未售出道具以领回
                final int end = rs.getInt("end");
                final String none = rs.getString("none");

                if (_id < id) {
                    _id = id;
                }

                final L1ShopS shopS = new L1ShopS();
                shopS.set_id(id);
                shopS.set_item_obj_id(item_obj_id);
                shopS.set_user_obj_id(user_obj_id);
                shopS.set_adena(adena);
                shopS.set_overtime(overtime);
                shopS.set_end(end);
                shopS.set_none(none);
                switch (end) {
                    case 0:// 0:出售中
                    case 1:// 1:已售出
                    case 3:// 3:未售出
                        L1ItemInstance item = _itemList.get(item_obj_id);
                        shopS.set_item(item);
                        break;
                    case 2:// 2:已售出领回天宝
                    case 4:// 2:已售出领回天宝
                        shopS.set_item(null);
                        break;
                }

                userMap(id, shopS);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    private static void userMap(int key, L1ShopS value) {
        if (_shopSList.get(key) == null) {
            _shopSList.put(key, value);
        }
    }

    /**
     * 删除错误物品资料
     * 
     * @param objid
     */
    private static void errorItem(int objid) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("DELETE FROM `character_shopinfo` WHERE `id`=?");
            ps.setInt(1, objid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 传回全部出售中物件资料数据
     * 
     * @return
     */
    @Override
    public HashMap<Integer, L1ShopS> allShopS() {
        final HashMap<Integer, L1ShopS> shopSList = new HashMap<Integer, L1ShopS>();

        for (L1ShopS value : _shopSList.values()) {
            if (value.get_end() == 0) {// 0:出售中
                shopSList.put(value.get_id(), value);
            }
        }
        return shopSList;
    }

    /**
     * 传回全部托售物件数据
     * 
     * @return
     */
    @Override
    public Map<Integer, L1ItemInstance> allItems() {
        return _itemList;
    }

    /**
     * 传回指定托售物件数据
     * 
     * @return
     */
    @Override
    public L1ShopS getShopS(int objid) {
        L1ShopS out = null;
        int i = 0;
        for (L1ShopS value : _shopSList.values()) {
            if (value.get_end() != 0) {
                continue;
            }
            if (value.get_item_obj_id() == objid) {
                out = value;
                i++;
            }
        }
        if (i > 1) {
            _log.error("取回托售物品资料异常-未售出物品OBJID重复:" + objid);
        }
        return out;
    }

    /**
     * 指定人物托售纪录
     * 
     * @param pcobjid
     * @return
     */
    @Override
    public HashMap<Integer, L1ShopS> getShopSMap(int pcobjid) {
        // 主清单
        final HashMap<Integer, L1ShopS> shopSMap = new HashMap<Integer, L1ShopS>();
        int index = 0;

        for (int i = (_shopSList.size() + 1); i > 0; i--) {
            L1ShopS value = _shopSList.get(i);
            if (value != null) {
                if (value.get_user_obj_id() == pcobjid) {
                    shopSMap.put(index, value);
                    index++;
                }
            }
        }

        if (shopSMap.size() > 0) {
            return shopSMap;

        } else {
            return null;
        }
    }

    /**
     * 加入托售物件数据
     */
    @Override
    public void insertItem(final int key, final L1ItemInstance item,
            final L1ShopS shopS) {
        addItem(key, item);

        set_userMap(shopS.get_id(), shopS);

        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_shopinfo` SET `id`=?,"
                    + "`item_id`= ?,`item_name`=?,`count`=?,"
                    + "`enchantlvl`=?,`is_id`=?,`durability`=?,"
                    + "`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,"
                    + "`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");

            int i = 0;
            ps.setInt(++i, item.getId());
            ps.setInt(++i, item.getItemId());
            ps.setString(++i, item.getItem().getName());
            ps.setLong(++i, item.getCount());
            ps.setInt(++i, item.getEnchantLevel());
            ps.setInt(++i, item.isIdentified() ? 1 : 0);
            ps.setInt(++i, item.get_durability());
            ps.setInt(++i, item.getChargeCount());
            ps.setInt(++i, item.getRemainingTime());
            ps.setTimestamp(++i, item.getLastUsed());
            ps.setInt(++i, item.getBless());
            ps.setInt(++i, item.getAttrEnchantKind());
            ps.setInt(++i, item.getAttrEnchantLevel());
            ps.setString(++i, item.getGamNo());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 加入托售纪录
     * 
     * @param getId
     * @param shopS
     */
    private void set_userMap(int getId, L1ShopS shopS) {
        userMap(shopS.get_id(), shopS);

        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_shop` SET `id`=?,"
                    + "`item_obj_id`= ?,`user_obj_id`=?,`adena`=?,"
                    + "`overtime`=?,`end`=?,`none`=?");

            int i = 0;
            ps.setInt(++i, shopS.get_id());
            ps.setInt(++i, shopS.get_item_obj_id());
            ps.setInt(++i, shopS.get_user_obj_id());
            ps.setInt(++i, shopS.get_adena());
            ps.setTimestamp(++i, shopS.get_overtime());
            ps.setInt(++i, shopS.get_end());
            ps.setString(++i, shopS.get_none());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 资料更新(托售状态)
     * 
     * @param item
     */
    @Override
    public void updateShopS(final L1ShopS shopS) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `character_shop` SET `end`=? WHERE `id`=?");
            pstm.setLong(1, shopS.get_end());
            pstm.setInt(2, shopS.get_id());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 托售物件资料删除
     * 
     * @param key
     */
    @Override
    public void deleteItem(final int key) {
        L1ItemInstance item = _itemList.get(key);
        if (item != null) {
            _itemList.remove(key);
            errorItem(key);
        }
    }
}

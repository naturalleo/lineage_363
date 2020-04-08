package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.storage.DwarfStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 仓库物件清单
 * 
 * @author dexc
 * 
 */
public class DwarfTable implements DwarfStorage {

    private static final Log _log = LogFactory.getLog(DwarfTable.class);

    // 仓库物件清单 (帐号名称) (物品清单)
    private static final Map<String, CopyOnWriteArrayList<L1ItemInstance>> _itemList = new ConcurrentHashMap<String, CopyOnWriteArrayList<L1ItemInstance>>();

    /**
     * 预先加载
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_warehouse`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int objid = rs.getInt("id");
                final String account_name = rs.getString("account_name")
                        .toLowerCase();

                final boolean account = AccountReading.get().isAccountUT(
                        account_name);
                if (account) {
                    final int item_id = rs.getInt("item_id");
                    // final String item_name = rs.getString("item_name");
                    final long count = rs.getLong("count");
                    // final int is_equipped = rs.getInt("is_equipped");
                    final int enchantlvl = rs.getInt("enchantlvl");
                    final int is_id = rs.getInt("is_id");
                    final int durability = rs.getInt("durability");
                    final int charge_count = rs.getInt("charge_count");
                    final int remaining_time = rs.getInt("remaining_time");
                    final Timestamp last_used = rs.getTimestamp("last_used");
                    final int bless = rs.getInt("bless");
                    final int attr_enchant_kind = rs
                            .getInt("attr_enchant_kind");
                    final int attr_enchant_level = rs
                            .getInt("attr_enchant_level");
                    final String gamno = rs.getString("gamno");

                    final L1ItemInstance item = new L1ItemInstance();
                    item.setId(objid);

                    final L1Item itemTemplate = ItemTable.get().getTemplate(
                            item_id);
                    if (itemTemplate == null) {
                        // 无该物品资料 移除
                        errorItem(objid);
                        continue;
                    }
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

                    addItem(account_name, item);
                    i++;

                } else {
                    deleteItem(account_name);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入人物仓库物件清单资料数量: " + _itemList.size() + "/" + i + "("
                + timer.get() + "ms)");
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
            ps = co.prepareStatement("DELETE FROM `character_warehouse` WHERE `id`=?");
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
     * 建立资料
     * 
     * @param accName
     * @param item
     */
    private static void addItem(final String account_name,
            final L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list == null) {
            list = new CopyOnWriteArrayList<L1ItemInstance>();
            if (!list.contains(item)) {
                list.add(item);
            }

        } else {
            if (!list.contains(item)) {
                list.add(item);
            }
        }
        // 将物品加入世界
        if (World.get().findObject(item.getId()) == null) {
            World.get().storeObject(item);
        }
        _itemList.put(account_name, list);
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void deleteItem(final String account_name) {
        final CopyOnWriteArrayList<L1ItemInstance> list = _itemList
                .remove(account_name);
        if (list != null) {
            // 移出世界
            for (L1ItemInstance item : list) {
                World.get().removeObject(item);
            }
        }

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_warehouse` WHERE `account_name`=?");
            ps.setString(1, account_name);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 传回全部仓库数据
     * 
     * @return
     */
    @Override
    public Map<String, CopyOnWriteArrayList<L1ItemInstance>> allItems() {
        return _itemList;
    }

    /**
     * 传回仓库数据
     * 
     * @return
     */
    @Override
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(
            final String account_name) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list != null) {
            return list;
        }
        return null;
    }

    /**
     * 删除仓库资料(完整)
     * 
     * @param account_name
     */
    @Override
    public void delUserItems(final String account_name) {
        deleteItem(account_name);
    }

    /**
     * 该仓库是否有指定数据
     * 
     * @param account_name
     * @param objid
     * @param count
     * @return
     */
    @Override
    public boolean getUserItems(final String account_name, final int objid,
            final int count) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list != null) {
            if (list.size() <= 0) {
                return false;
            }
            for (L1ItemInstance item : list) {
                if (item.getId() == objid) {
                    if (item.getCount() >= count) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 加入仓库数据
     */
    @Override
    public void insertItem(final String account_name, final L1ItemInstance item) {
//        _log.warn("帐号:" + account_name + " 加入仓库数据:" + item.getItem().getName()
//                + " OBJID:" + item.getId());
        _log.warn("帐号:" + account_name + " 加入仓库数据:" + item.getNumberedName_to_String()
                + " OBJID:" + item.getId());
        addItem(account_name, item);

        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_warehouse` SET `id`=?,"
                    + "`account_name`=?,`item_id`= ?,`item_name`=?,`count`=?,"
                    + "`is_equipped`=0,`enchantlvl`=?,`is_id`=?,`durability`=?,"
                    + "`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,"
                    + "`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");

            int i = 0;
            ps.setInt(++i, item.getId());
            ps.setString(++i, account_name);
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
     * 仓库资料更新(物品数量)
     * 
     * @param item
     */
    @Override
    public void updateItem(final L1ItemInstance item) {
        _log.warn("更新仓库数据:" + item.getItem().getName() + " OBJID:"
                + item.getId() + " Count:" + item.getCount());
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseFactory.get().getConnection();
            ps = con.prepareStatement("UPDATE `character_warehouse` SET `count`=? WHERE `id`=?");
            ps.setLong(1, item.getCount());
            ps.setInt(2, item.getId());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(con);
        }
    }

    /**
     * 仓库物品资料删除
     * 
     * @param account_name
     * @param item
     */
    @Override
    public void deleteItem(final String account_name, final L1ItemInstance item) {
        // System.out.println("仓库物品资料删除 : SQL");
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list != null) {
//            _log.warn("帐号:" + account_name + " 仓库物品移出 :"
//                    + item.getItem().getName() + " OBJID:" + item.getId());
            _log.warn("帐号:" + account_name + " 仓库物品移出 :"
                    + item.getNumberedName_to_String() + " OBJID:" + item.getId());
            list.remove(item);

            Connection co = null;
            PreparedStatement pstm = null;
            try {
                co = DatabaseFactory.get().getConnection();
                pstm = co
                        .prepareStatement("DELETE FROM `character_warehouse` WHERE `id`=?");
                pstm.setInt(1, item.getId());
                pstm.execute();

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(co);
            }
        }
    }
}

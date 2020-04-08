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
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.storage.CharItemsStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 人物背包资料
 * 
 * @author dexc
 * 
 */
public class CharItemsTable implements CharItemsStorage {

    private static final Log _log = LogFactory.getLog(BuddyTable.class);

    // 背包物件清单 (人物OBJID) (物品清单)
    private static final Map<Integer, CopyOnWriteArrayList<L1ItemInstance>> _itemList = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<L1ItemInstance>>();

    /**
     * 资料预先载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_items`");
            rs = ps.executeQuery();

            while (rs.next()) {
                final int objid = rs.getInt("id");
                final int item_id = rs.getInt("item_id");
                final int char_id = rs.getInt("char_id");
                // final String item_name = rs.getString("item_name");

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_id) != null) {
                    final L1Item itemTemplate = ItemTable.get().getTemplate(
                            item_id);
                    if (itemTemplate == null) {
                        // 无该物品资料 移除
                        errorItem(objid);
                        continue;
                    }
                    final long count = rs.getLong("count");
                    final int is_equipped = rs.getInt("is_equipped");
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

                    L1ItemInstance item = new L1ItemInstance();
                    item.setId(objid);
                    item.setItem(itemTemplate);
                    item.setCount(count);
                    item.setEquipped(is_equipped != 0 ? true : false);
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
                    item.set_char_objid(char_id);

                    item.getLastStatus().updateAll();

                    addItem(char_id, item);
                    i++;

                } else {
                    deleteItem(char_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入人物背包物件清单资料数量: " + _itemList.size() + "/" + i + "("
                + timer.get() + "ms)");
    }

    /**
     * 删除错误物品资料
     * 
     * @param objid
     */
    private static void errorItem(int objid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM `character_items` WHERE `id`=?");
            pstm.setInt(1, objid);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 建立资料
     * 
     * @param objid
     * @param item
     */
    private static void addItem(final Integer objid, final L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list == null) {// 该人物尚无背包数据
            list = new CopyOnWriteArrayList<L1ItemInstance>();
            if (!list.contains(item)) {// 清单中不包含该物品
                list.add(item);
            }

        } else {
            if (!list.contains(item)) {// 清单中不包含该物品
                list.add(item);
            }
        }
        // 世界物件中不包含该OBJID数据
        if (World.get().findObject(item.getId()) == null) {
            World.get().storeObject(item);
        }
        _itemList.put(objid, list);
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void deleteItem(final Integer objid) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.remove(objid);
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
            ps = cn.prepareStatement("DELETE FROM `character_items` WHERE `char_id`=?");
            ps.setInt(1, objid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 传回该人物背包资料
     * 
     * @param objid
     * @return
     */
    @Override
    public CopyOnWriteArrayList<L1ItemInstance> loadItems(final Integer objid) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list != null) {
            return list;
        }
        return null;
    }

    /**
     * 删除人物背包资料(完整)
     * 
     * @param objid
     */
    @Override
    public void delUserItems(final Integer objid) {
        deleteItem(objid);
    }

    /**
     * 该人物背包是否有指定数据
     * 
     * @param pcObjid
     * @param objid
     * @param count
     * @return
     */
    @Override
    public boolean getUserItems(final Integer pcObjid, final int objid,
            final long count) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(pcObjid);
        if (list != null) {
            for (L1ItemInstance item : list) {
                if (item.getId() == objid && item.getCount() >= count) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否有指定数据
     * 
     * @param pcObjid
     * @param objid
     * @param count
     * @return
     */
    @Override
    public boolean getUserItem(final int objid) {
        for (CopyOnWriteArrayList<L1ItemInstance> list : _itemList.values()) {
            for (L1ItemInstance item : list) {
                if (item.getId() == objid) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 传回佣有该物品ID的人物清单<BR>
     * (适用该物品每人只能佣有一个的状态)
     * 
     * @param itemid
     * @return
     */
    @Override
    public Map<Integer, L1ItemInstance> getUserItems(final int itemid) {
        // 人物OBJID / 物品
        final Map<Integer, L1ItemInstance> outList = new ConcurrentHashMap<Integer, L1ItemInstance>();
        try {
            for (Integer key : _itemList.keySet()) {
                CopyOnWriteArrayList<L1ItemInstance> value = _itemList.get(key);
                for (L1ItemInstance item : value) {
                    if (item.getItemId() == itemid) {
                        outList.put(key, item);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return outList;
    }

    /**
     * 删除指定编号全部数据
     * 
     * @param itemid
     */
    @Override
    public void del_item(final int itemid) {
        try {
            for (Integer key : _itemList.keySet()) {
                // 人物背包
                final CopyOnWriteArrayList<L1ItemInstance> value = _itemList
                        .get(key);
                for (L1ItemInstance item : value) {
                    if (item.getItemId() == itemid) {
                        deleteItem(key, item);
                    }
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 增加背包物品
     * 
     * @param objId
     * @param item
     * @throws Exception
     */
    @Override
    public void storeItem(final int objId, final L1ItemInstance item)
            throws Exception {
        addItem(objId, item);
        item.getLastStatus().updateAll();

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO `character_items` SET `id`=?,`item_id`=?,`char_id`=?,"
                            + "`item_name`=?,`count`=?,`is_equipped`=?,`enchantlvl`=?,`is_id`=?,"
                            + "`durability`=?,`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,"
                            + "`attr_enchant_kind`=?,`attr_enchant_level`=?,`gamno`=?");

            int i = 0;
            pstm.setInt(++i, item.getId());
            pstm.setInt(++i, item.getItem().getItemId());
            pstm.setInt(++i, objId);
            pstm.setString(++i, item.getItem().getName());
            pstm.setLong(++i, item.getCount());
            pstm.setInt(++i, item.isEquipped() ? 1 : 0);
            pstm.setInt(++i, item.getEnchantLevel());
            pstm.setInt(++i, item.isIdentified() ? 1 : 0);
            pstm.setInt(++i, item.get_durability());
            pstm.setInt(++i, item.getChargeCount());
            pstm.setInt(++i, item.getRemainingTime());
            pstm.setTimestamp(++i, item.getLastUsed());
            pstm.setInt(++i, item.getBless());
            pstm.setInt(++i, item.getAttrEnchantKind());
            pstm.setInt(++i, item.getAttrEnchantLevel());
            pstm.setString(++i, item.getGamNo());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error("背包物品增加时发生异常 人物OBJID:" + objId, e);

            /*
             * final L1Object object =
             * World.get().findObject(item.get_char_objid()); if (object !=
             * null) { if (object instanceof L1PcInstance) { final L1PcInstance
             * tgpc = (L1PcInstance) object; // 删除物品
             * tgpc.getInventory().removeItem(item); } }
             */

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 删除背包物品
     * 
     * @param objid
     *            人物OBJID
     * @param item
     *            物品
     * 
     * @throws Exception
     */
    @Override
    public void deleteItem(final int objid, final L1ItemInstance item)
            throws Exception {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list != null) {
            list.remove(item);

            Connection cn = null;
            PreparedStatement ps = null;
            try {
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("DELETE FROM `character_items` WHERE `id`=?");
                ps.setInt(1, item.getId());
                ps.execute();

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
        }
    }

    /**
     * 更新物品ITEMID 与中文名称
     * 
     * @param item
     */
    @Override
    public void updateItemId_Name(final L1ItemInstance item) throws Exception {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `character_items` SET `item_id`=?,`item_name`=?,`bless`=? WHERE `id`=?");
            pstm.setInt(1, item.getItemId());
            pstm.setString(2, item.getItem().getName());
            pstm.setInt(3, item.getItem().getBless());
            pstm.setInt(4, item.getId());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 更新ITEMID
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemId(final L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `item_id`=? WHERE `id`=?",
                item.getItemId());
        item.getLastStatus().updateItemId();
    }

    /**
     * 更新数量
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemCount(final L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `count`=? WHERE `id`=?",
                item.getCount());
        item.getLastStatus().updateCount();
    }

    /**
     * 更新损坏度
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemDurability(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `durability`=? WHERE `id`=?",
                item.get_durability());
        item.getLastStatus().updateDuraility();
    }

    /**
     * 更新可用次数
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemChargeCount(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `charge_count`=? WHERE `id`=?",
                item.getChargeCount());
        item.getLastStatus().updateChargeCount();
    }

    /**
     * 更新可用时间
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemRemainingTime(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `remaining_time`=? WHERE `id`=?",
                item.getRemainingTime());
        item.getLastStatus().updateRemainingTime();
    }

    /**
     * 更新强化度
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemEnchantLevel(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `enchantlvl`=? WHERE `id`=?",
                item.getEnchantLevel());
        item.getLastStatus().updateEnchantLevel();
    }

    /**
     * 更新使用状态
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemEquipped(final L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `is_equipped`=? WHERE `id`=?",
                (item.isEquipped() ? 1 : 0));
        item.getLastStatus().updateEquipped();
    }

    /**
     * 更新鉴定状态
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemIdentified(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `is_id`=? WHERE `id`=?",
                (item.isIdentified() ? 1 : 0));
        item.getLastStatus().updateIdentified();
    }

    /**
     * 更新祝福状态
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemBless(final L1ItemInstance item) throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `bless`=? WHERE `id`=?",
                item.getBless());
        item.getLastStatus().updateBless();
    }

    /**
     * 更新强化属性
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemAttrEnchantKind(final L1ItemInstance item)
            throws Exception {
        executeUpdate(
                item.getId(),
                "UPDATE `character_items` SET `attr_enchant_kind`=? WHERE `id`=?",
                item.getAttrEnchantKind());
        item.getLastStatus().updateAttrEnchantKind();
    }

    /**
     * 更新强化属性强化度
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemAttrEnchantLevel(final L1ItemInstance item)
            throws Exception {
        executeUpdate(
                item.getId(),
                "UPDATE `character_items` SET `attr_enchant_level`=? WHERE `id`=?",
                item.getAttrEnchantLevel());
        item.getLastStatus().updateAttrEnchantLevel();
    }

    /**
     * 更新最后使用时间
     * 
     * @param item
     * @throws Exception
     */
    @Override
    public void updateItemDelayEffect(final L1ItemInstance item)
            throws Exception {
        executeUpdate(item.getId(),
                "UPDATE `character_items` SET `last_used`=? WHERE `id`=?",
                item.getLastUsed());
        item.getLastStatus().updateLastUsed();
    }

    /**
     * 传回对应所有物品数量
     * 
     * @param objId
     * @return
     * @throws Exception
     */
    @Override
    public int getItemCount(final int objId) throws Exception {
        int count = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("SELECT * FROM `character_items` WHERE `char_id`=?");
            pstm.setInt(1, objId);
            rs = pstm.executeQuery();
            while (rs.next()) {
                count++;
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return count;
    }

    /**
     * 给予金币(对离线人物)
     * 
     * @param objid
     *            人物OBJID
     * @param count
     *            金币给予数量
     * @throws Exception
     */
    @Override
    public void getAdenaCount(final int objid, final long count)
            throws Exception {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(objid);
        if (list != null) {
            boolean isAdena = false;// 背包有金币
            for (L1ItemInstance item : list) {
                if (item.getItemId() == L1ItemId.ADENA) {
                    // 更新数量
                    item.setCount(item.getCount() + count);
                    updateItemCount(item);
                    isAdena = true;
                }
            }
            // 背包无金币
            if (!isAdena) {
                final L1ItemInstance item = ItemTable.get().createItem(
                        L1ItemId.ADENA);
                item.setCount(count);
                this.storeItem(objid, item);
            }
        }
    }

    private void executeUpdate(final int objId, final String sql,
            final long updateNum) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement(sql.toString());
            pstm.setLong(1, updateNum);
            pstm.setInt(2, objId);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    private static void executeUpdate(final int objId, final String sql,
            final Timestamp ts) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement(sql.toString());
            pstm.setTimestamp(1, ts);
            pstm.setInt(2, objId);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}

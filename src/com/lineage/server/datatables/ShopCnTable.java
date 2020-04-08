package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 特殊商店贩卖资料
 * 
 * @author dexc
 * 
 */
public class ShopCnTable {

    private static final Log _log = LogFactory.getLog(ShopCnTable.class);

    private static ShopCnTable _instance;

    private static final Map<Integer, ArrayList<L1ShopItem>> _shopList = new HashMap<Integer, ArrayList<L1ShopItem>>();

    public static ShopCnTable get() {
        if (_instance == null) {
            _instance = new ShopCnTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `shop_cn` ORDER BY `item_id`");
            rs = ps.executeQuery();

            L1ShopItem item;
            while (rs.next()) {
                final int id = rs.getInt("id");
                final int npcId = rs.getInt("npc_id");
                final int itemId = rs.getInt("item_id");

                if (ItemTable.get().getTemplate(itemId) == null) {
                    _log.error("特殊商店贩卖资料错误: 没有这个编号的道具:" + itemId + " 对应NPC编号:"
                            + npcId);
                    delete(npcId, itemId);
                    continue;
                }

                final int sellingPrice = rs.getInt("selling_price");
                final int packCount = rs.getInt("pack_count");
                item = new L1ShopItem(id, itemId, sellingPrice, packCount);

                this.addShopItem(npcId, item);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs, ps, cn);
        }
        _log.info("载入特殊商店贩卖资料数量: " + _shopList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除错误资料
     * 
     * @param clan_id
     */
    private static void delete(final int npc_id, final int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `shop_cn` WHERE `npc_id`=? AND `item_id`=?");
            ps.setInt(1, npc_id);
            ps.setInt(2, item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 加入贩卖物品
     * 
     * @param npcId
     * @param item
     */
    private void addShopItem(final int npcId, final L1ShopItem item) {
        ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list == null) {
            list = new ArrayList<L1ShopItem>();
            list.add(item);
            _shopList.put(npcId, list);

        } else {
            list.add(item);
        }
    }

    /**
     * 传回NPC贩卖清单
     * 
     * @param npcId
     * @return
     */
    public ArrayList<L1ShopItem> get(final int npcId) {
        final ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list != null) {
            return list;
        }
        return null;
    }

    /**
     * 传回该贩卖物品资料
     * 
     * @param npcId
     * @param id
     * @return
     */
    public L1ShopItem getTemp(final int npcId, final int id) {
        final ArrayList<L1ShopItem> list = _shopList.get(new Integer(npcId));
        if (list != null) {
            for (final L1ShopItem shopItem : list) {
                if (shopItem.getId() == id) {
                    return shopItem;
                }
            }
        }
        return null;
    }
}

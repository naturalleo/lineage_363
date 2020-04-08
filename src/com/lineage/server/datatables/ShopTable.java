package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 商店贩卖资料
 * 
 * @author dexc
 * 
 */
public class ShopTable {

    private static final Log _log = LogFactory.getLog(ShopTable.class);

    private static ShopTable _instance;
    
    // TODO 修正新所有东西贩卖  新增加全道具贩卖检测价格差距
//    private final Map<Integer, Integer> _allItemSells = new HashMap<Integer, Integer>();

    // 销售清单
    private static final Map<Integer, L1Shop> _allShops = new HashMap<Integer, L1Shop>();

    // 回收物品
    private static final Map<Integer, Integer> _allShopItem = new HashMap<Integer, Integer>();

    // 不回收的物品
    private static final Map<Integer, Integer> _noBuyList = new HashMap<Integer, Integer>();

    public static ShopTable get() {
        if (_instance == null) {
            _instance = new ShopTable();
        }
        return _instance;
    }
    // TODO 修正新所有东西贩卖  新增加全道具贩卖检测价格差距
//    private ShopTable() {
//    	loadShopAllSell();
//    	enumNpcIds();
//    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `shop` WHERE `npc_id`=? ORDER BY `order_id`");
            for (final int npcId : enumNpcIds()) {
                ps.setInt(1, npcId);

                rs = ps.executeQuery();

                final L1Shop shop = loadShop(npcId, rs);

                _allShops.put(npcId, shop);
                rs.close();
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs, ps, cn);
        }
        _log.info("载入商店贩卖资料数量: " + _allShops.size() + "(" + timer.get() + "ms)");
    }

    private static ArrayList<Integer> enumNpcIds() {
        final ArrayList<Integer> ids = new ArrayList<Integer>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT DISTINCT `npc_id` FROM `shop`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("npc_id"));
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        return ids;
    }

    private static L1Shop loadShop(final int npcId, final ResultSet rs)
            throws SQLException {
        // 卖出清单
        final List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();

        // 买入清单
        final List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();

        while (rs.next()) {
            final int itemId = rs.getInt("item_id");

            if (ItemTable.get().getTemplate(itemId) == null) {
                _log.error("商店贩卖资料错误: 没有这个编号的道具:" + itemId + " 对应NPC编号:"
                        + npcId);
                delete(npcId, itemId);
                continue;
            }
            final int sellingPrice = rs.getInt("selling_price");// 卖出金额
            final int purchasingPrice = rs.getInt("purchasing_price");// 回收金额

            int packCount = rs.getInt("pack_count");// 卖出数量
            // 加入出售物品价格查询清单
            addSellList(itemId, sellingPrice, purchasingPrice, packCount);

            packCount = packCount == 0 ? 1 : packCount;

            if (0 <= sellingPrice) {
                final L1ShopItem item = new L1ShopItem(itemId, sellingPrice,
                        packCount);
                sellingList.add(item);
            }

            if (0 <= purchasingPrice) {
                final L1ShopItem item = new L1ShopItem(itemId, purchasingPrice,
                        packCount);
                purchasingList.add(item);
            }
        }
        return new L1Shop(npcId, sellingList, purchasingList);
    }
	// TODO 修正新所有东西贩卖  新增加全道具贩卖检测价格差距
//	private void loadShopAllSell() {
//		
//		final PerformanceTimer timer = new PerformanceTimer();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		try {
//			System.out.print("INFO - 载入全道具贩卖价格差异");
//			con = DatabaseFactory.get().getConnection();
//			pstm = con.prepareStatement("SELECT * FROM william_system_sellall ORDER BY item_id");
//			rs = pstm.executeQuery();
//			while (rs.next()) {
//				int itemId = rs.getInt("item_id");
//				int price = rs.getInt("sell_price");
//				if (price >= 1) {
//					Connection conI = null;
//					PreparedStatement pstmI = null;
//					ResultSet rsI = null;
//					try {
//						conI = DatabaseFactory.get().getConnection();
//						pstmI = conI.prepareStatement("SELECT * FROM shop WHERE item_id='" + itemId + "'");
//						rsI = pstmI.executeQuery();
//						while (rsI.next()) {
//							if ((rsI.getInt("selling_price") >= 1) && (price > rsI.getInt("selling_price"))) {
//							System.out.println("NPCID=" + rsI.getInt("npc_id") + ", ItemID=" + itemId + ", 有价格差距!!!");
//                            price = -1;
//							}
//						}
//						rsI.close();
//					} catch (SQLException e) {
//						_log.error(e.getLocalizedMessage(), e);
//					} finally {
//						SQLUtil.close(rsI, pstmI, conI);
//					}
//				}
//				if (price >= 1) {
//					this._allItemSells.put(Integer.valueOf(itemId), Integer.valueOf(price));
//				}			
//			}
//		}
//		catch (SQLException e) {
//			_log.error(e.getLocalizedMessage(), e);
//		} finally {
//			SQLUtil.close(rs, pstm, con);
//			System.out.println("完成! " + timer.get() + "毫秒");
//		}
//	}
	// TODO 修正新所有东西贩卖  新增加全道具贩卖检测价格差距
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
            ps = cn.prepareStatement("DELETE FROM `shop` WHERE `npc_id`=? AND `item_id`=?");
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
     * 加入出售物品价格查询清单
     * 
     * @param key
     *            物品编号
     * 
     * @param value1
     *            卖出价
     * 
     * @param value2
     *            回收价
     * 
     * @param packCount
     *            数量
     * 
     */
    private static void addSellList(final int key, final int value1,
            final int value2, final int packCount) {
        // 已经加入不回收清单 忽略以下判断
        if (_noBuyList.get(key) != null) {
            return;
        }

        // 是否在回收物品清单中
        final Integer price = _allShopItem.get(new Integer(key));

        double value3 = 0;// 回收价格

        // 回收价格不为0
        if (value2 > 0) {
            if (packCount > 0) {
                // 售价 / 数量 / 2
                value3 = (value1 / packCount) / 2.0;

            } else {
                value3 = value2;
            }

        } else {
            if (value1 > 0) {
                if (packCount > 0) {
                    // 售价 / 数量 / 2
                    value3 = (value1 / packCount) / 2.0;

                } else {
                    // 售价 / 2
                    value3 = value1 / 2.0;
                }
            }
        }

        // 计算后回收价格小于1
        if (value3 < 1) {
            _noBuyList.put(new Integer(key), new Integer((int) value3));
            // 已经加入回收物品清单中
            if (price != null) {
                // 移出回收物品列
                _allShopItem.remove(new Integer(key));
            }
            return;
        }

        // 已经加入回收物品清单中
        if (price != null) {
            // 计算后回收价格 小于 列表中纪录
            if (value3 < price) {
                // 更新回收价格
                _allShopItem.put(new Integer(key), new Integer((int) value3));
            }

            // 尚未加入回收物品清单中
        } else {
            _allShopItem.put(new Integer(key), new Integer((int) value3));
        }
    }

    /**
     * 传回出售物品价格
     * 
     * @param key
     * @return
     */
    public int getPrice(final int key) {
        int tgprice = 1;
        final Integer price = _allShopItem.get(new Integer(key));
        if (price != null) {
            tgprice = price;
        }

        // 不回收物
        if (_noBuyList.get(key) != null) {
            tgprice = 1;
        }

        return tgprice;
    }

    public L1Shop get(final int npcId) {
        return _allShops.get(npcId);
    }
}

package com.lineage.server.model;

import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.OtherUserTitleReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_TradeAddItem;
import com.lineage.server.serverpackets.S_TradeStatus;
import com.lineage.server.world.World;

/**
 * 玩家相互交易判断类
 * 
 * @author dexc
 * 
 */
public class L1Trade {

    private static final Log _log = LogFactory.getLog(L1Trade.class);

    /**
     * 加入交易物品
     * 
     * @param pc
     * @param itemid
     * @param itemcount
     */
    public void tradeAddItem(final L1PcInstance pc, final int itemObjid,
            long itemcount) {
        L1PcInstance trading_partner = null;
        try {
            // 取回交易对象
            trading_partner = (L1PcInstance) World.get().findObject(
                    pc.getTradeID());
            // 取回要加入交易的物品
            final L1ItemInstance item = pc.getInventory().getItem(itemObjid);
            if (item == null) {
                return;
            }

            if (trading_partner == null) {
                return;
            }

            if (item.isEquipped()) {
                return;
            }
            

//            final ArrayList<L1TradeItem> map = pc.get_trade_items();
//            if (map.size() >= 16) {
//                return;
//            }

//            long count = 0;
//
//            for (final Iterator<L1TradeItem> iter = map.iterator(); iter
//                    .hasNext();) {
//                final L1TradeItem tg = iter.next();
//                if (tg.get_objid() == item.getId()) {
//                    count += tg.get_count();
//                }
//            }
            itemcount = Math.max(0, itemcount);
//            final long now_count = itemcount + count;// 本次物件数量 + 已输出相同物件数量

            // 检查数量
            final boolean checkItem = pc.getInventory().checkItem(
                    item.getItemId(), itemcount);
            if (checkItem) {
//                // 建立交易物件资讯
//                final L1TradeItem info = new L1TradeItem();
//                info.set_objid(item.getId());
//                info.set_item_id(item.getItemId());
//                info.set_item(item);
//                info.set_count(itemcount);
//
//                // 加入暂存清单
//                pc.add_trade_item(info);
//
//                // 输出新数量给客户端
//                final long out_count = item.getCount() - now_count;
//                if (out_count <= 0) {
//                    // 数量为0移除显示
//                    pc.sendPackets(new S_DeleteInventoryItem(item.getId()));
//
//                } else {
//                    // 数量不为0更新显示
//                    pc.sendPackets(new S_ItemStatus(item, out_count));
//                }
            	//改写交易防止丢失物品 hjx1000
				pc.getInventory().tradeItem(item, itemcount,
						pc.getTradeWindowInventory());
                // 交易内容新增
                pc.sendPackets(new S_TradeAddItem(item, itemcount, 0));
                trading_partner.sendPackets(new S_TradeAddItem(item, itemcount,
                        1));

            } else {
                pc.sendPackets(new S_TradeStatus(1));
                trading_partner.sendPackets(new S_TradeStatus(1));

                pc.setTradeOk(false);
                trading_partner.setTradeOk(false);

                pc.setTradeID(0);
                trading_partner.setTradeID(0);
                return;
            }

        } catch (Exception e) {
//            if (pc != null) {
//                pc.get_trade_clear();
//            }
//            if (trading_partner != null) {
//                trading_partner.get_trade_clear();
//            }
            _log.error(e.getLocalizedMessage(), e);

        } finally {

        }
    }

    /**
     * 交易完成
     * 
     * @param pc
     */
    public void tradeOK(final L1PcInstance pc) {
        L1PcInstance trading_partner = null;
        int cnt;
        try {
            // 取回交易对象
            trading_partner = (L1PcInstance) World.get().findObject(
                    pc.getTradeID());
            if (trading_partner != null) {
    			List<L1ItemInstance> player_tradelist = pc.getTradeWindowInventory().getItems();
    			int player_tradecount = pc.getTradeWindowInventory().getSize();

    			List<L1ItemInstance> trading_partner_tradelist = trading_partner
    					.getTradeWindowInventory().getItems();
    			int trading_partner_tradecount = trading_partner
    					.getTradeWindowInventory().getSize();
    			for (cnt = 0; cnt < player_tradecount; cnt++) {
    				L1ItemInstance l1iteminstance1 = player_tradelist
    						.get(0);
    				pc.getTradeWindowInventory().tradeItem(l1iteminstance1,
    						l1iteminstance1.getCount(),
    						trading_partner.getInventory());
    				// 个人交易物品纪录
                    OtherUserTitleReading.get().add(
                    		l1iteminstance1.getNumberedName_to_String() + "("
                                    + l1iteminstance1.getItemId() + ")", l1iteminstance1.getId(),
                            0, l1iteminstance1.getCount(), trading_partner.getId(),
                            trading_partner.getName(), pc.getId(),
                            pc.getName());
    			}
    			for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
    				L1ItemInstance l1iteminstance2 = trading_partner_tradelist
    						.get(0);
    				trading_partner.getTradeWindowInventory().tradeItem(
    						l1iteminstance2, l1iteminstance2.getCount(),
    						pc.getInventory());
                    // 个人交易物品纪录
                    OtherUserTitleReading.get().add(
                    		l1iteminstance2.getNumberedName_to_String() + "("
                                    + l1iteminstance2.getItemId() + ")", l1iteminstance2.getId(),
                            0, l1iteminstance2.getCount(), pc.getId(), pc.getName(),
                            trading_partner.getId(),
                            trading_partner.getName());
    			}

                pc.sendPackets(new S_TradeStatus(0));
                trading_partner.sendPackets(new S_TradeStatus(0));

                pc.setTradeOk(false);
                trading_partner.setTradeOk(false);

                pc.setTradeID(0);
                trading_partner.setTradeID(0);

                pc.turnOnOffLight();
                trading_partner.turnOnOffLight();
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
//            if (pc != null) {
//                pc.get_trade_clear();
//            }
//            if (trading_partner != null) {
//                trading_partner.get_trade_clear();
//            }
        }
    }

    /**
     * 交易取消
     * 
     * @param pc
     */
    public void tradeCancel(final L1PcInstance pc) {
        L1PcInstance trading_partner = null;
        int cnt;
        try {
            trading_partner = (L1PcInstance) World.get().findObject(
                    pc.getTradeID());
            if (trading_partner != null) {
                // 取回自己的交易物品
    			List<L1ItemInstance> player_tradelist = pc.getTradeWindowInventory().getItems();
    			int player_tradecount = pc.getTradeWindowInventory().getSize();

    			List<L1ItemInstance> trading_partner_tradelist = trading_partner
    					.getTradeWindowInventory().getItems();
    			int trading_partner_tradecount = trading_partner
    					.getTradeWindowInventory().getSize();

    			for (cnt = 0; cnt < player_tradecount; cnt++) {
    				L1ItemInstance l1iteminstance1 = player_tradelist
    						.get(0);
    				pc.getTradeWindowInventory().tradeItem(l1iteminstance1,
    						l1iteminstance1.getCount(), pc.getInventory());
    			}
    			for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
    				L1ItemInstance l1iteminstance2 = trading_partner_tradelist
    						.get(0);
    				trading_partner.getTradeWindowInventory().tradeItem(
    						l1iteminstance2, l1iteminstance2.getCount(),
    						trading_partner.getInventory());
    			}

                pc.sendPackets(new S_TradeStatus(1));
                trading_partner.sendPackets(new S_TradeStatus(1));

                pc.setTradeOk(false);
                trading_partner.setTradeOk(false);

                pc.setTradeID(0);
                trading_partner.setTradeID(0);
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
//            if (pc != null) {
//                pc.get_trade_clear();
//            }
//            if (trading_partner != null) {
//                trading_partner.get_trade_clear();
//            }
        }
    }
    
//    /**
//     * 恢复上次交易的临时物品 hjx1000
//     */
//    public void RecoveryTrade(final L1PcInstance pc) {
//    	try {
//            // 取回自己的交易物品
//            final ArrayList<L1TradeItem> map_1 = pc.get_trade_items();
//            // 物品还原清单
//            final HashMap<Integer, Long> temp1 = new HashMap<Integer, Long>();
//            
//            if (!map_1.isEmpty()) {// pc
//                for (final Iterator<L1TradeItem> iter = map_1.iterator(); iter
//                        .hasNext();) {
//                    final L1TradeItem tg = iter.next();
//                    final Long count = temp1.get(tg.get_objid());
//                    if (count == null) {
//                        temp1.put(tg.get_objid(), tg.get_count());
//                    } else {
//                        temp1.put(tg.get_objid(), tg.get_count() + count);
//                    }
//                }
//            }
//            if (!temp1.isEmpty()) {// pc
//                for (Integer key : temp1.keySet()) {
//                    final long count = temp1.get(key);
//                    final L1ItemInstance tg_item = pc.getInventory()
//                            .getItem(key);
//                    if (tg_item != null) {
//                        if (count == tg_item.getCount()) {
//                            pc.sendPackets(new S_AddItem(tg_item));// 取回删除物件
//                        } else {
//                            pc.sendPackets(new S_ItemStatus(tg_item,
//                                    tg_item.getCount()));// 还原物品数量
//                        }
//                    }
//                }
//            }
//            temp1.clear();
//
//            //pc.sendPackets(new S_TradeStatus(1));
//
//            //pc.setTradeOk(false);
//
//            //pc.setTradeID(0);
//            
//        } catch (Exception e) {
//            _log.error(e.getLocalizedMessage(), e);
//    	} finally {
//            if (pc != null) {
//                pc.get_trade_clear();
//            }
//    	}
//    }
}

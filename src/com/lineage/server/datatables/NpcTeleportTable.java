package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1TeleportLoc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * NPC传送点设置
 * 
 * @author dexc
 * 
 */
public class NpcTeleportTable {

    private static final Log _log = LogFactory.getLog(NpcTeleportTable.class);

    private static NpcTeleportTable _instance;

    private static final Map<String, HashMap<Integer, L1TeleportLoc>> _teleportLocList = new HashMap<String, HashMap<Integer, L1TeleportLoc>>();

    // 时间地图(地图编号/时间)
    private static final Map<Integer, Integer> _timeMap = new HashMap<Integer, Integer>();

    // 团队地图(地图编号/人数)
    private static final Map<Integer, Integer> _partyMap = new HashMap<Integer, Integer>();

    // 官方传送点设置
    private static final Map<Integer, HashMap<String, L1TeleportLoc>> _srcMap = new HashMap<Integer, HashMap<String, L1TeleportLoc>>();

    public static NpcTeleportTable get() {
        if (_instance == null) {
            _instance = new NpcTeleportTable();
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
            ps = cn.prepareStatement("SELECT * FROM `npcaction_teleport` ORDER BY `id`");
            rs = ps.executeQuery();

            L1TeleportLoc teleportLoc;
            while (rs.next()) {

                final int id = rs.getInt("id");
                final String name = rs.getString("name");
                final String orderid = rs.getString("orderid");
                final int locx = rs.getInt("locx");
                final int locy = rs.getInt("locy");
                final int mapid = rs.getInt("mapid");
                final int itemid = rs.getInt("itemid");
                final int price = rs.getInt("price");
                final int time = rs.getInt("time");
                final int user = rs.getInt("user");// 必须人数
                final int min = rs.getInt("min");// 等级限制
                final int max = rs.getInt("max");// 等级限制
                final String note = rs.getString("note");

                teleportLoc = new L1TeleportLoc();
                teleportLoc.set_id(id);

                teleportLoc.set_name(name);
                teleportLoc.set_orderid(orderid);

                teleportLoc.set_locx(locx);
                teleportLoc.set_locy(locy);
                teleportLoc.set_mapid(mapid);
                teleportLoc.set_itemid(itemid);
                teleportLoc.set_price(price);
                teleportLoc.set_user(user);
                teleportLoc.set_min(min);
                teleportLoc.set_max(max);

                if (name.equalsIgnoreCase(orderid)) {
                    final String[] set = note.replace(" ", "").split(",");
                    final int result[] = new int[set.length];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = Integer.parseInt(set[i]);
                    }
                    for (int npcid : result) {
                        final L1Npc npc = NpcTable.get().getTemplate(npcid);
                        if (npc == null) {
                            del(id);
                            continue;
                        }
                        if (!npc.getImpl().equalsIgnoreCase("L1Teleporter")) {
                            del(id);
                            continue;
                        }
                        HashMap<String, L1TeleportLoc> list = _srcMap
                                .get(npcid);
                        if (list != null) {
                            list.put(orderid, teleportLoc);

                        } else {
                            list = new HashMap<String, L1TeleportLoc>();
                            list.put(orderid, teleportLoc);
                        }

                        _srcMap.put(npcid, list);
                    }

                } else {
                    if (price > 0) {
                        if (time != 0) {
                            _timeMap.put(mapid, time);
                        }
                        teleportLoc.set_time(time);

                        if (user != 0) {
                            _partyMap.put(mapid, user);
                        }
                        addList(orderid, teleportLoc);
                    }
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入NPC传送点设置数量: " + _teleportLocList.size() + "("
                + timer.get() + "ms)");
        _log.info("载入时间地图设置数量: " + _timeMap.size() + "(" + timer.get() + "ms)");
        _log.info("载入团队地图设置数量: " + _partyMap.size() + "(" + timer.get() + "ms)");
        _log.info("载入官方传送点设置数量: " + _srcMap.size() + "(" + timer.get() + "ms)");
    }

    private void del(int id) {

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `npcaction_teleport` WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void addList(final String orderid, final L1TeleportLoc teleportLoc) {
        // System.out.print("orderid:" + orderid);
        HashMap<Integer, L1TeleportLoc> map = _teleportLocList.get(orderid);

        int id = 0;
        if (map != null) {
            id = map.size();
            map.put(new Integer(id), teleportLoc);

        } else {
            map = new HashMap<Integer, L1TeleportLoc>();
            map.put(new Integer(id), teleportLoc);
        }
        _teleportLocList.put(orderid, map);
    }

    public Map<String, HashMap<Integer, L1TeleportLoc>> get_locs() {
        return _teleportLocList;
    }

    /**
     * 是否为团队地图
     * 
     * @param mapid
     * @return !=0:是 0:不是
     */
    public Integer isPartyMap(final int mapid) {
        return _partyMap.get(mapid);
    }

    /**
     * 团队地图清单
     * 
     * @param mapid
     * @return true:是 false:不是
     */
    public Map<Integer, Integer> partyMaps() {
        return _partyMap;
    }

    /**
     * 是否为计时地图
     * 
     * @param mapid
     * @return true:是 false:不是
     */
    public boolean isTimeMap(final int mapid) {
        return _timeMap.get(mapid) != null;
    }

    /**
     * 计时地图清单
     * 
     * @param mapid
     * @return true:是 false:不是
     */
    public Map<Integer, Integer> timeMaps() {
        return _timeMap;
    }

    public HashMap<Integer, L1TeleportLoc> get_teles(final String orderid) {
        if (_teleportLocList.get(orderid) != null) {
            return _teleportLocList.get(orderid);
        }
        return null;
    }

    public L1TeleportLoc get_loc(final String orderid, final int id) {
        final HashMap<Integer, L1TeleportLoc> map = _teleportLocList
                .get(orderid);
        if (map != null) {
            return map.get(new Integer(id));
        }
        return null;
    }

    public boolean get_teleport(final L1PcInstance pc, final String orderid,
            final int npcid) {
        final HashMap<String, L1TeleportLoc> map = _srcMap.get(npcid);
        if (map != null) {
            L1TeleportLoc t = map.get(orderid);
            if (t != null) {
                if (t.get_price() > 0) {
                    if (!pc.getInventory().checkItem(t.get_itemid(),
                            t.get_price())) {
                        // 找回物品
                        final L1Item itemtmp = ItemTable.get().getTemplate(
                                t.get_itemid());
                        pc.sendPackets(new S_ServerMessage(337, itemtmp
                                .getNameId()));
                        return true;
                    }
                    pc.getInventory()
                            .consumeItem(t.get_itemid(), t.get_price());
                }
                L1Teleport.teleport(pc, t.get_locx(), t.get_locy(),
                        (short) t.get_mapid(), 5, true);
                return true;
            }
        }
        return false;
    }

    public void set(String orderid, int locx, int locy, int mapid, int price,
            String npcids) {
        npcids = npcids.equals("") ? "---" : npcids;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `npcaction_teleport` SET `name`=?,`orderid`=?,"
                    + "`locx`=?,`locy`=?,`mapid`=?,"
                    + "`itemid`=?,`price`=?,`time`=?,`user`=?,`min`=?,`max`=?,"
                    + "`note`=?");

            int i = 0;
            ps.setString(++i, orderid);
            ps.setString(++i, orderid);
            ps.setInt(++i, locx);
            ps.setInt(++i, locy);
            ps.setInt(++i, mapid);
            ps.setInt(++i, 40308);
            ps.setInt(++i, price);
            ps.setInt(++i, 0);
            ps.setInt(++i, 0);
            ps.setInt(++i, 0);
            ps.setInt(++i, 200);
            ps.setString(++i, npcids);
            ps.execute();

        } catch (final SQLException e) {
            // _log.error(e.getLocalizedMessage(), e);
            System.out.println("npcids:" + npcids);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

}

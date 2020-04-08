package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigBoxMsg;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Box;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 箱子开出物设置
 * 
 * @author dexc
 * 
 */
public class ItemBoxTable {

    private static final Log _log = LogFactory.getLog(ItemBoxTable.class);

    private static ItemBoxTable _instance;

    private static final Random _random = new Random();

    private static final Map<Integer, ArrayList<L1Box>> _box = new HashMap<Integer, ArrayList<L1Box>>();

    private static final Map<Integer, ArrayList<L1Box>> _boxs = new HashMap<Integer, ArrayList<L1Box>>();

    private static final Map<Integer, HashMap<Integer, ArrayList<L1Box>>> _boxkey = new HashMap<Integer, HashMap<Integer, ArrayList<L1Box>>>();

    public static ItemBoxTable get() {
        if (_instance == null) {
            _instance = new ItemBoxTable();
        }
        return _instance;
    }

    public void load() {
        load_box();
        load_boxs();
        load_box_key();
    }

    public void load_box() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_box`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("box_item_id");
                // 找回物品资讯
                final L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_box(key);
                    continue;
                }
                if (temp.getType() != 16) { // treasure_box
                    del_box(key);
                    continue;
                }

                final int get_item_id = rs.getInt("get_item_id");
                // 找回物品资讯
                final L1Item tempget = ItemTable.get().getTemplate(key);
                if (tempget == null) {
                    del_box2(key);
                    continue;
                }

                final L1Box box = new L1Box();
                box.set_box_item_id(key);
                box.set_get_item_id(get_item_id);
                box.set_randomint(rs.getInt("randomint"));
                box.set_random(rs.getInt("random"));
                box.set_min_count(rs.getInt("min_count"));
                box.set_max_count(rs.getInt("max_count"));
                box.set_out(rs.getBoolean("out"));

                ArrayList<L1Box> list = _box.get(key);
                if (list == null) {
                    list = new ArrayList<L1Box>();
                }
                list.add(box);

                _box.put(key, list);
                i++;
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入箱子开出物设置: " + _box.size() + "/" + i + "(" + timer.get()
                + "ms)");
    }

    private void del_box(final int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box` WHERE `box_item_id`=?");
            ps.setInt(1, id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box2(final int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void load_boxs() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_boxs`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int key = rs.getInt("box_item_id");
                // 找回物品资讯
                final L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_boxs(key);
                    continue;
                }
                if (temp.getType() != 16) { // treasure_box
                    del_boxs(key);
                    continue;
                }

                final int get_item_id = rs.getInt("get_item_id");
                // 找回物品资讯
                final L1Item tempget = ItemTable.get().getTemplate(key);
                if (tempget == null) {
                    del_boxs2(key);
                    continue;
                }

                final L1Box box = new L1Box();
                box.set_box_item_id(key);
                box.set_get_item_id(get_item_id);
                box.set_randomint(100);
                box.set_random(-1);
                int count = rs.getInt("count");
                box.set_min_count(count);
                box.set_max_count(count);
                box.set_out(false);
                box.set_use_type(rs.getInt("use_type"));

                ArrayList<L1Box> list = _boxs.get(key);
                if (list == null) {
                    list = new ArrayList<L1Box>();
                }
                list.add(box);

                _boxs.put(key, list);
                i++;
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入箱子开出物设置(多种): " + _boxs.size() + "/" + i + "("
                + timer.get() + "ms)");// */
    }

    private void del_boxs(final int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_boxs` WHERE `box_item_id`=?");
            ps.setInt(1, id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_boxs2(final int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_boxs` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void load_box_key() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_box_key`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final int keyId = rs.getInt("key_itemid");
                final L1Item tempkey = ItemTable.get().getTemplate(keyId);
                if (tempkey == null) {
                    del_box_key(keyId);
                    continue;
                }

                final int key = rs.getInt("box_item_id");
                // 找回物品资讯
                final L1Item temp = ItemTable.get().getTemplate(key);
                if (temp == null) {
                    del_box_key2(key);
                    continue;
                }
                if (temp.getType() != 16) { // treasure_box
                    del_box_key2(key);
                    continue;
                }

                final int get_item_id = rs.getInt("get_item_id");
                final L1Item tempget = ItemTable.get().getTemplate(get_item_id);
                if (tempget == null) {
                    del_box_key3(get_item_id);
                    continue;
                }

                final L1Box box = new L1Box();
                box.set_box_item_id(key);
                box.set_get_item_id(get_item_id);
                box.set_randomint(rs.getInt("randomint"));
                box.set_random(rs.getInt("random"));
                box.set_min_count(rs.getInt("min_count"));
                box.set_max_count(rs.getInt("max_count"));
                box.set_out(rs.getBoolean("out"));
                box.set_use_type(127);

                HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(key);
                if (map == null) {
                    map = new HashMap<Integer, ArrayList<L1Box>>();
                }

                ArrayList<L1Box> keylist = map.get(keyId);
                if (keylist == null) {
                    keylist = new ArrayList<L1Box>();
                }

                keylist.add(box);
                map.put(keyId, keylist);
                _boxkey.put(key, map);
                i++;
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入箱子开出物设置(指定使用物品开启): " + _boxkey.size() + "/" + i + "("
                + timer.get() + "ms)");// */
    }

    private void del_box_key(final int key_itemid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `key_itemid`=?");
            ps.setInt(1, key_itemid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box_key2(final int box_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `box_item_id`=?");
            ps.setInt(1, box_item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void del_box_key3(final int get_item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `etcitem_box_key` WHERE `get_item_id`=?");
            ps.setInt(1, get_item_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public ArrayList<L1Box> get(final L1PcInstance pc,
            final L1ItemInstance tgitem) {
        try {
            final ArrayList<L1Box> list = _box.get(tgitem.getItemId());
            if (list != null) {
                final BoxRandom boxs = new BoxRandom(pc, tgitem, list);
                boxs.getStart();
                return list;
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public void get_all(final L1PcInstance pc, final L1ItemInstance tgitem) {
        try {
            final ArrayList<L1Box> list = _boxs.get(tgitem.getItemId());
            if (list != null) {
                if (list.size() <= 0) {
                    return;
                }

                if (list.isEmpty()) {
                    return;
                }

                final String name = tgitem.getName();
                if (pc.getInventory().removeItem(tgitem, 1) == 1) {// 删除道具
                    for (L1Box box : list) {
                        if (!box.is_use(pc)) {
                            continue;
                        }
                        outItem(pc, box, name);
                    }
                }
            } else {
                // 没有任何事情发生
                pc.sendPackets(new S_ServerMessage(79));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 是否是正确钥匙
     * 
     * @param tgid
     * @param keyid
     * @return
     */
    public boolean is_key(final int tgid, final int keyid) {
        final HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(tgid);
        if (map != null) {
            final ArrayList<L1Box> keylist = map.get(keyid);
            if (keylist != null) {
                return true;
            }
        }
        return false;
    }

    public void get_key(final L1PcInstance pc, final L1ItemInstance tgitem,
            final int keyid) {
        try {
            final HashMap<Integer, ArrayList<L1Box>> map = _boxkey.get(tgitem
                    .getItemId());
            if (map != null) {
                final ArrayList<L1Box> keylist = map.get(keyid);
                if (keylist != null) {
                    if (keylist.size() <= 0) {
                        return;
                    }
                    if (keylist.isEmpty()) {
                        return;
                    }
                    final BoxRandom boxs = new BoxRandom(pc, tgitem, keylist);
                    boxs.getStart();
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 取回盒子物件
     * 
     * @param pc
     * @param box
     * @param tgitemName
     */
    private static void outItem(final L1PcInstance pc, final L1Box box,
            final String tgitemName) {
        if (box != null) {
            // 找回物品资讯
            final L1Item temp = ItemTable.get().getTemplate(box.get_item_id());
            // 给予随机的数量
            int count = 1;
            if (box.get_min_count() < box.get_max_count()) {
                count = _random.nextInt(box.get_max_count()
                        - box.get_min_count())
                        + box.get_min_count();

            } else {
                count = box.get_min_count();
            }

            L1ItemInstance item = null;
            if (temp.isStackable()) {
                item = ItemTable.get().createItem(box.get_item_id());
                item.setCount(count);
                createNewItem(pc, item);

            } else {
                for (int i = 0; i < count; i++) {
                    item = ItemTable.get().createItem(box.get_item_id());
                    createNewItem(pc, item);
                }
            }

            if (item != null && box.is_out()) {
                final String itemName = item.getName();
                ConfigBoxMsg.msg(pc.getName(), tgitemName, itemName);
            }
        }
    }

    /**
     * 给予物件的处理
     * 
     * @param pc
     * @param item
     * @param tgitem
     */
    private static void createNewItem(final L1PcInstance pc,
            final L1ItemInstance item) {
        try {
            pc.getInventory().storeItem(item);
            pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 获得0%。
            pc.saveInventory();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private class BoxRandom implements Runnable {

        private final L1PcInstance _pc;

        private final ArrayList<L1Box> _list_tmp = new ArrayList<L1Box>();

        private final L1ItemInstance _tgitem;

        public BoxRandom(final L1PcInstance pc, final L1ItemInstance tgitem,
                final ArrayList<L1Box> list) {
            _pc = pc;
            _tgitem = tgitem;
            _list_tmp.addAll(list);
        }

        /**
         * 启动
         * 
         * @return
         */
        public void getStart() {
            GeneralThreadPool.get().schedule(this, 0);
        }

        @Override
        public void run() {
            try {
                if (_list_tmp.size() <= 0) {
                    return;
                }
                if (_list_tmp.isEmpty()) {
                    return;
                }
                // 次数纪录(条件物品ID/次数)
                final Map<Integer, Integer> tempList = new HashMap<Integer, Integer>();

                L1Box box = null;
                int i = 0;
                while (box == null) {
                    box = runItem(tempList);
                    Thread.sleep(1);
                    i++;
                    if (i >= 300) {
                        final Object[] obj = _list_tmp.toArray();
                        box = (L1Box) obj[_random.nextInt(obj.length)];
                        if (box != null) {
                            break;
                        }
                    }
                }

                if (box != null) {
                    final String name = _tgitem.getName();
                    if (_pc.getInventory().removeItem(_tgitem, 1) == 1) {// 删除道具
                        outItem(_pc, box, name);
                    }
                }

            } catch (final Exception e) {
                _log.error("宝盒物品设置或取率可能太低,本次开启未获得任何物品(宝盒不会被删除) 宝盒编号:"
                        + _tgitem.getItemId());
            }
        }

        private L1Box runItem(final Map<Integer, Integer> tempList) {
            try {
                if (_list_tmp.size() <= 0) {
                    return null;
                }
                if (_list_tmp.isEmpty()) {
                    return null;
                }
                final int index = _random.nextInt(_list_tmp.size());
                // 抽出随机物件
                final L1Box score = _list_tmp.get(index);

                final int random = _random.nextInt(score.get_randomint());
                final int srcrandom = score.get_random();
                // 比对机率
                if (random <= srcrandom) {
                    return score;

                } else {
                    final Integer tmp = tempList.get(score.get_item_id());
                    int x = 1;
                    if (tmp != null) {
                        x = tmp + 1;
                        tempList.put(score.get_item_id(), tmp + x);

                    } else {
                        tempList.put(score.get_item_id(), 1);
                    }

                    boolean isremove = false;
                    if (srcrandom < 5000) {
                        isremove = true;

                    } else if (srcrandom >= 5000 && srcrandom < 10000) {
                        if (x > 2) {
                            isremove = true;
                        }

                    } else if (srcrandom >= 10000 && srcrandom < 20000) {
                        if (x > 4) {
                            isremove = true;
                        }

                    } else if (srcrandom >= 20000 && srcrandom < 40000) {
                        if (x > 8) {
                            isremove = true;
                        }

                    } else if (srcrandom >= 40000 && srcrandom < 80000) {
                        if (x > 16) {
                            isremove = true;
                        }

                    } else if (srcrandom >= 80000 && srcrandom < 160000) {
                        if (x > 32) {
                            isremove = true;
                        }

                    } else if (srcrandom >= 160000 && srcrandom < 320000) {
                        if (x > 64) {
                            isremove = true;
                        }
                    }
                    if (isremove) {
                        _list_tmp.remove(score);
                    }
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return null;
            }
            return null;
        }
    }

    /**
     * 新增 etcitem_box
     * 
     * @param box_item_id
     * @param get_item_id
     * @param name
     * @param randomint
     * @param random
     * @param min_count
     * @param max_count
     */
    public void set_box(final int box_item_id, final int get_item_id,
            final String name, final int randomint, final int random,
            final int min_count, final int max_count) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `etcitem_box` SET `box_item_id`=?,`get_item_id`=?,"
                    + "`name`=?,`randomint`=?,`random`=?,"
                    + "`min_count`=?,`max_count`=?," + "`out`=?");

            int i = 0;
            ps.setInt(++i, box_item_id);
            ps.setInt(++i, get_item_id);
            ps.setString(++i, name);
            ps.setInt(++i, randomint);
            ps.setInt(++i, random);
            ps.setInt(++i, min_count);
            ps.setInt(++i, max_count);
            ps.setBoolean(++i, false);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 新增 etcitem_boxs
     * 
     * @param box_item_id
     * @param get_item_id
     * @param name
     * @param count
     */
    public void set_boxs(final int box_item_id, final int get_item_id,
            final String name, final int count) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `etcitem_boxs` SET `box_item_id`=?,`get_item_id`=?,"
                    + "`name`=?,`count`=?");

            int i = 0;
            ps.setInt(++i, box_item_id);
            ps.setInt(++i, get_item_id);
            ps.setString(++i, name);
            ps.setInt(++i, count);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}

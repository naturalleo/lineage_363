package com.lineage.server.datatables.sql;

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
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.BuddyStorage;
import com.lineage.server.templates.L1BuddyTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 好友清单
 * 
 * @author dexc
 * 
 */
public class BuddyTable implements BuddyStorage {

    private static final Log _log = LogFactory.getLog(BuddyTable.class);

    private static final Map<Integer, ArrayList<L1BuddyTmp>> _buddyMap = new HashMap<Integer, ArrayList<L1BuddyTmp>>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_buddys`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final int char_id = rs.getInt("char_id");

                // 检查该资料所属是否遗失
                if (CharObjidTable.get().isChar(char_id) != null) {
                    final int buddy_id = rs.getInt("buddy_id");
                    if (CharObjidTable.get().isChar(buddy_id) != null) {
                        final String buddy_name = rs.getString("buddy_name");

                        final L1BuddyTmp buffTmp = new L1BuddyTmp();
                        buffTmp.set_char_id(char_id);
                        buffTmp.set_buddy_id(buddy_id);
                        buffTmp.set_buddy_name(buddy_name);
                        addBuddyList(char_id, buffTmp);

                    } else {
                        delete2(buddy_id);
                    }

                } else {
                    delete(char_id);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入人物好友纪录资料数量: " + _buddyMap.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 删除遗失资料
     * 
     * @param objid
     */
    private static void delete(final int objid) {
        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_buddys` WHERE `char_id`=?");
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
     * 删除遗失资料2
     * 
     * @param objid
     */
    private static void delete2(final int buddy_id) {
        // 清空资料库纪录
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_buddys` WHERE `buddy_id`=?");
            ps.setInt(1, buddy_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);

        }
    }

    /**
     * 加入清单
     * 
     * @param objId
     * @param buffTmp
     */
    private static void addBuddyList(final int objId, final L1BuddyTmp skillTmp) {
        final ArrayList<L1BuddyTmp> list = _buddyMap.get(objId);
        if (list == null) {
            final ArrayList<L1BuddyTmp> newlist = new ArrayList<L1BuddyTmp>();
            newlist.add(skillTmp);
            _buddyMap.put(objId, newlist);

        } else {
            list.add(skillTmp);
        }
    }

    /**
     * 取回该人物好友列表
     * 
     * @param pc
     * @return
     */
    @Override
    public ArrayList<L1BuddyTmp> userBuddy(final int playerobjid) {
        return _buddyMap.get(playerobjid);
    }

    /**
     * 加入好友清单
     * 
     * @param charId
     * @param objId
     * @param name
     */
    @Override
    public void addBuddy(final int charId, final int objId, final String name) {
        final ArrayList<L1BuddyTmp> list = _buddyMap.get(charId);
        if (list != null) {
            for (final L1BuddyTmp buddyTmp : list) {
                if (buddyTmp.get_buddy_id() == objId) {
                    return;
                }
            }
        }
        final L1BuddyTmp buffTmp = new L1BuddyTmp();
        buffTmp.set_char_id(charId);
        buffTmp.set_buddy_id(objId);
        buffTmp.set_buddy_name(name);

        addBuddyList(charId, buffTmp);

        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_buddys` SET "
                    + "`char_id`=?,`buddy_id`=?,`buddy_name`=?");
            ps.setInt(1, buffTmp.get_char_id());
            ps.setInt(2, buffTmp.get_buddy_id());
            ps.setString(3, buffTmp.get_buddy_name());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 移出好友清单
     * 
     * @param charId
     * @param buddyName
     */
    @Override
    public void removeBuddy(final int charId, final String buddyName) {
        final ArrayList<L1BuddyTmp> list = _buddyMap.get(charId);
        for (final L1BuddyTmp buddyTmp : list) {
            if (buddyName.equalsIgnoreCase(buddyTmp.get_buddy_name())) {
                Connection co = null;
                PreparedStatement ps = null;

                try {
                    co = DatabaseFactory.get().getConnection();
                    ps = co.prepareStatement("DELETE FROM `character_buddys` WHERE "
                            + "`char_id`=? AND `buddy_name`=?");
                    ps.setInt(1, charId);
                    ps.setString(2, buddyName);
                    ps.execute();

                } catch (final SQLException e) {
                    _log.error(e.getLocalizedMessage(), e);

                } finally {
                    SQLUtil.close(ps);
                    SQLUtil.close(co);
                }
                list.remove(buddyTmp);
            }
        }
    }
}

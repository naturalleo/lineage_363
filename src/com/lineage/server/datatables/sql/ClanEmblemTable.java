package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.storage.ClanEmblemStorage;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 盟辉图档纪录
 * 
 * @author dexc
 * 
 */
public class ClanEmblemTable implements ClanEmblemStorage {

    private static final Log _log = LogFactory.getLog(ClanEmblemTable.class);

    private static final Map<Integer, L1EmblemIcon> _iconList = new HashMap<Integer, L1EmblemIcon>();

    /**
     * 初始化载入
     */
    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `clan_emblem`");
            rs = pstm.executeQuery();

            L1EmblemIcon emblemIcon;
            while (rs.next()) {
                final int clanid = rs.getInt("clan_id");

                // 检查该资料所属是否遗失
                if (ClanReading.get().getTemplate(clanid) != null) {
                    final byte[] icon = rs.getBytes("emblemicon");
                    final int update = rs.getInt("update");

                    emblemIcon = new L1EmblemIcon();
                    emblemIcon.set_clanid(clanid);
                    emblemIcon.set_clanIcon(icon);
                    emblemIcon.set_update(update);

                    _iconList.put(clanid, emblemIcon);

                } else {
                    this.deleteIcon(clanid);
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入盟辉图档纪录资料数量: " + _iconList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 传回 Clan Icon
     */
    @Override
    public L1EmblemIcon get(final int clan_id) {
        return _iconList.get(clan_id);
    }

    /**
     * 增加虚拟血盟盟辉
     * 
     * @param clan_id
     * @param icon
     */
    @Override
    public void add(final int clan_id, final byte[] icon) {
        L1EmblemIcon emblemIcon = new L1EmblemIcon();
        emblemIcon.set_clanid(clan_id);
        emblemIcon.set_clanIcon(icon);
        emblemIcon.set_update(0);
        _iconList.put(clan_id, emblemIcon);
    }

    /**
     * 删除盟辉资料
     * 
     * @param clan_id
     */
    @Override
    public void deleteIcon(final int clan_id) {
        _iconList.remove(clan_id);

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `clan_emblem` WHERE `clan_id`=?");
            ps.setInt(1, clan_id);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    /**
     * 新建 ICON
     * 
     * @return
     */
    @Override
    public L1EmblemIcon storeClanIcon(final int clan_id, final byte[] icon) {
        final L1EmblemIcon emblemIcon = new L1EmblemIcon();
        emblemIcon.set_clanid(clan_id);
        emblemIcon.set_clanIcon(icon);
        emblemIcon.set_update(0);

        _iconList.put(clan_id, emblemIcon);

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO `clan_emblem` SET `clan_id`=?,`emblemicon`=?,`update`=?");
            int i = 0;
            pstm.setInt(++i, clan_id);
            pstm.setBytes(++i, icon);
            pstm.setInt(++i, 0);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return emblemIcon;
    }

    /**
     * 更新 ICON
     */
    @Override
    public void updateClanIcon(final L1EmblemIcon emblemIcon) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `clan_emblem` SET `emblemicon`=?,`update`=? WHERE `clan_id`=?");
            int i = 0;
            pstm.setBytes(++i, emblemIcon.get_clanIcon());
            pstm.setInt(++i, emblemIcon.get_update());
            pstm.setInt(++i, emblemIcon.get_clanid());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}

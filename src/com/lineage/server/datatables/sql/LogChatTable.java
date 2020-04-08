package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.LogChatStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;

/**
 * 对话纪录
 * 
 * @author dexc
 * 
 */
public class LogChatTable implements LogChatStorage {

    private static final Log _log = LogFactory.getLog(LogChatTable.class);

    /**
     * 具有传送对象
     * 
     * @param pc
     * @param target
     * @param content
     * @param type
     */
    @Override
    public void isTarget(final L1PcInstance pc, final L1PcInstance target,
            final String content, final int type) {
        if (target == null) {
            return;
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            final String account_name = pc.getAccountName();
            final int char_id = pc.getId();
            final String name = pc.isGm() ? "******" : pc.getName();
            final int clan_id = pc.getClanid();
            final String clan_name = pc.getClanname();
            final int locx = pc.getX();
            final int locy = pc.getY();
            final short mapid = pc.getMapId();

            final String target_account_name = target.getAccountName();
            final int target_id = target.getId();
            final String target_name = target.getName();
            final int target_clan_id = target.getClanid();
            final String target_clan_name = target.getClanname();
            final int target_locx = target.getX();
            final int target_locy = target.getY();
            final short target_mapid = target.getMapId();

            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO other_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, target_account_name, target_id, target_name, target_clan_id, target_clan_name, target_locx, target_locy, target_mapid, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
            pstm.setString(1, account_name);
            pstm.setInt(2, char_id);
            pstm.setString(3, name);
            pstm.setInt(4, clan_id);
            pstm.setString(5, clan_name);
            pstm.setInt(6, locx);
            pstm.setInt(7, locy);
            pstm.setInt(8, mapid);
            pstm.setInt(9, type);
            pstm.setString(10, target_account_name);
            pstm.setInt(11, target_id);
            pstm.setString(12, target_name);
            pstm.setInt(13, target_clan_id);
            pstm.setString(14, target_clan_name);
            pstm.setInt(15, target_locx);
            pstm.setInt(16, target_locy);
            pstm.setInt(17, target_mapid);
            pstm.setString(18, content);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 无传送对象
     * 
     * @param pc
     * @param content
     * @param type
     */
    @Override
    public void noTarget(final L1PcInstance pc, final String content,
            final int type) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            final String account_name = pc.getAccountName();
            final int char_id = pc.getId();
            final String name = pc.isGm() ? "******" : pc.getName();
            final int clan_id = pc.getClanid();
            final String clan_name = pc.getClanname();
            final int locx = pc.getX();
            final int locy = pc.getY();
            final short mapid = pc.getMapId();

            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO other_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
            pstm.setString(1, account_name);
            pstm.setInt(2, char_id);
            pstm.setString(3, name);
            pstm.setInt(4, clan_id);
            pstm.setString(5, clan_name);
            pstm.setInt(6, locx);
            pstm.setInt(7, locy);
            pstm.setInt(8, mapid);
            pstm.setInt(9, type);
            pstm.setString(10, content);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

}

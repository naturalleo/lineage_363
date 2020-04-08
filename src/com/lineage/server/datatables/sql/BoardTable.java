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
import com.lineage.server.datatables.storage.BoardStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 布告栏资料
 * 
 * @author dexc
 * 
 */
public class BoardTable implements BoardStorage {

    private static final Log _log = LogFactory.getLog(BoardTable.class);

    private static final Map<Integer, L1Board> _boards = new HashMap<Integer, L1Board>();

    private static int _maxid = 0;

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
            ps = co.prepareStatement("SELECT * FROM `server_board` ORDER BY `id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                final L1Board board = new L1Board();
                final int id = rs.getInt("id");
                if (id > _maxid) {
                    _maxid = id;
                }
                board.set_id(id);
                board.set_name(rs.getString("name"));
                board.set_date(rs.getString("date"));
                board.set_title(rs.getString("title"));
                board.set_content(rs.getString("content"));

                _boards.put(board.get_id(), board);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("载入布告栏资料数量: " + _boards.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 传回公告MAP
     */
    @Override
    public Map<Integer, L1Board> getBoardMap() {
        return _boards;
    }

    /**
     * 传回公告阵列
     */
    @Override
    public L1Board[] getBoardTableList() {
        return _boards.values().toArray(new L1Board[_boards.size()]);
    }

    /**
     * 传回指定公告
     */
    @Override
    public L1Board getBoardTable(final int houseId) {
        return _boards.get(houseId);
    }

    /**
     * 传回已用最大公告编号
     */
    @Override
    public int getMaxId() {
        return _maxid;
    }

    /**
     * 增加布告栏资料
     */
    @Override
    public void writeTopic(final L1PcInstance pc, final String date,
            final String title, final String content) {

        final L1Board board = new L1Board();
        board.set_id(++_maxid);
        board.set_name(pc.getName());
        board.set_date(date);
        board.set_title(title);
        board.set_content(content);

        _boards.put(board.get_id(), board);

        Connection co = null;
        PreparedStatement ps = null;
        final ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `server_board` SET `id`=?,`name`=?,`date`=?,`title`=?,`content`=?");
            ps.setInt(1, board.get_id());
            ps.setString(2, board.get_name());
            ps.setString(3, board.get_date());
            ps.setString(4, board.get_title());
            ps.setString(5, board.get_content());
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    /**
     * 删除布告栏资料
     */
    @Override
    public void deleteTopic(final int number) {
        final L1Board board = _boards.get(number);
        if (board != null) {
            Connection co = null;
            PreparedStatement ps = null;
            try {
                co = DatabaseFactory.get().getConnection();
                ps = co.prepareStatement("DELETE FROM `server_board` WHERE `id`=?");
                ps.setInt(1, board.get_id());
                ps.execute();

                _boards.remove(number);

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(co);
            }
        }
    }
}

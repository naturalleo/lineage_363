package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.datatables.storage.AuctionBoardStorage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 盟屋拍卖公告栏资料
 * 
 * @author dexc
 * 
 */
public class AuctionBoardTable implements AuctionBoardStorage {

    private static final Log _log = LogFactory.getLog(AuctionBoardTable.class);

    private static final Map<Integer, L1AuctionBoardTmp> _boards = new HashMap<Integer, L1AuctionBoardTmp>();

    private Calendar timestampToCalendar(final Timestamp ts) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        return cal;
    }

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
            pstm = con.prepareStatement("SELECT * FROM `server_board_auction`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1AuctionBoardTmp board = new L1AuctionBoardTmp();
                final int houseId = rs.getInt("house_id");
                String house_name = rs.getString("house_name");
                final int house_area = rs.getInt("house_area");
                final Calendar deadline = this
                        .timestampToCalendar((Timestamp) rs
                                .getObject("deadline"));
                String location = rs.getString("location");
                final long price = rs.getLong("price");
                final String old_owner = rs.getString("old_owner");
                final int old_owner_id = rs.getInt("old_owner_id");
                final String bidder = rs.getString("bidder");
                final int bidder_id = rs.getInt("bidder_id");

                if (!location.startsWith("$")) {
                    String townName = "";
                    if ((houseId >= 262145) && (houseId <= 262189)) { // 奇岩
                        townName = "$1242 ";
                    }

                    if ((houseId >= 327681) && (houseId <= 327691)) { // 海音
                        townName = "$1513 ";
                    }

                    if ((houseId >= 458753) && (houseId <= 458819)) { // 亚丁
                        townName = "$2129 ";
                    }

                    if ((houseId >= 524289) && (houseId <= 524294)) { // 古鲁丁
                        townName = "$381 ";
                    }
                    location = townName + " " + location;
                }
                if (house_name.equals("null")) {
                    house_name = location + "$1195";
                }

                board.setHouseId(houseId);
                board.setHouseName(house_name);
                board.setHouseArea(house_area);
                board.setDeadline(deadline);
                board.setPrice(price);
                board.setLocation(location);
                board.setOldOwner(old_owner);
                board.setOldOwnerId(old_owner_id);
                board.setBidder(bidder);
                board.setBidderId(bidder_id);

                _boards.put(board.getHouseId(), board);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入盟屋拍卖公告栏资料数量: " + _boards.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 传回公告阵列
     */
    @Override
    public Map<Integer, L1AuctionBoardTmp> getAuctionBoardTableList() {
        return _boards;
    }

    /**
     * 传回指定公告
     */
    @Override
    public L1AuctionBoardTmp getAuctionBoardTable(final int houseId) {
        return _boards.get(houseId);
    }

    /**
     * 增加公告
     */
    @Override
    public void insertAuctionBoard(final L1AuctionBoardTmp board) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO `server_board_auction` SET "
                            + "`house_id`=?,`house_name`=?,`house_area`=?,"
                            + "`deadline`=?,`price`=?,`location`=?,`old_owner`=?,"
                            + "`old_owner_id`=?,`bidder`=?,`bidder_id`=?");
            pstm.setInt(1, board.getHouseId());
            pstm.setString(2, board.getHouseName());
            pstm.setInt(3, board.getHouseArea());

            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
            final String fm = sdf.format(Calendar.getInstance(tz).getTime());
            pstm.setString(4, fm);

            pstm.setLong(5, board.getPrice());
            pstm.setString(6, board.getLocation());
            pstm.setString(7, board.getOldOwner());
            pstm.setInt(8, board.getOldOwnerId());
            pstm.setString(9, board.getBidder());
            pstm.setInt(10, board.getBidderId());
            pstm.execute();

            _boards.put(board.getHouseId(), board);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 更新公告
     */
    @Override
    public void updateAuctionBoard(final L1AuctionBoardTmp board) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `server_board_auction` SET "
                    + "`house_name`=?,`house_area`=?,`deadline`=?,"
                    + "`price`=?,`location`=?,`old_owner`=?,"
                    + "`old_owner_id`=?,`bidder`=?,"
                    + "`bidder_id`=? WHERE `house_id`=?");
            pstm.setString(1, board.getHouseName());
            pstm.setInt(2, board.getHouseArea());

            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            final String fm = sdf.format(board.getDeadline().getTime());

            pstm.setString(3, fm);

            pstm.setLong(4, board.getPrice());
            pstm.setString(5, board.getLocation());
            pstm.setString(6, board.getOldOwner());
            pstm.setInt(7, board.getOldOwnerId());
            pstm.setString(8, board.getBidder());
            pstm.setInt(9, board.getBidderId());
            pstm.setInt(10, board.getHouseId());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 删除公告
     */
    @Override
    public void deleteAuctionBoard(final int houseId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM `server_board_auction` WHERE `house_id`=?");
            pstm.setInt(1, houseId);
            pstm.execute();

            _boards.remove(houseId);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

}

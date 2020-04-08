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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.HouseStorage;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.templates.L1House;
import com.lineage.server.templates.L1HouseLocTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 盟屋资料
 * 
 * @author dexc
 * 
 */
public class HouseTable implements HouseStorage {

    private static final Log _log = LogFactory.getLog(HouseTable.class);

    // 小屋资料(小屋编号/小屋资料)
    private static final Map<Integer, L1House> _house = new HashMap<Integer, L1House>();

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
            pstm = con
                    .prepareStatement("SELECT * FROM `server_house` ORDER BY `house_id`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1House house = new L1House();

                final int houseId = rs.getInt("house_id");
                house.setHouseId(houseId);

                String house_name = rs.getString("house_name");
                int house_area = rs.getInt("house_area");
                String location = rs.getString("location");
                int keeper_id = rs.getInt("keeper_id");
                boolean is_on_sale = rs.getInt("is_on_sale") == 1 ? true
                        : false;
                boolean is_purchase_basement = rs
                        .getInt("is_purchase_basement") == 1 ? true : false;
                final Timestamp tax_deadline = rs.getTimestamp("tax_deadline");

                house.setHouseName(house_name);
                house.setHouseArea(house_area);
                house.setLocation(location);
                house.setKeeperId(keeper_id);
                house.setOnSale(is_on_sale);
                house.setPurchaseBasement(is_purchase_basement);
                house.setTaxDeadline(this.timestampToCalendar(tax_deadline));

                // 加载入小屋资讯
                _house.put(houseId, house);

                int locx1 = rs.getInt("locx1");
                int locx2 = rs.getInt("locx2");
                int locy1 = rs.getInt("locy1");
                int locy2 = rs.getInt("locy2");
                int locx3 = rs.getInt("locx3");
                int locx4 = rs.getInt("locx4");
                int locy3 = rs.getInt("locy3");
                int locy4 = rs.getInt("locy4");
                int mapid = rs.getInt("mapid");

                int basement = rs.getInt("basement");

                int homelocx = rs.getInt("homelocx");
                int homelocy = rs.getInt("homelocy");

                if (locx1 != 0) {
                    // 建立小屋座标资讯
                    L1HouseLocTmp locTmp = new L1HouseLocTmp();
                    locTmp.set_locx1(locx1);
                    locTmp.set_locx2(locx2);
                    locTmp.set_locy1(locy1);
                    locTmp.set_locy2(locy2);
                    locTmp.set_locx3(locx3);
                    locTmp.set_locx4(locx4);
                    locTmp.set_locy3(locy3);
                    locTmp.set_locy4(locy4);
                    locTmp.set_mapid(mapid);

                    locTmp.set_basement(basement);

                    locTmp.set_homelocx(homelocx);
                    locTmp.set_homelocy(homelocy);

                    // 加入小屋座标资讯
                    L1HouseLocation.put(houseId, locTmp);
                }

                // L1HouseLocation.add(houseId);
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入盟屋资料数量: " + _house.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 传回小屋列表
     * 
     * @return
     */
    @Override
    public Map<Integer, L1House> getHouseTableList() {
        return _house;
    }

    /**
     * 传回指定小屋资料
     * 
     * @param houseId
     * @return
     */
    @Override
    public L1House getHouseTable(final int houseId) {
        return _house.get(houseId);
    }

    /**
     * 更新小屋资料
     * 
     * @param house
     */
    @Override
    public void updateHouse(final L1House house) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `server_house` SET `house_name`=?,`house_area`=?,`location`=?, "
                            + "`keeper_id`=?,`is_on_sale`=?,`is_purchase_basement`=?,`tax_deadline`=? WHERE `house_id`=?");
            pstm.setString(1, house.getHouseName());
            pstm.setInt(2, house.getHouseArea());
            pstm.setString(3, house.getLocation());
            pstm.setInt(4, house.getKeeperId());
            pstm.setInt(5, house.isOnSale() == true ? 1 : 0);
            pstm.setInt(6, house.isPurchaseBasement() == true ? 1 : 0);

            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss");
            final String fm = sdf.format(house.getTaxDeadline().getTime());

            // System.out.println(fm);
            pstm.setString(7, fm);

            pstm.setInt(8, house.getHouseId());
            pstm.execute();
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}

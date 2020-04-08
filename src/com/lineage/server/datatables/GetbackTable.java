package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 回村座标设置
 * 
 * @author dexc
 * 
 */
public class GetbackTable {

    private static final Log _log = LogFactory.getLog(GetbackTable.class);

    private static Random _random = new Random();

    private static Map<Integer, ArrayList<GetbackTable>> _getback = new HashMap<Integer, ArrayList<GetbackTable>>();

    private int _areaX1;
    private int _areaY1;
    private int _areaX2;
    private int _areaY2;
    private int _areaMapId;
    private int _getbackX1;
    private int _getbackY1;
    private int _getbackX2;
    private int _getbackY2;
    private int _getbackX3;
    private int _getbackY3;
    private int _getbackMapId;
    private int _getbackTownId;
    private int _getbackTownIdForElf;
    private int _getbackTownIdForDarkelf;

    private GetbackTable() {
    }

    /**
     * 具有多重回村座标
     * 
     * @return
     */
    private boolean isSpecifyArea() {
        return ((this._areaX1 != 0) && (this._areaY1 != 0)
                && (this._areaX2 != 0) && (this._areaY2 != 0));
    }

    public static void loadGetBack() {
        _getback.clear();
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            // 同マップでエリア指定と无指定が混在していたら、エリア指定を先に读み迂む为にarea_x1 DESC
            final String sSQL = "SELECT * FROM `getback` ORDER BY `area_mapid`,`area_x1` DESC ";
            pstm = con.prepareStatement(sSQL);
            rs = pstm.executeQuery();
            while (rs.next()) {
                final GetbackTable getback = new GetbackTable();
                getback._areaX1 = rs.getInt("area_x1");
                getback._areaY1 = rs.getInt("area_y1");
                getback._areaX2 = rs.getInt("area_x2");
                getback._areaY2 = rs.getInt("area_y2");
                getback._areaMapId = rs.getInt("area_mapid");
                getback._getbackX1 = rs.getInt("getback_x1");
                getback._getbackY1 = rs.getInt("getback_y1");
                getback._getbackX2 = rs.getInt("getback_x2");
                getback._getbackY2 = rs.getInt("getback_y2");
                getback._getbackX3 = rs.getInt("getback_x3");
                getback._getbackY3 = rs.getInt("getback_y3");
                getback._getbackMapId = rs.getInt("getback_mapid");
                getback._getbackTownId = rs.getInt("getback_townid");
                getback._getbackTownIdForElf = rs.getInt("getback_townid_elf");
                getback._getbackTownIdForDarkelf = rs
                        .getInt("getback_townid_darkelf");
                // getback._escapable = rs.getBoolean("scrollescape");
                ArrayList<GetbackTable> getbackList = _getback
                        .get(getback._areaMapId);

                if (getbackList == null) {
                    getbackList = new ArrayList<GetbackTable>();
                    _getback.put(getback._areaMapId, getbackList);
                }
                getbackList.add(getback);
            }
        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入回村座标设置数量: " + _getback.size() + "(" + timer.get() + "ms)");
    }

    /**
     * pcの现在地から归还ポイントを取得する。
     * 
     * @param pc
     * @param bScroll_Escape
     *            (未使用)
     * @return locx,locy,mapidの顺に格纳されている配列
     */
    public static int[] GetBack_Location(final L1PcInstance pc,
            final boolean bScroll_Escape) {

        int[] loc = new int[3];

        final int nPosition = _random.nextInt(3);

        final int pcLocX = pc.getX();
        final int pcLocY = pc.getY();
        final int pcMapId = pc.getMapId();
        final ArrayList<GetbackTable> getbackList = _getback.get(pcMapId);

        if (getbackList != null) {
            GetbackTable getback = null;
            for (final GetbackTable gb : getbackList) {
                if (gb.isSpecifyArea()) {
                    if ((gb._areaX1 <= pcLocX) && (pcLocX <= gb._areaX2)
                            && (gb._areaY1 <= pcLocY) && (pcLocY <= gb._areaY2)) {
                        getback = gb;
                        break;
                    }
                } else {
                    getback = gb;
                    break;
                }
            }

            loc = ReadGetbackInfo(getback, nPosition);

            // town_idが指定されている场合はそこへ归还させる
            if (pc.isElf() && (getback._getbackTownIdForElf > 0)) {
                loc = L1TownLocation
                        .getGetBackLoc(getback._getbackTownIdForElf);

            } else if (pc.isDarkelf() && (getback._getbackTownIdForDarkelf > 0)) {
                loc = L1TownLocation
                        .getGetBackLoc(getback._getbackTownIdForDarkelf);

            } else if (getback._getbackTownId > 0) {
                loc = L1TownLocation.getGetBackLoc(getback._getbackTownId);
            }
        }
        // getbackテーブルにデータがない场合、SKTに归还
        else {
            loc[0] = 33089;
            loc[1] = 33397;
            loc[2] = 4;
        }
        return loc;
    }

    private static int[] ReadGetbackInfo(final GetbackTable getback,
            final int nPosition) {
        final int[] loc = new int[3];
        switch (nPosition) {
            case 0:
                loc[0] = getback._getbackX1;
                loc[1] = getback._getbackY1;
                break;

            case 1:
                loc[0] = getback._getbackX2;
                loc[1] = getback._getbackY2;
                break;

            case 2:
                loc[0] = getback._getbackX3;
                loc[1] = getback._getbackY3;
                break;
        }
        loc[2] = getback._getbackMapId;

        return loc;
    }
}

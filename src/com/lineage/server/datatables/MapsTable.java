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
import com.lineage.server.templates.MapData;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 地图设置资料
 * 
 * @author dexc
 * 
 */
public final class MapsTable {

    private static final Log _log = LogFactory.getLog(MapsTable.class);

    /*
     * private class MapData { public int startX = 0; public int endX = 0;
     * public int startY = 0; public int endY = 0; public double monster_amount
     * = 1; public double dropRate = 1; public boolean isUnderwater = false;
     * public boolean markable = false; public boolean teleportable = false;
     * public boolean escapable = false; public boolean isUseResurrection =
     * false; public boolean isUsePainwand = false; public boolean
     * isEnabledDeathPenalty = false; public boolean isTakePets = false; public
     * boolean isRecallPets = false; public boolean isUsableItem = false; public
     * boolean isUsableSkill = false; }
     */

    private static MapsTable _instance;

    /**
     * Key(mapId) Value(MapData)
     */
    private static final Map<Integer, MapData> _maps = new HashMap<Integer, MapData>();

    /**
     * マップのテレポート可否フラグをデータベースから读み迂み、HashMap _mapsに格纳する。
     */
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `mapids`");

            for (rs = pstm.executeQuery(); rs.next();) {
                final MapData data = new MapData();
                final int mapId = rs.getInt("mapid");

                data.mapId = mapId;
                data.startX = rs.getInt("startX");
                data.endX = rs.getInt("endX");
                data.startY = rs.getInt("startY");
                data.endY = rs.getInt("endY");
                data.monster_amount = rs.getDouble("monster_amount");
                data.dropRate = rs.getDouble("drop_rate");
                data.isUnderwater = rs.getBoolean("underwater");
                data.markable = rs.getBoolean("markable");
                data.teleportable = rs.getBoolean("teleportable");
                data.escapable = rs.getBoolean("escapable");
                data.isUseResurrection = rs.getBoolean("resurrection");
                data.isUsePainwand = rs.getBoolean("painwand");
                data.isEnabledDeathPenalty = rs.getBoolean("penalty");
                data.isTakePets = rs.getBoolean("take_pets");
                data.isRecallPets = rs.getBoolean("recall_pets");
                data.isUsableItem = rs.getBoolean("usable_item");
                data.isUsableSkill = rs.getBoolean("usable_skill");

                _maps.put(new Integer(mapId), data);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        _log.info("载入地图设置资料数量: " + _maps.size() + "(" + timer.get() + "ms)");
    }

    /**
     * MapsTableのインスタンスを返す。
     * 
     * @return MapsTableのインスタンス
     */
    public static MapsTable get() {
        if (_instance == null) {
            _instance = new MapsTable();
        }
        return _instance;
    }

    /**
     * 传回全部地图资料
     * 
     * @return
     */
    public Map<Integer, MapData> getMaps() {
        return _maps;
    }

    /**
     * X开始座标
     * 
     * @param mapId
     *            地图编号
     * @return X开始座标
     */
    public int getStartX(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).startX;
    }

    /**
     * マップがのX终了座标を返す。
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return X终了座标
     */
    public int getEndX(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).endX;
    }

    /**
     * マップがのY开始座标を返す。
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return Y开始座标
     */
    public int getStartY(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).startY;
    }

    /**
     * マップがのY终了座标を返す。
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return Y终了座标
     */
    public int getEndY(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return _maps.get(mapId).endY;
    }

    /**
     * NPC数量
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return モンスター量の倍率
     */
    public double getMonsterAmount(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return map.monster_amount;
    }

    /**
     * 掉落倍率
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return ドロップ倍率
     */
    public double getDropRate(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return 0;
        }
        return map.dropRate;
    }

    /**
     * 水中
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return 水中であればtrue
     */
    public boolean isUnderwater(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUnderwater;
    }

    /**
     * 记忆座标
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return ブックマーク可能であればtrue
     */
    public boolean isMarkable(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).markable;
    }

    /**
     * 使用传送
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return 可能であればtrue
     */
    public boolean isTeleportable(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).teleportable;
    }

    /**
     * 使用回卷
     * 
     * @param mapId
     *            调べるマップのマップID
     * @return 可能であればtrue
     */
    public boolean isEscapable(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).escapable;
    }

    /**
     * 复活
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return 复活可能であればtrue
     */
    public boolean isUseResurrection(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUseResurrection;
    }

    /**
     * 使用魔杖
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return パインワンド使用可能であればtrue
     */
    public boolean isUsePainwand(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsePainwand;
    }

    /**
     * 死亡逞罚
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return デスペナルティであればtrue
     */
    public boolean isEnabledDeathPenalty(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isEnabledDeathPenalty;
    }

    /**
     * 携带宠物
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return ペット?サモンを连れて行けるならばtrue
     */
    public boolean isTakePets(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isTakePets;
    }

    /**
     * 召唤宠物
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return ペット?サモンを呼び出せるならばtrue
     */
    public boolean isRecallPets(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isRecallPets;
    }

    /**
     * 使用物品
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return アイテムを使用できるならばtrue
     */
    public boolean isUsableItem(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsableItem;
    }

    /**
     * 使用技能
     * 
     * @param mapId
     *            调べるマップのマップID
     * 
     * @return スキルを使用できるならばtrue
     */
    public boolean isUsableSkill(final int mapId) {
        final MapData map = _maps.get(mapId);
        if (map == null) {
            return false;
        }
        return _maps.get(mapId).isUsableSkill;
    }

}

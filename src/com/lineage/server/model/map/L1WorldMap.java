package com.lineage.server.model.map;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.MapReader;
import com.lineage.TextMapReader;
import com.lineage.server.utils.PerformanceTimer;

/**
 * 世界地图资讯
 * 
 * @author dexc
 * 
 */
public class L1WorldMap {

    private static final Log _log = LogFactory.getLog(TextMapReader.class);

    private static L1WorldMap _instance;

    // MAPID MAP资讯
    private Map<Integer, L1Map> _maps;

    public static L1WorldMap get() {
        if (_instance == null) {
            _instance = new L1WorldMap();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        _log.info("MAP进行数据加载...");

        final MapReader in = new TextMapReader();

        this._maps = in.read();
        if (this._maps == null) {
            _log.error("MAP数据载入异常 maps未建立初始化");
            System.exit(0);
            return;
        }

        _log.info("MAP数据加载完成(" + timer.get() + " ms)");
    }
    
    /**
     * 增加指定的新虚拟地图.
     * 
     * @param map
     *            新虚拟地图
     */
    public synchronized void addMap(final L1Map map) {
        this._maps.put(map.getId(), map);
    }

    /**
     * 指定地图编号 返回地图资讯
     * 
     * @param mapId
     *            地图编号
     * @return 地图资讯
     */
    public L1Map getMap(final short mapId) {
        L1Map map = this._maps.get((int) mapId);
        if (map == null) {
            map = L1Map.newNull();
        }
        return map;
    }
    
    /**
     * 删除指定的虚拟地图.
     * 
     * @param map
     *            虚拟地图ID
     */
    public synchronized void removeMap(final int mapId) {
        this._maps.remove(mapId);
    }
}

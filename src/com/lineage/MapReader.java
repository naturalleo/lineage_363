package com.lineage;

import java.io.IOException;
import java.util.Map;

import com.lineage.server.model.map.L1Map;

/**
 * 世界地图设定档案加载
 */
public abstract class MapReader {

    /**
     * 全部地图资料生成
     * 
     * @return Map
     * @throws IOException
     */
    public abstract Map<Integer, L1Map> read();

}

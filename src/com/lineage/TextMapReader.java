package com.lineage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1V1Map;
import com.lineage.server.templates.MapData;

/**
 * 世界地图设定档案加载
 */
public class TextMapReader extends MapReader {

    private static final Log _log = LogFactory.getLog(TextMapReader.class);

    /** 存放设定档的资料夹 */
    private static final String MAP_DIR = "./maps/";

    /**
     * 指定地图加载障碍数据
     * 
     * @param mapId
     *            地图编号
     * @param xSize
     *            X座标长度
     * @param ySize
     *            Y座标长度
     * 
     * @return byte[][]
     * 
     * @throws IOException
     */
    private byte[][] read(final int mapId, final int xSize, final int ySize) {
        final byte[][] map = new byte[xSize][ySize];
        try {
            final LineNumberReader in = new LineNumberReader(
                    new BufferedReader(new FileReader(MAP_DIR + mapId + ".txt")));

            int y = 0;
            String line;
            while ((line = in.readLine()) != null) {
                if ((line.trim().length() == 0) || line.startsWith("#")) {
                    continue; // 空行とコメントをスキップ
                }

                int x = 0;
                final StringTokenizer tok = new StringTokenizer(line, ",");
                while (tok.hasMoreTokens()) {
                    final byte tile = Byte.parseByte(tok.nextToken());

                    try {
                        map[x][y] = tile;

                    } catch (final ArrayIndexOutOfBoundsException e) {
                        // TXT档案内文字长度以及行数少于要求读取的长度
                        _log.error("指定地图加载障碍数据异常: " + mapId + " X:" + x + " Y:"
                                + y, e);
                    }

                    x++;
                }
                y++;
            }
            in.close();

        } catch (final Exception e) {
            _log.error("指定地图加载障碍数据异常: " + mapId);
        }
        return map;
    }

    /**
     * 全部地图资料生成
     * 
     * @return Map<Integer, L1Map>
     */
    @Override
    public Map<Integer, L1Map> read() {
        final Map<Integer, L1Map> maps = new HashMap<Integer, L1Map>();

        final Map<Integer, MapData> mapDatas = MapsTable.get().getMaps();

        for (final Integer key : mapDatas.keySet()) {
            final MapData mapData = mapDatas.get(key);

            final int mapId = mapData.mapId;
            final int xSize = mapData.endX - mapData.startX + 1;
            final int ySize = mapData.endY - mapData.startY + 1;
            L1V1Map map = null;
            try {
                map = new L1V1Map((short) mapId,
                        this.read(mapId, xSize, ySize), mapData.startX,
                        mapData.startY, mapData.isUnderwater, mapData.markable,
                        mapData.teleportable, mapData.escapable,
                        mapData.isUseResurrection, mapData.isUsePainwand,
                        mapData.isEnabledDeathPenalty, mapData.isTakePets,
                        mapData.isRecallPets, mapData.isUsableItem,
                        mapData.isUsableSkill);

            } catch (final Exception e) {
                _log.error("地图资料生成数据载入异常: " + mapId, e);
            }

            if (map != null) {
                maps.put(mapId, map);
            }
        }
        return maps;
    }
}

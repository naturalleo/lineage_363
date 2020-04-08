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
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.storage.FurnitureSpawnStorage;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.templates.L1Furniture;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 家具资料
 * 
 * @author dexc
 * 
 */
public class FurnitureSpawnTable implements FurnitureSpawnStorage {

    private static final Log _log = LogFactory
            .getLog(FurnitureSpawnTable.class);

    private static final Map<Integer, L1Furniture> _furnitureList = new HashMap<Integer, L1Furniture>();

    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `spawnlist_furniture`");
            rs = ps.executeQuery();

            while (rs.next()) {
                int npcid = rs.getInt("npcid");

                final L1Npc npc = NpcTable.get().getTemplate(npcid);

                if (npc != null) {
                    int item_obj_id = rs.getInt("item_obj_id");
                    int locx = rs.getInt("locx");
                    int locy = rs.getInt("locy");
                    short mapid = rs.getShort("mapid");

                    // 搜寻对应道具是否存在
                    if (World.get().findObject(item_obj_id) != null) {
                        L1Furniture value = new L1Furniture();

                        value.set_npcid(npcid);
                        value.set_item_obj_id(item_obj_id);
                        value.set_locx(locx);
                        value.set_locy(locy);
                        value.set_mapid(mapid);

                        _furnitureList.put(item_obj_id, value);

                    } else {
                        delFurniture(item_obj_id);
                    }
                }
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);

            spawnFurniture();
        }
        _log.info("载入家具位置资料数量: " + _furnitureList.size() + "(" + timer.get()
                + "ms)");
    }

    /**
     * 召唤
     */
    private static void spawnFurniture() {
        for (Integer key : _furnitureList.keySet()) {
            L1Furniture value = _furnitureList.get(key);
            L1SpawnUtil.spawn(value);
        }
    }

    /**
     * 删除错误物品资料
     * 
     * @param objid
     */
    private static void delFurniture(final int objid) {
        Connection cn = null;
        PreparedStatement pm = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            pm = cn.prepareStatement("DELETE FROM `spawnlist_furniture` WHERE `item_obj_id`=?");
            pm.setInt(1, objid);
            pm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(cn);
        }
    }

    /**
     * 删除物品资料
     */
    @Override
    public void deleteFurniture(final L1FurnitureInstance furniture) {
        int key = furniture.getItemObjId();
        if (_furnitureList.remove(key) != null) {
            delFurniture(key);
        }
    }

    /**
     * 新建物品资料
     */
    @Override
    public void insertFurniture(final L1FurnitureInstance furniture) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            int item_obj_id = furniture.getItemObjId();
            int npcid = furniture.getNpcTemplate().get_npcId();
            int locx = furniture.getX();
            int locy = furniture.getY();
            short mapid = furniture.getMapId();

            L1Furniture value = new L1Furniture();

            value.set_npcid(npcid);
            value.set_item_obj_id(item_obj_id);
            value.set_locx(locx);
            value.set_locy(locy);
            value.set_mapid(mapid);

            _furnitureList.put(item_obj_id, value);

            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `spawnlist_furniture` SET `item_obj_id`=?,`npcid`=?,`locx`=?,`locy`=?,`mapid`=?");
            ps.setInt(1, item_obj_id);
            ps.setInt(2, npcid);
            ps.setInt(3, locx);
            ps.setInt(4, locy);
            ps.setInt(5, mapid);
            ps.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}

package com.lineage.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.ServerReading;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * OBJID分配器
 * 
 * @author dexc
 * 
 */
public class IdFactory {

    private static final Log _log = LogFactory.getLog(IdFactory.class);

    private static IdFactory _instance;

    // 同步层次
    private Object _monitor;

    private AtomicInteger _nextId;

    public static IdFactory get() {
        if (_instance == null) {
            _instance = new IdFactory();
            // _firstID = 0xf4240;// 1000000
        }
        return _instance;
    }

    public IdFactory() {
        _monitor = new Object();
    }

    /**
     * 以原子方式将当前值加 1
     * 
     * @return
     */
    public int nextId() {
        synchronized (_monitor) {
            return _nextId.getAndIncrement();
        }
    }

    /**
     * 获取当前值
     * 
     * @return
     */
    public int maxId() {
        synchronized (_monitor) {
            return _nextId.get();
        }
    }

    /**
     * 取回资料库中已用最大编号
     */
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT MAX(id)+1 AS " + "NEXTID FROM ("
                    + "SELECT `id` FROM `character_items` UNION ALL " + // 背包
                    "SELECT `id` FROM `character_warehouse` UNION ALL " + // 仓库
                    "SELECT `id` FROM `character_elf_warehouse` UNION ALL " + // 精灵仓库
                    "SELECT `id` FROM `clan_warehouse` UNION ALL " + // 血盟仓库
                    "SELECT `id` FROM `character_shopinfo` UNION ALL " + // 拍卖道具
                    "SELECT `objid` AS `id` FROM `characters` UNION ALL " + // 人物
                    "SELECT `clan_id` AS `id` FROM `clan_data` UNION ALL " + // 血盟
                    "SELECT `id` FROM `character_teleport` UNION ALL " + // 传送点
                    "SELECT `id` FROM `character_mail` UNION ALL " + // MAIL
                    "SELECT `objid` AS `id` FROM `character_pets`) t"// 宠物
            );
            rs = ps.executeQuery();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt("nextid");
            }

            if (id < ServerReading.get().minId()) {
                id = ServerReading.get().minId();
            }

            if (id < ServerReading.get().maxId()) {
                id = ServerReading.get().maxId();
            }

            // this._curId = id;
            _nextId = new AtomicInteger(id);
            _log.info("载入已用最大id编号: " + id + "(" + timer.get() + "ms)");

        } catch (final SQLException e) {
            _log.error("id数据加载异常!", e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}

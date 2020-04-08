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
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.templates.L1Scenery;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 景观设置资料
 * 
 * @author dexc
 * 
 */
public class SceneryTable {

    private static final Log _log = LogFactory.getLog(SceneryTable.class);

    private static SceneryTable _instance;

    private static final Map<Integer, L1Scenery> _sceneryList = new HashMap<Integer, L1Scenery>();

    private static final Map<Integer, L1Scenery> _fieldList = new HashMap<Integer, L1Scenery>();

    public static SceneryTable get() {
        if (_instance == null) {
            _instance = new SceneryTable();
        }
        return _instance;
    }

    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `spawnlist_scenery`");
            rs = ps.executeQuery();

            L1Scenery scenery;
            while (rs.next()) {
                final int id = rs.getInt("id");
                final int gfxid = rs.getInt("gfxid");
                final int locx = rs.getInt("locx");
                final int locy = rs.getInt("locy");
                final int heading = rs.getInt("heading");
                final int mapid = rs.getInt("mapid");
                final String html = rs.getString("html");

                scenery = new L1Scenery();
                scenery.set_gfxid(gfxid);
                scenery.set_locx(locx);
                scenery.set_locy(locy);
                scenery.set_heading(heading);
                scenery.set_mapid(mapid);
                scenery.set_html(html);

                _sceneryList.put(new Integer(id), scenery);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("载入景观设置资料数量: " + _sceneryList.size() + "(" + timer.get()
                + "ms)");
        this.satrt();
    }

    private void satrt() {
        for (final L1Scenery scenery : _sceneryList.values()) {
            final L1FieldObjectInstance field = (L1FieldObjectInstance) NpcTable
                    .get().newNpcInstance(71081);

            if (field != null) {
                field.setId(IdFactoryNpc.get().nextId());
                field.setGfxId(scenery.get_gfxid());
                field.setTempCharGfx(scenery.get_gfxid());
                field.setMap((short) scenery.get_mapid());
                field.setX(scenery.get_locx());
                field.setY(scenery.get_locy());
                field.setHomeX(scenery.get_locx());
                field.setHomeY(scenery.get_locy());
                field.setHeading(scenery.get_heading());

                World.get().storeObject(field);
                World.get().addVisibleObject(field);

                _fieldList.put(new Integer(field.getId()), scenery);
            }
        }
    }

    public String get_sceneryHtml(final int objid) {
        final L1Scenery scenery = _fieldList.get(new Integer(objid));
        if (scenery != null) {
            if (!scenery.get_html().equals("0")) {
                return scenery.get_html();
            }
        }
        return null;
    }

    public void storeScenery(final String note, final int gfxid,
            final int locx, final int locy, final int heading, final int mapid,
            final String html) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO `spawnlist_scenery` SET `note`=?,`gfxid`=?,"
                            + "`locx`=?,`locy`=?,`heading`=?,`mapid`=?,`html`=?");
            int i = 0;
            pstm.setString(++i, note);
            pstm.setInt(++i, gfxid);
            pstm.setInt(++i, locx);
            pstm.setInt(++i, locy);
            pstm.setInt(++i, heading);
            pstm.setInt(++i, mapid);
            pstm.setString(++i, html);

            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}

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
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.storage.CharacterStorage;
import com.lineage.server.storage.mysql.MySqlCharacterStorage;
import com.lineage.server.templates.L1CharName;
import com.lineage.server.utils.SQLUtil;

/**
 * 人物数据
 * 
 * @author daien
 * 
 */
public class CharacterTable {

    private static final Log _log = LogFactory.getLog(CharacterTable.class);

    private CharacterStorage _charStorage;

    private static CharacterTable _instance;

    private static final Map<String, L1CharName> _charNameList = new HashMap<String, L1CharName>();

    private CharacterTable() {
        _charStorage = new MySqlCharacterStorage();
    }

    public static CharacterTable get() {
        if (_instance == null) {
            _instance = new CharacterTable();
        }
        return _instance;
    }

    public void storeNewCharacter(final L1PcInstance pc) throws Exception {
        synchronized (pc) {
            _charStorage.createCharacter(pc);
            final String name = pc.getName();
            if (!_charNameList.containsKey(name)) {
                final L1CharName cn = new L1CharName();
                cn.setName(name);
                cn.setId(pc.getId());
                _charNameList.put(name, cn);
            }
        }
    }

    public void storeCharacter(final L1PcInstance pc) throws Exception {
        synchronized (pc) {
            _charStorage.storeCharacter(pc);
        }
    }

    public void deleteCharacter(final String accountName, final String charName)
            throws Exception {
        // 多分、同期は必要ない
        _charStorage.deleteCharacter(accountName, charName);
        if (_charNameList.containsKey(charName)) {
            _charNameList.remove(charName);
        }
        // _log.error("deleteCharacter");
    }

    /**
     * 传回人物数据
     * 
     * @param charName
     *            人物名称
     * @return
     * @throws Exception
     */
    public L1PcInstance restoreCharacter(final String charName)
            throws Exception {
        final L1PcInstance pc = _charStorage.loadCharacter(charName);
        return pc;
    }

    /**
     * 传回人物数据
     * 
     * @param charName
     *            人物名称
     * @return
     * @throws Exception
     */
    public L1PcInstance loadCharacter(final String charName) throws Exception {
        L1PcInstance pc = null;
        try {
            pc = restoreCharacter(charName);

            // マップの范围外ならSKTに移动させる
            final L1Map map = L1WorldMap.get().getMap(pc.getMapId());

            if (!map.isInMap(pc.getX(), pc.getY())) {
                pc.setX(33087);
                pc.setY(33396);
                pc.setMap((short) 4);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        }
        return pc;

    }

    /**
     * 变更全部人物状态为离线
     */
    public static void clearOnlineStatus() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `characters` SET `OnlineStatus`='0'");
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 变更指定人物连线状态
     * 
     * @param pc
     */
    public static void updateOnlineStatus(final L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `characters` SET `OnlineStatus`=? WHERE `objid`=?");
            pstm.setInt(1, pc.getOnlineStatus());
            pstm.setInt(2, pc.getId());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 清除指定人物婚姻对象
     * 
     * @param targetId
     */
    public static void updatePartnerId(final int targetId) {
        updatePartnerId(targetId, 0);
    }

    /**
     * 设定人物婚姻对象
     * 
     * @param targetId
     * @param partnerId
     */
    public static void updatePartnerId(final int targetId, final int partnerId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `characters` SET `PartnerID`=? WHERE `objid`=?");
            pstm.setInt(1, partnerId);
            pstm.setInt(2, targetId);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 建立人物初始化素质状态
     * 
     * @param pc
     */
    public static void saveCharStatus(final L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `characters` SET `OriginalStr`=?,"
                            + "`OriginalCon`=?,`OriginalDex`=?,`OriginalCha`=?,"
                            + "`OriginalInt`=?,`OriginalWis`=?"
                            + " WHERE `objid`=?");

            pstm.setInt(1, pc.getBaseStr());
            pstm.setInt(2, pc.getBaseCon());
            pstm.setInt(3, pc.getBaseDex());
            pstm.setInt(4, pc.getBaseCha());
            pstm.setInt(5, pc.getBaseInt());
            pstm.setInt(6, pc.getBaseWis());

            pstm.setInt(7, pc.getId());
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
    
    /**
     * 蜡烛专用更改初始人物点数
     * 
     * @param pc
     */
    public static void saveCharStatus(final L1PcInstance pc, final int str,
    		final int con, final int dex, final int cha, final int intel, final int wis) {
        Connection con1 = null;
        PreparedStatement pstm = null;
        try {
            con1 = DatabaseFactory.get().getConnection();
            pstm = con1
                    .prepareStatement("UPDATE `characters` SET `OriginalStr`=?,"
                            + "`OriginalCon`=?,`OriginalDex`=?,`OriginalCha`=?,"
                            + "`OriginalInt`=?,`OriginalWis`=?"
                            + " WHERE `objid`=?");

            pstm.setInt(1, str);
            pstm.setInt(2, con);
            pstm.setInt(3, dex);
            pstm.setInt(4, cha);
            pstm.setInt(5, intel);
            pstm.setInt(6, wis);

            pstm.setInt(7, pc.getId());
            pstm.execute();
            // 变更原始素质设定
//            pc.setOriginalStr(str);
//            pc.setOriginalDex(dex);
//            pc.setOriginalCon(con);
//            pc.setOriginalWis(wis);
//            pc.setOriginalInt(intel);
//            pc.setOriginalCha(cha);

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con1);
        }
    }
    
    /**
     * 蜡烛初始化点数更改
     * -不保存到DB-
     */
    public static void saveCharStatus_temp(final L1PcInstance pc, final int str,
    		final int con, final int dex, final int cha, final int intel, final int wis) {
        pc.setOriginalStr(str);
        pc.setOriginalDex(dex);
        pc.setOriginalCon(con);
        pc.setOriginalWis(wis);
        pc.setOriginalInt(intel);
        pc.setOriginalCha(cha);
    }

    /**
     * 取得人物物品资料
     * 
     * @param pc
     */
    public static void restoreInventory(final L1PcInstance pc) {
        // 背包
        pc.getInventory().loadItems();
        // 矮人仓库
        pc.getDwarfInventory().loadItems();
        // 精灵仓库
        pc.getDwarfForElfInventory().loadItems();
    }

    /**
     * 检查数据库中是否有指定人物
     * 
     * @param name
     *            人物名称
     * @return
     */
    public static boolean doesCharNameExist(final String name) {
        boolean result = true;
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("SELECT `account_name` FROM `characters` WHERE `char_name`=?");
            pstm.setString(1, name);
            rs = pstm.executeQuery();
            result = rs.next();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    /**
     * 变更人物名称
     * 
     * @param objid
     * @param name
     */
    public void newCharName(int objid, String name) {
        L1CharName cn = new L1CharName();
        cn.setName(name);
        cn.setId(objid);
        _charNameList.put(name, cn);

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE `characters` SET `char_name`=? WHERE `objid`=?");
            pstm.setString(1, name);
            pstm.setInt(2, objid);
            pstm.execute();

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 暂存已用人物名称
     */
    public static void loadAllCharName() {
        L1CharName cn = null;
        String name = null;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `characters`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                cn = new L1CharName();
                name = rs.getString("char_name");
                cn.setName(name);
                cn.setId(rs.getInt("objid"));
                _charNameList.put(name, cn);
            }

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 占用阵列纪录
     * 
     * @return
     */
    public L1CharName[] getCharNameList() {
        return _charNameList.values().toArray(
                new L1CharName[_charNameList.size()]);
    }

    /**
     * 取回该OBJID人物名称
     * 
     * @param objid
     * @return
     */
    public String getCharName(int objid) {
        for (L1CharName charName : _charNameList.values()) {
            if (charName.getId() == objid) {
                return charName.getName();
            }
        }
        return null;
    }
}

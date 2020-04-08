package com.lineage.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.storage.CharacterStorage;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.utils.SQLUtil;

/**
 * PC资料
 * 
 * @author daien
 * 
 */
public class MySqlCharacterStorage implements CharacterStorage {

    private static final Log _log = LogFactory
            .getLog(MySqlCharacterStorage.class);

    /**
     * 载入PC资料
     */
    @Override
    public L1PcInstance loadCharacter(final String charName) {
        L1PcInstance pc = null;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {

            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("SELECT * FROM characters WHERE char_name=?");
            pstm.setString(1, charName);

            rs = pstm.executeQuery();
            if (!rs.next()) {
                /*
                 * SELECTが结果を返さなかった。
                 */
                return null;
            }
            pc = new L1PcInstance();
            String loginName = rs.getString("account_name").toLowerCase();
            pc.setAccountName(loginName);

            final int objid = rs.getInt("objid");
            pc.setId(objid);
            // 副本编号预先以-1为准
            pc.set_showId(-1);
            // TODO 额外纪录项次
            L1PcOther other = CharOtherReading.get().getOther(pc);
            if (other == null) {
                other = new L1PcOther();
                other.set_objid(objid);
            }
            pc.set_other(other);

            pc.setName(rs.getString("char_name"));
            pc.setHighLevel(rs.getInt("HighLevel"));
            pc.setExp(rs.getLong("Exp"));
            pc.addBaseMaxHp(rs.getShort("MaxHp"));
            short currentHp = rs.getShort("CurHp");
            if (currentHp < 1) {
                currentHp = 1;
            }
            pc.setDead(false);
            pc.setCurrentHpDirect(currentHp);
            pc.setStatus(0);
            pc.addBaseMaxMp(rs.getShort("MaxMp"));
            pc.setCurrentMpDirect(rs.getShort("CurMp"));
            pc.addBaseStr(rs.getInt("Str"));
            pc.addBaseCon(rs.getInt("Con"));
            pc.addBaseDex(rs.getInt("Dex"));
            pc.addBaseCha(rs.getInt("Cha"));
            pc.addBaseInt(rs.getInt("Intel"));
            pc.addBaseWis(rs.getInt("Wis"));
            final int status = rs.getInt("Status");
            pc.setCurrentWeapon(status);
            final int classId = rs.getInt("Class");
            pc.setClassId(classId);
            pc.setTempCharGfx(classId);
            pc.setGfxId(classId);
            pc.set_sex(rs.getInt("Sex"));
            pc.setType(rs.getInt("Type"));
            int head = rs.getInt("Heading");
            if (head > 7) {
                head = 0;
            }
            pc.setHeading(head);

            pc.setX(rs.getInt("locX"));
            pc.setY(rs.getInt("locY"));
            pc.setMap(rs.getShort("MapID"));
            pc.set_food(rs.getInt("Food"));
            pc.setLawful(rs.getInt("Lawful"));
            pc.setTitle(rs.getString("Title"));
            pc.setClanid(rs.getInt("ClanID"));
            pc.setClanname(rs.getString("Clanname"));
            pc.setClanRank(rs.getInt("ClanRank"));
            pc.setBonusStats(rs.getInt("BonusStatus"));
            pc.setElixirStats(rs.getInt("ElixirStatus"));
            pc.setElfAttr(rs.getInt("ElfAttr"));
            pc.set_PKcount(rs.getInt("PKcount"));
            pc.setPkCountForElf(rs.getInt("PkCountForElf"));
            pc.setExpRes(rs.getInt("ExpRes"));
            pc.setPartnerId(rs.getInt("PartnerID"));
            pc.setAccessLevel(rs.getShort("AccessLevel"));

            if (pc.getAccessLevel() >= 200) {
                pc.setGm(true);
                pc.setMonitor(false);

            } else if (pc.getAccessLevel() == 100) {
                pc.setGm(false);
                pc.setMonitor(true);

            } else {
                pc.setGm(false);
                pc.setMonitor(false);
            }

            pc.setOnlineStatus(rs.getInt("OnlineStatus"));
            pc.setHomeTownId(rs.getInt("HomeTownID"));
            pc.setContribution(rs.getInt("Contribution"));
            pc.setHellTime(rs.getInt("HellTime"));
            pc.setBanned(rs.getBoolean("Banned"));
            pc.setKarma(rs.getInt("Karma"));
            pc.setLastPk(rs.getTimestamp("LastPk"));
            pc.setLastPkForElf(rs.getTimestamp("LastPkForElf"));
            pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
            pc.setOriginalStr(rs.getInt("OriginalStr"));
            pc.setOriginalCon(rs.getInt("OriginalCon"));
            pc.setOriginalDex(rs.getInt("OriginalDex"));
            pc.setOriginalCha(rs.getInt("OriginalCha"));
            pc.setOriginalInt(rs.getInt("OriginalInt"));
            pc.setOriginalWis(rs.getInt("OriginalWis"));
            pc.setRocksPrisonTime(rs.getInt("RocksPrisonTime"));
            pc.setforcetitle(rs.getString("forcetitle"));
            //pc.setRecommend_account(rs.getString("Recommend_account"));//推荐人系统hjx1000

            pc.refresh();
            pc.setMoveSpeed(0);
            pc.setBraveSpeed(0);
            pc.setGmInvis(false);

            // _log.finest("restored char data: ");

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return pc;
    }

    @Override
    public void createCharacter(final L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            int i = 0;
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("INSERT INTO characters SET account_name=?,objid=?,"
                            + "char_name=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,"
                            + "MaxMp=?,CurMp=?,Ac=?,Str=?,Con=?,Dex=?,Cha=?,Intel=?,"
                            + "Wis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,"
                            + "LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,"
                            + "ClanRank=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,PKcount=?,"
                            + "PkCountForElf=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,"
                            + "HomeTownID=?,Contribution=?,Pay=?,HellTime=?,Banned=?,Karma=?,"
                            + "LastPk=?,LastPkForElf=?,DeleteTime=?,CreateTime=?,RocksPrisonTime=?");
            pstm.setString(++i, pc.getAccountName());
            pstm.setInt(++i, pc.getId());
            pstm.setString(++i, pc.getName());
            pstm.setInt(++i, pc.getLevel());
            pstm.setInt(++i, pc.getHighLevel());
            pstm.setLong(++i, pc.getExp());
            pstm.setInt(++i, pc.getBaseMaxHp());
            int hp = pc.getCurrentHp();
            if (hp < 1) {
                hp = 1;
            }
            pstm.setInt(++i, hp);
            pstm.setInt(++i, pc.getBaseMaxMp());
            pstm.setInt(++i, pc.getCurrentMp());
            pstm.setInt(++i, pc.getAc());
            pstm.setInt(++i, pc.getBaseStr());
            pstm.setInt(++i, pc.getBaseCon());
            pstm.setInt(++i, pc.getBaseDex());
            pstm.setInt(++i, pc.getBaseCha());
            pstm.setInt(++i, pc.getBaseInt());
            pstm.setInt(++i, pc.getBaseWis());
            pstm.setInt(++i, pc.getCurrentWeapon());
            pstm.setInt(++i, pc.getClassId());
            pstm.setInt(++i, pc.get_sex());
            pstm.setInt(++i, pc.getType());
            pstm.setInt(++i, pc.getHeading());
            pstm.setInt(++i, pc.getX());
            pstm.setInt(++i, pc.getY());
            pstm.setInt(++i, pc.getMapId());
            pstm.setInt(++i, pc.get_food());
            pstm.setInt(++i, pc.getLawful());
            pstm.setString(++i, pc.getTitle());
            pstm.setInt(++i, pc.getClanid());
            pstm.setString(++i, pc.getClanname());
            pstm.setInt(++i, pc.getClanRank());
            pstm.setInt(++i, pc.getBonusStats());
            pstm.setInt(++i, pc.getElixirStats());
            pstm.setInt(++i, pc.getElfAttr());
            pstm.setInt(++i, pc.get_PKcount());
            pstm.setInt(++i, pc.getPkCountForElf());
            pstm.setInt(++i, pc.getExpRes());
            pstm.setInt(++i, pc.getPartnerId());
            pstm.setShort(++i, pc.getAccessLevel());
            pstm.setInt(++i, pc.getOnlineStatus());
            pstm.setInt(++i, pc.getHomeTownId());
            pstm.setInt(++i, pc.getContribution());
            pstm.setInt(++i, 0);
            pstm.setInt(++i, pc.getHellTime());
            pstm.setBoolean(++i, pc.isBanned());
            pstm.setInt(++i, pc.getKarma());
            pstm.setTimestamp(++i, pc.getLastPk());
            pstm.setTimestamp(++i, pc.getLastPkForElf());
            pstm.setTimestamp(++i, pc.getDeleteTime());

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final String times = sdf.format(System.currentTimeMillis());
            int time = Integer.parseInt(times.replace("-", ""));
            pstm.setInt(++i, time);
            pstm.setInt(++i, pc.getRocksPrisonTime());
            //pstm.setString(++i, pc.getRecommend_account());//推荐人系统hjx1000

            pstm.execute();

            // _log.finest("stored char data: " + pc.getName());
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override
    public void deleteCharacter(final String accountName, final String charName)
            throws Exception {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("SELECT * FROM characters WHERE account_name=? AND char_name=?");
            pstm.setString(1, accountName);
            pstm.setString(2, charName);
            rs = pstm.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("could not delete character");
            }

            int objid = CharObjidTable.get().charObjid(charName);

            if (objid != 0) {
                // 删除人物背包资料
                CharItemsReading.get().delUserItems(objid);
            }

            pstm = con
                    .prepareStatement("DELETE FROM character_buddys WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM character_buff WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM character_config WHERE object_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM character_quests WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM character_skills WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM character_teleport WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm.setString(1, charName);
            pstm.execute();

            pstm = con
                    .prepareStatement("DELETE FROM characters WHERE char_name=?");
            pstm.setString(1, charName);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);

        }
    }

    @Override
    public void storeCharacter(final L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            int i = 0;
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("UPDATE characters SET level=?,HighLevel=?,Exp=?,"
                            + "MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,"
                            + "Con=?,Dex=?,Cha=?,Intel=?,Wis=?,Status=?,"
                            + "Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,"
                            + "MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,"
                            + "Clanname=?,ClanRank=?,BonusStatus=?,"
                            + "ElixirStatus=?,ElfAttr=?,PKcount=?,PkCountForElf=?,"
                            + "ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,"
                            + "HomeTownID=?,Contribution=?,HellTime=?,Banned=?,"
                            + "Karma=?,LastPk=?,LastPkForElf=?,"
                            + "DeleteTime=?,RocksPrisonTime=? WHERE objid=?");
            pstm.setInt(++i, pc.getLevel());
            pstm.setInt(++i, pc.getHighLevel());
            pstm.setLong(++i, pc.getExp());
            pstm.setInt(++i, pc.getBaseMaxHp());
            int hp = pc.getCurrentHp();
            if (hp < 1) {
                hp = 1;
            }
            pstm.setInt(++i, hp);
            pstm.setInt(++i, pc.getBaseMaxMp());
            pstm.setInt(++i, pc.getCurrentMp());
            pstm.setInt(++i, pc.getAc());
            pstm.setInt(++i, pc.getBaseStr());
            pstm.setInt(++i, pc.getBaseCon());
            pstm.setInt(++i, pc.getBaseDex());
            pstm.setInt(++i, pc.getBaseCha());
            pstm.setInt(++i, pc.getBaseInt());
            pstm.setInt(++i, pc.getBaseWis());
            pstm.setInt(++i, pc.getCurrentWeapon());
            pstm.setInt(++i, pc.getClassId());
            pstm.setInt(++i, pc.get_sex());
            pstm.setInt(++i, pc.getType());
            pstm.setInt(++i, pc.getHeading());
            pstm.setInt(++i, pc.getX());
            pstm.setInt(++i, pc.getY());
            pstm.setInt(++i, pc.getMapId());
            pstm.setInt(++i, pc.get_food());
            pstm.setInt(++i, pc.getLawful());
            pstm.setString(++i, pc.getTitle());
            pstm.setInt(++i, pc.getClanid());
            pstm.setString(++i, pc.getClanname());
            pstm.setInt(++i, pc.getClanRank());
            pstm.setInt(++i, pc.getBonusStats());
            pstm.setInt(++i, pc.getElixirStats());
            pstm.setInt(++i, pc.getElfAttr());
            pstm.setInt(++i, pc.get_PKcount());
            pstm.setInt(++i, pc.getPkCountForElf());
            pstm.setInt(++i, pc.getExpRes());
            pstm.setInt(++i, pc.getPartnerId());
            short leve = pc.getAccessLevel();
            if (leve >= 20000) {
                leve = 0;
            }
            pstm.setShort(++i, leve);
            pstm.setInt(++i, pc.getOnlineStatus());
            pstm.setInt(++i, pc.getHomeTownId());
            pstm.setInt(++i, pc.getContribution());
            pstm.setInt(++i, pc.getHellTime());
            pstm.setBoolean(++i, pc.isBanned());
            pstm.setInt(++i, pc.getKarma());
            pstm.setTimestamp(++i, pc.getLastPk());
            pstm.setTimestamp(++i, pc.getLastPkForElf());
            pstm.setTimestamp(++i, pc.getDeleteTime());
            pstm.setInt(++i, pc.getRocksPrisonTime());
            pstm.setInt(++i, pc.getId());
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}

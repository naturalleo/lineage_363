package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.MailStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 信件资料
 * 
 * @author dexc
 * 
 */
public class MailTable implements MailStorage {

    private static final Log _log = LogFactory.getLog(MailTable.class);

    // private static final ArrayList<L1Mail> _allMail = new
    // ArrayList<L1Mail>();
    private static final Map<Integer, L1Mail> _allMail = new HashMap<Integer, L1Mail>();

    @Override
    public void load() {
        final PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_mail`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                final L1Mail mail = new L1Mail();
                final int id = rs.getInt("id");
                mail.setId(id);
                mail.setType(rs.getInt("type"));
                String sender = rs.getString("sender");
                mail.setSenderName(sender);

                String receiver = rs.getString("receiver");
                mail.setReceiverName(receiver);
                mail.setDate(rs.getString("date"));
                mail.setReadStatus(rs.getInt("read_status"));
                mail.setSubject(rs.getBytes("subject"));
                mail.setContent(rs.getBytes("content"));
                // 检查名称是否以被使用
                if (CharObjidTable.get().charObjid(receiver) != 0) {
                    _allMail.put(id, mail);

                } else {
                    deleteMail(receiver);
                }
            }
        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("载入信件资料数量: " + _allMail.size() + "(" + timer.get() + "ms)");
    }

    /**
     * 收件人遗失 删除信件
     * 
     * @param receiver
     */
    private void deleteMail(String receiver) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con
                    .prepareStatement("DELETE FROM `character_mail` WHERE `receiver`=?");
            pstm.setString(1, receiver);
            pstm.execute();

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override
    public void setReadStatus(final int mailId) {
        final L1Mail mail = _allMail.get(mailId);
        if (mail != null) {
            mail.setReadStatus(1);

            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                rs = con.createStatement().executeQuery(
                        "SELECT * FROM `character_mail` WHERE `id`=" + mailId);
                if ((rs != null) && rs.next()) {
                    pstm = con
                            .prepareStatement("UPDATE `character_mail` SET `read_status`=? WHERE `id`="
                                    + mailId);
                    pstm.setInt(1, 1);
                    pstm.execute();

                }
            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override
    public void setMailType(final int mailId, final int type) {
        final L1Mail mail = _allMail.get(mailId);
        if (mail != null) {
            mail.setType(type);

            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                rs = con.createStatement().executeQuery(
                        "SELECT * FROM `character_mail` WHERE `id`=" + mailId);
                if ((rs != null) && rs.next()) {
                    pstm = con
                            .prepareStatement("UPDATE `character_mail` SET `type`=? WHERE `id`="
                                    + mailId);
                    pstm.setInt(1, type);
                    pstm.execute();
                }

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override
    public void deleteMail(final int mailId) {
        final L1Mail mail = _allMail.remove(mailId);
        if (mail != null) {
            Connection con = null;
            PreparedStatement pstm = null;
            try {
                con = DatabaseFactory.get().getConnection();
                pstm = con
                        .prepareStatement("DELETE FROM `character_mail` WHERE `id`=?");
                pstm.setInt(1, mailId);
                pstm.execute();

            } catch (final SQLException e) {
                _log.error(e.getLocalizedMessage(), e);

            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override
    public void writeMail(final int type, final String receiver,
            final L1PcInstance writer, final byte[] text) {
        final int readStatus = 0;

        final SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
        final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
        final String date = sdf.format(Calendar.getInstance(tz).getTime());

        // subjectとcontentの区切り(0x00 0x00)位置を见つける
        int spacePosition1 = 0;
        int spacePosition2 = 0;
        for (int i = 0; i < text.length; i += 2) {
            if ((text[i] == 0) && (text[i + 1] == 0)) {
                if (spacePosition1 == 0) {
                    spacePosition1 = i;
                } else if ((spacePosition1 != 0) && (spacePosition2 == 0)) {
                    spacePosition2 = i;
                    break;
                }
            }
        }

        // mailテーブルに书き迂む
        final int subjectLength = spacePosition1 + 2;
        int contentLength = spacePosition2 - spacePosition1;
        if (contentLength <= 0) {
            contentLength = 1;
        }
        final byte[] subject = new byte[subjectLength];
        final byte[] content = new byte[contentLength];
        System.arraycopy(text, 0, subject, 0, subjectLength);
        System.arraycopy(text, subjectLength, content, 0, contentLength);

        Connection con = null;
        PreparedStatement pstm2 = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm2 = con.prepareStatement("INSERT INTO `character_mail` SET "
                    + "`id`=?,`type`=?,`sender`=?,`receiver`=?,"
                    + "`date`=?,`read_status`=?,`subject`=?,`content`=?");
            final int id = IdFactory.get().nextId();
            pstm2.setInt(1, id);
            pstm2.setInt(2, type);
            pstm2.setString(3, writer.getName());
            pstm2.setString(4, receiver);
            pstm2.setString(5, date);
            pstm2.setInt(6, readStatus);
            pstm2.setBytes(7, subject);
            pstm2.setBytes(8, content);
            pstm2.execute();

            final L1Mail mail = new L1Mail();
            mail.setId(id);
            mail.setType(type);
            mail.setSenderName(writer.getName());
            mail.setReceiverName(receiver);
            mail.setDate(date);
            mail.setSubject(subject);
            mail.setContent(content);
            mail.setReadStatus(readStatus);

            _allMail.put(id, mail);

        } catch (final SQLException e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }
    }

    @Override
    public Map<Integer, L1Mail> getAllMail() {
        return _allMail;
    }

    @Override
    public L1Mail getMail(final int mailId) {
        return _allMail.get(mailId);
    }
}

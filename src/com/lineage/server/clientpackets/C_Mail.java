package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.MailReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Mail;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Mail;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求使用信件系统
 * 
 * @author daien
 * 
 */
public class C_Mail extends ClientBasePacket {

    private static final Log _log = LogFactory.getLog(C_Mail.class);

    /*
     * public C_Mail() { }
     * 
     * public C_Mail(final byte[] abyte0, final ClientExecutor client) {
     * super(abyte0); try { this.start(abyte0, client);
     * 
     * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
     */

    /** 一般 */
    private static final int TYPE_NORMAL_MAIL = 0x00;

    /** 血盟 */
    private static final int TYPE_CLAN_MAIL = 0x01;

    /** 保管箱 */
    private static final int TYPE_MAIL_BOX = 0x02;

    @Override
    public void start(final byte[] decrypt, final ClientExecutor client) {
        try {
            // 资料载入
            this.read(decrypt);

            final int type = this.readC();
            final L1PcInstance pc = client.getActiveChar();

            switch (type) {
                case 0x00:
                case 0x01:
                case 0x02: // 开く
                    if (pc != null) {
                        clientPackA(pc, type);
                    }
                    break;

                case 0x10:
                case 0x11:
                case 0x12: // 读取
                    if (pc != null) {
                        final int id = this.readD();
                        clientPackB(pc, type, id);
                    }
                    break;

                case 0x20: // 一般信件寄出
                    if (pc != null) {
                        final int unknow01 = this.readH();
                        final String receiverName = this.readS();
                        final byte[] textr = this.readByte();

                        clientPackD(pc, type, receiverName, textr);
                    }
                    break;

                case 0x21: // 血盟信件寄出
                    if (pc != null) {
                        final int unknow02 = this.readH();
                        final String clanName = this.readS();
                        final byte[] text = this.readByte();

                        clientPackE(pc, type, clanName, text);
                    }
                    break;

                case 0x30:
                case 0x31:
                case 0x32: // 削除
                    if (pc != null) {
                        final int delid = this.readD();

                        clientPackF(pc, type, delid);
                    }
                    break;

                case 0x40: // 保管箱に保存
                    if (pc != null) {
                        final int saveid = this.readD();

                        clientPackG(pc, type, saveid);
                    }
                    break;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private void clientPackG(L1PcInstance pc, int type, int saveid) {
        try {
            final L1Mail mail = MailReading.get().getMail(saveid);

            if (mail != null) {
                MailReading.get().setMailType(saveid, TYPE_MAIL_BOX);
                pc.sendPackets(new S_Mail(mail, type));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackF(L1PcInstance pc, int type, int delid) {
        try {
            final L1Mail mail = MailReading.get().getMail(delid);

            if (mail != null) {
                MailReading.get().deleteMail(delid);
                pc.sendPackets(new S_Mail(mail, type));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackE(L1PcInstance pc, int type, String clanName,
            byte[] text) {
        try {
            final L1Clan clan = WorldClan.get().getClan(clanName);
            if (pc.getInventory().consumeItem(40308, 1000)) {
                if (clan != null) {
                    for (final String name : clan.getAllMembers()) {
                        final int size = MailReading.get()
                                .getMailSizeByReceiver(name, TYPE_CLAN_MAIL);
                        if (size >= 50) {
                            continue;
                        }

                        MailReading.get().writeMail(TYPE_CLAN_MAIL, name, pc,
                                text);
                        final L1PcInstance clanPc = World.get().getPlayer(name);
                        if (clanPc != null) { // オンライン中

                            final ArrayList<L1Mail> mails = MailReading.get()
                                    .getMails(name);
                            if (!mails.isEmpty()) {
                                clanPc.sendPackets(new S_Mail(mails,
                                        TYPE_NORMAL_MAIL));
                            }
                            // clanPc.sendPackets(new S_Mail(name,
                            // TYPE_CLAN_MAIL));
                        }
                    }
                }

            } else {
                // 189：\f1金币不足。
                pc.sendPackets(new S_ServerMessage(189));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void clientPackD(L1PcInstance pc, int type, String receiverName,
            byte[] textr) {
        try {
            final L1PcInstance receiver = World.get().getPlayer(receiverName);

            if (pc.getInventory().consumeItem(40308, 50)) {
                if (receiver != null) { // 连线
                    if (MailReading.get().getMailSizeByReceiver(receiverName,
                            TYPE_NORMAL_MAIL) >= 20) {
                        pc.sendPackets(new S_Mail(type));
                        return;
                    }
                    MailReading.get().writeMail(TYPE_NORMAL_MAIL, receiverName,
                            pc, textr);
                    if (receiver.getOnlineStatus() == 1) {

                        final ArrayList<L1Mail> mails = MailReading.get()
                                .getMails(receiverName);
                        if (!mails.isEmpty()) {
                            receiver.sendPackets(new S_Mail(mails,
                                    TYPE_NORMAL_MAIL));
                        }
                    }

                } else { // 离线
                    try {
                        final L1PcInstance restorePc = CharacterTable.get()
                                .restoreCharacter(receiverName);
                        if (restorePc != null) {
                            if (MailReading.get().getMailSizeByReceiver(
                                    receiverName, TYPE_NORMAL_MAIL) >= 20) {
                                pc.sendPackets(new S_Mail(type));
                                return;
                            }
                            MailReading.get().writeMail(TYPE_NORMAL_MAIL,
                                    receiverName, pc, textr);

                        } else {
                            // 109:没有叫%0的人。
                            pc.sendPackets(new S_ServerMessage(109,
                                    receiverName));
                        }
                    } catch (final Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                }
            } else {
                // 189：\f1金币不足。
                pc.sendPackets(new S_ServerMessage(189));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 
     * @param pc
     * @param type
     * @param id
     */
    private void clientPackB(L1PcInstance pc, int type, int id) {
        try {
            final L1Mail mail = MailReading.get().getMail(id);

            if (mail != null) {
                if (mail.getReadStatus() == 0) {
                    MailReading.get().setReadStatus(id);
                }
                pc.sendPackets(new S_Mail(mail, type));
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 
     * @param pc
     * @param type
     */
    private void clientPackA(L1PcInstance pc, int type) {
        try {
            final ArrayList<L1Mail> mails = MailReading.get().getMails(
                    pc.getName());

            if (mails != null) {
                if (!mails.isEmpty()) {
                    pc.sendPackets(new S_Mail(mails, type));
                }
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}

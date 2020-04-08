package com.lineage.server.serverpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.templates.L1Mail;

/**
 * 邮件系统
 * 
 * @author dexc
 * 
 */
public class S_Mail extends ServerBasePacket {

    private static final Log _log = LogFactory.getLog(S_Mail.class);

    private byte[] _byte = null;

    /**
     * 取回一般信件的标题
     * 
     * @param mails
     * @param type
     */
    public S_Mail(final ArrayList<L1Mail> mails, final int type) {
        try {
            this.writeC(S_OPCODE_MAIL);
            this.writeC(type);
            this.writeH(mails.size());
            for (int i = 0; i < mails.size(); i++) {
                final L1Mail mail = mails.get(i);
                this.writeD(mail.getId());
                this.writeC(mail.getReadStatus());

                String[] st = mail.getDate().split("/");
                this.writeC(Integer.parseInt(st[0]));
                this.writeC(Integer.parseInt(st[1]));
                this.writeC(Integer.parseInt(st[2]));

                this.writeS(mail.getSenderName());
                this.writeByte(mail.getSubject());
                this.writeC(0x00);
            }

        } catch (final Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 无法传送信件
     * 
     * @param type
     */
    public S_Mail(final int type) { // 受信者にメール通知
        this.writeC(S_OPCODE_MAIL);
        this.writeC(type);
    }

    /**
     * 读取一般信件 信件存到保管箱
     * 
     * @param mail
     * @param type
     */
    public S_Mail(final L1Mail mail, final int type) {
        switch (type) {
            case 0x30:// 删除一般
            case 0x31:// 删除血盟
            case 0x32:// 一般存到保管箱
            case 0x40:// 删除保管箱
                buildPacket_1(mail, type);
                break;

            default:
                buildPacket_2(mail, type);
                break;
        }
    }

    private void buildPacket_1(final L1Mail mail, final int type) {
        this.writeC(S_OPCODE_MAIL);
        this.writeC(type);
        this.writeD(mail.getId());
        this.writeC(0x01);
    }

    private void buildPacket_2(final L1Mail mail, final int type) {
        this.writeC(S_OPCODE_MAIL);
        this.writeC(type);
        this.writeD(mail.getId());
        this.writeByte(mail.getContent());
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this.getBytes();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}

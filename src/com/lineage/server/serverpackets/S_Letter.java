package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 阅读邮件(旧) - 已取消使用
 * 
 * @author DaiEn
 * 
 */
public class S_Letter extends ServerBasePacket {

    private byte[] _byte = null;

    public S_Letter(final L1ItemInstance item) {
        writeC(S_OPCODE_LETTER);
    }

    public S_Letter() {
        writeC(S_OPCODE_LETTER);
        writeD(0x00);
        writeH(615);
        writeH(0x00);
        writeS("123");
        writeS("456");
        writeByte(null);
        writeByte(null);
        writeC(0x00);
        writeS("info");
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

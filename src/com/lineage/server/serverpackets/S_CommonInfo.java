package com.lineage.server.serverpackets;

/**
 * 服务器登入讯息(使用string.tbl)
 * 
 * @author dexc
 * 
 */
public class S_CommonInfo extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 服务器登入讯息(使用string.tbl)
     * 
     * @param skillid
     */
    public S_CommonInfo(final int type, final String[] info) {
        this.buildPacket(type, info);
    }

    private void buildPacket(final int type, final String[] info) {
        this.writeC(S_OPCODE_COMMONINFO);
        this.writeH(type);

        if (info == null) {
            this.writeC(0x00);

        } else {
            this.writeC(info.length);
            for (int i = 0; i < info.length; i++) {
                this.writeS(info[i]);
            }
        }
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

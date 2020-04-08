package com.lineage.server.serverpackets;

/**
 * 画面中红色讯息(登入来源)
 * 
 * @author dexc
 * 
 */
public class S_RedMessage extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 画面中红色讯息(登入来源)
     * 
     * @param acc
     *            帐号名称
     * @param msg1
     *            IP OR MAC讯息(不支援$)
     */
    public S_RedMessage(final String acc, final String msg1) {
        this.buildPacket(acc, new String[] { msg1 });
    }

    private void buildPacket(final String acc, final String[] info) {
        this.writeC(S_OPCODE_RED);
        this.writeS(acc);
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

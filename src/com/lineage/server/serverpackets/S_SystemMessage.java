package com.lineage.server.serverpackets;

public class S_SystemMessage extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * クライアントにデータの存在しないオリジナルのメッセージを表示する。
     * メッセージにnameid($xxx)が含まれている场合はオーバーロードされたもう一方を使用する。
     * 
     * @param msg
     *            - 表示する文字列
     */
    public S_SystemMessage(final String msg) {
        this.writeC(S_OPCODE_GLOBALCHAT);
        this.writeC(0x09);
        this.writeS(msg);
    }

    /**
     * クライアントにデータの存在しないオリジナルのメッセージを表示する。
     * 
     * @param msg
     *            - 表示する文字列
     * @param nameid
     *            - 文字列にnameid($xxx)が含まれている场合trueにする。
     */
    public S_SystemMessage(final String msg, final boolean nameid) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(2);
        this.writeD(0);
        this.writeS(msg);
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

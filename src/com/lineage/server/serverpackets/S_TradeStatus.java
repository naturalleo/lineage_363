package com.lineage.server.serverpackets;

/**
 * 交易状态
 * 
 * @author dexc
 * 
 */
public class S_TradeStatus extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 交易状态
     * 
     * @param type
     *            0:交易完成 1:交易取消
     */
    public S_TradeStatus(final int type) {
        this.writeC(S_OPCODE_TRADESTATUS);
        this.writeC(type); // 0:交易完成 1:交易取消
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

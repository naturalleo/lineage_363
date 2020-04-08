package com.lineage.server.serverpackets;

/**
 * 角色创造结果
 * 
 * @author DaiEn
 * 
 */
public class S_CharCreateStatus extends ServerBasePacket {

    private byte[] _byte = null;

    public static final int REASON_OK = 0x02;

    public static final int REASON_ALREADY_EXSISTS = 0x06;

    public static final int REASON_INVALID_NAME = 0x09;

    public static final int REASON_WRONG_AMOUNT = 0x15;

    /**
     * 角色创造结果
     * 
     * @param reason
     */
    public S_CharCreateStatus(final int reason) {
        // 0000: 1d 02 3c 6c 12 a1 43 46 ..<l..CF
        this.writeC(S_OPCODE_NEWCHARWRONG);
        this.writeC(reason);
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

package com.lineage.server.serverpackets;

/**
 * 未知 B 人物列表之前
 * 
 * @author dexc
 * 
 */
public class S_Unknown_B extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 未知 B 人物列表之前
     * 
     * @param i
     */
    public S_Unknown_B() {
        /*
         * Server op: 43 0000: 2b 0a 02 00 00 00 2b 7f +.....+
         */
        this.writeC(S_OPCODE_CHARRESET);

        this.writeC(0x0a);
        this.writeC(0x02);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x00);
        this.writeC(0x2b);
        this.writeC(0x7f);
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

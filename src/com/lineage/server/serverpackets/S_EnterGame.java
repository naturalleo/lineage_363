package com.lineage.server.serverpackets;

/**
 * 宣告进入游戏
 * 
 * @author dexc Server OP NO: 54 0000: 36 03 c9 ea a5 c4 f2 1c
 */
public class S_EnterGame extends ServerBasePacket {

    private byte[] _byte = null;

    public S_EnterGame() {
        // 0000: 29 03 6a e0 cf 83 e3 da ).j.....
        this.writeC(S_OPCODE_UNKNOWN1);
        this.writeC(0x03);

        /*
         * this.writeC(0x6a); this.writeC(0xe0); this.writeC(0xcf);
         * this.writeC(0x83); this.writeC(0xe3); this.writeC(0xda);
         */
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

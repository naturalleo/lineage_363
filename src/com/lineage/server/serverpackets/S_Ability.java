package com.lineage.server.serverpackets;

/**
 * 戒指
 * 
 * @author dexc
 * 
 */
public class S_Ability extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 戒指
     * 
     * @param type
     * @param equipped
     */
    public S_Ability(final int type, final boolean equipped) {
        this.buildPacket(type, equipped);
    }

    private void buildPacket(final int type, final boolean equipped) {
        this.writeC(S_OPCODE_ABILITY);
        this.writeC(type); // 1:ROTC 5:ROSC
        this.writeC(equipped ? 0x01 : 0x00);
        // this.writeC(0x02);
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

package com.lineage.server.serverpackets;

/**
 * 慎重药水
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxWisdomPotion extends ServerBasePacket {

    private byte[] _byte = null;

    /** 慎重药水 */
    public static final int WISDOM_POTION = 0x39;// 57;

    public S_PacketBoxWisdomPotion(final int time) {
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(WISDOM_POTION);// 57
        this.writeC(0x2c);// 44
        this.writeH(time);
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

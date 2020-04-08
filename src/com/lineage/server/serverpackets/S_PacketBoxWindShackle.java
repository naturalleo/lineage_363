package com.lineage.server.serverpackets;

/**
 * 风之枷锁(S_OPCODE_PACKETBOX)
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxWindShackle extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * <font color=#00800>风之枷锁</font>
     */
    public static final int WIND_SHACKLE = 0x2c;// 44;

    /**
     * 风之枷锁
     * 
     * @param objectId
     * @param time
     */
    public S_PacketBoxWindShackle(final int objectId, final int time) {
        final int buffTime = time >> 2; // なぜか4倍されるため4で割っておく
        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(WIND_SHACKLE);// 44
        this.writeD(objectId);
        this.writeH(buffTime);
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

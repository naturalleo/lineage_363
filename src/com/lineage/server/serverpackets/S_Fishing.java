package com.lineage.server.serverpackets;

/**
 * 物件动作种类(短时间)<BR>
 * 钓鱼
 * 
 * @author dexc
 * 
 */
public class S_Fishing extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件动作种类(短时间)<BR>
     * 钓鱼
     * 
     * @param objectId
     * @param motionNum
     * @param x
     * @param y
     */
    public S_Fishing(final int objectId, final int motionNum, final int x,
            final int y) {
        this.buildPacket(objectId, motionNum, x, y);
    }

    private void buildPacket(final int objectId, final int motionNum,
            final int x, final int y) {
        this.writeC(S_OPCODE_DOACTIONGFX);
        this.writeD(objectId);
        this.writeC(motionNum);
        this.writeH(x);
        this.writeH(y);
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

package com.lineage.server.serverpackets;

/**
 * 物件动作种类(短时间)
 * 
 * @author dexc
 * 
 */
public class S_DoActionGFX extends ServerBasePacket {

    public static int ACTION_MAGIC = 0x16;

    private byte[] _byte = null;

    /**
     * 物件动作种类(短时间)
     * 
     * @param objectId
     * @param actionId
     */
    public S_DoActionGFX(final int objectId, final int actionId) {
        this.writeC(S_OPCODE_DOACTIONGFX);
        this.writeD(objectId);
        this.writeC(actionId);
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

package com.lineage.server.serverpackets;

/**
 * 魔法效果:精准目标
 * 
 * @author DaiEn
 * 
 */
public class S_TrueTarget extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 魔法效果:精准目标
     * 
     * @param targetId
     *            目标OBJID
     * @param objectId
     *            施展者OBJID
     * @param message
     *            附加讯息
     */
    public S_TrueTarget(final int targetId, final int objectId,
            final String message) {
        this.buildPacket(targetId, objectId, message);
    }

    private void buildPacket(final int targetId, final int objectId,
            final String message) {
        this.writeC(S_OPCODE_TRUETARGET);
        this.writeD(targetId);
        this.writeD(objectId);
        this.writeS(message);
    }

    public S_TrueTarget(final int targetId, final String message) {
        this.writeC(S_OPCODE_TRUETARGET);
        this.writeD(targetId);
        this.writeD(targetId);
        this.writeS(message);
        //this.writeH(gfxid);
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

package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

/**
 * 物件面向
 * 
 * @author dexc
 * 
 */
public class S_ChangeHeading extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件面向
     * 
     * @param cha
     */
    public S_ChangeHeading(final L1Character cha) {
        this.buildPacket(cha);
    }

    private void buildPacket(final L1Character cha) {
        this.writeC(S_OPCODE_CHANGEHEADING);
        this.writeD(cha.getId());
        this.writeC(cha.getHeading());
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

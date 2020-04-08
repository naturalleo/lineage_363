package com.lineage.server.serverpackets;

/**
 * 角色名称变紫色
 * 
 * @author DaiEn
 * 
 */
public class S_PinkName extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色名称变紫色
     * 
     * @param objecId
     * @param time
     */
    public S_PinkName(final int objecId, final int time) {
        // 0000: 19 40 60 b8 00 0f d2 03 .@`.....
        this.writeC(S_OPCODE_PINKNAME);
        this.writeD(objecId);
        this.writeC(time);
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

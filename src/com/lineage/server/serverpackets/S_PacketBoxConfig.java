package com.lineage.server.serverpackets;

import com.lineage.server.templates.L1Config;

/**
 * 人物快速键纪录档案(S_OPCODE_PACKETBOX)
 * 
 * @author dexc
 * 
 */
public class S_PacketBoxConfig extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * <font color=#00800>更新角色使用的快速键</font>
     */
    public static final int CHARACTER_CONFIG = 0x29;// 41

    /**
     * 人物快速键纪录档案
     * 
     * @param config
     */
    public S_PacketBoxConfig(L1Config config) {
        final int length = config.getLength();
        final byte data[] = config.getData();

        this.writeC(S_OPCODE_PACKETBOX);
        this.writeC(CHARACTER_CONFIG);// 41
        this.writeD(length);
        this.writeByte(data);
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

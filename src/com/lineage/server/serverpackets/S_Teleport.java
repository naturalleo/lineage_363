package com.lineage.server.serverpackets;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 传送锁定(瞬间移动)
 * 
 * @author dexc
 * 
 */
public class S_Teleport extends ServerBasePacket {

    private byte[] _byte = null;

    private static AtomicInteger _nextId = new AtomicInteger(100000);

    /**
     * 传送锁定(瞬间移动)
     * 
     * @param pc
     */
    public S_Teleport() {
        // 0000: 1a eb a7 44 02 d9 cd a7 ...D....
        this.writeC(S_OPCODE_TELEPORT);

        this.writeC(0xeb);
        this.writeC(0xa7);

        this.writeD(_nextId.incrementAndGet());
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

package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

public class S_NoSell extends ServerBasePacket {
    private byte[] _byte = null;

    public S_NoSell(final L1NpcInstance npc) {
        this.buildPacket(npc);
    }

    private void buildPacket(final L1NpcInstance npc) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(npc.getId());
        this.writeS("nosell");
        this.writeC(0x01);
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

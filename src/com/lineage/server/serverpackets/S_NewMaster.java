package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;

/**
 * 物件新增主人
 * 
 * @author dexc
 * 
 */
public class S_NewMaster extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 物件新增主人
     * 
     * @param name
     *            主人名称
     * @param npc
     */
    public S_NewMaster(final String name, final L1NpcInstance npc) {
        this.buildPacket(name, npc);
    }

    private void buildPacket(final String name, final L1NpcInstance npc) {
        this.writeC(S_OPCODE_NEWMASTER);
        this.writeD(npc.getId());
        this.writeS(name);
    }

    public S_NewMaster(final L1NpcInstance npc) {
        this.writeC(S_OPCODE_NEWMASTER);
        this.writeD(npc.getId());
        this.writeS("");
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

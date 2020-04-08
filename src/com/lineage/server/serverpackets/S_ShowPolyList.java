package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;

/**
 * NPC对话视窗(变身清单)
 * 
 * @author dexc
 * 
 */
public class S_ShowPolyList extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * NPC对话视窗(变身清单)
     * 
     * @param objid
     */
    public S_ShowPolyList(final int objid) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS("monlist");
    }

    /**
     * NPC对话视窗(变身清单)
     * 
     * @param target
     */
    public S_ShowPolyList(final L1Character target) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(target.getId());
        this.writeS("monlist");
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

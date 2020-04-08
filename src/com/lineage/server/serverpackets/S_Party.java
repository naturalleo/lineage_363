package com.lineage.server.serverpackets;

/**
 * 队伍名单
 * 
 * @author dexc
 * 
 */
public class S_Party extends ServerBasePacket {

    private byte[] _byte = null;

    public S_Party(final String htmlid, final int objid) {
        this.buildPacket(htmlid, objid, "", "", 0);
    }

    public S_Party(final String htmlid, final int objid,
            final String partyname, final String partymembers) {

        this.buildPacket(htmlid, objid, partyname, partymembers, 1);
    }

    private void buildPacket(final String htmlid, final int objid,
            final String partyname, final String partymembers, final int type) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS(htmlid);
        this.writeH(type);
        this.writeH(0x02);
        this.writeS(partyname);
        this.writeS(partymembers);
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

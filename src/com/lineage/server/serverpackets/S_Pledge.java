package com.lineage.server.serverpackets;

/**
 * 血盟成员清单
 * 
 * @author dexc
 * 
 */
public class S_Pledge extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 血盟成员清单(盟主查询)
     * 
     * @param string
     * @param objid
     * @param clanName
     * @param olmembers
     * @param allmembers
     */
    public S_Pledge(final int objid, final String clanName,
            final StringBuilder olmembers, final StringBuilder allmembers) {
        this.buildPacket(objid, clanName, olmembers, allmembers);
    }

    private void buildPacket(final int objid, final String clanname,
            final StringBuilder olmembers, final StringBuilder allmembers) {

        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS("pledgeM");
        this.writeH(0X02);
        this.writeH(0x03);
        this.writeS(clanname);
        this.writeS(olmembers.toString());
        this.writeS(allmembers.toString());
    }

    /**
     * 血盟成员清单(成员查询)
     * 
     * @param string
     * @param objid
     * @param clanName
     * @param olmembers
     */
    public S_Pledge(final int objid, final String clanName,
            final StringBuilder olmembers) {
        this.buildPacket(objid, clanName, olmembers);
    }

    private void buildPacket(final int objid, final String clanname,
            final StringBuilder olmembers) {

        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS("pledge");
        this.writeH(0X01);
        this.writeH(0x02);
        this.writeS(clanname);
        this.writeS(olmembers.toString());
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

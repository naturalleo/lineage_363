package com.lineage.server.serverpackets;

/**
 * 角色封号
 * 
 * @author dexc
 * 
 */
public class S_CharTitle extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色封号
     * 
     * @param objid
     * @param title
     */
    /*
     * public S_CharTitle(final int objid, final String title) {
     * this.writeC(S_OPCODE_CHARTITLE); this.writeD(objid); this.writeS(title);
     * }
     */

    /**
     * 角色封号
     * 
     * @param objid
     * @param title
     */
    public S_CharTitle(int objid, StringBuilder title) {
        this.writeC(S_OPCODE_CHARTITLE);
        this.writeD(objid);
        this.writeS(title.toString());
    }

    /**
     * 消除角色封号
     * 
     * @param objid
     * @param title
     */
    public S_CharTitle(final int objid) {
        this.writeC(S_OPCODE_CHARTITLE);
        this.writeD(objid);
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

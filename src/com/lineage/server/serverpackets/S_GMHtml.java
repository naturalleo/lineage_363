package com.lineage.server.serverpackets;

/**
 * 显示指定HTML
 * 
 * @author dexc
 * 
 */
public class S_GMHtml extends ServerBasePacket {

    /**
     * 显示指定HTML
     * 
     * @param _objid
     * @param html
     */
    public S_GMHtml(final int _objid, final String html) {
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(_objid);
        this.writeS(html);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}

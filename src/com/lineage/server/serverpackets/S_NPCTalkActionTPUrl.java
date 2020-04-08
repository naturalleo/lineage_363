package com.lineage.server.serverpackets;

import com.lineage.server.model.L1NpcTalkData;

/**
 * NPC对话视窗
 * 
 * @author dexc
 * 
 */
public class S_NPCTalkActionTPUrl extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * NPC对话视窗
     * 
     * @param cha
     * @param prices
     * @param objid
     */
    public S_NPCTalkActionTPUrl(final L1NpcTalkData cha, final Object[] prices,
            final int objid) {
        this.buildPacket(cha, prices, objid);
    }

    private void buildPacket(final L1NpcTalkData npc, final Object[] prices,
            final int objid) {
        String htmlid = "";
        htmlid = npc.getTeleportURL();
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objid);
        this.writeS(htmlid);
        this.writeH(0x01); // 不明
        this.writeH(prices.length); // 引数の数

        for (final Object price : prices) {
            this.writeS(String.valueOf(((Integer) price).intValue()));
        }
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

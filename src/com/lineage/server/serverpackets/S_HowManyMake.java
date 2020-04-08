package com.lineage.server.serverpackets;

/**
 * 选取物品数量 (NPC道具交换数量)
 * 
 * @author dexc
 * 
 */
public class S_HowManyMake extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 选取物品数量 (NPC道具交换-附加HTML)
     * 
     * @param objId
     * @param max
     * @param htmlId
     */
    public S_HowManyMake(final int objId, final int max, final String htmlId) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objId);
        this.writeD(0x00000000);// ?
        this.writeD(0x00000000);// 数量初始质
        this.writeD(0x00000000);// 最低可换数量
        this.writeD(max);// 最高可换数量
        this.writeH(0x0000);// ?
        this.writeS("request");// HTML
        this.writeS(htmlId);// 命令
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

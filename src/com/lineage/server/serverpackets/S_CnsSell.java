package com.lineage.server.serverpackets;

import com.lineage.data.event.ShopXSet;

/**
 * 选取物品数量 (卖出价格定义)
 * 
 * @author dexc
 * 
 */
public class S_CnsSell extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 选取物品数量 (卖出价格定义)
     * 
     * @param objectId
     *            NPC OBJID
     * @param htmlid
     *            HTML名称
     * @param command
     *            命令
     */
    public S_CnsSell(final int objectId, final String htmlid,
            final String command) {
        this.buildPacket(objectId, htmlid, command);
    }

    private void buildPacket(final int objectId, final String htmlid,
            final String command) {
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objectId);
        this.writeD(0x00000000);// ?
        this.writeD(ShopXSet.MIN);// 数量初始质
        this.writeD(ShopXSet.MIN);// 最低可换数量
        this.writeD(ShopXSet.MAX);// 最高可换数量
        this.writeH(0x0000);// ?
        this.writeS(htmlid);// HTML
        this.writeS(command);// 命令
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

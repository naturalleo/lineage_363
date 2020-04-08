package com.lineage.server.serverpackets;

/**
 * NPC物品贩卖(测试用NPCID 50000)
 * 
 * @author dexc
 * 
 */
public class S_ShopSellListTmp extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * TEST
     * 
     * @param objId
     * @param tmp
     */
    public S_ShopSellListTmp(final int objId, int tmp) {
        System.out.println("objId: " + objId + " tmp: " + tmp);
        // System.out.println(12456);
        final int size = 100;
        this.writeC(S_OPCODE_SHOWSHOPBUYLIST);
        this.writeD(objId);

        this.writeH(size);

        for (int i = 0; i < size; i++) {
            final int gfx = tmp++;
            this.writeD(i);
            this.writeH(gfx);
            this.writeD(0x00);
            this.writeS("gfxid = " + gfx);
            System.out.println("gfxid = " + gfx);
            this.writeC(0x00);
        }
        this.writeH(0x0007); // 0x0000:无显示 0x0001:珍珠 0x0007:金币 0x17d4:天宝
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

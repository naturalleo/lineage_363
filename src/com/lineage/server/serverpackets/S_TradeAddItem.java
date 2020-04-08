package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 交易增加物品
 * 
 * @author dexc
 * 
 */
public class S_TradeAddItem extends ServerBasePacket {

    /**
     * 交易增加物品
     * 
     * @param item
     * @param count
     * @param type
     */
    public S_TradeAddItem(final L1ItemInstance item, final long count,
            final int type) {
        // 0000: 21 00 3e 01 24 37 36 37 00 03 00 b7 32 b3 9c 2f
        // !.>.$767....2../
        this.writeC(S_OPCODE_TRADEADDITEM);
        this.writeC(type); // 0:交易视窗上半部 1:交易视窗下半部
        this.writeH(item.getItem().getGfxId());

        String name = item.getNumberedViewName(count);

        this.writeS(name);
        // 0:祝福
        // 1:通常
        // 2:诅咒
        // 3:未鉴定
        // 128:祝福&封印
        // 129:&封印
        // 130:诅咒&封印
        // 131:未鉴定&封印
        this.writeC(item.getBless());
    }

    /**
     * 交易增加物品 - 测试
     */
    public S_TradeAddItem() {
        this.writeC(S_OPCODE_TRADEADDITEM);
        this.writeC(0x01); // 0:交易视窗上半部 1:交易视窗下半部
        this.writeH(714);// 恶魔头盔
        this.writeS("测试物品(55)");
        // 0:祝福
        this.writeC(0x00);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}

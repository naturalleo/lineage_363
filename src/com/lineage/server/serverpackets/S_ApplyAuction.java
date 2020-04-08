package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.templates.L1AuctionBoardTmp;

/**
 * 选取物品数量 (盟屋拍卖公告)
 * 
 * @author dexc
 * 
 */
public class S_ApplyAuction extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 选取物品数量 (盟屋拍卖公告)
     * 
     * @param objectId
     * @param houseNumber
     */
    public S_ApplyAuction(final int objectId, final String houseNumber) {
        this.buildPacket(objectId, houseNumber);
    }

    private void buildPacket(final int objectId, final String houseNumber) {
        final int number = Integer.valueOf(houseNumber);
        final L1AuctionBoardTmp board = AuctionBoardReading.get()
                .getAuctionBoardTable(number);
        this.writeC(S_OPCODE_INPUTAMOUNT);
        this.writeD(objectId);
        this.writeD(0x00000000); // ?
        if (board.getBidderId() == 0) { // 无目前竞标者
            this.writeD((int) board.getPrice()); // 数量初始质
            this.writeD((int) board.getPrice()); // 最低可换数量

        } else { // 具有目前竞标者
            this.writeD((int) board.getPrice() + 1); // 数量初始质
            this.writeD((int) board.getPrice() + 1); // 最低可换数量
        }

        this.writeD(0x77359400); // 2000000000 最高可换数量
        this.writeH(0x0000); // ?
        this.writeS("agapply");// HTML
        this.writeS("agapply " + houseNumber);// 命令
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

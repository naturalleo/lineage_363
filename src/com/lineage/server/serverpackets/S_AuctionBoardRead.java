package com.lineage.server.serverpackets;

import java.util.Calendar;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.templates.L1AuctionBoardTmp;

/**
 * 盟屋拍卖公告栏内容
 * 
 * @author dexc
 * 
 */
public class S_AuctionBoardRead extends ServerBasePacket {

    private byte[] _byte = null;

    public S_AuctionBoardRead(final int objectId, final String house_number) {
        this.buildPacket(objectId, house_number);
    }

    private void buildPacket(final int objectId, final String house_number) {
        final int number = Integer.valueOf(house_number);
        final L1AuctionBoardTmp board = AuctionBoardReading.get()
                .getAuctionBoardTable(number);
        this.writeC(S_OPCODE_SHOWHTML);
        this.writeD(objectId);
        this.writeS("agsel");
        this.writeS(house_number); // アジトの番号
        this.writeH(0x0009); // 以下の文字列の个数
        this.writeS(board.getHouseName()); // アジトの名前
        this.writeS(board.getLocation() + "$1195"); // アジトの位置
        this.writeS(String.valueOf(board.getHouseArea())); // アジトの广さ
        this.writeS(board.getOldOwner()); // 以前の所有者
        this.writeS(board.getBidder()); // 现在の入札者
        this.writeS(String.valueOf(board.getPrice())); // 现在の入札価格
        final Calendar cal = board.getDeadline();
        final int month = cal.get(Calendar.MONTH) + 1;
        final int day = cal.get(Calendar.DATE);
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        this.writeS(String.valueOf(month)); // 缔切月
        this.writeS(String.valueOf(day)); // 缔切日
        this.writeS(String.valueOf(hour)); // 缔切时
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

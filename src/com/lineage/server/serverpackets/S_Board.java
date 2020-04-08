package com.lineage.server.serverpackets;

import java.util.ArrayList;

import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Board;

/**
 * 布告栏列表
 * 
 * @author dexc
 * 
 */
public class S_Board extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 布告栏列表
     * 
     * @param npc
     */
    public S_Board(final L1NpcInstance npc) {
        this.buildPacket(npc, 0);
    }

    /**
     * 布告栏列表
     * 
     * @param npc
     * @param number
     */
    public S_Board(final L1NpcInstance npc, final int number) {
        this.buildPacket(npc, number);
    }

    private void buildPacket(final L1NpcInstance npc, final int number) {
        int count = 0;

        final ArrayList<L1Board> showList = new ArrayList<L1Board>();

        int maxid = BoardReading.get().getMaxId();
        while ((count < 8) && (maxid > 0)) {
            final L1Board boardInfo = BoardReading.get().getBoardTable(maxid--);
            if (boardInfo != null) {
                if ((boardInfo.get_id() <= number) || (number == 0)) {
                    showList.add(count, boardInfo);
                    count++;
                }
            }
        }

        this.writeC(S_OPCODE_BOARD);
        this.writeC(0x00); // ?
        this.writeD(npc.getId());
        this.writeC(0xff); // ?
        this.writeC(0xff); // ?
        this.writeC(0xff); // ?
        this.writeC(0x7f); // ?
        this.writeH(showList.size());
        this.writeH(0x012c);// 300
        for (int i = 0; i < showList.size(); i++) {
            L1Board boardInfo = showList.get(i);
            if (boardInfo != null) {
                this.writeD(boardInfo.get_id());
                this.writeS(boardInfo.get_name());
                this.writeS(boardInfo.get_date());
                this.writeS(boardInfo.get_title());
            }
        }
    }

    /**
     * 布告栏列表 - 测试
     * 
     * @param objectid
     */
    public S_Board(final int objectid) {
        this.writeC(S_OPCODE_BOARD);
        this.writeC(0x00); // ?
        this.writeD(objectid);
        this.writeC(0xFF); // ?
        this.writeC(0xFF); // ?
        this.writeC(0xFF); // ?
        this.writeC(0x7F); // ?
        this.writeH(2);
        this.writeH(300);

        this.writeD(2);
        this.writeS("布告栏列表");
        this.writeS("2010-2-2");
        this.writeS("布告栏列表TITLE");

        this.writeD(1);
        this.writeS("布告栏列表2");
        this.writeS("2010-2-3");
        this.writeS("布告栏列表TITLE2");
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

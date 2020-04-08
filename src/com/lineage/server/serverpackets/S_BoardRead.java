package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.templates.L1Board;

/**
 * 布告栏内容
 * 
 * @author dexc
 * 
 */
public class S_BoardRead extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 布告栏内容
     * 
     * @param number
     */
    public S_BoardRead(final int number) {
        this.buildPacket(number);
    }

    private void buildPacket(final int number) {
        final L1Board board = BoardReading.get().getBoardTable(number);
        this.writeC(S_OPCODE_BOARDREAD);
        this.writeD(board.get_id());
        this.writeS(board.get_name());
        this.writeS(board.get_date());
        this.writeS(board.get_title());
        this.writeS(board.get_content());
    }

    /**
     * 布告栏内容 - 测试
     * 
     * @param number
     */
    public S_BoardRead() {
        this.writeC(S_OPCODE_BOARDREAD);
        this.writeD(10);
        this.writeS("测试NAME");
        this.writeS("2010-02-02");
        this.writeS("测试TITLE");
        this.writeS("测试内容");
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

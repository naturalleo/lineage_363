package com.lineage.server.serverpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;

/**
 * 角色列表
 * 
 * @author dexc
 * 
 */
public class S_CharAmount extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 角色列表
     * 
     * @param value
     *            已创人物数量
     * @param client
     */
    public S_CharAmount(final int value, final ClientExecutor client) {
        this.buildPacket(value, client);
    }

    private void buildPacket(final int value, final ClientExecutor client) {
        final int characterSlot = client.getAccount().get_character_slot();

        final int maxAmount = ConfigAlt.DEFAULT_CHARACTER_SLOT + characterSlot;

        // 0000: 0c 04 06 81 00 90 01 a3 ........
        this.writeC(S_OPCODE_CHARAMOUNT);
        this.writeC(value);
        this.writeC(maxAmount); // max amount
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

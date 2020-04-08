package com.lineage.server.serverpackets;

import com.lineage.server.model.gametime.L1GameTimeClock;

/**
 * 
 * @author dexc
 * 
 */
public class S_GameTime extends ServerBasePacket {
    public S_GameTime(final int time) {
        this.buildPacket(time);
    }

    public S_GameTime() {
        final int time = L1GameTimeClock.getInstance().currentTime()
                .getSeconds();
        this.buildPacket(time);
    }

    private void buildPacket(final int time) {
        // 0000: 30 84 15 37 20 04 08 00 0..7 ...
        this.writeC(S_OPCODE_GAMETIME);
        this.writeD(time);
    }

    @Override
    public byte[] getContent() {
        return this.getBytes();
    }
}

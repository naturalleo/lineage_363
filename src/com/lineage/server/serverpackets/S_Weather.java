package com.lineage.server.serverpackets;

/**
 * 游戏天气
 * 
 * 1~3雨 17~19雪
 * 
 * @author dexc
 * 
 */
public class S_Weather extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 游戏天气
     * 
     * @param weather
     */
    public S_Weather(final int weather) {
        this.buildPacket(weather);
    }

    private void buildPacket(final int weather) {
        this.writeC(S_OPCODE_WEATHER);
        this.writeC(weather);
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

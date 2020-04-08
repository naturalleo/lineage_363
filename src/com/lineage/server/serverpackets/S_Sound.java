package com.lineage.server.serverpackets;

/**
 * 拨放音效
 * 
 * @author dexc
 * 
 */
public class S_Sound extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 拨放音效
     * 
     * @param sound
     *            音效编号
     */
    public S_Sound(final int sound) {
        this.buildPacket(sound, 0);
    }

    /**
     * 拨放音效
     * 
     * @param sound
     *            音效编号
     * @param repeat
     *            重复
     */
    public S_Sound(final int sound, final int repeat) {
        this.buildPacket(sound, repeat);
    }

    private void buildPacket(final int sound, final int repeat) {
        // 0000: 68 00 b5 01 d2 af 45 10 h.....E.
        this.writeC(S_OPCODE_SOUND);
        this.writeC(repeat); // 重复
        this.writeH(sound);// 音效编号
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

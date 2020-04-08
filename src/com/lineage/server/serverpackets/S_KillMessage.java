package com.lineage.server.serverpackets;

import java.util.Random;

import com.lineage.config.ConfigKill;

/**
 * 杀人公告
 * 
 * @author dexc
 * 
 */
public class S_KillMessage extends ServerBasePacket {

    private byte[] _byte = null;

    private static final Random _random = new Random();

    /**
     * 杀人公告
     * 
     * @param winName
     * @param deathName
     */
    public S_KillMessage(final String winName, final String deathName) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 颜色
        this.writeD(0x00000000);
        String x1 = ConfigKill.KILL_TEXT_LIST.get(_random
                .nextInt(ConfigKill.KILL_TEXT_LIST.size()) + 1);
        this.writeS(String.format(x1, winName, deathName));
    }

    /**
     * 赌场NPC对话
     * 
     * @param winName
     * @param deathName
     */
    public S_KillMessage(final String name, final String msg, int i) {
        this.writeC(S_OPCODE_NPCSHOUT);
        this.writeC(0x00);// 颜色
        this.writeD(0x00000000);
        this.writeS(" \\fY[" + name + "] " + msg);
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

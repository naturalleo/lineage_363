package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 广播频道
 * 
 * @author dexc
 * 
 */
public class S_ChatGlobal extends ServerBasePacket {

    private byte[] _byte = null;

    /**
     * 广播频道
     * 
     * @param pc
     * @param chat
     */
    public S_ChatGlobal(final L1PcInstance pc, final String chat) {
        buildPacket(pc, chat);
    }

    private void buildPacket(final L1PcInstance pc, final String chat) {
        writeC(S_OPCODE_GLOBALCHAT);
        writeC(0x03);
        writeS(pc.isGm() ? "[******] " + chat : "[" + pc.getName() + "] "
                + chat);
    }

    /**
     * NPC对话输出
     * 
     * @param npc
     * @param chat
     */
    public S_ChatGlobal(final L1NpcInstance npc, final String chat) {
        writeC(S_OPCODE_GLOBALCHAT);
        writeC(0x03);
        writeS("[" + npc.getNameId() + "] " + chat);
    }

    /**
     * 共用广播频道
     * 
     * @param pc
     * @param chat
     */
    public S_ChatGlobal(final String chat) {
        writeC(S_OPCODE_GLOBALCHAT);
        writeC(0x03);
        writeS(chat);
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
